package com.sungjae.coinsurfer.activity.fragment.tradehistory;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.sungjae.coinsurfer.R;
import com.sungjae.coinsurfer.activity.fragment.BaseFragment;
import com.sungjae.coinsurfer.tradedata.CoinType;

import java.util.ArrayList;
import java.util.List;


public class TradeHistoryFragment extends BaseFragment {
    private final static int TRADE_LIST_LOADER_ID = 1;

    private ListView mTradeListView;
    private Spinner mFilterSpinner;
    private CoinType mFilterCoin = null;

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

            String selection = null;
            if (mFilterCoin != null) {
                selection = "coin = " + mFilterCoin.ordinal();
            }

            return new CursorLoader(getContext(), Uri.parse("content://coinsurfer/trade"), projection, selection, null, "_id desc");
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

        createFilterSpinner(view);

        getLoaderManager().initLoader(TRADE_LIST_LOADER_ID, null, mTradeListLoader);
    }

    private void createFilterSpinner(View view) {
        mFilterSpinner = view.findViewById(R.id.filterSpinner);
        List<String> spinner_items = getFilterSpinnerItems();
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, spinner_items);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFilterSpinner.setAdapter(spinner_adapter);
        mFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position >= CoinType.values().length) {
                    mFilterCoin = null;
                } else {
                    mFilterCoin = CoinType.getCoinType(position);
                }
                getLoaderManager().restartLoader(TRADE_LIST_LOADER_ID, null, mTradeListLoader);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mFilterSpinner.setSelection(spinner_items.size() - 1); //default ALL
    }

    @NonNull
    private List<String> getFilterSpinnerItems() {
        List<String> spinner_items = new ArrayList<>();

        for (CoinType coinType : CoinType.values()) {
            spinner_items.add(coinType.toString());
        }
        spinner_items.add("ALL");

        return spinner_items;
    }

    @Override
    protected void updateView() {

    }
}
