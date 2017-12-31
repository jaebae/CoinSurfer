package com.sungjae.coinsurfer.activity.fragment.balance;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.sungjae.coinsurfer.R;
import com.sungjae.coinsurfer.activity.fragment.BaseFragment;
import com.sungjae.coinsurfer.tradedata.Balance;
import com.sungjae.coinsurfer.tradedata.Coin;
import com.sungjae.coinsurfer.tradedata.CoinType;
import com.sungjae.coinsurfer.tradedata.TradeModel;

import java.util.Calendar;
import java.util.Date;


public class BalanceInfoFragment extends BaseFragment {
    private final static int BALANCE_LIST_LOADER_ID = 1;
    private final static int BALANCE_GRAPH_LOADER_ID = 2;

    private Balance mBalance;
    private TradeModel mTradeModel;

    private TextView mTotalAsKrwView;
    private TextView mProfitView;
    private TextView mCurKrwView;
    private TextView mInvestedKrwView;
    private TextView mTargetCoinKrwView;

    private ListView mCoinListView;
    private CoinInfoAdapter mCoinListAdapter;

    private ListView mBalanceListView;
    private BalanceListAdapter mBalanceListAdapter;
    AdapterView.OnItemLongClickListener mBalanceListViewOnItemLongClickListener = new AdapterView.OnItemLongClickListener() {

        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
            Cursor cursor = mBalanceListAdapter.getCursor();
            cursor.moveToPosition(position);
            long dateKey = cursor.getLong(3);
            double oldTotalKrw = cursor.getDouble(1);

            String msg = considerValue(dateKey, mBalance, oldTotalKrw);

            Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();

            return false;
        }

        private long getBalanceLogDateKey(int position) {
            Cursor cursor = mBalanceListAdapter.getCursor();
            cursor.moveToPosition(position);
            return cursor.getLong(3);
        }
    };
    private BalanceGraphView mGraphView;
    private LoaderManager.LoaderCallbacks<Cursor> mBalanceListLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            String projection[] = new String[]{
                    "STRFTIME ('%Y-%m-%d %H:%M', date / 1000, 'unixepoch', 'localtime') as strdate",
                    "total_krw",
                    "_id",
                    "date"
            };

            return new CursorLoader(getContext(), Uri.parse("content://coinsurfer/balance_total"), projection, null, null, "_id desc");
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            mBalanceListAdapter.changeCursor(cursor);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mBalanceListAdapter.changeCursor(null);
        }
    };
    private LoaderManager.LoaderCallbacks<Cursor> mBalanceGraphLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(getContext(), Uri.parse("content://coinsurfer/hour_balance"), null, null, null, null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            cursor.setNotificationUri(getContext().getContentResolver(), Uri.parse("content://coinsurfer/balance_total"));
            mGraphView.setCursor(cursor);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.balance_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTotalAsKrwView = view.findViewById(R.id.total_as_krw);
        mProfitView = view.findViewById(R.id.profit);
        mCurKrwView = view.findViewById(R.id.cur_krw);
        mInvestedKrwView = view.findViewById(R.id.invested_krw);
        mTargetCoinKrwView = view.findViewById(R.id.target_coin_krw);

        mGraphView = view.findViewById(R.id.graph);

        mCoinListView = view.findViewById(R.id.coin_list);
        mCoinListAdapter = new CoinInfoAdapter(this.getContext(), mBalance.getCoinList());
        mCoinListView.setAdapter(mCoinListAdapter);

        mBalanceListAdapter = new BalanceListAdapter(getContext(), R.layout.balance_list_item_layout, null, 0);
        mBalanceListView = view.findViewById(R.id.balance_list);
        mBalanceListView.setAdapter(mBalanceListAdapter);
        mBalanceListView.setOnItemLongClickListener(mBalanceListViewOnItemLongClickListener);

        getLoaderManager().initLoader(BALANCE_LIST_LOADER_ID, null, mBalanceListLoader);
        getLoaderManager().initLoader(BALANCE_GRAPH_LOADER_ID, null, mBalanceGraphLoader);

        //graphTest();
    }

    private String considerValue(long dateKey, Balance mBalance, double oldTotalKrw) {
        ContentResolver cr = getContext().getContentResolver();
        Uri uri = Uri.parse("content://coinsurfer/balance");
        Cursor cursor = cr.query(uri, null, "date=" + dateKey, null, null);
        double curAmount_oldPrice = 0;
        double oldAmount_curPrice = 0;
        if (cursor.moveToFirst()) {
            curAmount_oldPrice += mBalance.getKrw();

            do {
                int coinTypeIndex = cursor.getInt(2);

                if (coinTypeIndex < 0) {
                    oldAmount_curPrice += cursor.getDouble(4);//KRW
                } else {
                    CoinType coinType = CoinType.getCoinType(coinTypeIndex);
                    double coinPrice = cursor.getDouble(3);
                    double coinAmount = cursor.getDouble(4);

                    int balanceIndex = mBalance.getIndex(coinType);
                    Coin curCoin = null;
                    if (balanceIndex >= 0) {
                        curCoin = mBalance.getCoin(balanceIndex);
                        curAmount_oldPrice += coinPrice * curCoin.getCoinValue();
                        oldAmount_curPrice += coinAmount * curCoin.getCurPrice();
                    }
                }
            } while (cursor.moveToNext());

        }
        String ret = String.format("Delta = %,.0f\n%,.0f",
                curAmount_oldPrice - oldTotalKrw,
                mBalance.getTotalAsKrw() - oldAmount_curPrice
        );
        return ret;
    }

    void graphTest() {
        GraphView graph = mGraphView;

        // generate Dates
        Calendar calendar = Calendar.getInstance();
        Date d1 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d2 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d3 = calendar.getTime();

        calendar.add(Calendar.DATE, 1);
        Date d4 = calendar.getTime();

        calendar.add(Calendar.DATE, 1);
        Date d5 = calendar.getTime();

        calendar.add(Calendar.DATE, 1);
        Date d6 = calendar.getTime();


// you can directly pass Date objects to DataPoint-Constructor
// this will convert the Date to double via Date#getTime()
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(d1, 1),
                new DataPoint(d2, 5),
                new DataPoint(d3, 1),
                new DataPoint(d4, 5),
                new DataPoint(d5, 1),
                new DataPoint(d6, 6),
        });

        graph.addSeries(series);

