package com.example.gallery;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.db.DBManager;
import com.example.db.Photo;

public class ImageAdapter extends BaseAdapter {

    private LayoutInflater inflater;  
    static List<Photo> photos;  
    private DBManager mgr;
    static int position;
    private Context context;
    ContentResolver resolver;
    
    
    public ImageAdapter(DBManager mgr, Context context)  
    {  
        super();  
        photos = new ArrayList<Photo>();  
        inflater = LayoutInflater.from(context); 
        this.mgr=mgr;
        this.context = context;
        resolver = context.getContentResolver();
    }  
    
   @Override  
    public int getCount() {  
        // TODO Auto-generated method stub  
        if (null != photos) {  
            return photos.size();  
        } else {  
            return 0;  
        }  
    }  
   
    @Override  
    public Photo getItem(int position) {  
        // TODO Auto-generated method stub  
        System.out.println("--" + position);  
        return photos.get(position);  
    }  
  
    @Override  
    public long getItemId(int position) {  
        // TODO Auto-generated method stub  
        System.out.println("--1---" + position);  
        return position;  
    } 
    
    public void refreshData(){
        photos=mgr.query();
    }
     
    public void addItem(Uri path){
        Photo photo = new Photo("add", path.toString());  
        List<Photo> pList=new ArrayList();
        pList.add(photo);
        mgr.add(pList);
        photos.add(photo);
    }
    
    public void removeItem(int position) {
        mgr.remove(photos.get(position)._id);
        photos.remove(position);
    }
    
    public void updateTitle(int position, String title){
        photos.get(position).setTitle(title);
        mgr.update(photos.get(position)._id, title);
    }
    @Override  
    public View getView(int position, View convertView, ViewGroup parent) {  
        // TODO Auto-generated method stub  
        
        ViewHolder viewHolder;  
        if (convertView == null) {  
            convertView = inflater.inflate(R.layout.picture_item, null);  
            viewHolder = new ViewHolder();  
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);  
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);  
            convertView.setTag(viewHolder);  
        } else {  
            viewHolder = (ViewHolder) convertView.getTag();  
        }  
        viewHolder.title.setText(photos.get(position).getTitle());  
        if(photos.get(position).getImageid()==0){
            loadBitmap(photos.get(position).getPath(), viewHolder.image);
        } else {
            viewHolder.image.setImageResource(photos.get(position).getImageid());  
        }
        return convertView;  
    } 
    
    public void loadBitmap(String path, ImageView imageView) {
        if (cancelPotentialWork(path, imageView)) {
            final BitmapWorkerTask task = new BitmapWorkerTask(context,path, imageView);
            final AsyncDrawable asyncDrawable =
                    new AsyncDrawable(context.getResources(), null, task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute();
        }
    }
    
    static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap,
                BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTaskReference =
                new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }
    }

    public static boolean cancelPotentialWork(String path, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final String bitmapData = bitmapWorkerTask.path;
            if (bitmapData != path) {
                // Cancel previous task
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
       if (imageView != null) {
           final Drawable drawable = imageView.getDrawable();
           if (drawable instanceof AsyncDrawable) {
               final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
               return asyncDrawable.getBitmapWorkerTask();
           }
        }
        return null;
    }

}  
  
class ViewHolder {  
    public TextView title;  
    public ImageView image;  
}  

class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewReference;
    String path;
    byte[] mContent;
    ContentResolver resolver;
    public BitmapWorkerTask(Context context, String path, ImageView imageView) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        imageViewReference = new WeakReference<ImageView>(imageView);
        this.path = path;
        resolver = context.getContentResolver();
    }

    // Decode image in background.
    @Override
    protected Bitmap doInBackground(Integer... params) {
        return decodeSampledBitmap(path, 100, 100);
    }

    // Once complete, see if ImageView is still around and set bitmap.
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
    
    public  Bitmap decodeSampledBitmap(String path, 
            int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        try {
            mContent = readStream(resolver.openInputStream(Uri.parse(path)));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inPreferredConfig  =  Bitmap .Config.RGB_565;    
        bmpFactoryOptions.inPurgeable  =  true ;   
        bmpFactoryOptions.inInputShareable  =  true ; 
        bmpFactoryOptions.inSampleSize = 30;  
        Bitmap bitmap = BitmapFactory.decodeByteArray(mContent, 0, mContent.length, bmpFactoryOptions);
        return bitmap;
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
