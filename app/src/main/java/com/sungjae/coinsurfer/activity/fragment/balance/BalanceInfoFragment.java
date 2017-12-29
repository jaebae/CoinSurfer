package com.sungjae.coinsurfer.activity.fragment.balance;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.sungjae.coinsurfer.R;
import com.sungjae.coinsurfer.activity.fragment.BaseFragment;
import com.sungjae.coinsurfer.tradedata.Balance;
import com.sungjae.coinsurfer.tradedata.TradeModel;


public class BalanceInfoFragment extends BaseFragment {

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

    private GraphView mGraphView;

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
        addCursorLoader(5, null, createLoader());

        graphTest();
    }

    void graphTest() {
        GraphView graph = mGraphView;

        // first series is a line
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

        graph.addSeries(series);

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
    protected ResourceCursorAdapter getAdapterById(int id) {
        return mBalanceListAdapter;
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


    private CursorLoader createLoader() {
        String projection[] = new String[]{
                "STRFTIME ('%Y-%m-%d %H:%M', date / 1000, 'unixepoch', 'localtime') as strdate",
                "krw",
                "_id"
        };

        return new CursorLoader(getContext(), Uri.parse("content://coinsurfer/balance"), projection, null, null, "_id desc");
    }
}
