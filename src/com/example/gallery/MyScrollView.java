package com.example.gallery;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
/**
 * 
 * @author XINYE
 *
 */
public class MyScrollView extends HorizontalScrollView {
    public int subChildCount = 0;
    private ViewGroup firstChild = null;
    private int downX = 0;
    private int currentPage = 0;
    private ArrayList<Integer> pointList =new ArrayList<Integer>();
    public Map<View, Integer> viewMap= new HashMap<View, Integer>();
    private Context mContext;
    private int mCurrentIndex;
    int i;
    ContentResolver resolver;
    byte[] mContent;
    
    public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext=context;
        init();
    }


    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext=context;
        init();
    }

    public MyScrollView(Context context) {
        super(context);
        this.mContext=context;
        init();
    }
    public void init() {
        setHorizontalScrollBarEnabled(false);
        mCurrentIndex = ImageAdapter.position;
        resolver = mContext.getContentResolver();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        receiveChildInfo();
    }
    public void receiveChildInfo() {
        
        firstChild = (ViewGroup) getChildAt(0);
        if(firstChild != null){
            subChildCount = firstChild.getChildCount();
            if (mCurrentIndex==0)
                i = 1;
            else 
                i = 0;
                
            for(;i < subChildCount;i++){
                if(((View)firstChild.getChildAt(i)).getWidth() > 0){
                    pointList.add(((View)firstChild.getChildAt(i)).getLeft());
                    viewMap.put(((View)firstChild.getChildAt(i)),mCurrentIndex-1+i);
                }
            }
        }
        Log.i("test", "pointList"+pointList.size());
        Log.i("test", "subChildCount"+subChildCount);
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
        case MotionEvent.ACTION_DOWN:
            downX = (int) ev.getX();
            break;
        case MotionEvent.ACTION_MOVE:{
            
        }break;
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_CANCEL:{
            if( Math.abs((ev.getX() - downX)) > getWidth() / 4){
                if(ev.getX() - downX > 0){
                    scrollToPrePage();
                }else{
                    scrollToNextPage();
                }
            }else{            
                scrollToCurrent();
            }
            return true;
        }
        }
        return super.onTouchEvent(ev);
    }

    private void scrollToCurrent() {
        if(mCurrentIndex==0)
            scrollTo(0,0);
        else 
            scrollTo(getWinWidth(), 0);
    }

    private void scrollToNextPage() {
        
        if(mCurrentIndex<ImageAdapter.photos.size()-1){
            if (mCurrentIndex!=0){
                viewMap.remove(firstChild.getChildAt(0));
                firstChild.removeViewAt(0);
            }
            if(mCurrentIndex!=ImageAdapter.photos.size()-2){
                LayoutParams params = new LayoutParams(getWinWidth(), getWinHeight());
                ImageView imageView = new ImageView(mContext);
                imageView.setLayoutParams(params);
                if (ImageAdapter.photos.get(mCurrentIndex+2).getImageid() != 0){
                    imageView.setImageResource(ImageAdapter.photos.get(mCurrentIndex+2).getImageid());
                } else {
                    try {
                        mContent = readStream(resolver.openInputStream(Uri.parse(ImageAdapter.photos.get(mCurrentIndex+2).getPath())));
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
                firstChild.addView(imageView);
                viewMap.put(imageView, mCurrentIndex+2);
            }
            
            scrollTo(getWinWidth(), 0);
            mCurrentIndex++;
        }
    }

    private void scrollToPrePage() {
        if(mCurrentIndex>0){
            if (mCurrentIndex!=ImageAdapter.photos.size()-1){
                if(mCurrentIndex==1)
                {
                    viewMap.remove(firstChild.getChildAt(1));
                    firstChild.removeViewAt(1);
                } else {
                    viewMap.remove(firstChild.getChildAt(2));
                    firstChild.removeViewAt(2);
                }
            }
            if(mCurrentIndex!=1){
                LayoutParams params = new LayoutParams(getWinWidth(), getWinHeight());
                ImageView imageView = new ImageView(mContext);
                imageView.setLayoutParams(params);
                if (ImageAdapter.photos.get(mCurrentIndex-2).getImageid() != 0){
                    imageView.setImageResource(ImageAdapter.photos.get(mCurrentIndex-2).getImageid());
                } else {
                    try {
                        mContent = readStream(resolver.openInputStream(Uri.parse(ImageAdapter.photos.get(mCurrentIndex-2).getPath())));
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
                firstChild.addView(imageView,0);
                viewMap.put(imageView, mCurrentIndex-1);
            }
            
            if(mCurrentIndex==1)
                scrollTo(0,0);
            else
                scrollTo(getWinWidth(), 0);
            mCurrentIndex--;
        }
    }
    /**
     * 下一页
     */
    public void nextPage(){
        scrollToNextPage();
    }
    /**
     * 上一页
     */
    public void prePage(){
        scrollToPrePage();
    }
    /**
     * 跳转到指定的页面
     * @param page
     * @return
     */
    public boolean gotoPage(int page){
        if(page > 0 && page < subChildCount){
            scrollTo(pointList.get(page), 0);
            currentPage = page;
            return true;
        }
        return false;
    }
    
    private int getWinWidth(){
        DisplayMetrics dm = new DisplayMetrics();
        //获取屏幕信息
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }
    private int getWinHeight(){
        DisplayMetrics dm = new DisplayMetrics();
        //获取屏幕信息
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
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
}