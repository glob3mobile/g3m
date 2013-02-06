

package org.glob3.mobile.specific;

import java.awt.Cursor;
import java.io.ByteArrayOutputStream;
import java.io.File;

import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.GTask;
import org.glob3.mobile.generated.IByteBuffer;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IStorage;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;


public final class SQLiteStorage_JavaDesktop
    extends
      IStorage {

  private final String _databaseName;
  private final String _dirPath;


  private SQLiteDatabase _writeDB;
  private SQLiteDatabase _readDB;


  private String getPath() {
    final File f = new File(_dirPath);
    final String documentsDirectory = f.getAbsolutePath();
    final File f2 = new File(new File(documentsDirectory), _databaseName);

    final String path = f2.getAbsolutePath();
    ILogger.instance().logWarning("SQLiteStorage_JavaDesktop",
        "Creating DB in " + path);
    return path;
  }


  public SQLiteStorage_JavaDesktop(final String dirPath,
                                   final String databaseName) {
    _databaseName = databaseName;
    _dirPath = dirPath;


    _writeDB = _dbHelper.getWritableDatabase();
    _readDB = _dbHelper.getReadableDatabase();

    // _db = SQLiteDatabase.openOrCreateDatabase(getPath(), null);
    //
    //
    // if (_db == null) {
    // ILogger.instance().logError("SQL: Can't open database \"%s\"\n",
    // _databaseName);
    // }
    // else {
    // try {
    // _db.execSQL("CREATE TABLE IF NOT EXISTS buffer (name TEXT, contents TEXT);");
    // _db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS buffer_name ON buffer(name);");
    //
    // _db.execSQL("CREATE TABLE IF NOT EXISTS image (name TEXT, contents TEXT);");
    // _db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS image_name ON image(name);");
    // }
    // catch (final SQLException e) {
    // e.printStackTrace();
    // }
    // }
  }


  // @Override
  // public boolean containsBuffer(final URL url) {
  // final String name = url.getPath();
  // final Cursor cursor = _readDB.query("buffer2", new String[] { "1" },
  // "name = ?", new String[] { name }, null, null, null);
  // final boolean hasAny = (cursor.getCount() > 0);
  // cursor.close();
  // return hasAny;
  // }


  @Override
  public void saveBuffer(final URL url,
                         final IByteBuffer buffer,
                         final TimeInterval timeToExpires,
                         final boolean saveInBackground) {
    final String table = "buffer2";

    final byte[] contents = ((ByteBuffer_JavaDesktop) buffer).getBuffer().array();
    final String name = url.getPath();

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
    final ContentValues values = new ContentValues();
    values.put("name", name);
    values.put("contents", contents);
    final long expiration = System.currentTimeMillis()
                            + timeToExpires.milliseconds();
    values.put("expiration", Long.toString(expiration));

    if (_writeDB != null) {
      final long r = _writeDB.insertWithOnConflict(table, null, values,
          SQLiteDatabase.CONFLICT_REPLACE);
      if (r == -1) {
        ILogger.instance().logError(
            "SQL: Can't write " + table + " in database \"%s\"\n",
            _databaseName);
      }
    }
    else {
      ILogger.instance().logError(
          "SQL: Can't write " + table
              + " in database \"%s\". _writeDB not available\n", _databaseName);
    }
  }


  @Override
  public synchronized IByteBuffer readBuffer(final URL url) {
    ByteBuffer_JavaDesktop result = null;
    final String name = url.getPath();

    final Cursor cursor = _readDB.query( //
        "buffer2", //
        new String[] { "contents", "expiration" }, //
        "name = ?", //
        new String[] { name }, //
        null, //
        null, //
        null);
    if (cursor.moveToFirst()) {
      final byte[] data = cursor.getBlob(0);
      final String expirationS = cursor.getString(1);
      final long expirationInterval = Long.parseLong(expirationS);

      if (expirationInterval > System.currentTimeMillis()) {
        result = new ByteBuffer_JavaDesktop(data);
      }
    }
    cursor.close();

    return result;
  }


  // @Override
  // public boolean containsImage(final URL url) {
  // final String name = url.getPath();
  // final Cursor cursor = _readDB.query("image2", new String[] { "1" },
  // "name = ?", new String[] { name }, null, null, null);
  // final boolean hasAny = (cursor.getCount() > 0);
  // cursor.close();
  // return hasAny;
  // }


  @Override
  public void saveImage(final URL url,
                        final IImage image,
                        final TimeInterval timeToExpires,
                        final boolean saveInBackground) {
    // final ITimer timer = IFactory.instance().createTimer();

    final Image_JavaDesktop image_android = (Image_JavaDesktop) image;
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
    final String name = url.getPath();

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

    // final ContentValues values = new ContentValues();
    // values.put("name", name);
    // values.put("contents", contents);
    //
    // final long r = _db.insertWithOnConflict(table, null, values,
    // SQLiteDatabase.CONFLICT_REPLACE);
    // if (r == -1) {
    // ILogger.instance().logError("SQL: Can't write " + table +
    // " in database \"%s\"\n", _databaseName);
    // }

    // final TimeInterval elapsedTime = timer.elapsedTime();
    // System.out.println("** Saved image in " + elapsedTime.milliseconds()
    // + "ms");
    // IFactory.instance().deleteTimer(timer);
  }


  @Override
  public synchronized IImage readImage(final URL url) {
    IImage result = null;
    final String name = url.getPath();

    final Cursor cursor = _readDB.query( //
        "image2", //
        new String[] { "contents", "expiration" }, //
        "name = ?", //
        new String[] { name }, //
        null, //
        null, //
        null);
    if (cursor.moveToFirst()) {
      final byte[] data = cursor.getBlob(0);
      final String expirationS = cursor.getString(1);
      final long expirationInterval = Long.parseLong(expirationS);

      if (expirationInterval > System.currentTimeMillis()) {
        final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
            data.length);
        if (bitmap == null) {
          ILogger.instance().logError(
              "Can't create bitmap from content of storage");
        }
        else {
          result = new Image_Android(bitmap, null);
        }
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
