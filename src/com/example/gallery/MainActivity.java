
package com.example.gallery;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends Activity {
    
    ImageAdapter mAdapter;
    GridView gridview;
    
    private static final int REQUISTE_CODE=1;
//    private boolean mReturningWithResult = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);  
        gridview=(GridView)findViewById(R.id.gridview);//找到activity_main.xml中定义gridview 的id  
        mAdapter=new ImageAdapter(titles, mThumbIds,MainActivity.this);
        gridview.setAdapter(mAdapter);//调用ImageAdapter.java  
        gridview.setOnItemClickListener(listener); 
    }
    
    AdapterView.OnItemClickListener listener=new AdapterView.OnItemClickListener(){//监听事件  
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            Intent intent0=new Intent(MainActivity.this, ShowImageActivity.class);
            intent0.putExtra("imageId", mThumbIds[position]);
            ActivityOptions opts = ActivityOptions.makeCustomAnimation(MainActivity.this,
                    R.anim.zoom_enter, R.anim.zoom_enter);
            // Request the activity be started, using the custom animation options.
            startActivity(intent0, opts.toBundle());
        }  
        };

private Integer[] mThumbIds={//显示的图片数组   
     R.drawable.gallery_photo_1,R.drawable.gallery_photo_2,  
     R.drawable.gallery_photo_3,R.drawable.gallery_photo_4,  
     R.drawable.gallery_photo_5,R.drawable.gallery_photo_6,  
     R.drawable.gallery_photo_7,R.drawable.gallery_photo_8,  
    };  

   private String[] titles={
           "title1", "title2", "title3", "title4", "title5", "title6", "title7", "title8"
   };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.add) {
            Intent addIntent=new Intent(this, SDlist.class);
            startActivityForResult(addIntent, REQUISTE_CODE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
//        mReturningWithResult = true;
        if(requestCode==REQUISTE_CODE && resultCode == RESULT_OK){
            Bundle b=data.getExtras();
            String sdPath=b.getString("path");
            Toast.makeText(this, sdPath, Toast.LENGTH_SHORT)
            .show();
        
        mAdapter.addItem(sdPath);
        mAdapter.notifyDataSetChanged();
        }
    }

}
