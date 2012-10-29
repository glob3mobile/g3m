

package org.glob3.mobile.specific;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.glob3.mobile.generated.GTask;
import org.glob3.mobile.generated.IByteBuffer;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IStorage;
import org.glob3.mobile.generated.IThreadUtils;
import org.glob3.mobile.generated.InitializationContext;
import org.glob3.mobile.generated.URL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


public final class SQLiteStorage_Android
         implements
            IStorage {

   private final String             _databaseName;
   private final Context            _context;


   private final MySQLiteOpenHelper _dbHelper;
   private SQLiteDatabase           _writeDB;
   private SQLiteDatabase           _readDB;


   private class MySQLiteOpenHelper
            extends
               SQLiteOpenHelper {

      public MySQLiteOpenHelper(final Context context,
                                final String name) {
         super(context, name, null, 1);
      }


      private void createTables(final SQLiteDatabase db) {
         db.execSQL("CREATE TABLE IF NOT EXISTS buffer (name TEXT, contents TEXT);");
         db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS buffer_name ON buffer(name);");

         db.execSQL("CREATE TABLE IF NOT EXISTS image (name TEXT, contents TEXT);");
         db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS image_name ON image(name);");
      }


      @Override
      public void onCreate(final SQLiteDatabase db) {
         createTables(db);
      }


      @Override
      public void onUpgrade(final SQLiteDatabase db,
                            final int oldVersion,
                            final int newVersion) {
         createTables(db);
      }

   }


   private String getPath() {
      File f = _context.getExternalCacheDir();
      if ((f == null) || !f.exists()) {
         f = _context.getCacheDir();
      }
      final String documentsDirectory = f.getAbsolutePath();

      final File f2 = new File(new File(documentsDirectory), _databaseName);

      final String path = f2.getAbsolutePath();
      Log.d("SQLiteStorage_Android", "Creating DB in " + path);

      return path;
   }


   SQLiteStorage_Android(final String path,
                         final Context context) {
      _databaseName = path;
      _context = context;


      _dbHelper = new MySQLiteOpenHelper(context, getPath());
      _writeDB = _dbHelper.getWritableDatabase();
      _readDB = _dbHelper.getReadableDatabase();

      //      _db = SQLiteDatabase.openOrCreateDatabase(getPath(), null);
      //
      //
      //      if (_db == null) {
      //         ILogger.instance().logError("SQL: Can't open database \"%s\"\n", _databaseName);
      //      }
      //      else {
      //         try {
      //            _db.execSQL("CREATE TABLE IF NOT EXISTS buffer (name TEXT, contents TEXT);");
      //            _db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS buffer_name ON buffer(name);");
      //
      //            _db.execSQL("CREATE TABLE IF NOT EXISTS image (name TEXT, contents TEXT);");
      //            _db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS image_name ON image(name);");
      //         }
      //         catch (final SQLException e) {
      //            e.printStackTrace();
      //         }
      //      }
   }


   @Override
   public boolean containsBuffer(final URL url) {
      final String name = url.getPath();
      final Cursor cursor = _readDB.query("buffer", new String[] { "1" }, "name = ?", new String[] { name }, null, null, null);
      final boolean hasAny = (cursor.getCount() > 0);
      cursor.close();
      return hasAny;
   }


   @Override
   public void saveBuffer(final URL url,
                          final IByteBuffer buffer,
                          final boolean saveInBackground) {
      final String table = "buffer";

      final byte[] contents = ((ByteBuffer_Android) buffer).getBuffer().array();
      final String name = url.getPath();

      if (saveInBackground) {
         IThreadUtils.instance().invokeInBackground( //
                  new GTask() {
                     @Override
                     public void run() {
                        rawSave(table, name, contents);
                     }
                  }, //
                  true //
         );
      }
      else {
         rawSave(table, name, contents);
      }
   }


   private void rawSave(final String table,
                        final String name,
                        final byte[] contents) {
      final ContentValues values = new ContentValues();
      values.put("name", name);
      values.put("contents", contents);

      if (_writeDB != null) {
         final long r = _writeDB.insertWithOnConflict(table, null, values, SQLiteDatabase.CONFLICT_REPLACE);
         if (r == -1) {
            ILogger.instance().logError("SQL: Can't write " + table + "in database \"%s\"\n", _databaseName);
         }
      }
      else {
         ILogger.instance().logError("SQL: Can't write " + table + "in database \"%s\". _writeDB not available\n", _databaseName);
      }
   }


   @Override
   public IByteBuffer readBuffer(final URL url) {
      final String name = url.getPath();

      final Cursor cursor = _readDB.query("buffer", new String[] { "contents" }, "name = ?", new String[] { name }, null, null,
               null);

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
      final Cursor cursor = _readDB.query("image", new String[] { "1" }, "name = ?", new String[] { name }, null, null, null);
      final boolean hasAny = (cursor.getCount() > 0);
      cursor.close();
      return hasAny;
   }


   @Override
   public void saveImage(final URL url,
                         final IImage image,
                         final boolean saveInBackground) {
      //final ITimer timer = IFactory.instance().createTimer();

      final Image_Android image_android = (Image_Android) image;
      final Bitmap bitmap = image_android.getBitmap();

      byte[] contents = image_android.getSourceBuffer();
      if (contents == null) {
         final ByteArrayOutputStream baos = new ByteArrayOutputStream();
         bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
         contents = baos.toByteArray();
      }
      else {
         image_android.releaseSourceBuffer();
      }

      final String table = "image";
      final String name = url.getPath();

      final byte[] contentsF = contents;
      if (saveInBackground) {
         IThreadUtils.instance().invokeInBackground( //
                  new GTask() {
                     @Override
                     public void run() {
                        rawSave(table, name, contentsF);
                     }
                  }, //
                  true //
         );
      }
      else {
         rawSave(table, name, contents);
      }

      //      final ContentValues values = new ContentValues();
      //      values.put("name", name);
      //      values.put("contents", contents);
      //
      //      final long r = _db.insertWithOnConflict(table, null, values, SQLiteDatabase.CONFLICT_REPLACE);
      //      if (r == -1) {
      //         ILogger.instance().logError("SQL: Can't write " + table + " in database \"%s\"\n", _databaseName);
      //      }

      //final TimeInterval elapsedTime = timer.elapsedTime();
      //System.out.println("** Saved image in " + elapsedTime.milliseconds() + "ms");
      //IFactory.instance().deleteTimer(timer);
   }


   @Override
   public IImage readImage(final URL url) {
      IImage result = null;

      final String name = url.getPath();

      final Cursor cursor = _readDB.query("image", new String[] { "contents" }, "name = ?", new String[] { name }, null, null,
               null);

      if (cursor.moveToFirst()) {
         final byte[] data = cursor.getBlob(0);
         final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

         if (bitmap == null) {
            ILogger.instance().logError("Can't create bitmap from content of storage");
         }
         else {
            result = new Image_Android(bitmap, null);
         }
      }
      cursor.close();
      return result;
   }


   public synchronized void close() {
      if (_readDB != null) {
         _readDB.close();
         _readDB = null;
      }
      if (_writeDB != null) {
         _writeDB.close();
         _writeDB = null;
      }
   }


   @Override
   public synchronized void onResume(final InitializationContext ic) {
      if (_writeDB == null) {
         _writeDB = _dbHelper.getWritableDatabase();
      }
      if (_readDB == null) {
         _readDB = _dbHelper.getReadableDatabase();
      }
   }


   @Override
   public synchronized void onPause(final InitializationContext ic) {
      close();
   }


   @Override
   public synchronized boolean isAvailable() {
      return (_readDB != null) && (_writeDB != null);
   }

}
