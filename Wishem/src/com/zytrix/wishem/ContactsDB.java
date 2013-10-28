package com.zytrix.wishem;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class ContactsDB extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "contacts.db";
	private static final int DATABASE_VERSION = 1;
	
	public static final String TABLE = "init";
	
	public static final String NAME = "name";
	public static final String MOBILE = "mobile";
	
	public ContactsDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table " + TABLE + "( " + BaseColumns._ID
				+ " integer primary key autoincrement, " + NAME + " text not null, "
				+ MOBILE + " text not null);";
		
		db.execSQL(sql);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
