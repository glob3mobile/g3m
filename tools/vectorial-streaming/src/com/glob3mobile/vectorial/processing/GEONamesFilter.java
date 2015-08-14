

package com.glob3mobile.vectorial.processing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.function.Predicate;

import com.glob3mobile.utils.Progress;
import com.glob3mobile.vectorial.GEOGeometry;
import com.glob3mobile.vectorial.parsing.GEOFeatureHandler;
import com.glob3mobile.vectorial.parsing.GEONamesParser;
import com.glob3mobile.vectorial.utils.Counter;


public class GEONamesFilter {


   public static void main(final String[] args) throws IOException {
      System.out.println("GEOFeaturesStatistics 0.1");
      System.out.println("-------------------------\n");


      final GEONamesParser parser = (GEONamesParser) GEONamesParser.INSTANCE;
      final String sourceFileName = "test-files/geonames/allCountries.txt";
      final String targetFileName = "test-files/GEONames-PopulatedPlaces.txt";

      final File sourceFile = new File(sourceFileName);
      final File targetFile = new File(targetFileName);
      if (targetFile.exists()) {
         targetFile.delete();
      }

      final long featuresCount = parser.countFeatures(sourceFile, true);

      final Predicate<Map<String, Object>> propertiesFilter = properties -> {
         final String featureClass = (String) properties.get("featureClass");
         if (!featureClass.equals("P")) {
            return false;
         }

         final String featureCode = (String) properties.get("featureCode");
         if (featureCode.isEmpty() || //
             featureCode.equals("PPLCH") || //
             featureCode.equals("PPLH") || //
             featureCode.equals("PPLQ") || //
             featureCode.equals("PPLW") || //
             // featureCode.equals("PPLX") || //
             featureCode.equals("STLMT")) {
            return false;
         }

         final long population = (Long) properties.get("population");
         if (population <= 0) {
            return false;
         }

         return true;
      };


      try (final OutputStreamWriter os = new OutputStreamWriter(new FileOutputStream(targetFile), "UTF-8")) {

         parser.parse(sourceFile, new GEOFeatureHandler<RuntimeException>() {
            private Progress _progress = null;
            private Counter  _featureClassCounter;
            private Counter  _featureCodeCounter;
            private long     _accepted;


            @Override
            public void onStart() {
               _progress = new Progress(featuresCount) {
                  @Override
                  public void informProgress(final long stepsDone,
                                             final double percent,
                                             final long elapsed,
                                             final long estimatedMsToFinish) {
                     System.out.println(sourceFile + ": " + //
                                        progressString(stepsDone, percent, elapsed, estimatedMsToFinish) + //
                                        " (accepted: " + _accepted + ")");
                  }
               };

               _featureClassCounter = new Counter("featureClass");
               _featureCodeCounter = new Counter("featureCode");

               _accepted = 0;
            }


            @Override
            public void onFinish() {
               _progress.finish();
               _progress = null;

               System.out.println();
               _featureClassCounter.show();
               _featureCodeCounter.show();

               System.out.println("\nAccepted: " + _accepted);
            }


            @Override
            public void onFinishWithException() {
            }


            @Override
            public void onFeature(final Map<String, Object> properties,
                                  final GEOGeometry geometry) {

               if (propertiesFilter.test(properties)) {
                  _accepted++;

                  final String featureClass = (String) properties.get("featureClass");
                  final String featureCode = (String) properties.get("featureCode");

                  _featureClassCounter.count(featureClass);
                  _featureCodeCounter.count(featureClass + "." + featureCode);

                  try {
                     os.write(parser.getCurrentLine());
                     os.write("\n");
                  }
                  catch (final IOException e) {
                     throw new RuntimeException(e);
                  }
               }

               //               System.out.println("\"" + properties.get("name") + "\"" + //
               //                                  ", featureClass=" + featureClass + //
               //                                  ", featureCode=" + featureCode);

               _progress.stepDone();
            }


            @Override
            public void onError(final Map<String, Object> properties,
                                final GEOGeometry geometry) {
               //_progress.stepDone();
            }
         });


      }

      System.out.println();

      System.out.println("\n- done!");
   }


}
