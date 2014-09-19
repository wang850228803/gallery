package com.example.gallery;

import android.net.Uri;
import android.provider.BaseColumns;

public final class Image {

    public static final String AUTHORITY = "com.example.gallery.Image";
    
    private Image () {}
    
    public static final class ImageColumns implements BaseColumns{
        
        private ImageColumns () {}
        
        public static final Uri CONTENT_URI=Uri.parse("content://"+AUTHORITY+"/images");
        
        public static final String TITLE="title";
        
        public static final String IMAGEID="imageid";
        
        public static final String DEFAULT_ORDER="title ASC";
    } 
}
