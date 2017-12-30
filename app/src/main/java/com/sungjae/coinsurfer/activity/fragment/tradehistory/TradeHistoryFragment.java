package com.sungjae.coinsurfer.activity.fragment.tradehistory;

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
import android.widget.ListView;

import com.sungjae.coinsurfer.R;
import com.sungjae.coinsurfer.activity.fragment.BaseFragment;


public class TradeHistoryFragment extends BaseFragment {
    private final static int TRADE_LIST_LOADER_ID = 1;

    private ListView mTradeListView;
    private TradeListAdapter mTradeListAdapter;
    private LoaderManager.LoaderCallbacks<Cursor> mTradeListLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
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
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            mTradeListAdapter.changeCursor(cursor);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mTradeListAdapter.changeCursor(null);
        }
    };

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

        getLoaderManager().initLoader(TRADE_LIST_LOADER_ID, null, mTradeListLoader);
    }

    @Override
    protected void updateView() {

    }
}
