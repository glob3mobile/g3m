

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
import android.util.Log;


public class SQLiteStorage_Android
         implements
            IStorage {

   private final String  _databaseName;
   private final Context _ctx;

   SQLiteDatabase        _db;


   String getPath() {
      File f = _ctx.getExternalCacheDir();
      if (!f.exists()) {
         f = _ctx.getCacheDir();
      }
      final String documentsDirectory = f.getAbsolutePath();

      final File f2 = new File(new File(documentsDirectory), _databaseName);

      final String path = f2.getAbsolutePath();
      Log.d("SQLiteStorage_Android", "Creating DB in " + path);

      return path;
   }


   SQLiteStorage_Android(final String path,
                         final Context ctx) {
      _databaseName = path;
      _ctx = ctx;

      _db = SQLiteDatabase.openOrCreateDatabase(getPath(), null);

      if (_db == null) {
         ILogger.instance().logError("SQL", "Can't open database \"%s\"\n", _databaseName);
      }
      else {
         try {
            _db.execSQL("CREATE TABLE IF NOT EXISTS entry (name TEXT, contents BLOB);");
            _db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS entry_name ON entry(name);");
         }
         catch (final SQLException e) {
            e.printStackTrace();
         }
      }
   }


   @Override
   public boolean contains(final URL url) {
      final String name = url.getPath();

      final Cursor cursor = _db.query("entry", new String[] { "1" }, "name = ?", new String[] { name }, null, null, null);

      final boolean hasAny = (cursor.getCount() > 0);

      cursor.close();

      return hasAny;
   }


   @Override
   public void save(final URL url,
                    final ByteBuffer buffer) {
      final ContentValues values = new ContentValues();
      values.put("name", url.getPath());
      values.put("contents", buffer.getData());

      final long r = _db.insertWithOnConflict("entry", null, values, SQLiteDatabase.CONFLICT_REPLACE);
      if (r == -1) {
         ILogger.instance().logError("SQL", "Can't write in database \"%s\"\n", _databaseName);
      }
   }


   @Override
   public ByteBuffer read(final URL url) {
      final String name = url.getPath();

      final Cursor cursor = _db.query("entry", new String[] { "contents" }, "name = ?", new String[] { name }, null, null, null);

      if (cursor.moveToFirst()) {
         final byte[] data = cursor.getBlob(0);
         final ByteBuffer bb = new ByteBuffer(data, data.length);
         cursor.close();
         return bb;
      }
      cursor.close();
      return null;
   }

}
