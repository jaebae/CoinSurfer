package com.sungjae.coinsurfer.activity.fragment.balance;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.sungjae.coinsurfer.R;
import com.sungjae.coinsurfer.setting.TradeSetting;
import com.sungjae.coinsurfer.tradedata.Balance;
import com.sungjae.coinsurfer.tradedata.CoinType;
import com.sungjae.coinsurfer.tradedata.TradeModel;

import java.util.ArrayList;


public class BalanceInfoFragment extends Fragment implements TradeSetting.OnSettingChangeListener {

    private TradeSetting mTradeSetting;
    private Balance mBalance;
    private TradeModel mTradeModel;

    private TextView mTotalAsKrwView;
    private TextView mProfitView;
    private TextView mCurKrwView;
    private TextView mInvestedKrwView;

    private ListView mCoinListView;
    private CoinInfoAdapter mCoinListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.balance_fragment, container, false);
    }

    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateView();
        }
    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTotalAsKrwView = view.findViewById(R.id.total_as_krw);
        mProfitView = view.findViewById(R.id.profit);
        mCurKrwView = view.findViewById(R.id.cur_krw);
        mInvestedKrwView = view.findViewById(R.id.invested_krw);

        mCoinListView = view.findViewById(R.id.coin_list);
        mCoinListAdapter = new CoinInfoAdapter(this.getContext(), mBalance.getCoinList());
        mCoinListView.setAdapter(mCoinListAdapter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBalance = Balance.getsInstance();
        mTradeSetting = TradeSetting.getInstance(getContext());
        mTradeSetting.addOnChangedListener(this);

        mTradeModel = TradeModel.getInstance();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTradeSetting.removeOnChangedListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerBroadcastReceivers();
        updateView();
    }

    private void updateView() {
        double totalKrw = mBalance.getTotalAsKrw();
        double curKrw = mBalance.getKrw();
        double investedKrw = totalKrw - curKrw;
        double initialKrw = mTradeSetting.getInvestKrw();
        double profit = ((totalKrw - initialKrw) / initialKrw) * 100;
        mTotalAsKrwView.setText("총 자산 : " + String.format("%,.0f", totalKrw));
        mProfitView.setText("이익률 : " + String.format("%,.02f", profit));
        mCurKrwView.setText("현금 : " + String.format("%,.0f", curKrw));
        mInvestedKrwView.setText("투자금 : " + String.format("%,.0f", investedKrw));


        /*for (int i = 0; i < mBalance.getCoinCount(); i++) {
            Coin coin = mBalance.getCoin(i);

        }*/

    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterBroadcastReceivers();
    }

    @Override
    public void onSettingChange() {
        double investKrw = mTradeSetting.getInvestKrw();
        ArrayList<CoinType> enableCoin = mTradeSetting.getEnableCoinList();


    }

    private void registerBroadcastReceivers() {
        IntentFilter filter = new IntentFilter("UPDATE_VIEW");
        getContext().registerReceiver(mBroadcastReceiver, filter);

    }

    private void unregisterBroadcastReceivers() {
        getContext().unregisterReceiver(mBroadcastReceiver);
    }
}
