

package com.glob3mobile.vectorial.processing;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.glob3mobile.geo.Geodetic2D;
import com.glob3mobile.geo.Sector;
import com.glob3mobile.utils.Progress;
import com.glob3mobile.vectorial.GEOGeometry;
import com.glob3mobile.vectorial.GEOPoint;
import com.glob3mobile.vectorial.parsing.GEOFeatureHandler;
import com.glob3mobile.vectorial.parsing.GEONamesParser;
import com.glob3mobile.vectorial.parsing.GEOParseException;
import com.glob3mobile.vectorial.parsing.GEOParser;
import com.glob3mobile.vectorial.parsing.GEOStatisticsGatherer;
import com.glob3mobile.vectorial.storage.PointFeature;
import com.glob3mobile.vectorial.storage.PointFeatureStorage;
import com.glob3mobile.vectorial.storage.mapdb.PointFeatureMapDBStorage;


public class PointFeaturesImporter {


   public static void main(final String[] args) throws IOException, GEOParseException {
      System.out.println("Point-Features Importer 0.1");
      System.out.println("---------------------------\n");


      // final GEOParser parser = GEOJSONParser.INSTANCE;
      // final String featuresFileName = "test-files/ne_10m_populated_places.geojson";
      // final String storageName = "PopulatedPlaces";

      final GEOParser parser = GEONamesParser.INSTANCE;
      // final String featuresFileName = "test-files/geonames/US.txt";
      // final String featuresFileName = "test-files/geonames/MX.txt";
      // final String storageName = "MX";

      // final String featuresFileName = "test-files/geonames/NO.txt";
      // final String storageName = "NO";

      // final String featuresFileName = "test-files/geonames/ES.txt";
      // final String storageName = "ES";

      // final String featuresFileName = "test-files/geonames/AR.txt";
      // final String storageName = "AR";

      // final String featuresFileName = "test-files/geonames/allCountries.txt";
      // final String featuresFileName = "test-files/geonames/NO.txt";
      // final String storageName = "NO";

      // final String featuresFileName = "test-files/geonames/cities1000.txt";
      // final String storageName = "Cities1000";

      final String featuresFileName = "test-files/GEONames-PopulatedPlaces.txt";
      final String storageName = "GEONames-PopulatedPlaces";


      final File featuresFile = new File(featuresFileName);
      final File storageDir = new File("PointFeaturesStorage");


      final int maxBufferSize = 2048;
      final int maxFeaturesPerNode = 2048;


      final GEOStatisticsGatherer.Statistics statistics = GEOStatisticsGatherer.getStatistics(parser, featuresFile);
      final long featuresCount = statistics._featuresCount;
      final Sector sector = statistics._boundingSector;

      parser.parse(featuresFile, new GEOFeatureHandler<RuntimeException>() {
         private PointFeatureStorage _storage;
         private Progress            _progress = null;


         @Override
         public void onStart() {
            try {
               _storage = PointFeatureMapDBStorage.createEmpty(sector, storageDir, storageName, maxBufferSize, maxFeaturesPerNode);
            }
            catch (final IOException e) {
               throw new RuntimeException(e);
            }

            _progress = new Progress(featuresCount) {
               @Override
               public void informProgress(final long stepsDone,
                                          final double percent,
                                          final long elapsed,
                                          final long estimatedMsToFinish) {
                  System.out.println(featuresFile + ": Importing "
                                     + progressString(stepsDone, percent, elapsed, estimatedMsToFinish));
               }
            };
         }


         @Override
         public void onFinish() {
            try {
               _storage.close();
            }
            catch (final IOException e) {
               throw new RuntimeException(e);
            }
            _storage = null;

            _progress.finish();
            _progress = null;
         }


         @Override
         public void onFinishWithException() {
            onFinish();
         }


         @Override
         public void onFeature(final Map<String, Object> properties,
                               final GEOGeometry geometry) {
            final Geodetic2D position = ((GEOPoint) geometry)._position;
            final PointFeature feature = new PointFeature(properties, position);

            try {
               _storage.addFeature(feature);
            }
            catch (final IOException e) {
               throw new RuntimeException(e);
            }

            _progress.stepDone();
         }


         @Override
         public void onError(final Map<String, Object> properties,
                             final GEOGeometry geometry) {
            //_progress.stepDone();
         }
      });

      System.out.println();

      try (final PointFeatureStorage storage = PointFeatureMapDBStorage.openReadOnly(storageDir, storageName)) {
         storage.getStatistics(true).show();
      }

      System.out.println("\n- done!");
   }


}
