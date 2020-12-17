package com.nxgenminds.eduminds.ju.cms.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseBaseDao {

	private DataBaseHelper dbHelper;
	@SuppressWarnings("unused")
	private Context context;
	private SQLiteDatabase dataBase;
	
	public DataBaseBaseDao(Context context) {
		dbHelper = DataBaseHelper.getInstance(context);
		this.context = context;
	}

	public void open() {
		try{
			dataBase = dbHelper.getWritableDatabase();
		}catch(Exception e){
			dataBase = dbHelper.getReadableDatabase();
		}
	}


	public void close(){
		dataBase.close();
		dbHelper.close();
	}
	
	public long Insert(String tableName, ContentValues values) {
		return dataBase.insert(tableName, null, values);
	}

	public long Update(String tableName, ContentValues values,
			String whereClause, String[] whereArgs) {
		long rowCount = dataBase.update(tableName, values, whereClause,
				whereArgs);
		return rowCount;
	}

	public int Delete(String tableName, String whereClause, String[] whereArgs) {
		SQLiteDatabase dataBase = dbHelper.getReadableDatabase();
		int rowCount = dataBase.delete(tableName, whereClause, whereArgs);
		return rowCount;
	}

	public Cursor Query(String tableName, String selection,
			String[] SelectionArgs) {
		SQLiteDatabase dataBase = dbHelper.getReadableDatabase();
		Cursor cursor = dataBase.query(tableName, null, selection,
				SelectionArgs, null, null, null);
		cursor.moveToFirst();
		return cursor;
	}

	public Cursor Query(String tableName,String[] columns, String selection) 
	{
		SQLiteDatabase dataBase = dbHelper.getReadableDatabase();
		Cursor cursor = dataBase.query(tableName, columns, selection,
				null, null, null, null);
		cursor.moveToFirst();
		return cursor;
	}
	
	public Cursor Query(String tableName) {
		SQLiteDatabase dataBase = dbHelper.getReadableDatabase();
		Cursor cursor = dataBase.query(tableName, null, null,
				null, null, null, null);
		cursor.moveToFirst();
		return cursor;
	}
	
	public Cursor RawQuery(String Sql, String[] SelectionArgs) {
		SQLiteDatabase dataBase = dbHelper.getReadableDatabase();
		Cursor cursor = dataBase.rawQuery(Sql, SelectionArgs);
		cursor.moveToFirst();
		return cursor;
	}
	public Cursor Query(String tablename,String[] columns,String orderBy,String[] selection){
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		Cursor cursor = database.query(tablename, columns,orderBy,selection,null,null,null);
		cursor.moveToNext();
		return cursor;
	}
}
