package com.yalong;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDatabase {

	private static final String DATABASE_TABLE = "Preferences";
	public static final String KEY_ROWID = "_id";
	public static final String KEY_USERDATA = "userdata";
	public static final String KEY_USERDATA_VALUE = "userdata_value";
	
	private static final String DATABASE_NAME = "UserDatabase";	
	private static final int DATABASE_VERSION = 1;
	
	private DBHelper myHelper;
	private final Context myContext;
	private SQLiteDatabase myDatabase;
	
	private static class DBHelper extends SQLiteOpenHelper{

		public DBHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" +
					KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					KEY_USERDATA + " TEXT NOT NULL, " +
					KEY_USERDATA_VALUE + " TEXT NOT NULL);"								
			);
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}
		
	}
	
	public UserDatabase(Context c){
		myContext = c;
	}
	
	public UserDatabase open() throws SQLException{
		myHelper = new DBHelper(myContext);
		myDatabase = myHelper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		myHelper.close();
	}
	
	public long createEntry(String userdata, String userdataValue) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put(KEY_USERDATA, userdata);
		cv.put(KEY_USERDATA_VALUE, userdataValue);
		return myDatabase.insert(DATABASE_TABLE, null, cv);
	}
	
	public long createEntryWithID(long l, String userdata, String userdataValue) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put(KEY_ROWID, l);
		cv.put(KEY_USERDATA, userdata);
		cv.put(KEY_USERDATA_VALUE, userdataValue);
		return myDatabase.insert(DATABASE_TABLE, null, cv);
	}
	
	/**
	 * Lire les données en base de données avec un curseur
	 * @return
	 */
	public String getData() {
		// TODO Auto-generated method stub
		String[] columns = new String[]{KEY_ROWID, KEY_USERDATA, KEY_USERDATA_VALUE};
		Cursor c = myDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);
		String result = "";
		
		int iRow = c.getColumnIndex(KEY_ROWID);
		int iUserdata = c.getColumnIndex(KEY_USERDATA);
		int iUserdataValue = c.getColumnIndex(KEY_USERDATA_VALUE);
		
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			result += c.getString(iRow) + " " + c.getString(iUserdata) + " " + c.getString(iUserdataValue) + "\n";
		}
		
		return result;
	}
	
	public String getUserdata(long l) throws SQLException{
		// TODO Auto-generated method stub
		String[] columns = new String[]{KEY_ROWID, KEY_USERDATA, KEY_USERDATA_VALUE};
		Cursor c = myDatabase.query(DATABASE_TABLE, columns, KEY_ROWID + "=" + l, null, null, null, null);
		if(c != null){
			c.moveToFirst();
			String userdata = c.getString(1);
			return userdata;
		}
		return null;
	}
	
	public String getUserdataValue(long l) throws SQLException{
		// TODO Auto-generated method stub
		String[] columns = new String[]{KEY_ROWID, KEY_USERDATA, KEY_USERDATA_VALUE};
		Cursor c = myDatabase.query(DATABASE_TABLE, columns, KEY_ROWID + "=" + l, null, null, null, null);
		if(c != null){
			c.moveToFirst();
			String userdataValue = c.getString(2);
			return userdataValue;
		}
		return null;
	}
	
	public void updateEntry(long lRow, String sUserdata, String sUserdataValue) throws SQLException{
		// TODO Auto-generated method stub
		ContentValues cvUpdate = new ContentValues();
		cvUpdate.put(KEY_USERDATA, sUserdata);
		cvUpdate.put(KEY_USERDATA_VALUE, sUserdataValue);
		myDatabase.update(DATABASE_TABLE, cvUpdate, KEY_ROWID + "=" + lRow, null);
	}
	
	public void deleteEntry(long lRow1) throws SQLException{
		// TODO Auto-generated method stub
		myDatabase.delete(DATABASE_TABLE, KEY_ROWID + "=" + lRow1, null);		
	}
	
}
