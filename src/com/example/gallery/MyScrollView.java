package com.example.gallery;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
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
    private int deltaY = 0;
    private int deltaX = 0;
    private int lastY = 0;
    private int lastX = 0;
    private int downY = 0;
    private int downX = 0;
    private int disY = 0;
    private int currentPage = 0;
    private ArrayList<Integer> pointList =new ArrayList<Integer>();
    private Context mContext;
    private int mCurrentIndex;
    int i;
    ContentResolver resolver;
    byte[] mContent;
    ScaleGestureDetector mScaleDetector;
    float saveScale = 1f;
    float minScale = 1f;
    float maxScale = 3f;
    Matrix matrix;
    ImageView mImg;

    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    PointF last = new PointF();

    int current_x;
    int current_y;
    int last_x;
    int last_y;
    int start_x, start_y,delta_y,start_top;
    int top, bottom;
    private float beforeLenght, afterLenght;
    private boolean isControl_V = false;
    private boolean isControl_H = false;
    private boolean isScaleAnim = false;
    private float scale_temp=1f;
    private float move_temp=0;
    private int MAX_W, MAX_H, MIN_W, MIN_H;
    private int current_Top, current_Right, current_Bottom, current_Left;
    
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
        /*mScaleDetector = new ScaleGestureDetector(mContext, new ScaleListener());
        matrix = new Matrix();*/
        MAX_W = getWinWidth() * 3;
        MAX_H = getWinHeight() * 3;

        MIN_W = getWinWidth() / 2;
        MIN_H = getWinHeight() / 2;
        bottom = getWinHeight();
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
                }
            }
        }
        Log.i("test", "pointList"+pointList.size());
        Log.i("test", "subChildCount"+subChildCount);
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        /*mScaleDetector.onTouchEvent(ev);
        PointF curr = new PointF(ev.getX(), ev.getY());*/

        switch (ev.getAction()& MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_DOWN:
            /*mode = DRAG;
            last.set(curr);*/
            //downX = (int) ev.getX();
            //event.getY()貌似是相对于图片的像素来说的，setscale后像素貌似没有变化。
            onTouchDown(ev);
            break;
         // 多点触摸
        case MotionEvent.ACTION_POINTER_DOWN:
            onPointerDown(ev);
            break;
        case MotionEvent.ACTION_MOVE:{
            /*if (mode == DRAG) {
                float deltaX = curr.x - last.x;
                float deltaY = curr.y - last.y;
                MyScrollView.this.setTranslationX(deltaX);
                MyScrollView.this.setTranslationY(deltaY);
                last.set(curr.x, curr.y);
            }*/
            onTouchMove(ev);
        }break;
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_CANCEL:{
            lastY = deltaY;
            lastX = deltaX;
            /*if(scale_temp == 1){
                if( Math.abs((ev.getX() - downX)) > getWidth() / 4){
                    if(ev.getX() - downX > 0){
                        scrollToPrePage();
                    }else{
                        scrollToNextPage();
                    }
                }else{  
                    scrollToCurrent();
                }
            } else {
                move_temp += (ev.getX() - downX);
                if( Math.abs(move_temp) > scale_temp*getWidth() / 4){
                    if(ev.getX() - downX > 0){
                        scrollToPrePage();
                    }else{
                        scrollToNextPage();
                    }
                }
            }*/

            if( Math.abs(deltaX) > scale_temp*getWidth() / 4){
                if(deltaX > 0){
                    this.setTranslationY(0);
                    scrollToPrePage();
                } else {
                    this.setTranslationY(0);
                    scrollToNextPage();
                }
            }else if(scale_temp == 1) {  
                scrollToCurrent();
            }
            mode = NONE;
            return true;
        }
        case MotionEvent.ACTION_POINTER_UP:
            mode = NONE;
            /*if (isScaleAnim) {
                doScaleAnim();
            }*/
            break;
        }

        return super.onTouchEvent(ev);
    }

    /** 按下 **/
    void onTouchDown(MotionEvent event) {
        mode = DRAG;

        //current_x = (int) event.getRawX();
        //start_y = (int) event.getRawY();

        //start_x = (int) event.getX();
        //start_top = (int)(start_y - event.getY());
        //current_y = (int) event.getRawY();
        deltaY = lastY;
        deltaX = lastX;
        downY = (int)event.getRawY() - deltaY;
        downX = (int)event.getRawX() - deltaX;

        //Log.i("test", this.getTop()+"");
    }

    /** 两个手指 只能放大缩小 **/
    void onPointerDown(MotionEvent event) {
        if (event.getPointerCount() == 2) {
            mode = ZOOM;
            beforeLenght = getDistance(event);// 获取两点的距离
        }
    }
    
    float getDistance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);

        return FloatMath.sqrt(x * x + y * y);
    }
    
    /** 移动的处理 **/
    void onTouchMove(MotionEvent event) {
        //int left = 0, top = 0, right = 0, bottom = 0, lastTop = 0;
        if (mode == DRAG) {


            current_y = (int) event.getRawY();
            current_x = (int) event.getRawX();
            deltaY = current_y - downY;
            deltaX = current_x - downX;
            //left = current_x - start_x;
            //right = current_x + this.getWidth() - start_x;
            //top = current_y - start_y + start_top;
            //bottom = current_y - start_y + this.getHeight();

/*            if (isControl_H) {
                if (left >= 0) {
                    left = 0;
                    right = this.getWidth();
                }
                if (right <= screen_W) {
                    left = screen_W - this.getWidth();
                    right = screen_W;
                }
            } else {
                left = this.getLeft();
                right = this.getRight();
            }*/
            /*if (isControl_V) {
                if (top > 0) {
                    top = 0;
                    bottom = this.getHeight();
                }

                if (bottom < getWinHeight()) {
                    top = getWinHeight() - this.getHeight();
                    bottom = getWinHeight();
                }
            }else {
                top = this.getTop();
                bottom = this.getBottom();
            }*/
            if (isControl_V && Math.abs(deltaY) < Math.abs(disY) && scale_temp > 1){
                this.setTranslationY(deltaY);
            }
        }
        //** 处理缩放 **//*
        else if (mode == ZOOM) {

            afterLenght = getDistance(event);// 获取两点的距离

            float gapLenght = afterLenght - beforeLenght;// 变化的长度

            if (Math.abs(gapLenght) > 5f) {
                scale_temp *= afterLenght / beforeLenght;// 求的缩放的比例

                this.setScale(scale_temp);

                beforeLenght = afterLenght;
            }
            
        }

    }
    
    /** 处理缩放 **/
    void setScale(float scale) {
        //int disX = (int) (this.getWidth() * Math.abs(1 - scale)) / 2;// 获取缩放水平距离
        //int disY = (int) (this.getHeight() * Math.abs(1 - scale)) / 2;// 获取缩放垂直距离

        // 放大
        if (scale > 1 && scale <3) {
            //current_Left = this.getLeft() - disX;
            //current_Top = this.getTop() - disY;
            //current_Right = this.getRight() + disX;
            //current_Bottom = this.getBottom() + disY;
            /*
            if(firstChild.getChildAt(0).getWidth()> getWinWidth()){
                isControl_H = true;
                Log.i("MyScrollView:", isControl_H+"");
            }*/

            this.setScaleX(scale);
            this.setScaleY(scale);
            isControl_V = true;// 开启垂直监控
            disY = (int) (this.getHeight() * Math.abs(1 - scale)) / 2;// 获取缩放垂直距离
        }
        // 缩小
        else if(scale_temp > 1/2 && scale_temp < 1){
             scale_temp = 1f;
             this.setTranslationY(0);
             this.setScaleX(scale_temp);
             this.setScaleY(scale_temp);
             isControl_V = false;// 关闭垂直监听
             isControl_H = false;
             disY = 0;
        }

    }

    
    /*public void doScaleAnim() {
        myAsyncTask = new MyAsyncTask(screen_W, this.getWidth(),
                this.getHeight());
        myAsyncTask.setLTRB(this.getLeft(), this.getTop(), this.getRight(),
                this.getBottom());
        myAsyncTask.execute();
        isScaleAnim = false;
    }*/
    
    private void scrollToCurrent() {
        deltaX = 0;
        lastX = 0;
        deltaY = 0;
        lastY = 0;
        if(mCurrentIndex==0)
            scrollTo(0,0);
        else 
            scrollTo(getWinWidth(), 0);
    }

    private void scrollToNextPage() {
        deltaX = 0;
        lastX = 0;
        deltaY = 0;
        lastY = 0;
        scale_temp = 1;
        this.setScaleX(1);
        this.setScaleY(1);

        if(mCurrentIndex<ImageAdapter.photos.size()-1){
            if (mCurrentIndex!=0){
                firstChild.removeViewAt(0);
                subChildCount--;
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
                subChildCount++;
            }
            
            scrollTo(getWinWidth(), 0);
            mCurrentIndex++;
        }
    }

    private void scrollToPrePage() {
        lastX = 0;
        deltaX = 0;
        scale_temp = 1;
        this.setScaleX(1);
        this.setScaleY(1);

        if(mCurrentIndex>0){
            if (mCurrentIndex!=ImageAdapter.photos.size()-1){
                if(mCurrentIndex==1)
                {
                    firstChild.removeViewAt(1);
                } else {
                    firstChild.removeViewAt(2);
                }
                subChildCount--;
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
                subChildCount++;
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

/*    private class ScaleListener extends
    ScaleGestureDetector.SimpleOnScaleGestureListener {
@Override
public boolean onScaleBegin(ScaleGestureDetector detector) {
    return true;
}

@Override
public boolean onScale(ScaleGestureDetector detector) {
    float mScaleFactor = detector.getScaleFactor();
    if (saveScale > maxScale) {
        saveScale = maxScale;
        mScaleFactor = maxScale / saveScale;
    } else if (saveScale < minScale) {
        saveScale = minScale;
        mScaleFactor = minScale / saveScale;
    }
    saveScale *= mScaleFactor;    
    MyScrollView.this.setScaleX(mScaleFactor);
    MyScrollView.this.setScaleY(mScaleFactor);
        
    return true;
}

}*/ 
}