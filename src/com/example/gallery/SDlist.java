package com.example.gallery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class SDlist extends Activity {
    
    public static String TAG = "Thumbnails";
    private ListView listview;
    private ArrayList<HashMap<String, String>> list;
    private ContentResolver cr;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdlist);
        findViews();
    }
 
    private void findViews() {
        listview = (ListView) findViewById(R.id.listview);
        list = new ArrayList<HashMap<String, String>>();
        cr = getContentResolver();
        String[] projection = { Thumbnails._ID, Thumbnails.IMAGE_ID,
                Thumbnails.DATA };
        Cursor cursor = cr.query(Thumbnails.EXTERNAL_CONTENT_URI, projection,
                null, null, null);
        getColumnData(cursor);
 
        String[] from = { "path" };
        int[] to = { R.id.path };
        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.listitem, from,
                to);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(listener);
 
    }
 
    private void getColumnData(Cursor cur) {
        if (cur.moveToFirst()) {
            int _id;
            int image_id;
            String image_path;
            int _idColumn = cur.getColumnIndex(Thumbnails._ID);
            int image_idColumn = cur.getColumnIndex(Thumbnails.IMAGE_ID);
            int dataColumn = cur.getColumnIndex(Thumbnails.DATA);
 
            do {
                // Get the field values
                _id = cur.getInt(_idColumn);
                image_id = cur.getInt(image_idColumn);
                image_path = cur.getString(dataColumn);
 
                // Do something with the values.
                Log.i(TAG, _id + " image_id:" + image_id + " path:"
                        + image_path + "---");
                HashMap hash = new HashMap();
                hash.put("image_id", image_id + "");
                hash.put("path", image_path);
                list.add(hash);
 
            } while (cur.moveToNext());
 
        }
    }
 
    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
 
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
            // TODO Auto-generated method stub
            String image_id = list.get(position).get("image_id");
            Log.i(TAG, "---(^o^)----" + image_id);
            String[] projection = { Media._ID, Media.DATA };
            Cursor cursor = cr.query(Media.EXTERNAL_CONTENT_URI, projection,
                    Media._ID + "=" + image_id, null, null);
            if (cursor != null && cursor.getCount()>0) {
                cursor.moveToFirst();
                String path = cursor.getString(cursor
                        .getColumnIndex(Media.DATA));
                Intent intent = new Intent(SDlist.this,
                        MainActivity.class);
                Bundle b=new Bundle();
                b.putString("path", path);
                intent.putExtras(b);
                setResult(RESULT_OK,intent);
                finish();
            } else {
                Toast.makeText(SDlist.this, "This Thumbnail don't have Corresponding image!",
                        Toast.LENGTH_SHORT).show();
            }
 
        }
    };
 
}
