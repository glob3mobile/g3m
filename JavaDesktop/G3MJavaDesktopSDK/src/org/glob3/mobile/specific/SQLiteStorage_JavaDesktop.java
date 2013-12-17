

package org.glob3.mobile.specific;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

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


public final class SQLiteStorage_JavaDesktop
         extends
            IStorage {

   private final String             IMAGE_TABLE_NAME  = "image2";
   private final String             BUFFER_TABLE_NAME = "buffer2";


   private static final String[]    COLUMNS           = new String[] { "contents", "expiration" };
   private static final String      SELECTION         = "name = ?";

   private final String             _dirPath;
   private final String             _databaseName;

   private final MySQLiteOpenHelper _dbHelper;
   private SQLiteDatabase           _writeDB;
   private SQLiteDatabase           _readDB;


   private class SQLiteDatabase {

      private final Connection _connection;


      public SQLiteDatabase(final Connection connection) {
         _connection = connection;
      }


      public void execSQL(final String sql) {

      }


      public long insertWithOnConflict(final String table,
                                       final Map<String, Object> values) {
         long result = -1;
         PreparedStatement ps = null;
         try {
            ps = _connection.prepareStatement("INSERT INTO " + table + " (name,contents,expiration) VALUES (?,?,?)");

            ps.setString(0, (String) values.get("name"));
            ps.setBytes(1, (byte[]) values.get("contents"));
            ps.setString(2, (String) values.get(""));
            result = ps.executeUpdate();
            if (result > 0) {
               ILogger.instance().logInfo("Image Uploaded");
            }
         }
         catch (final Exception e) {
            ILogger.instance().logError(e.getMessage());
         }
         finally {
            try {
               if (ps != null) {
                  ps.close();
               }
               _connection.commit();
            }
            catch (final Exception e) {
               ILogger.instance().logError(e.getMessage());
            }
         }

         return result;
      }


      public ResultSet query(final String tableName,
                             final String[] columns,
                             final String selection,
                             final String[] strConditions) throws SQLException {
         final StringBuffer sql = new StringBuffer("SELECT ");
         boolean first = true;
         for (final String column : columns) {
            if (!first) {
               sql.append(",");
            }
            else {
               first = false;
            }
            sql.append(column);
         }
         sql.append(" FROM ");
         sql.append(tableName);
         sql.append(" WHERE ");
         sql.append(selection);


         final PreparedStatement stmnt = _connection.prepareStatement(sql.toString());
         for (int i = 0; i < strConditions.length; i++) {
            stmnt.setString(i, strConditions[i]);
         }
         return stmnt.executeQuery();
      }


      public void close() {
         try {
            _connection.close();
         }
         catch (final SQLException e) {
            ILogger.instance().logError(e.getMessage());
         }
      }
   }


   private class MySQLiteOpenHelper {

      public MySQLiteOpenHelper(final String path) {

         try {
            Class.forName("org.sqlite.JDBC");

            final Connection conexion = DriverManager.getConnection("jdbc:sqlite:" + path);
            conexion.setAutoCommit(false);
            final Statement stmtReadOnly = conexion.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            _readDB = new SQLiteDatabase(conexion);
            final Statement stmtWritable = conexion.createStatement();
            _writeDB = new SQLiteDatabase(conexion);

         }
         catch (final SQLException e) {
            ILogger.instance().logError(e.getMessage());
         }
         catch (final ClassNotFoundException e) {
            ILogger.instance().logError(e.getMessage());
         }
      }


      private void createTables(final SQLiteDatabase db) {
         db.execSQL("DROP TABLE IF EXISTS buffer;");
         db.execSQL("DROP TABLE IF EXISTS image;");

         db.execSQL("CREATE TABLE IF NOT EXISTS " + BUFFER_TABLE_NAME + " (name TEXT, contents TEXT, expiration TEXT);");
         db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS buffer_name ON " + BUFFER_TABLE_NAME + "(name);");

         db.execSQL("CREATE TABLE IF NOT EXISTS " + IMAGE_TABLE_NAME + " (name TEXT, contents TEXT, expiration TEXT);");
         db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS image_name ON " + IMAGE_TABLE_NAME + "(name);");
      }


      public SQLiteDatabase getWritableDatabase() {
         return _writeDB;
      }


      public SQLiteDatabase getReadableDatabase() {
         return _readDB;
      }
   }


   private String getPath() {
      return new File(_dirPath, _databaseName).getAbsolutePath();
   }


   SQLiteStorage_JavaDesktop(final String dirPath,
                             final String databaseName) {
      _dirPath = dirPath;
      _databaseName = databaseName;

      _dbHelper = new MySQLiteOpenHelper(getPath());
      _writeDB = _dbHelper.getWritableDatabase();
      _readDB = _dbHelper.getReadableDatabase();
   }


   @Override
   public void saveBuffer(final URL url,
                          final IByteBuffer buffer,
                          final TimeInterval timeToExpires,
                          final boolean saveInBackground) {

      final byte[] contents = ((ByteBuffer_JavaDesktop) buffer).getBuffer().array();
      final String name = url.getPath();

      if (saveInBackground) {
         _context.getThreadUtils().invokeInBackground( //
                  new GTask() {
                     @Override
                     public void run(final G3MContext context) {
                        rawSave(BUFFER_TABLE_NAME, name, contents, timeToExpires);
                     }
                  }, //
                  true);
      }
      else {
         rawSave(BUFFER_TABLE_NAME, name, contents, timeToExpires);
      }
   }


   private synchronized void rawSave(final String table,
                                     final String name,
                                     final byte[] contents,
                                     final TimeInterval timeToExpires) {
      final Map<String, Object> values = new HashMap<String, Object>();
      values.put("name", name);
      values.put("contents", contents);
      final long expiration = System.currentTimeMillis() + timeToExpires.milliseconds();
      values.put("expiration", Long.toString(expiration));

      if (_writeDB == null) {
         ILogger.instance().logError("SQL: Can't write " + table + " in database \"%s\". _writeDB not available\n", _databaseName);
      }
      else {
         final long rowID = _writeDB.insertWithOnConflict(table, values);
         if (rowID == -1) {
            ILogger.instance().logError("SQL: Can't write " + table + " in database \"%s\"\n", _databaseName);
         }
      }
   }


   @Override
   public IByteBufferResult readBuffer(final URL url,
                                       final boolean readExpired) {
      ByteBuffer_JavaDesktop buffer = null;
      boolean expired = false;
      final String name = url.getPath();

      ResultSet resultSet = null;
      try {
         resultSet = _readDB.query( // 
                  BUFFER_TABLE_NAME, //
                  COLUMNS, //
                  SELECTION, //
                  new String[] { name });
         if (resultSet.first()) {
            final byte[] data = resultSet.getBytes(0);
            final String expirationS = resultSet.getString(1);
            final long expirationInterval = Long.parseLong(expirationS);

            expired = (expirationInterval <= System.currentTimeMillis());
            if (!expired || readExpired) {
               buffer = new ByteBuffer_JavaDesktop(data);
            }
         }
      }
      catch (final NumberFormatException e) {
         ILogger.instance().logError(e.getMessage());
      }
      catch (final SQLException e) {
         ILogger.instance().logError(e.getMessage());
      }
      finally {
         if (resultSet != null) {
            try {
               resultSet.close();
            }
            catch (final SQLException e) {
               ILogger.instance().logError(e.getMessage());
            }
         }
      }

      return new IByteBufferResult(buffer, expired);
   }


   @Override
   public void saveImage(final URL url,
                         final IImage image,
                         final TimeInterval timeToExpires,
                         final boolean saveInBackground) {
      //final ITimer timer = IFactory.instance().createTimer();

      final Image_JavaDesktop image_specific = (Image_JavaDesktop) image;
      final BufferedImage bitmap = image_specific.getBufferedImage();

      byte[] contents = image_specific.getSourceBuffer();
      if (contents == null) {
         final ByteArrayOutputStream baos = new ByteArrayOutputStream();
         //TODO: bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
         contents = baos.toByteArray();
      }
      else {
         image_specific.releaseSourceBuffer();
      }

      final String name = url.getPath();

      final byte[] contentsF = contents;
      if (saveInBackground) {
         _context.getThreadUtils().invokeInBackground( //
                  new GTask() {
                     @Override
                     public void run(final G3MContext context) {
                        rawSave(IMAGE_TABLE_NAME, name, contentsF, timeToExpires);
                     }
                  }, //
                  true);
      }
      else {
         rawSave(IMAGE_TABLE_NAME, name, contents, timeToExpires);
      }
   }


   @Override
   public IImageResult readImage(final URL url,
                                 final boolean readExpired) {
      IImage image = null;
      boolean expired = false;
      final String name = url.getPath();

      ResultSet resultSet = null;

      try {
         resultSet = _readDB.query( //
                  IMAGE_TABLE_NAME, //
                  COLUMNS, //
                  SELECTION, //
                  new String[] { name });
         if (resultSet.first()) {
            final byte[] data = resultSet.getBytes(0);
            final String expirationS = resultSet.getString(1);
            final long expirationInterval = Long.parseLong(expirationS);

            expired = (expirationInterval <= System.currentTimeMillis());

            if (!expired || readExpired) {
               // final long start = System.currentTimeMillis();
               final InputStream in = new ByteArrayInputStream(data);
               final BufferedImage bImageFromConvert = ImageIO.read(in);
               // ILogger.instance().logInfo("CACHE: Bitmap parsed in " + (System.currentTimeMillis() - start) + "ms");
               in.close();
               if (bImageFromConvert == null) {
                  ILogger.instance().logError("Can't create bitmap from content of storage");
               }
               else {
                  image = new Image_JavaDesktop(bImageFromConvert, null);
               }
            }
         }
      }
      catch (final NumberFormatException e) {
         ILogger.instance().logError(e.getMessage());
      }
      catch (final SQLException e) {
         ILogger.instance().logError(e.getMessage());
      }
      catch (final IOException e) {
         ILogger.instance().logError(e.getMessage());
      }
      finally {
         if (resultSet != null) {
            try {
               resultSet.close();
            }
            catch (final SQLException e) {
               ILogger.instance().logError(e.getMessage());
            }
         }
      }

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
