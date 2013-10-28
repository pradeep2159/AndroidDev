package com.zytrix.wishem;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class MessageThreadDatabase extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "threads.db";
	private static final int DATABASE_VERSION = 1;

	public static final String TABLE = "threads";
	SQLiteDatabase db;
	public static final String UID = "uid";
	public static final String NUMBER = "number";
	public static final String FREQ = "freq";
	public static final String STATUS = "status";
	public static final String MESSAGE = "message";

	public MessageThreadDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String msql = "create table " + TABLE + "( " + BaseColumns._ID+ " integer primary key autoincrement, " + UID + " text not null, " + NUMBER + " text not null, " + FREQ + " text not null, " + STATUS + " text not null, " + MESSAGE + " text not null);";
		db.execSQL(msql);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	public boolean deleterow(int rowId) 
	{
		SQLiteDatabase db = this.getWritableDatabase();
		return db.delete(MessageThreadDatabase.TABLE, UID +"=" +rowId, null) > 0;

	}

	public boolean UpdateStatus(String rowId,String stat) 
	{

		db = this.getWritableDatabase();

		ContentValues args1 = new ContentValues();
		args1.put("STATUS", stat);
		return db.update(this.TABLE,args1, UID +"=" +rowId,null) > 0;


	}

}
