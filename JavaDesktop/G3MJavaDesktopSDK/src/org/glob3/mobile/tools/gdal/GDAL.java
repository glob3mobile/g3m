

package org.glob3.mobile.tools.gdal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.glob3.mobile.tools.commandline.core.CommandLine;
import org.glob3.mobile.tools.commandline.core.CommandLine.StreamGobbler;
import org.glob3.mobile.tools.utils.FileUtils;


public class GDAL {

   private static GDAL   _gdal;


   private static String GDAL_HOME = System.getenv("GDAL_HOME");


   public static synchronized GDAL instance() {
      return _gdal;
   }


   /**
    * @return the gdalHome
    * @throws GDALException
    */
   public static void initialize(final String gdalHome) throws GDALException {


      //      if ((gdalHome == null) || (gdalHome.trim().length() == 0)) {
      //         throw new GDALException("This GDAL_HOME isn't valid", null);
      //      }
      GDAL_HOME = (gdalHome == null) ? "" : gdalHome;
      if (!checkGdal()) {
         throw new GDALException("GDAL isn't install properly", null);
      }

      if (_gdal == null) {
         _gdal = new GDAL();
      }

      instance();
   }


   private static boolean checkGdal() throws GDALException {

      final String[] cmd = new String[2];
      cmd[0] = "type";
      cmd[1] = GDAL_HOME + GDALCommands.srsInfo;

      //      ExecResult sb = null;
      //      try {
      //         sb = CommandLineUtils.getInstance().exec(cmd);
      //         if ((sb.getType() == ExecResultType.OUTPUT) && sb.getResult().startsWith(GDAL_HOME + GDALCommands.srsInfo + " is ")) {
      //            return true;
      //         }
      //      }
      //      catch (final IOException e) {
      //         throw new GDALException(e.getMessage(), e.getCause());
      //      }
      //      catch (final InterruptedException e) {
      //         throw new GDALException(e.getMessage(), e.getCause());
      //      }

      StreamGobbler sb = null;
      try {
         sb = CommandLine.getInstance().execute(cmd);
         if ((sb.getType() == StreamGobbler.streamGobblerType.OUTPUT)
             && sb.getResult().startsWith(GDAL_HOME + GDALCommands.srsInfo + " is ")) {
            return true;
         }
      }
      catch (final IOException e) {
         throw new GDALException(e.getMessage(), e.getCause());
      }
      catch (final InterruptedException e) {
         throw new GDALException(e.getMessage(), e.getCause());
      }
      finally {
         if (sb != null) {
            sb.destroy();
         }
      }

      return false;
   }


   public static boolean isInitialized() {
      return (_gdal == null) ? false : true;
   }


   public boolean validateSRS(final File file) throws GDALException {
      if ((file != null) && file.exists() && file.isFile()) {

         final String[] cmd = new String[5];
         cmd[0] = GDAL_HOME + GDALCommands.srsInfo;
         cmd[1] = "-v";
         cmd[2] = file.getAbsolutePath();
         cmd[3] = "-o";
         cmd[4] = "proj4";

         StreamGobbler sb = null;
         try {
            sb = CommandLine.getInstance().execute(cmd);

            if ((sb.getType() == StreamGobbler.streamGobblerType.OUTPUT) && sb.getResult().startsWith("Validate Succeeds")) {
               return true;
            }
         }
         catch (final IOException e) {
            throw new GDALException(e.getMessage(), e.getCause());
         }
         catch (final InterruptedException e) {
            throw new GDALException(e.getMessage(), e.getCause());

         }
         finally {
            if (sb != null) {
               sb.destroy();
            }
         }
      }
      return false;
   }


   public boolean ogrInfo(final File file) throws GDALException {
      if ((file != null) && file.exists() && file.isFile()) {

         final String[] cmd = new String[3];
         cmd[0] = GDAL_HOME + GDALCommands.ogrinfo;
         cmd[1] = "-ro";
         cmd[2] = file.getAbsolutePath();


         StreamGobbler sb = null;
         try {
            sb = CommandLine.getInstance().execute(cmd);
            if ((sb.getType() == StreamGobbler.streamGobblerType.OUTPUT) && sb.getResult().contains("successful")) {
               return true;
            }
         }
         catch (final IOException e) {
            throw new GDALException(e.getMessage(), e.getCause());
         }
         catch (final InterruptedException e) {
            throw new GDALException(e.getMessage(), e.getCause());
         }
         finally {
            if (sb != null) {
               sb.destroy();
            }
         }

      }
      return false;
   }


