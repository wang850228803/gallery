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

public class ImageAdapter extends BaseAdapter {

    private LayoutInflater inflater;  
    private List<Picture> pictures;  
    
    public ImageAdapter(String[] titles, Integer[] images, Context context)  
    {  
        super();  
        pictures = new ArrayList<Picture>();  
        inflater = LayoutInflater.from(context);  
        for (int i = 0; i < images.length; i++) {  
            Picture picture = new Picture(titles[i], images[i]);  
            pictures.add(picture);  
        }   
    }  
    
    @Override  
    public int getCount() {  
        // TODO Auto-generated method stub  
        if (null != pictures) {  
            return pictures.size();  
        } else {  
            return 0;  
        }  
    }  
  
    @Override  
    public Object getItem(int position) {  
        // TODO Auto-generated method stub  
        System.out.println("--" + position);  
        return pictures.get(position);  
    }  
  
    @Override  
    public long getItemId(int position) {  
        // TODO Auto-generated method stub  
        System.out.println("--1---" + position);  
        return position;  
    } 
    
    public void addItem(String path){
        Picture picture = new Picture("add", path);  
        pictures.add(picture);
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
        viewHolder.title.setText(pictures.get(position).getTitle());  
        if(pictures.get(position).getImageId()==0){
            Bitmap bit = BitmapFactory.decodeFile(pictures.get(position).getPath());  
            viewHolder.image.setImageBitmap(bit); 
        } else {
            viewHolder.image.setImageResource(pictures.get(position).getImageId());  
        }
        return convertView;  
    }  
}  
  
class ViewHolder {  
    public TextView title;  
    public ImageView image;  
}  
  
class Picture {  
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
