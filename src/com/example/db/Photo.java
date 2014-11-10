package com.example.db;

import android.net.Uri;

public class Photo {
    public int _id;  
    public String title;  
    public int imageid;  
    public String path;  
      
    public Photo() {  
    }  
      
    public Photo(String title, int imageid) {  
        this.title = title;  
        this.imageid = imageid;  
    } 
    
    public Photo(String title, String path){
        this.title = title;
        this.path=path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageid() {
        return imageid;
    }

    public void setImageid(int imageid) {
        this.imageid = imageid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    
    
}
