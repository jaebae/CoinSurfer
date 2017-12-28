package com.sungjae.coinsurfer.activity.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.ResourceCursorAdapter;
import android.util.SparseArray;
import android.view.View;

import com.sungjae.coinsurfer.setting.TradeSetting;

abstract public class BaseFragment extends Fragment implements TradeSetting.OnSettingChangeListener, LoaderManager.LoaderCallbacks<Cursor> {
    protected TradeSetting mTradeSetting;
    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateView();
        }
    };
    private SparseArray<Loader<Cursor>> mLoaderList;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLoaderList = new SparseArray<>();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mLoaderList.clear();
    }

    protected void addCursorLoader(int id, Bundle args, CursorLoader cursorLoader) {
        this.getLoaderManager().initLoader(id, args, this);
        mLoaderList.put(id, cursorLoader);
    }

    abstract protected void updateView();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTradeSetting = TradeSetting.getInstance(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        mTradeSetting.addOnChangedListener(this);
        registerBroadcastReceivers();
        updateView();
    }

    @Override
    public void onPause() {
        super.onPause();
        mTradeSetting.removeOnChangedListener(this);
        unregisterBroadcastReceivers();
    }

    private void registerBroadcastReceivers() {
        IntentFilter filter = new IntentFilter("UPDATE_VIEW");
        getContext().registerReceiver(mBroadcastReceiver, filter);
    }

    private void unregisterBroadcastReceivers() {
        getContext().unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onSettingChange() {
        updateView();
    }

    abstract protected ResourceCursorAdapter getAdapterById(int id);

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return mLoaderList.get(id);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        int id = mLoaderList.indexOfValue(loader);
        ResourceCursorAdapter adapter = getAdapterById(id);
        adapter.changeCursor(cursor);
        //adapter.notifyDataSetChanged();
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        int index = mLoaderList.indexOfValue(loader);
        if (index >= 0) {
            int id = mLoaderList.keyAt(index);
            mLoaderList.remove(id);
            ResourceCursorAdapter adapter = getAdapterById(id);
            adapter.changeCursor(null);
        }
    }
}
