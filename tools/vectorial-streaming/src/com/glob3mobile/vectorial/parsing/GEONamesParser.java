

package com.glob3mobile.vectorial.parsing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.glob3mobile.geo.Geodetic2D;
import com.glob3mobile.utils.UndeterminateProgress;
import com.glob3mobile.vectorial.GEOGeometry;
import com.glob3mobile.vectorial.GEOPoint;


public class GEONamesParser
   implements
      GEOParser {


   public final static GEOParser INSTANCE = new GEONamesParser();
   private String                _currentLine;


   private GEONamesParser() {
   }


   @Override
   public long countFeatures(final File file,
                             final boolean showProgress) throws IOException {

      final UndeterminateProgress progress = !showProgress ? null : new UndeterminateProgress() {
         @Override
         public void informProgress(final long stepsDone,
                                    final long elapsed) {
            System.out.println(file + ": Counting features" + progressString(stepsDone, elapsed));
         }
      };

      long counter = 0;
      try (BufferedReader br = new BufferedReader(new FileReader(file))) {
         while (br.readLine() != null) {
            counter++;
            if (progress != null) {
               progress.stepDone();
            }
         }
      }
      if (progress != null) {
         progress.finish();
      }
      return counter;
   }


   @Override
   public <E extends Exception> void parse(final File file,
                                           final GEOFeatureHandler<E> handler) throws IOException, E {
      boolean finishedOK = false;
      handler.onStart();
      try (BufferedReader br = new BufferedReader(new FileReader(file))) {
         String line;
         while ((line = br.readLine()) != null) {
            parseLine(handler, line);
         }
      }
      handler.onFinish();
      finishedOK = true;
      if (!finishedOK) {
         handler.onFinishWithException();
      }
   }


   private <E extends Exception> void parseLine(final GEOFeatureHandler<E> handler,
                                                final String line) throws E {
      final String[] tokens = line.split("\\t");

      if (tokens.length != 19) {
         handler.onError(null, null);
      }
      else {
         _currentLine = line;
         final Map<String, Object> properties = new LinkedHashMap<>();
         properties.put("geonameid", Long.parseLong(tokens[0]));
         properties.put("name", tokens[1]);
         properties.put("asciiname", tokens[2]);
         properties.put("alternatenames", tokens[3]);
         properties.put("featureClass", tokens[6]);
         properties.put("featureCode", tokens[7]);
         properties.put("countryCode", tokens[8]);
         properties.put("cc2", tokens[9]);
         properties.put("admin1Code", tokens[10]);
         properties.put("admin2Code", tokens[11]);
         properties.put("admin3Code", tokens[12]);
         properties.put("admin4Code", tokens[13]);
         properties.put("population", Long.parseLong(tokens[14]));
         properties.put("elevation", tokens[15].isEmpty() ? Integer.MIN_VALUE : Integer.parseInt(tokens[15]));
         properties.put("dem", tokens[16]);
         properties.put("timezone", tokens[17]);
         properties.put("modificationDate", tokens[18]);


         final double latitude = Double.parseDouble(tokens[4]);
         final double longitude = Double.parseDouble(tokens[5]);
         final GEOGeometry geometry = new GEOPoint(Geodetic2D.fromDegrees(latitude, longitude));

         handler.onFeature(properties, geometry);

         /*
         geonameid         : integer id of record in geonames database
         name              : name of geographical point (utf8) varchar(200)
         asciiname         : name of geographical point in plain ascii characters, varchar(200)
         alternatenames    : alternatenames, comma separated, ascii names automatically transliterated, convenience attribute from alternatename table, varchar(10000)
         latitude          : latitude in decimal degrees (wgs84)
         longitude         : longitude in decimal degrees (wgs84)
         feature class     : see http://www.geonames.org/export/codes.html, char(1)
         feature code      : see http://www.geonames.org/export/codes.html, varchar(10)
         country code      : ISO-3166 2-letter country code, 2 characters
         cc2               : alternate country codes, comma separated, ISO-3166 2-letter country code, 200 characters
         admin1 code       : fipscode (subject to change to iso code), see exceptions below, see file admin1Codes.txt for display names of this code; varchar(20)
         admin2 code       : code for the second administrative division, a county in the US, see file admin2Codes.txt; varchar(80)
         admin3 code       : code for third level administrative division, varchar(20)
         admin4 code       : code for fourth level administrative division, varchar(20)
         population        : bigint (8 byte int)
         elevation         : in meters, integer
         dem               : digital elevation model, srtm3 or gtopo30, average elevation of 3''x3'' (ca 90mx90m) or 30''x30'' (ca 900mx900m) area in meters, integer. srtm processed by cgiar/ciat.
         timezone          : the timezone id (see file timeZone.txt) varchar(40)
         modification date : date of last modification in yyyy-MM-dd format
         */
      }
   }


   public String getCurrentLine() {
      return _currentLine;
   }


}
