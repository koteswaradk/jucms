package com.nxgenminds.eduminds.ju.cms.database;

import android.content.ContentValues;
import android.content.Context;

public class DataBaseBaseService {
	@SuppressWarnings("unused")
	private DataBaseBaseDao baseDao;
	@SuppressWarnings("unused")
	private Context context;
	ContentValues contentValues;

	public DataBaseBaseService(Context context) {
		baseDao = new DataBaseBaseDao(context);
		this.context = context;
	}
	
	
	
	

}

