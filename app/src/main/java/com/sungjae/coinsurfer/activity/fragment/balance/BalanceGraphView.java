package com.sungjae.coinsurfer.activity.fragment.balance;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class BalanceGraphView extends GraphView {
    public BalanceGraphView(Context context) {
        super(context);
    }

    public BalanceGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BalanceGraphView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setCursor(Cursor cursor, double curTotalKrw) {
        DataPoint[] dataList = getDataPoints(cursor);
        DataPoint[] dataCurList = getCurDataPoints(curTotalKrw, dataList.length);


        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataList);
        series.setDataPointsRadius(10);
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(dataCurList);
        series2.setColor(Color.RED);
        removeAllSeries();
        addSeries(series);
        addSeries(series2);


        getViewport().setXAxisBoundsManual(true);
        getViewport().setMinX(0);
        getViewport().setMaxX(cursor.getCount());
        getGridLabelRenderer().setNumHorizontalLabels(0);
    }

    private DataPoint[] getCurDataPoints(double curTotalKrw, int length) {
        DataPoint[] dataList = new DataPoint[length];

        for (int i = 0; i < length; i++) {
            DataPoint data = new DataPoint(i, curTotalKrw / 1000);
            dataList[i] = data;
        }
        return dataList;
    }

    @NonNull
    private DataPoint[] getDataPoints(Cursor cursor) {
        DataPoint[] dataList = new DataPoint[cursor.getCount()];
        int index = 0;
        if (cursor.moveToLast()) {
            do {
                DataPoint data = new DataPoint(index, cursor.getDouble(2) / 1000);
                dataList[index++] = data;
            } while (cursor.moveToPrevious());
        }
        return dataList;
    }
}
