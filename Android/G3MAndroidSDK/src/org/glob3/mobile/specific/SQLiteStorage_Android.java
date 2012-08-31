package org.glob3.mobile.specific;

import java.io.File;

import org.glob3.mobile.generated.ByteBuffer;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IStorage;
import org.glob3.mobile.generated.URL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteStorage_Android implements IStorage {
	
	private final String _databaseName;
	
	SQLiteDatabase _db;
	
	SQLiteStorage_Android(String path, Context ctx){
		_databaseName = path;
		
		StorageSQLHelper helper = new StorageSQLHelper(ctx, _databaseName, null, 1);
		_db = helper.getWritableDatabase();

		if (_db != null) {
		    ILogger.instance().logError("SQL", "Can't open database \"%s\"\n", _databaseName);
		}
	}

	@Override
	public boolean contains(URL url) {
		  String name = url.getPath();
		  
		  Cursor cursor = _db.query("entry", new String [] {"contents"}, "name = " + name, 
				  null, null, null, null);
		  
		  boolean hasAny = (cursor.getCount() > 0);
		  
		  cursor.close();
		  
		  return hasAny;
	}

	@Override
	public void save(URL url, ByteBuffer buffer) {
		ContentValues cv = new ContentValues();
		cv.put("name", url.getPath());
		//cv.put("contents", buffer.get)

	}

	@Override
	public ByteBuffer read(URL url) {
		// TODO Auto-generated method stub
		return null;
	}

}

class StorageSQLHelper extends SQLiteOpenHelper{

	public StorageSQLHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE IF NOT EXISTS entry (name TEXT, contents TEXT);");
			db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS entry_name ON entry(name);");
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		int needed_questionmark;
	}
	
}
