package com.example.gallery;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    List<Photo> mPhotos;
    ContentResolver resolver;
    byte[] mContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        resolver = getContentResolver();
        setContentView(R.layout.activity_image);
        mScrollView = (MyScrollView) findViewById(R.id.scrollView);
        
        mContainer = (LinearLayout) findViewById(R.id.container);
        position = getIntent().getExtras().getInt("position");
        ImageAdapter.position = position;
        
        LayoutParams params = new LayoutParams(getWinWidth(), getWinHeight());
        
        mPhotos = ImageAdapter.photos;
        
        if(position>0 && position<mPhotos.size()-1){
            for (int i=position-1;i<position+2;i++){
                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(params);
                if (mPhotos.get(i).getImageid() != 0){
                    imageView.setImageResource(mPhotos.get(i).getImageid());
                } else {
                    try {
                        mContent = readStream(resolver.openInputStream(Uri.parse(mPhotos.get(i).getPath())));
                        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
                        bmpFactoryOptions.inPurgeable  =  true ; 
                        Bitmap bitmap = BitmapFactory.decodeByteArray(mContent, 0, mContent.length, bmpFactoryOptions);
                        imageView.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                imageView.setScaleType(ScaleType.FIT_CENTER);
                mContainer.addView(imageView);
                Log.i("test", "di"+i+"ge image"+mPhotos.get(i).getImageid());
                Log.i("test", mPhotos.size()+"");
            }
        } else if(position==0){
            for (int i=position;i<position+2;i++){
                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(params);
                if (mPhotos.get(i).getImageid() != 0){
                    imageView.setImageResource(mPhotos.get(i).getImageid());
                } else {
                    try {
                        mContent = readStream(resolver.openInputStream(Uri.parse(mPhotos.get(i).getPath())));
                        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
                        bmpFactoryOptions.inPurgeable  =  true ; 
                        Bitmap bitmap = BitmapFactory.decodeByteArray(mContent, 0, mContent.length, bmpFactoryOptions);
                        imageView.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                imageView.setScaleType(ScaleType.FIT_CENTER);
                mContainer.addView(imageView);
                Log.i("test", "di"+i+"ge image"+mPhotos.get(i).getImageid());
                Log.i("test", mPhotos.size()+"");
            }
        } else {
            for (int i=position-1;i<position+1;i++){
                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(params);
                if (mPhotos.get(i).getImageid() != 0){
                    imageView.setImageResource(mPhotos.get(i).getImageid());
                } else {
                    try {
                        mContent = readStream(resolver.openInputStream(Uri.parse(mPhotos.get(i).getPath())));
                        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
                        bmpFactoryOptions.inPreferredConfig  =  Bitmap .Config.RGB_565;    
                        bmpFactoryOptions.inPurgeable  =  true ;   
                        bmpFactoryOptions.inInputShareable  =  true ; 
                        Bitmap bitmap = BitmapFactory.decodeByteArray(mContent, 0, mContent.length, bmpFactoryOptions);
                        imageView.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                imageView.setScaleType(ScaleType.FIT_CENTER);
                mContainer.addView(imageView);
                Log.i("test", "di"+i+"ge image"+mPhotos.get(i).getImageid());
                Log.i("test", mPhotos.size()+"");
            }
        }
        
        Log.i(TAG, "position="+position);
        Log.i(TAG, "subChildCount="+mScrollView.hashCode());
        
    }
    
    public byte[] readStream(InputStream inStream) throws Exception { 
        byte[] buffer = new byte[1024]; 
        int len = -1; 
        ByteArrayOutputStream outStream = new ByteArrayOutputStream(); 
        while ((len = inStream.read(buffer)) != -1) { 
                 outStream.write(buffer, 0, len); 
        } 
        byte[] data = outStream.toByteArray(); 
        outStream.close(); 
        inStream.close(); 
        return data; 

   } 
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
//        new Handler().postDelayed(new Runnable() { @Override public void run() { mScrollView.gotoPage(position);}},5);
        new Handler().postDelayed(new Runnable() { 
            @Override public void run() { 
                if(0<position && position<mPhotos.size())
                    mScrollView.gotoPage(1);
                else 
                    mScrollView.gotoPage(0);
                } 
            },5);
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
