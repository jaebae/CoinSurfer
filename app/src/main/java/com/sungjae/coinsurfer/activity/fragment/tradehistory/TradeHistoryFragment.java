package com.sungjae.coinsurfer.activity.fragment.tradehistory;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.sungjae.coinsurfer.R;
import com.sungjae.coinsurfer.activity.fragment.BaseFragment;


public class TradeHistoryFragment extends BaseFragment {

    private ListView mTradeListView;
    private TradeListAdapter mTradeListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.trade_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTradeListAdapter = new TradeListAdapter(getContext(), R.layout.trade_list_item_layout, null, 0);
        mTradeListView = view.findViewById(R.id.trade_list);
        mTradeListView.setAdapter(mTradeListAdapter);
        addCursorLoader(10, null, createLoader());
    }


    private CursorLoader createLoader() {
        String projection[] = new String[]{
                "STRFTIME ('%m-%d %H:%M', date / 1000, 'unixepoch', 'localtime') as strdate",
                "coin",
                "trade",
                "unit",
                "price",
                "krw",
                "_id"
        };

        return new CursorLoader(getContext(), Uri.parse("content://coinsurfer/trade"), projection, null, null, "_id desc");
    }

    @Override
    protected void updateView() {

    }

    @Override
    protected ResourceCursorAdapter getAdapterById(int id) {
        return mTradeListAdapter;
    }
}
