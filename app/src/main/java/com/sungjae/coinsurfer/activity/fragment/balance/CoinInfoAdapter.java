package com.sungjae.coinsurfer.activity.fragment.balance;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sungjae.coinsurfer.R;
import com.sungjae.coinsurfer.tradedata.Coin;
import com.sungjae.coinsurfer.tradedata.TradeModel;

import java.util.List;

public class CoinInfoAdapter extends ArrayAdapter<Coin> {

    private TradeModel mTradeModel;

    public CoinInfoAdapter(@NonNull Context context, @NonNull List<Coin> objects) {
        super(context, R.layout.coin_list_item_layout, objects);

        mTradeModel = TradeModel.getInstance();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.coin_list_item_layout, parent, false);
        }
        Coin coin = getItem(position);

        TextView tvName = convertView.findViewById(R.id.name);
        TextView tvAmount = convertView.findViewById(R.id.amount);
        TextView tvKrw = convertView.findViewById(R.id.krw);
        TextView tvPrice = convertView.findViewById(R.id.cur_price);
        TextView tvDiffRate = convertView.findViewById(R.id.diff_rate);

        tvName.setText(coin.getCoinName());
        tvAmount.setText(String.format("%,.03f", coin.getCoinValue()));
        tvKrw.setText(String.format("%,.0f", coin.getCurKrw()));
        tvPrice.setText(String.format("%,.0f", coin.getCurPrice()));


        double diff = (coin.getCurKrw() - mTradeModel.getTargetCoinAsKrw()) / mTradeModel.getTargetCoinAsKrw();

        if (diff > 0) {
            tvDiffRate.setTextColor(Color.RED);
        } else {
            tvDiffRate.setTextColor(Color.BLUE);
        }

        tvDiffRate.setText(String.format("%,.02f%%", Math.abs(diff)));

        return convertView;
    }
}
