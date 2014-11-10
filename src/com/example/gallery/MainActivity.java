
package com.example.gallery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.db.DBManager;
import com.example.db.Photo;


//test
public class MainActivity extends Activity implements OnItemLongClickListener{
    
    private ImageAdapter mAdapter;
    private GridView gridview;
    private int selectedPosition;
    private String TAG="MAIN";
    private DBManager mgr;
    List<Photo> photos;
    
    private static final int REQUISTE_CODE=1;
    private static final int REQUEST_CODE_EDIT=2;
//    private boolean mReturningWithResult = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);  
        gridview=(GridView)findViewById(R.id.gridview);//找到activity_main.xml中定义gridview 的id
        mgr=new DBManager(this);
        mAdapter=new ImageAdapter(mgr, this);
        mAdapter.refreshData();
        gridview.setAdapter(mAdapter);//调用ImageAdapter.java  
        gridview.setOnItemClickListener(listener); 
        registerForContextMenu(gridview); //为GirdView对象注册快捷菜单
        gridview.setOnItemLongClickListener(this); //为GirdView注册长按事件
    }
    
    AdapterView.OnItemClickListener listener=new AdapterView.OnItemClickListener(){//监听事件  
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            /*Intent intent0=new Intent(MainActivity.this, ShowImageActivity.class);
            Bundle b=new Bundle();
            Photo pho=mAdapter.getItem(position);
            if(pho.getImageid()!=0){
                b.putInt("imageid", pho.getImageid());
            } else {
                b.putString("path", pho.getPath());
            }
            intent0.putExtras(b);
            ActivityOptions opts = ActivityOptions.makeCustomAnimation(MainActivity.this,
                    R.anim.zoom_enter, R.anim.zoom_enter);
            // Request the activity be started, using the custom animation options.
            startActivity(intent0, opts.toBundle());*/
            Intent intent0=new Intent(MainActivity.this, ImageViewActivity.class);
            Bundle b=new Bundle();
            b.putInt("position", position);
            intent0.putExtras(b);
           // ActivityOptions opts = ActivityOptions.makeCustomAnimation(MainActivity.this,
                  //  R.anim.zoom_enter, R.anim.zoom_enter);
            // Request the activity be started, using the custom animation options.
            //startActivity(intent0, opts.toBundle());
            startActivity(intent0);
        }  
       };
       
       /**
       * 记录手指所按的position，
       * 返回值为false，不能是true否则不会在执行onCreateContextMenu函数
       */
           @Override
           public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
                   long arg3) {
               // TODO Auto-generated method stub
               selectedPosition = arg2;
               return false;
           }
       
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
        switch(id){
            case R.id.add_sd:
                //Intent addIntent=new Intent(this, SDlist.class);
                //startActivityForResult(addIntent, REQUISTE_CODE);
                Intent picture = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(picture,REQUISTE_CODE);
                break;
            case R.id.clear:
                mgr.deleteAll();mAdapter.refreshData();
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.photo_init:
                photos=new ArrayList<Photo>();
                for (int i = 0; i < mThumbIds.length; i++) {
                    photos.add(new Photo(i+"", mThumbIds[i]));
                }
                mgr.add(photos);
                mAdapter.refreshData();
                mAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {

        // The data from the menu item.
        AdapterView.AdapterContextMenuInfo info;

        // Tries to get the position of the item in the ListView that was long-pressed.
        try {
            // Casts the incoming data object into the type for AdapterView objects.
            info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        } catch (ClassCastException e) {
            // If the menu object can't be cast, logs an error.
            Log.e(TAG, "bad menuInfo", e);
            return;
        }

        /*
         * Gets the data associated with the item at the selected position. getItem() returns
         * whatever the backing adapter of the ListView has associated with the item. In NotesList,
         * the adapter associated all of the data for a note with its list item. As a result,
         * getItem() returns that data as a Cursor.
         */
        Photo photo = (Photo) gridview.getAdapter().getItem(selectedPosition);

        // If the cursor is empty, then for some reason the adapter can't get the data from the
        // provider, so returns null to the caller.
        if (photo == null) {
            // For some reason the requested item isn't available, do nothing
            return;
        }

        // Inflate menu from XML resource
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_context_menu, menu);
        
        menu.setHeaderTitle(photo.getTitle());

        /*// Append to the
        // menu items for any other activities that can do stuff with it
        // as well.  This does a query on the system for any activities that
        // implement the ALTERNATIVE_ACTION for our data, adding a menu item
        // for each one that is found.
        Intent intent = new Intent(null, Uri.withAppendedPath(getIntent().getData(), 
                                        Integer.toString((int) info.id) ));
        intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
        menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, 0,
                new ComponentName(this, NotesList.class), null, intent, 0, null);*/
    }
    
    public boolean onContextItemSelected(MenuItem item) {
        // The data from the menu item.
        AdapterView.AdapterContextMenuInfo info;

        /*
         * Gets the extra info from the menu item. When an note in the Notes list is long-pressed, a
         * context menu appears. The menu items for the menu automatically get the data
         * associated with the note that was long-pressed. The data comes from the provider that
         * backs the list.
         *
         * The note's data is passed to the context menu creation routine in a ContextMenuInfo
         * object.
         *
         * When one of the context menu items is clicked, the same data is passed, along with the
         * note ID, to onContextItemSelected() via the item parameter.
         */
        try {
            // Casts the data object in the item into the type for AdapterView objects.
            info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        } catch (ClassCastException e) {

            // If the object can't be cast, logs an error
            Log.e(TAG, "bad menuInfo", e);

            // Triggers default processing of the menu item.
            return false;
        }

        /*
         * Gets the menu item's ID and compares it to known actions.
         */
        switch (item.getItemId()) {

        case R.id.context_delete:
            mAdapter.removeItem(selectedPosition);
            mAdapter.notifyDataSetChanged();
            return true;
        case R.id.edit_title:
            Intent editIntent=new Intent(this, TitleEditor.class);
            editIntent.putExtra("title", mAdapter.getItem(selectedPosition).getTitle());
            startActivityForResult(editIntent, REQUEST_CODE_EDIT);
        default:
            return super.onContextItemSelected(item);
        }
    }

        
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
//        mReturningWithResult = true;
        if(requestCode==REQUISTE_CODE && resultCode == RESULT_OK){
            //Bundle b=data.getExtras();
//            String sdPath=b.getString("path");
            Uri imageUri= data.getData();
            Toast.makeText(this, imageUri+"", Toast.LENGTH_SHORT)
            .show();
        
        mAdapter.addItem(imageUri);
        mAdapter.notifyDataSetChanged();
        }
        
        if (requestCode==REQUEST_CODE_EDIT && resultCode == RESULT_OK){
            String nTitle=(String)data.getExtras().get("newtitle");
            Log.i(TAG, nTitle);
            mAdapter.updateTitle(selectedPosition, nTitle);
            mAdapter.notifyDataSetChanged();
        }
    }

}
