package com.sungjae.coinsurfer.activity.fragment.balance;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

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

        mCoinListView = view.findViewById(R.id.coin_list);
        mCoinListAdapter = new CoinInfoAdapter(this.getContext(), mBalance.getCoinList());
        mCoinListView.setAdapter(mCoinListAdapter);
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
