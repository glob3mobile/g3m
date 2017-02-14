

package org.glob3.mobile.tools.gdal;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import org.glob3.mobile.generated.IFactory;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IMathUtils;
import org.glob3.mobile.generated.IStringBuilder;
import org.glob3.mobile.generated.LogLevel;
import org.glob3.mobile.specific.Factory_JavaDesktop;
import org.glob3.mobile.specific.Logger_JavaDesktop;
import org.glob3.mobile.specific.MathUtils_JavaDesktop;
import org.glob3.mobile.specific.StringBuilder_JavaDesktop;
import org.glob3.mobile.tools.commandline.core.CommandLineException;
import org.glob3.mobile.tools.commandline.core.DecompressManager;
import org.glob3.mobile.tools.conversion.jbson2bjson.JBson2BJson;
import org.glob3.mobile.tools.conversion.jbson2bjson.JBson2BJsonException;
import org.glob3.mobile.tools.utils.FileUtils;


public class GeoBSONConverter {


   private final ILogger           _logger;


   private static GeoBSONConverter _geoBsonConverter = null;


   // private final JBson2BJson       _jsonParser;
   //   private final GDAL              _gdalConverter;


   public static synchronized GeoBSONConverter instance() {
      if (_geoBsonConverter == null) {
         _geoBsonConverter = new GeoBSONConverter();
      }

      return _geoBsonConverter;
   }


   private GeoBSONConverter() {
      initialize();
      _logger = ILogger.instance();
      // _jsonParser = JBson2BJson.instance();
      // _gdalConverter = GDAL.instance();
   }


   static private void initialize() {
      IStringBuilder.setInstance(new StringBuilder_JavaDesktop());
      if (IMathUtils.instance() == null) {
         IMathUtils.setInstance(new MathUtils_JavaDesktop());
      }
      if (IFactory.instance() == null) {
         IFactory.setInstance(new Factory_JavaDesktop());
      }
      if (ILogger.instance() == null) {
         ILogger.setInstance(new Logger_JavaDesktop(LogLevel.ErrorLevel));
      }
   }


   public void convert(final File inputFile,
                       final File outputDir,
                       final String outputFileName) throws GDALException, JBson2BJsonException, CommandLineException, IOException {
      convert(inputFile, outputDir, outputFileName, true);
   }


   public void convert(final File inputFile,
                       final File outputDir,
                       final String outputFileName,
                       final boolean overwrite) throws GDALException, JBson2BJsonException, CommandLineException, IOException {
      if (inputFile.exists()) {
         if (inputFile.isFile()) {
            _logger.logInfo("----------------------------- File: " + inputFile.getName() + " -----------------------------");
            final String inputFileName = inputFile.getName().toLowerCase();
            if (inputFileName.endsWith(".shp")) {
               shpToBson(inputFile, outputDir, outputFileName, overwrite);
            }
            else if (inputFileName.endsWith(".geojson")) {
               geoJsonToBson(inputFile, outputDir, outputFileName, overwrite);
            }
            else if (inputFileName.endsWith(".dat") || inputFileName.endsWith(".txt")) {
               aeronavFAAToBson(inputFile, outputDir, outputFileName, overwrite);
            }
            else if (inputFileName.endsWith(".gpx")) {
               gpxToBson(inputFile, outputDir, outputFileName, overwrite, false);
            }
            else if (inputFileName.endsWith(".xml")) {
               geoRSSToBson(inputFile, outputDir, outputFileName, overwrite);
            }
            else if (inputFileName.endsWith(".xyz") || inputFileName.endsWith(".asc")) {
               xyzToBson(inputFile, outputDir, outputFileName, overwrite);
            }
            else if (inputFileName.endsWith(DecompressManager.ZIP_EXT)) {
               final File decompressDir = DecompressManager.unzipFile(inputFile);
               if (decompressDir != null) {
                  if (decompressDir.exists() && decompressDir.isDirectory()) {
                     convert(decompressDir, outputDir, outputFileName, overwrite);
                  }
                  FileUtils.deleteDirectory(decompressDir);
               }
            }
            else {
               _logger.logInfo("This format file isn't yet supperted: " + inputFileName);
            }

         }
         else {
            _logger.logInfo("This functionality is only supported by shp files");
            final File[] files = inputFile.listFiles(new FilenameFilter() {
               @Override
               public boolean accept(final File dir,
                                     final String name) {
                  return name.toLowerCase().endsWith(".shp");
               }
            });
            for (final File file : files) {
               shpToBson(file, outputDir, FileUtils.getFileNameWithoutExtension(file.getName()), overwrite);
            }
         }
      }
      else {
         _logger.logError("Specified Input File doesn't exist");
      }
   }


