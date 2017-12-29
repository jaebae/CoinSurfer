package com.sungjae.coinsurfer.provider;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class TradeProvider extends ContentProvider {
    private static final String DATABASE_NAME = "coinsurfer.db";
    private static final int DATABASE_VERSION = 100;

    private SQLiteDatabase mDatabase;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        TradeDbHelper helper = new TradeDbHelper(context, context.getExternalFilesDir("databases") + "/" + DATABASE_NAME);
        mDatabase = helper.getReadableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projectionIn, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = mDatabase.query(getTableName(uri), projectionIn, selection, selectionArgs, null, null, sortOrder);
        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @NonNull
    private String getTableName(Uri uri) {
        return uri.getEncodedPath().substring(1);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        Uri ret = null;
        long rowId = mDatabase.insert(getTableName(uri), null, contentValues);

        if (rowId > 0) {
            ret = ContentUris.withAppendedId(uri, rowId);
            notifyChange(uri);
        }


        return ret;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    private void notifyChange(Uri uri) {
        getContext().getContentResolver().notifyChange(uri, null);
    }


    private static class TradeDbHelper extends SQLiteOpenHelper {

        public TradeDbHelper(Context context, String dbPath) {
            super(context, dbPath, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(getDropTradeTableQuery());
            db.execSQL(getDropBalanceTableQuery());

            db.execSQL(getCreateTradeTableQuery());
            db.execSQL(getCreateBalanceTableQuery());
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

        private String getCreateTradeTableQuery() {
            return "CREATE TABLE IF NOT EXISTS TRADE " +
                    "( _id integer PRIMARY KEY, " +
                    " date long, " +
                    " trade int, " +
                    " coin int, " +
                    " unit double, " +
                    " price double, " +
                    " krw double)";
        }

        private String getCreateBalanceTableQuery() {
            return "CREATE TABLE IF NOT EXISTS BALANCE " +
                    "( _id integer PRIMARY KEY, " +
                    " date long, " +
                    " krw double ) ";
        }

        private String getDropTradeTableQuery() {
            return "DROP TABLE IF EXISTS TRADE";
        }

        private String getDropBalanceTableQuery() {
            return "DROP TABLE IF EXISTS BALANCE";
        }

    }
}