   //   public File vector2GeoJSON(final File inputFile,
   //                              final File outputDir,
   //                              final String outputFileName) throws GDALException {
   //      vector2GeoJSON(inputFile, outputDir, outputFileName, "");
   //
   //
   //      if (FileUtils.checkFile(inputFile) && FileUtils.checkDir(outputDir)) {
   //         File outputFile = new File(outputDir, outputFileName + ".geojson");
   //         int i = 1;
   //         while (outputFile.exists()) {
   //            outputFile = new File(outputDir, outputFileName + "(" + i + ").geojson");
   //            i++;
   //         }
   //         final String[] cmd = new String[7];
   //         cmd[0] = GDAL_HOME + GDALCommands.ogr2ogr;
   //         cmd[1] = "-f";
   //         cmd[2] = "GeoJSON";
   //         cmd[3] = "-t_srs";
   //         cmd[4] = "EPSG:4326";
   //         cmd[5] = outputFile.getAbsolutePath();
   //         cmd[6] = inputFile.getAbsolutePath();
   //
   //         try {
   //            final StreamGobbler sb = CommandLineUtils.getInstance().execute(cmd);
   //            System.out.println(sb.getResult());
   //            return outputFile;
   //         }
   //         catch (final IOException e) {
   //            throw new GDALException(e.getMessage(), e.getCause());
   //         }
   //         catch (final InterruptedException e) {
   //            throw new GDALException(e.getMessage(), e.getCause());
   //         }
   //      }
   //
   //      return null;
   //   }


   public File vector2GeoJSON(final File inputFile,
                              final File outputDir,
                              final String outputFileName,
                              final String... options) throws GDALException {
      if (FileUtils.checkFile(inputFile) && FileUtils.checkDir(outputDir)) {
         File outputFile = new File(outputDir, outputFileName + ".geojson");
         int i = 1;
         while (outputFile.exists()) {
            outputFile = new File(outputDir, outputFileName + "(" + i + ").geojson");
            i++;
         }


         final String[] cmd = new String[7 + options.length];
         cmd[0] = GDAL_HOME + GDALCommands.ogr2ogr;
         cmd[1] = "-f";
         cmd[2] = "GeoJSON";
         cmd[3] = "-t_srs";
         cmd[4] = "EPSG:4326";
         cmd[5] = outputFile.getAbsolutePath();
         cmd[6] = inputFile.getAbsolutePath();
         for (int j = 0; j < options.length; j++) {
            cmd[7 + j] = options[j];
         }

         StreamGobbler sb = null;
         try {
            sb = CommandLine.getInstance().execute(cmd);
            System.out.println(sb.getResult());
            return outputFile;
         }
         catch (final IOException e) {
            throw new GDALException(e.getMessage(), e.getCause());
         }
         catch (final InterruptedException e) {
            throw new GDALException(e.getMessage(), e.getCause());
         }
         finally {
            if (sb != null) {
               sb.destroy();
            }
         }
      }

      return null;
   }


   public File xyz2JSON(final File inputFile,
                        final File outputDir,
                        final String outputFileName) throws GDALException {
      if (FileUtils.checkFile(inputFile) && FileUtils.checkDir(outputDir)) {
         File outputFile = new File(outputDir, outputFileName + ".json");
         int i = 1;
         while (outputFile.exists()) {
            outputFile = new File(outputDir, outputFileName + "(" + i + ").json");
            i++;
         }

         try {
            if (outputFile.createNewFile()) {
               final FileInputStream fstream = new FileInputStream(inputFile);
               final BufferedWriter out = new BufferedWriter(new FileWriter(outputFile));

               final DataInputStream in = new DataInputStream(fstream);
               final BufferedReader br = new BufferedReader(new InputStreamReader(in));
               String strLine;

               final int factor = 10;

               out.write("{\"points\": [");
               //Read File Line By Linel
               boolean firstComma = true;
               int k = 0;
               while ((strLine = br.readLine()) != null) {
                  // Print the content on the console
                  System.out.println("" + strLine);
                  if (firstComma) {
                     out.write(strLine);
                     firstComma = false;
                  }
                  else {
                     if ((k % factor) == 0) {
                        out.write("," + strLine);
                     }
                  }
                  k++;
               }
               out.write("]}");
               //Close the input stream
               br.close();
               in.close();
               out.close();
               fstream.close();
            }
            else {
               throw new GDALException("Out put file can't be created.", null);
            }
         }
         catch (final FileNotFoundException e) {
            throw new GDALException(e.getMessage(), e.getCause());

         }
         catch (final IOException e) {
            throw new GDALException(e.getMessage(), e.getCause());

         }
         return outputFile;
      }
      throw new GDALException("Input file or output fil don't exists", null);
   }

   public class GDALCommands {
      public final static String ogrinfo = "ogrinfo";
      public final static String srsInfo = "gdalsrsinfo";
      public final static String ogr2ogr = "ogr2ogr";
   }


   public class GDALVectorFormats {
      public final static String shp    = ".shp";
      /**
       * Aeronav FAA
       * 
       * This driver reads text files describing aeronav information - obstacles, navaids and routes - as provided by the FAA.
       */
      public final static String faa    = ".faa";

      /**
       * GeoRSS : Geographically Encoded Objects for RSS feeds
       * 
       * http://www.gdal.org/ogr/drv_georss.html
       */
      public final static String georss = ".xml";


   }


}
