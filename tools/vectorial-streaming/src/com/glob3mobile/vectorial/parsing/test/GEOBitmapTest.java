

package com.glob3mobile.vectorial.parsing.test;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.glob3mobile.geo.Sector;
import com.glob3mobile.utils.UndeterminateProgress;
import com.glob3mobile.vectorial.GEOGeometry;
import com.glob3mobile.vectorial.GEOPoint;
import com.glob3mobile.vectorial.parsing.GEOFeatureHandler;
import com.glob3mobile.vectorial.parsing.GEOJSONParser;
import com.glob3mobile.vectorial.parsing.GEOParseException;
import com.glob3mobile.vectorial.utils.GEOBitmap;


public class GEOBitmapTest {


   private static class DrawPointsHandler
      implements
         GEOFeatureHandler<RuntimeException> {

      private long                  _counter;
      private long                  _errors;
      private long                  _started;
      private GEOBitmap             _geoBitmap;
      private UndeterminateProgress _progress;


      @Override
      public void onError(final Map<String, Object> properties,
                          final GEOGeometry geometry) {
         System.out.println("ERROR: " + properties + " " + geometry);
         _errors++;
      }


      @Override
      public void onFeature(final Map<String, Object> properties,
                            final GEOGeometry geometry) {
         _counter++;

         _geoBitmap.drawPoint(((GEOPoint) geometry)._position, 4, 4, new Color(1, 1, 0, 0.99f));

         _progress.stepDone();
      }


      @Override
      public void onStart() {
         _counter = 0;
         _errors = 0;
         _started = System.currentTimeMillis();
         _geoBitmap = new GEOBitmap(Sector.FULL_SPHERE, 4096, 2048, Color.BLACK);


         _progress = new UndeterminateProgress(2, true) {
            @Override
            public void informProgress(final long stepsDone,
                                       final long elapsed) {
               System.out.println("Drawing" + progressString(stepsDone, elapsed));
            }
         };


      }


      @Override
      public void onFinish() {
         _progress.finish();

         try {
            _geoBitmap.save(new File("test.png"));
         }
         catch (final IOException e) {
            e.printStackTrace();
         }

         if (_errors > 0) {
            System.err.println("Errors: " + _errors);
         }
         System.out.println("Parsed " + _counter + " features.");
         final long elapsed = System.currentTimeMillis() - _started;
         System.out.println("Elapsed time: " + elapsed + "ms");

         System.out.println();
      }


      @Override
      public void onFinishWithException() {
      }

   }


   public static void main(final String[] args) throws IOException, GEOParseException {
      System.out.println("GEOBitmapTest 0.1");
      System.out.println("-----------------\n");


      //final String fileName = "../server-mapboo/test-files/populated_places.geojson";
      final String fileName = "test-files/ne_10m_populated_places.geojson";
      GEOJSONParser.INSTANCE.parse( //
               new File(fileName), //
               new DrawPointsHandler());


      //      // final String fileName = "/Users/dgd/Downloads/US/US.txt";
      //      final String fileName = "/Users/dgd/Downloads/allCountries.txt";
      //
      //      GEONamesParser.parse( //
      //               new File(fileName), //
      //               new DrawPointsHandler());
      //
      //      // All Countries
      //      //      Parsed 10261129 features.
      //      //      Elapsed time: 272873ms
      //

      System.out.println("- done!");
   }


}
