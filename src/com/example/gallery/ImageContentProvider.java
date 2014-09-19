package com.example.gallery;

import com.example.gallery.Image.*;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class ImageContentProvider extends ContentProvider {

private static final String DATEBASE_NAME="mydatabase";
    
    private static final int DATEBASE_VERRION=3;
    
    private static final String IMAGES_TABLE_NAME="image";
    
    private static final UriMatcher uriMatcher;
    
    private static final int IMAGES=1;
    private static final int IMAGES_ID=2;

    public static class DatebaseHelper extends SQLiteOpenHelper {

        DatebaseHelper(Context context) {
            super(context, DATEBASE_NAME, null, DATEBASE_VERRION);
            Log.i("PROVIDER", "DATABASE_VERSION=" + DATEBASE_VERRION);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i("PROVIDER", "onCreate(SQLiteDatabase db)");
            String sql ="CREATE TABLE " + IMAGES_TABLE_NAME + " ("
            + ImageColumns._ID + " INTEGER PRIMARY KEY,"
            + ImageColumns.TITLE + " varchar(255)," + ImageColumns.IMAGEID
            + " integer" + ");";
            Log.i("PROVIDER", "sql="+sql);
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            Log.i("PROVIDER",
                    " onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)="
                            + newVersion);
            db.execSQL("DROP TABLE IF EXISTS contact");
            onCreate(db);
        }
        
    }
    
    private DatebaseHelper mOpenHelper;
    
    static {
        uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Image.AUTHORITY, "images", IMAGES);
        uriMatcher.addURI(Image.AUTHORITY, "images/#", IMAGES_ID);
    }
    
    @Override
    public boolean onCreate() {
        // TODO Auto-generated method stub
        mOpenHelper=new DatebaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        // TODO Auto-generated method stub
        SQLiteQueryBuilder qb=new SQLiteQueryBuilder();
        
        switch(uriMatcher.match(uri)){
            case IMAGES:
                qb.setTables(IMAGES_TABLE_NAME);
                break;
            case IMAGES_ID:
                qb.setTables(IMAGES_TABLE_NAME);
                qb.appendWhere(ImageColumns._ID + "="
                        + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown uri:" + uri);
        }
        
        String orderBy;
        if(TextUtils.isEmpty(sortOrder)){
            orderBy=ImageColumns.DEFAULT_ORDER;
        } else {
            orderBy=sortOrder;
        }
        SQLiteDatabase db=mOpenHelper.getReadableDatabase();
        System.out.println(db.getMaximumSize());
        Cursor c = qb.query(db, projection, selection, selectionArgs, null,
                null, orderBy);
        return c;
    }

    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO Auto-generated method stub
        if(uriMatcher.match(uri)!=IMAGES){
            throw new IllegalArgumentException("Illegal uri:" + uri);
        }
        ContentValues inValues;
        if(values==null){
            inValues=new ContentValues();
        } else {
            inValues=new ContentValues(values);
        }
        if(inValues.containsKey(Image.ImageColumns.TITLE)==false){
            inValues.put(Image.ImageColumns.TITLE, "");
        }
        if(inValues.containsKey(Image.ImageColumns.IMAGEID)==false){
            inValues.put(Image.ImageColumns.IMAGEID, "");
        }
        SQLiteDatabase db=mOpenHelper.getWritableDatabase();
        long itemId=db.insert(IMAGES_TABLE_NAME, null, inValues);
        if(itemId>0){
            Uri contactUri=ContentUris.withAppendedId(Image.ImageColumns.CONTENT_URI, itemId);
            return contactUri;
        }
        throw new SQLException("fail to insert");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        SQLiteDatabase db=mOpenHelper.getWritableDatabase();
        
        switch(uriMatcher.match(uri)){
            case IMAGES:
                return db.delete(IMAGES_TABLE_NAME, null, null);
            case IMAGES_ID:
                String itemId=uri.getPathSegments().get(1);
                return db.delete(IMAGES_TABLE_NAME, ImageColumns._ID+"="+itemId, null);
            default:
                throw new IllegalArgumentException("Unknown uri:" + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        SQLiteDatabase db=mOpenHelper.getWritableDatabase();
        String itemId=uri.getPathSegments().get(1);
        return db.update(IMAGES_TABLE_NAME, values, ImageColumns._ID+"="+itemId, null);
    }


}
