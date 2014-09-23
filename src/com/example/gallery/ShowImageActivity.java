package com.example.gallery;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class ShowImageActivity extends Activity {

    private ImageView image;
    Integer position;

    private static final String TAG = "Touch";

  //These matrices will be used to move and zoom image
  Matrix matrix = new Matrix();
  Matrix savedMatrix = new Matrix();  

  // We can be in one of these 3 states
  static final int NONE = 0;
  static final int DRAG = 1;
  static final int ZOOM = 2;
  static final int DRAW =3;
  int mode = NONE;

  // Remember some things for zooming
  PointF start = new PointF();
  PointF mid = new PointF();
  float oldDist = 1f;

  // Limit zoomable/pannable image
  private float[] matrixValues = new float[9];
  private float maxZoom;
  private float minZoom;
  private float height;
  private float width;
  private RectF viewRect;
  private ImageView myimage;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
      // TODO Auto-generated method stub
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_show);
      image=(ImageView)findViewById(R.id.image);
      if (getIntent().getExtras().get("imageid")!=null){
          image.setImageResource(getIntent().getExtras().getInt("imageid"));
      }
  }
  
}
