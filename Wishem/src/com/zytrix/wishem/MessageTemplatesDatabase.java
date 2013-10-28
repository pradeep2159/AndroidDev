package com.zytrix.wishem;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class MessageTemplatesDatabase extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "message.db";
	private static final int DATABASE_VERSION = 1;

	SQLiteDatabase db;
	
	public static final String TABLE = "init";
	


	public static final String MESSAGE = "message";

	
	
	public MessageTemplatesDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table " + TABLE + "( " + BaseColumns._ID+ " integer primary key autoincrement, " + MESSAGE + " text not null);";

		db.execSQL(sql);
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	
	 public boolean deleterow(int rowId) 
	   {
		 SQLiteDatabase db = this.getWritableDatabase();
	       return db.delete(this.TABLE,  BaseColumns._ID +"=" +rowId, null) > 0;
	      
	       
	   }
	 
	 public boolean UpdateRow(int rowId,String msg) 
	   {
		
	 db = this.getWritableDatabase();
	
		 ContentValues args = new ContentValues();
		 args.put("MESSAGE", msg);
	      return db.update(this.TABLE,args, BaseColumns._ID +"=" +rowId,null) > 0;
	      
	      
	   }
	 


}

