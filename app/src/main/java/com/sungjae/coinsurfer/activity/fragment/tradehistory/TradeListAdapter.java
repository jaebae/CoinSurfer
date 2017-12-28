package com.sungjae.coinsurfer.activity.fragment.tradehistory;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;
import android.widget.TextView;

import com.sungjae.coinsurfer.R;
import com.sungjae.coinsurfer.tradedata.CoinType;
import com.sungjae.coinsurfer.tradedata.TradeInfo;


public class TradeListAdapter extends ResourceCursorAdapter {


    public TradeListAdapter(Context context, int layout, Cursor c, int flags) {
        super(context, layout, c, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView dateView = view.findViewById(R.id.date);
        TextView typeView = view.findViewById(R.id.type);
        TextView tradeView = view.findViewById(R.id.trade);
        TextView unitView = view.findViewById(R.id.unit);
        TextView priceView = view.findViewById(R.id.price);
        TextView amountView = view.findViewById(R.id.amount);

        dateView.setText(cursor.getString(0));
        typeView.setText(CoinType.getCoinType(cursor.getInt(1)).toString());
        tradeView.setText(TradeInfo.TradeType.values()[cursor.getInt(2)].toString());

        unitView.setText(String.format("%,.02f", cursor.getDouble(3)));
        priceView.setText(String.format("%,.0f", cursor.getDouble(4)));
        amountView.setText(String.format("%,.0f", cursor.getDouble(5)));
    }


}
