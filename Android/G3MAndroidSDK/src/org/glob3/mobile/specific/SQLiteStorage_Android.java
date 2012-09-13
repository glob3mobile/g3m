

package org.glob3.mobile.specific;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.glob3.mobile.generated.IByteBuffer;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IStorage;
import org.glob3.mobile.generated.URL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


public class SQLiteStorage_Android
         implements
            IStorage {

   private final String         _databaseName;
   private final Context        _ctx;

   private final SQLiteDatabase _db;


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
   public boolean containsBuffer(final URL url) {
      final String name = url.getPath();
      final Cursor cursor = _db.query("entry", new String[] { "1" }, "name = ?", new String[] { name }, null, null, null);
      final boolean hasAny = (cursor.getCount() > 0);
      cursor.close();
      return hasAny;
   }


   @Override
   public void saveBuffer(final URL url,
                          final IByteBuffer buffer) {
      final byte[] data = ((ByteBuffer_Android) buffer).getBuffer().array();

      final ContentValues values = new ContentValues();
      values.put("name", url.getPath());
      values.put("contents", data);

      final long r = _db.insertWithOnConflict("entry", null, values, SQLiteDatabase.CONFLICT_REPLACE);
      if (r == -1) {
         ILogger.instance().logError("SQL", "Can't write in database \"%s\"\n", _databaseName);
      }
   }


   @Override
   public IByteBuffer readBuffer(final URL url) {
      final String name = url.getPath();

      final Cursor cursor = _db.query("entry", new String[] { "contents" }, "name = ?", new String[] { name }, null, null, null);

      if (cursor.moveToFirst()) {
         final byte[] data = cursor.getBlob(0);
         final ByteBuffer_Android bb = new ByteBuffer_Android(data);
         cursor.close();
         return bb;
      }
      cursor.close();
      return null;
   }


   @Override
   public boolean containsImage(final URL url) {
      final String name = url.getPath();
      final Cursor cursor = _db.query("entry", new String[] { "1" }, "name = ?", new String[] { name }, null, null, null);
      final boolean hasAny = (cursor.getCount() > 0);
      cursor.close();
      return hasAny;
   }


   @Override
   public void saveImage(final URL url,
                         final IImage image) {
      final Image_Android image_android = (Image_Android) image;
      final Bitmap bitmap = image_android.getBitmap();

      byte[] contents = image_android.getSourceBuffer();
      if (contents == null) {
         final ByteArrayOutputStream baos = new ByteArrayOutputStream();
         bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
         contents = baos.toByteArray();
      }
      else {
         image_android.releaseSourceBuffer();
      }

      final ContentValues values = new ContentValues();
      values.put("name", url.getPath());
      values.put("contents", contents);

      final long r = _db.insertWithOnConflict("entry", null, values, SQLiteDatabase.CONFLICT_REPLACE);
      if (r == -1) {
         ILogger.instance().logError("SQL", "Can't write image in database \"%s\"\n", _databaseName);
      }
   }


   @Override
   public IImage readImage(final URL url) {
      final String name = url.getPath();

      final Cursor cursor = _db.query("entry", new String[] { "contents" }, "name = ?", new String[] { name }, null, null, null);

      if (cursor.moveToFirst()) {
         final byte[] data = cursor.getBlob(0);
         final Bitmap b = BitmapFactory.decodeByteArray(data, 0, data.length);
         cursor.close();

         return new Image_Android(b, null);
      }
      cursor.close();
      return null;
   }

}
