package com.example.gallery;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    private List<Photo> photos;  
    private DBManager mgr;
    
    public ImageAdapter(DBManager mgr, Context context)  
    {  
        super();  
        photos = new ArrayList<Photo>();  
        inflater = LayoutInflater.from(context); 
        this.mgr=mgr;
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
     
    public void addItem(String path){
        Photo photo = new Photo("add", path);  
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
            Bitmap bit = BitmapFactory.decodeFile(photos.get(position).getPath());  
            viewHolder.image.setImageBitmap(bit); 
        } else {
            viewHolder.image.setImageResource(photos.get(position).getImageid());  
        }
        return convertView;  
    }  
}  
  
class ViewHolder {  
    public TextView title;  
    public ImageView image;  
}  
  
/*class Picture {  
    private String title;  
    private int imageId; 
    private String path;
  
    public Picture() {  
        super();  
    }  
  
    public Picture(String title, int imageId) {  
        super();  
        this.title = title;  
        this.imageId = imageId;  
    }  
    
    public Picture(String title, String path){
        this.title=title;
        this.path=path;
    }
  
    public String getTitle() {  
        return title;  
    }  
  
    public void setTitle(String title) {  
        this.title = title;  
    }  
  
    public int getImageId() {  
        return imageId;  
    }  
  
    public void setImageId(int imageId) {  
        this.imageId = imageId;  
    }  
    public String getPath() {  
        return path;  
    }  
  
    public void setPath(String path) {  
        this.path = path;  
    }
}
*/