// set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        graph.getGridLabelRenderer().setNumHorizontalLabels(6); // only 4 because of the space

// set manual x bounds to have nice steps
        graph.getViewport().setMinX(d1.getTime());
        graph.getViewport().setMaxX(d3.getTime());
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setScrollable(true);

// as we use dates as labels, the human rounding to nice readable numbers
// is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);


        /*LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });

        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 2),
                new DataPoint(1, 3),
                new DataPoint(2, 4),
                new DataPoint(3, 5),
                new DataPoint(4, 1)
        });

        series.setTitle("Random Curve 1");
        series.setColor(Color.GREEN);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        series.setThickness(8);


        graph.addSeries(series);
        graph.addSeries(series2);*/


        /*// first series is a line
        DataPoint[] points = new DataPoint[100];
        for (int i = 0; i < points.length; i++) {
            points[i] = new DataPoint(i, Math.sin(i * 0.5) * 20 * (Math.random() * 10 + 1));
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);

        // set manual X bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(-150);
        graph.getViewport().setMaxY(150);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(4);
        graph.getViewport().setMaxX(80);

        // enable scaling and scrolling
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);

        graph.addSeries(series);*/

        /*LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 60)
        });
        graph.addSeries(series);

        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 30),
                new DataPoint(1, 30),
                new DataPoint(2, 60),
                new DataPoint(3, 20),
                new DataPoint(4, 50)
        });

// set second scale
        graph.getSecondScale().addSeries(series2);
// the y bounds are always manual for second scale
        graph.getSecondScale().setMinY(0);
        graph.getSecondScale().setMaxY(100);
        series2.setColor(Color.RED);
        graph.getGridLabelRenderer().setVerticalLabelsSecondScaleColor(Color.RED);*/
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBalance = Balance.getsInstance();
        mTradeModel = TradeModel.getInstance();
    }

    @Override
    protected void updateView() {
        double totalKrw = mBalance.getTotalAsKrw();
        double curKrw = mBalance.getKrw();
        double investedKrw = totalKrw - curKrw;
        double initialKrw = mTradeSetting.getInvestKrw();
        double profit = ((totalKrw - initialKrw) / initialKrw) * 100;
        mTotalAsKrwView.setText("총 자산 : " + String.format("%,.0f", totalKrw));
        mProfitView.setText("이익률 : " + String.format("%,.02f%%", profit));
        mCurKrwView.setText("현금 : " + String.format("%,.0f", curKrw));
        mInvestedKrwView.setText("투자금 : " + String.format("%,.0f", investedKrw));
        mTargetCoinKrwView.setText("기준값 : " + String.format("%,.0f", mTradeModel.getTargetCoinAsKrw()));

        mCoinListAdapter.notifyDataSetChanged();
    }
}
