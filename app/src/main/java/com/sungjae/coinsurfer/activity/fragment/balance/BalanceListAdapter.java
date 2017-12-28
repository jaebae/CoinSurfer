package com.sungjae.coinsurfer.activity.fragment.balance;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;
import android.widget.TextView;

import com.sungjae.coinsurfer.R;


public class BalanceListAdapter extends ResourceCursorAdapter {


    public BalanceListAdapter(Context context, int layout, Cursor c, int flags) {
        super(context, layout, c, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView dateView = view.findViewById(R.id.date);
        TextView krwView = view.findViewById(R.id.balance);

        dateView.setText(cursor.getString(0));
        krwView.setText(String.format("%,.0f", cursor.getDouble(1)));
    }


}
