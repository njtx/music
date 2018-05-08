package com.example.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper{

	public final static String DB_NAME = "musicplay.db";
	private static final int version=1;
	private static SQLiteDatabase db;
	private static DBOpenHelper helper;
	private static Context mContext;
	
	private static class DBOpenHelper extends SQLiteOpenHelper {
		
		public DBOpenHelper(Context context) {
			super(context, DB_NAME, null, version);
			// TODO Auto-generated constructor stub
			mContext = context;
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			//群组列表
			String sql_group = "CREATE TABLE tb_group (id integer NOT NULL PRIMARY KEY AUTOINCREMENT, groupname VARCHAR(512))";
			db.execSQL(sql_group);
			
			//自定义群组数据
			String sql_group_children = "CREATE TABLE tb_group_children (id integer NOT NULL PRIMARY KEY AUTOINCREMENT, path VARCHAR(512), groupid VARCHAR(12))";
			db.execSQL(sql_group_children);
			
			//播放历史
			String sql_history = "CREATE TABLE tb_history (id integer NOT NULL PRIMARY KEY AUTOINCREMENT, path VARCHAR(512))";
			db.execSQL(sql_history);
			
			//最近添加
			String sql_local = "CREATE TABLE tb_local (id integer NOT NULL PRIMARY KEY AUTOINCREMENT, path VARCHAR(512))";
			db.execSQL(sql_local);
			
			db.execSQL("insert into tb_group(groupname) values(?)", new Object[]{"多次播放的"}); 
			db.execSQL("insert into tb_group(groupname) values(?)", new Object[]{"最近播放的"}); 
			db.execSQL("insert into tb_group(groupname) values(?)", new Object[]{"最近添加的"}); 
			db.execSQL("insert into tb_group(groupname) values(?)", new Object[]{"新建列表"}); 
		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
			// TODO Auto-generated method stub
		}
	}
	
	public DBHelper(Context context) {
		// TODO Auto-generated constructor stub
		helper=new DBOpenHelper(context);
		if(db != null && db.isOpen()) db.close();
	}

	public void insertGroup(String group){
		db=helper.getWritableDatabase();
		ContentValues content=new ContentValues();
	    content.put("groupname",group);
		db.insert("tb_group", null, content);
		db.close();
	}
	
	public boolean isExistGroup(String group){
		db=helper.getWritableDatabase();
	    int i=0;
	    Cursor cursor=db.rawQuery("select * from tb_group where groupname=?",new String[]{group});
	    while(cursor.moveToNext()){
	    	i=i+1;
	    }
	    cursor.close();
	    db.close();
	    if(i>0) return true;
	    else return false;
	}
	
	public void deleteGroup(String groupid){
		db=helper.getWritableDatabase();
	    db.delete("tb_group", "id=?", new String[]{groupid});
	    db.close();
    }
	
	public void updateGroup(String group, String groupid){ 
		db=helper.getWritableDatabase();
		db.execSQL("update tb_group set groupname=? where id=?",
				new Object[]{group, groupid}); 
		db.close();
	}
	
	public List<GroupEntity> getAllGroup(){
		db=helper.getWritableDatabase();
		List<GroupEntity> list = new ArrayList<GroupEntity>();
		Cursor cursor=db.rawQuery("select * from tb_group order by id",null);
		while(cursor.moveToNext()){
			GroupEntity groupEntity = new GroupEntity();
			groupEntity.setGroupid(cursor.getString(cursor.getColumnIndex("id")));
			groupEntity.setGroup(cursor.getString(cursor.getColumnIndex("groupname")));
			list.add(groupEntity);
		}
		cursor.close();
		db.close();
		return list;
	}
	
	public List<GroupEntity> getAllGroupExceptDefault(){
		db=helper.getWritableDatabase();
		List<GroupEntity> list = new ArrayList<GroupEntity>();
		Cursor cursor=db.rawQuery("select * from tb_group order by id",null);
		while(cursor.moveToNext()){
			GroupEntity groupEntity = new GroupEntity();
			groupEntity.setGroupid(cursor.getString(cursor.getColumnIndex("id")));
			groupEntity.setGroup(cursor.getString(cursor.getColumnIndex("groupname")));
			if(!cursor.getString(cursor.getColumnIndex("id")).equals("1") && !cursor.getString(cursor.getColumnIndex("id")).equals("2")
					&& !cursor.getString(cursor.getColumnIndex("id")).equals("3")){	
				list.add(groupEntity);
			}
		}
		cursor.close();
		db.close();
		return list;
	}
	
	public void insertGroupChild(String groupid,String path){
		db=helper.getWritableDatabase();
		ContentValues content=new ContentValues();
	    content.put("path",path);
	    content.put("groupid",groupid);
		db.insert("tb_group_children", null, content);
		db.close();
	}
	
	public List<GroupChildEntity> getAllGroupChildrenByGroupId(String groupid){
		db=helper.getWritableDatabase();
		List<GroupChildEntity> list = new ArrayList<GroupChildEntity>();
		Cursor cursor=db.rawQuery("select * from tb_group_children order by id",null);
		while(cursor.moveToNext()){
			GroupChildEntity groupChildEntity = new GroupChildEntity();
			groupChildEntity.setGroupid(cursor.getString(cursor.getColumnIndex("groupid")));
			groupChildEntity.setId(cursor.getString(cursor.getColumnIndex("id")));
			groupChildEntity.setPath(cursor.getString(cursor.getColumnIndex("path")));
			if(groupid.equals(cursor.getString(cursor.getColumnIndex("groupid")))){
				list.add(groupChildEntity);
			}
		}
		cursor.close();
		db.close();
		return list;
	}
	
	public void insertHistory(String path){
		db=helper.getWritableDatabase();
		ContentValues content=new ContentValues();
	    content.put("path",path);
		db.insert("tb_history", null, content);
		db.close();
	}
	
	public List<String> getAllHistory(){
		db=helper.getWritableDatabase();
		List<String> list = new ArrayList<String>();
		Cursor cursor=db.rawQuery("select * from tb_history order by id",null);
		while(cursor.moveToNext()){
//			PathEntity historyEntity = new PathEntity();
//			historyEntity.setId(cursor.getString(cursor.getColumnIndex("id")));
//			historyEntity.setPath(cursor.getString(cursor.getColumnIndex("path")));
//			list.add(historyEntity);
			list.add(cursor.getString(cursor.getColumnIndex("path")));
		}
		cursor.close();
		db.close();
		return list;
	}
	
	public void insertLocal(String path){
		db=helper.getWritableDatabase();
		ContentValues content=new ContentValues();
	    content.put("path",path);
		db.insert("tb_local", null, content);
		db.close();
	}
	
	public List<String> getAllLocal(){
		db=helper.getWritableDatabase();
		List<String> list = new ArrayList<String>();
		Cursor cursor=db.rawQuery("select * from tb_local order by id",null);
		while(cursor.moveToNext()){
			list.add(cursor.getString(cursor.getColumnIndex("path")));
//			PathEntity pathEntity = new PathEntity();
//			pathEntity.setId(cursor.getString(cursor.getColumnIndex("id")));
//			pathEntity.setPath(cursor.getString(cursor.getColumnIndex("path")));
//			list.add(pathEntity);
		}
		cursor.close();
		db.close();
		return list;
	}
	
	public void clearAllLocal(){
		db=helper.getWritableDatabase();
	    String sql = "DELETE FROM tb_local;";
	    db.execSQL(sql);
		db.close();
	}
}
