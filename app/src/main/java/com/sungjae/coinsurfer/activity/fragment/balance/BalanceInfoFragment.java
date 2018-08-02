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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sungjae.coinsurfer.R;
import com.sungjae.coinsurfer.activity.fragment.BaseFragment;
import com.sungjae.coinsurfer.tradedata.Balance;
import com.sungjae.coinsurfer.tradedata.Coin;
import com.sungjae.coinsurfer.tradedata.CoinType;
import com.sungjae.coinsurfer.tradedata.TradeModel;

import java.util.ArrayList;
import java.util.List;


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

    private BalanceGraphView mGraphView;
    private Spinner mFilterSpinner;

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
            CursorLoader loader = null;
            if (mFilterSpinner.getSelectedItemPosition() == 0) {
                int limit = mTradeSetting.getHourGraphLimit();
                loader = new CursorLoader(getContext(), Uri.parse("content://coinsurfer/hour_balance"), null, null, null, getLimitQuery(limit));
            } else {
                int limit = mTradeSetting.getDayGraphLimit();
                loader = new CursorLoader(getContext(), Uri.parse("content://coinsurfer/day_balance"), null, null, null, getLimitQuery(limit));
            }
            return loader;
        }

        private String getLimitQuery(int limit) {
            if (limit > 0) {
                return "_id desc limit 0, " + limit;
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            cursor.setNotificationUri(getContext().getContentResolver(), Uri.parse("content://coinsurfer/balance_total"));
            mGraphView.setCursor(cursor, mBalance.getTotalAsKrw());
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
        createSpinner(view);

        mCoinListView = view.findViewById(R.id.coin_list);
        mCoinListAdapter = new CoinInfoAdapter(this.getContext(), mBalance.getCoinList());
        mCoinListView.setAdapter(mCoinListAdapter);
        mCoinListView.setFastScrollEnabled(true);
        //mCoinListView.setFastScrollStyle();

        mBalanceListAdapter = new BalanceListAdapter(getContext(), R.layout.balance_list_item_layout, null, 0);
        mBalanceListView = view.findViewById(R.id.balance_list);
        mBalanceListView.setAdapter(mBalanceListAdapter);
        mBalanceListView.setOnItemLongClickListener(mBalanceListViewOnItemLongClickListener);

        getLoaderManager().initLoader(BALANCE_LIST_LOADER_ID, null, mBalanceListLoader);
        getLoaderManager().initLoader(BALANCE_GRAPH_LOADER_ID, null, mBalanceGraphLoader);
    }

    private void createSpinner(View view) {
        mFilterSpinner = view.findViewById(R.id.filterSpinner);
        mFilterSpinner.setAdapter(getSpinner_adapter());
        mFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                getLoaderManager().restartLoader(BALANCE_GRAPH_LOADER_ID, null, mBalanceGraphLoader);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private ArrayAdapter<String> getSpinner_adapter() {
        List<String> spinner_items = new ArrayList<>();

        spinner_items.add("시간");
        spinner_items.add("일자");

        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, spinner_items);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return spinner_adapter;
    }

    private String considerValue(long dateKey, Balance mBalance, double oldTotalKrw) {
        ContentResolver cr = getContext().getContentResolver();
        Uri uri = Uri.parse("content://coinsurfer/balance");
        Cursor cursor = cr.query(uri, null, "date=" + dateKey, null, null);
        double curAmount_oldPrice = 0;
        double oldAmount_curPrice = 0;

        String info = "";
        ArrayList<CoinType> handledCoin = new ArrayList<>();
        if (cursor.moveToFirst()) {
            curAmount_oldPrice += mBalance.getKrw();

            do {
                int coinTypeIndex = cursor.getInt(2);

                if (coinTypeIndex < 0) {
                    info += String.format("현금 : %.0f \n", cursor.getDouble(4));
                    oldAmount_curPrice += cursor.getDouble(4);//KRW
                } else {
                    CoinType coinType = CoinType.getCoinType(coinTypeIndex);
                    handledCoin.add(coinType);

                    double coinPrice = cursor.getDouble(3);
                    double coinAmount = cursor.getDouble(4);

                    info += String.format("%s : %.3f x %.0f = %.0f\n", coinType.toString(), coinAmount, coinPrice, coinPrice * coinAmount);
                    int balanceIndex = mBalance.getIndex(coinType);
                    Coin curCoin = null;
                    if (balanceIndex >= 0) {
                        curCoin = mBalance.getCoin(balanceIndex);
                        curAmount_oldPrice += coinPrice * curCoin.getCoinValue();
                        oldAmount_curPrice += coinAmount * curCoin.getCurPrice();

                        info += String.format("%s : %.3f x %.0f = %.0f\n", coinType.toString(), coinAmount, curCoin.getCurPrice(), curCoin.getCurPrice() * coinAmount);
                        info += String.format("%s : %.3f x %.0f = %.0f\n", coinType.toString(), curCoin.getCoinValue(), coinPrice, coinPrice * curCoin.getCoinValue());
                    }
                }
            } while (cursor.moveToNext());

            ArrayList<Coin> coinList = (ArrayList<Coin>) mBalance.getCoinList().clone();
            ArrayList<Coin> unHandledCoinList = new ArrayList<>();
            for (Coin coin : coinList) {
                boolean handled = false;
                for (CoinType type : handledCoin) {
                    if (coin.getCoinType() == type) {
                        handled = true;
                        break;
                    }
                }
                if (!handled) {
                    unHandledCoinList.add(coin);
                }
            }

            for (Coin coin : unHandledCoinList) {
                curAmount_oldPrice += coin.getCurKrw();
            }

        }
        String ret = info + String.format("curAmount_oldPrice = %,.0f\noldAmount_curPrice = %,.0f\nDelta = %,.0f\n%,.0f",
                curAmount_oldPrice,
                oldAmount_curPrice,
                curAmount_oldPrice - oldTotalKrw,
                mBalance.getTotalAsKrw() - oldAmount_curPrice
        );
        return ret;
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
