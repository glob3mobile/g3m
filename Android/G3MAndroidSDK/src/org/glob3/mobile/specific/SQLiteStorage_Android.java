

package org.glob3.mobile.specific;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.GTask;
import org.glob3.mobile.generated.IByteBuffer;
import org.glob3.mobile.generated.IByteBufferResult;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.IImageResult;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IStorage;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


public final class SQLiteStorage_Android
         extends
            IStorage {

   private static final String[]         COLUMNS       = new String[] { "contents", "expiration" };
   private static final String           SELECTION     = "name = ?";

   private final String                  _databaseName;
   private final android.content.Context _androidContext;

   private final MySQLiteOpenHelper      _dbHelper;
   private SQLiteDatabase                _writeDB;
   private SQLiteDatabase                _readDB;

   private final BitmapFactory.Options   _options;
   private final byte[]                  _temp_storage = new byte[128 * 1024];


   private class MySQLiteOpenHelper
            extends
               SQLiteOpenHelper {

      public MySQLiteOpenHelper(final android.content.Context context,
                                final String name) {
         super(context, name, null, 1);
      }


      private void createTables(final SQLiteDatabase db) {
         db.execSQL("DROP TABLE IF EXISTS buffer;");
         db.execSQL("DROP TABLE IF EXISTS image;");

         db.execSQL("CREATE TABLE IF NOT EXISTS buffer2 (name TEXT, contents TEXT, expiration TEXT);");
         db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS buffer_name ON buffer2(name);");

         db.execSQL("CREATE TABLE IF NOT EXISTS image2 (name TEXT, contents TEXT, expiration TEXT);");
         db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS image_name ON image2(name);");
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
      File f = _androidContext.getExternalCacheDir();
      if ((f == null) || !f.exists()) {
         f = _androidContext.getCacheDir();
      }
      final String documentsDirectory = f.getAbsolutePath();

      final File f2 = new File(new File(documentsDirectory), _databaseName);

      final String path = f2.getAbsolutePath();
      Log.d("SQLiteStorage_Android", "Creating DB in " + path);

      return path;
   }


   public SQLiteStorage_Android(final String path,
                                final android.content.Context context) {
      _databaseName = path;
      _androidContext = context;

      _dbHelper = new MySQLiteOpenHelper(context, getPath());
      _writeDB = _dbHelper.getWritableDatabase();
      _readDB = _dbHelper.getReadableDatabase();

      _options = new BitmapFactory.Options();
      _options.inTempStorage = _temp_storage;
   }


   @Override
   public void saveBuffer(final URL url,
                          final IByteBuffer buffer,
                          final TimeInterval timeToExpires,
                          final boolean saveInBackground) {
      final String table = "buffer2";

      final byte[] contents = ((ByteBuffer_Android) buffer).getBuffer();
      final String name = url._path;

      if (saveInBackground) {
         _context.getThreadUtils().invokeInBackground( //
                  new GTask() {
                     @Override
                     public void run(final G3MContext context) {
                        rawSave(table, name, contents, timeToExpires);
                     }
                  }, //
                  true);
      }
      else {
         rawSave(table, name, contents, timeToExpires);
      }
   }


   private synchronized void rawSave(final String table,
                                     final String name,
                                     final byte[] contents,
                                     final TimeInterval timeToExpires) {
      final ContentValues values = new ContentValues(3);
      values.put("name", name);
      values.put("contents", contents);
      final long expiration = System.currentTimeMillis() + timeToExpires.milliseconds();
      values.put("expiration", Long.toString(expiration));

      if (_writeDB == null) {
         ILogger.instance().logError("SQL: Can't write " + table + " in database \"%s\". _writeDB not available\n", _databaseName);
      }
      else {
         final long rowID = _writeDB.insertWithOnConflict(table, null, values, SQLiteDatabase.CONFLICT_REPLACE);
         if (rowID == -1) {
            ILogger.instance().logError("SQL: Can't write " + table + " in database \"%s\"\n", _databaseName);
         }
      }
   }


   @Override
   public IByteBufferResult readBuffer(final URL url,
                                       final boolean readExpired) {
      ByteBuffer_Android buffer = null;
      boolean expired = false;
      final String name = url._path;

      final Cursor cursor = _readDB.query( // 
               "buffer2", //
               COLUMNS, //
               SELECTION, //
               new String[] { name }, //
               null, //
               null, //
               null);
      if (cursor.moveToFirst()) {
         final byte[] data = cursor.getBlob(0);
         final String expirationS = cursor.getString(1);
         final long expirationInterval = Long.parseLong(expirationS);

         expired = (expirationInterval <= System.currentTimeMillis());
         if (!expired || readExpired) {
            buffer = new ByteBuffer_Android(data);
         }
      }
      cursor.close();

      return new IByteBufferResult(buffer, expired);
   }


   @Override
   public void saveImage(final URL url,
                         final IImage image,
                         final TimeInterval timeToExpires,
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

      final String table = "image2";
      final String name = url._path;

      final byte[] contentsF = contents;
      if (saveInBackground) {
         _context.getThreadUtils().invokeInBackground( //
                  new GTask() {
                     @Override
                     public void run(final G3MContext context) {
                        rawSave(table, name, contentsF, timeToExpires);
                     }
                  }, //
                  true);
      }
      else {
         rawSave(table, name, contents, timeToExpires);
      }
   }


   @Override
   public IImageResult readImage(final URL url,
                                 final boolean readExpired) {
      IImage image = null;
      boolean expired = false;
      final String name = url._path;

      final Cursor cursor = _readDB.query( //
               "image2", //
               COLUMNS, //
               SELECTION, //
               new String[] { name }, //
               null, //
               null, //
               null);
      if (cursor.moveToFirst()) {
         final byte[] data = cursor.getBlob(0);
         final String expirationS = cursor.getString(1);
         final long expirationInterval = Long.parseLong(expirationS);

         expired = (expirationInterval <= System.currentTimeMillis());
         if (!expired || readExpired) {
            // final long start = System.currentTimeMillis();
            final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, _options);
            // ILogger.instance().logInfo("CACHE: Bitmap parsed in " + (System.currentTimeMillis() - start) + "ms");

            if (bitmap == null) {
               ILogger.instance().logError("Can't create bitmap from content of storage");
            }
            else {
               image = new Image_Android(bitmap, null);
            }
         }
      }
      cursor.close();

      return new IImageResult(image, expired);
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
   public synchronized void onResume(final G3MContext context) {
      if (_writeDB == null) {
         _writeDB = _dbHelper.getWritableDatabase();
      }
      if (_readDB == null) {
         _readDB = _dbHelper.getReadableDatabase();
      }
   }


   @Override
   public synchronized void onPause(final G3MContext context) {
      close();
   }


   @Override
   public synchronized void onDestroy(final G3MContext context) {
      close();
   }


   @Override
   public synchronized boolean isAvailable() {
      return (_readDB != null) && (_writeDB != null);
   }

}
