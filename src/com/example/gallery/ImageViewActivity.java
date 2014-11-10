package com.example.gallery;

import java.util.List;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.example.db.Photo;

/**
 * 
 * @author xinye
 *
 */
public class ImageViewActivity extends Activity {
    private static String TAG = "ImageViewActivity";
    
    private LinearLayout mContainer = null;
    private MyScrollView mScrollView;
    private int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        mScrollView = (MyScrollView) findViewById(R.id.scrollView);
        
        mContainer = (LinearLayout) findViewById(R.id.container);
        
        LayoutParams params = new LayoutParams(getWinWidth(), getWinHeight());
        
        List<Photo> mPhotos = ImageAdapter.photos;
        for (int i=0; i<mPhotos.size(); i++){
            ImageView imageView = new ImageView(this);
            Log.i(TAG, (imageView==null)+"");
            imageView.setLayoutParams(params);
            if (mPhotos.get(i).imageid != 0){
                imageView.setImageResource(mPhotos.get(i).imageid);
            } else {
                imageView.setImageURI(Uri.parse(mPhotos.get(i).path));
            }
            imageView.setScaleType(ScaleType.FIT_CENTER);
            mContainer.addView(imageView);
            Log.i("test", "di"+i+"ge image"+mPhotos.get(i).imageid);
            Log.i("test", mPhotos.size()+"");
        }
        
        position = getIntent().getExtras().getInt("position");
        Log.i(TAG, "position="+position);
        Log.i(TAG, "subChildCount="+mScrollView.hashCode());
        
    }
    
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        new Handler().postDelayed(new Runnable() { @Override public void run() { mScrollView.gotoPage(position);}},5);
    }



    @Override
    protected void onResume() {
        ((MyScrollView)mContainer.getParent()).init();
        super.onResume();
        /*if(mScrollView.pointList.size()>0){
            mScrollView.gotoPage(position);
        }*/
        
    }
    
    private int getWinWidth(){
        DisplayMetrics dm = new DisplayMetrics();
        //获取屏幕信息
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }
    private int getWinHeight(){
        DisplayMetrics dm = new DisplayMetrics();
        //获取屏幕信息
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }
}