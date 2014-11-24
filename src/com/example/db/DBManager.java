package com.example.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
    private DBHelper helper;  
    private SQLiteDatabase db; 
      
    public DBManager(Context context) {  
        helper = new DBHelper(context);  
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);  
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里  
        db = helper.getWritableDatabase();  
    }  
      
    /** 
     * add persons 
     * @param persons 
     */  
    public void add(List<Photo> photos) {  
        db.beginTransaction();  //开始事务  
        try {  
            for (Photo photo : photos) {  
                db.execSQL("INSERT INTO photo VALUES(null, ?, ?, ?)", new Object[]{photo.getTitle(), photo.getImageid(), photo.getPath()});  
            }  
            db.setTransactionSuccessful();  //设置事务成功完成  
        } finally {  
            db.endTransaction();    //结束事务  
        }  
    }
    
    public void update(int id, String tit){
        db.execSQL("UPDATE photo SET title = '"+tit+"' WHERE _id="+id);
    }
    
    public void remove(int id){
        db.execSQL("DELETE FROM photo WHERE _id="+id);
    }
    
    public List<Photo> query() {  
        ArrayList<Photo> photos = new ArrayList<Photo>();  
        Cursor c = queryTheCursor();  
        while (c.moveToNext()) {  
            Photo person = new Photo();  
            person._id = c.getInt(c.getColumnIndex("_id"));  
            person.setTitle(c.getString(c.getColumnIndex("title")));  
            person.setImageid(c.getInt(c.getColumnIndex("imageid"))); 
            person.setPath(c.getString(c.getColumnIndex("path")));  
            photos.add(person);  
        }  
        c.close();  
        return photos;  
    }  
    
    public void deleteAll(){
        db.execSQL("DELETE FROM photo");
    }
    /** 
     * query all persons, return cursor 
     * @return  Cursor 
     */  
    public Cursor queryTheCursor() {  
        Cursor c = db.rawQuery("SELECT * FROM photo", null);  
        return c;  
    }  
      
    /** 
     * close database 
     */  
    public void closeDB() {  
        db.close();  
    }  
}