   private void gpxToBson(final File inputFile,
                          final File outputDir,
                          final String outputFileName,
                          final boolean overwrite,
                          final boolean multiFile) throws GDALException, JBson2BJsonException, IOException {
      final File geoJsonWaypoints = GDAL.vector2GeoJSON(inputFile, outputDir, outputFileName + "_waypoints", overwrite,
               "waypoints");
      final File geoJsonRoutes = GDAL.vector2GeoJSON(inputFile, outputDir, outputFileName + "_routes", overwrite, "routes");
      final File geoJsonTracks = GDAL.vector2GeoJSON(inputFile, outputDir, outputFileName + "_tracks", overwrite, "tracks");
      if (multiFile) {
         JBson2BJson.json2bson(geoJsonWaypoints, new File(outputDir, geoJsonWaypoints.getName().replace(".geojson", ".bson")),
                  overwrite);
         JBson2BJson.json2bson(geoJsonRoutes, new File(outputDir, geoJsonRoutes.getName().replace(".geojson", ".bson")),
                  overwrite);
         JBson2BJson.json2bson(geoJsonTracks, new File(outputDir, geoJsonTracks.getName().replace(".geojson", ".bson")),
                  overwrite);
      }
      else {
         JBson2BJson.jsons2bson(new File(outputDir, outputFileName + ".bson"), overwrite, geoJsonWaypoints, geoJsonRoutes,
                  geoJsonTracks);

      }
      _logger.logInfo("Gpx conversion has been successful");
   }


   static private void aeronavFAAToBson(final File inputFile,
                                        final File outputDir,
                                        final String outputFileName,
                                        final boolean overwrite) throws GDALException, JBson2BJsonException, IOException {
      final File geoJson = GDAL.vector2GeoJSON(inputFile, outputDir, outputFileName, overwrite);
      JBson2BJson.json2bson(geoJson, new File(outputDir, geoJson.getName().replace(".geojson", ".bson")), overwrite);
   }


   static private void geoJsonToBson(final File inputFile,
                                     final File outputDir,
                                     String outputFileName,
                                     final boolean overwrite) throws GDALException, JBson2BJsonException, IOException {
      if (inputFile.getParentFile().equals(outputDir)) {
         if (inputFile.getName().compareTo(outputFileName) == 0) {
            outputFileName = "generated_" + outputFileName;
         }
      }
      final File geoJson = GDAL.vector2GeoJSON(inputFile, outputDir, outputFileName, overwrite);
      JBson2BJson.json2bson(geoJson, new File(outputDir, geoJson.getName().replace(".geojson", ".bson")), overwrite);
   }


   static private void geoRSSToBson(final File inputFile,
                                    final File outputDir,
                                    final String outputFileName,
                                    final boolean overwrite) throws GDALException, JBson2BJsonException, IOException {
      final File geoJson = GDAL.vector2GeoJSON(inputFile, outputDir, outputFileName, overwrite);
      JBson2BJson.json2bson(geoJson, new File(outputDir, geoJson.getName().replace(".geojson", ".bson")), overwrite);
   }


   static private void shpToBson(final File inputFile,
                                 final File outputDir,
                                 final String outputFileName,
                                 final boolean overwrite) throws GDALException, JBson2BJsonException, IOException {
      final String inputFileName = FileUtils.getFileNameWithoutExtension(inputFile.getName());
      if (ShpUtils.checkShpDir(inputFile.getParentFile(), inputFileName)) {
         final File geoJson = GDAL.vector2GeoJSON(inputFile, outputDir, outputFileName, overwrite);
         JBson2BJson.json2bson(geoJson, new File(outputDir, geoJson.getName().replace(".geojson", ".bson")), overwrite);
      }
   }


   static private void xyzToBson(final File inputFile,
                                 final File outputDir,
                                 final String outputFileName,
                                 final boolean overwrite) throws GDALException, JBson2BJsonException, IOException {
      if (inputFile.exists()) {
         final File json = GDAL.xyz2JSON(inputFile, outputDir, outputFileName);
         JBson2BJson.json2bson(json, new File(outputDir, json.getName().replace(".json", ".bson")), overwrite);
      }
   }


}
