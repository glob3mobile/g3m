

package com.glob3mobile.vectorial.processing;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiPredicate;

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


   private static final class Importer
      implements
         GEOFeatureHandler<RuntimeException> {
      private final PointFeatureStorage _storage;
      private final Sector              _sector;
      private final Progress            _progress;
      private final int                 _row;
      private final int                 _col;
      private final int                 _maxRow;
      private final int                 _maxCol;


      private Importer(final PointFeatureStorage storage,
                       final Sector sector,
                       final Progress progress,
                       final int row,
                       final int col,
                       final int maxRow,
                       final int maxCol) {
         _storage = storage;
         _sector = sector;
         _progress = progress;

         _row = row;
         _col = col;
         _maxRow = maxRow;
         _maxCol = maxCol;
      }


      @Override
      public void onStart() {
      }


      @Override
      public void onFinish() {
      }


      @Override
      public void onFinishWithException() {
         onFinish();
      }


      @Override
      public void onFeature(final Map<String, Object> properties,
                            final GEOGeometry geometry) {
         final Geodetic2D position = ((GEOPoint) geometry)._position;

         if (_sector.contains(position)) {
            final int r = (int) (_sector.getUCoordinate(position._longitude) * (_maxRow - 0.01));
            final int c = (int) (_sector.getVCoordinate(position._latitude) * (_maxCol - 0.01));
            //                         System.out.println(row + "x" + col);
            if ((r == _row) && (c == _col)) {
               final PointFeature feature = new PointFeature(properties, position);
               try {
                  _storage.addFeature(feature);
               }
               catch (final IOException e) {
                  throw new RuntimeException(e);
               }
               _progress.stepDone();
            }
         }
      }


      @Override
      public void onError(final Map<String, Object> properties,
                          final GEOGeometry geometry) {
         //_progress.stepDone();
      }
   }


   public static void main(final String[] args) throws IOException, GEOParseException {
      System.out.println("Point-Features Importer 0.1");
      System.out.println("---------------------------\n");


      //      final GEOParser parser = GEOJSONParser.INSTANCE;
      //      final String featuresFileName = "/Users/dgd/Downloads/sfcrimes.geojson";
      //      final String storageName = "SFCrimes";

      // final String featuresFileName = "test-files/ne_10m_populated_places.geojson";
      // final String storageName = "PopulatedPlaces";

      final GEOParser parser = GEONamesParser.INSTANCE;
      final String featuresFileName = "test-files/GEONames-PopulatedPlaces.txt";
      final String storageName = "GEONames-PopulatedPlaces";


      final File featuresFile = new File(featuresFileName);
      final File storageDir = new File("PointFeaturesStorage");


      // final int maxFeaturesPerNode = 4096;
      // final int maxFeaturesPerNode = 32 * 1024;
      // final int maxFeaturesPerNode = 64 * 1024;
      final int maxFeaturesPerNode = 16 * 1024;
      // final int maxFeaturesPerNode = 8 * 1024;
      final int maxBufferSize = maxFeaturesPerNode;


      //      final BiPredicate<Map<String, Object>, Geodetic2D> filter = (properties,
      //                                                                   position) -> {
      //         return (position._latitude._degrees < 80) && (position._longitude._degrees < 80);
      //      };

      final BiPredicate<Map<String, Object>, Geodetic2D> filter = null;

      final GEOStatisticsGatherer.Statistics statistics = GEOStatisticsGatherer.getStatistics(parser, featuresFile, filter);
      final long featuresCount = statistics._featuresCount;
      final Sector sector = statistics._boundingSector;

      System.out.println("- Features: " + featuresCount + ", sector: " + sector);

      try (final PointFeatureStorage storage = PointFeatureMapDBStorage.createEmpty(sector, storageDir, storageName,
               maxBufferSize, maxFeaturesPerNode)) {

         final AtomicInteger rowContainer = new AtomicInteger();
         final AtomicInteger colContainer = new AtomicInteger();

         final Progress progress = new Progress(featuresCount) {
            @Override
            public void informProgress(final long stepsDone,
                                       final double percent,
                                       final long elapsed,
                                       final long estimatedMsToFinish) {
               System.out.println(featuresFile.getName() + //
                                  ": Importing " + //
                                  "[" + rowContainer.get() + "-" + colContainer.get() + "] " + //
                                  progressString(stepsDone, percent, elapsed, estimatedMsToFinish));
            }
         };

         final int maxRow = 5;
         final int maxCol = 5;
         for (int row = 0; row < maxRow; row++) {
            rowContainer.set(row);
            for (int col = 0; col < maxCol; col++) {
               colContainer.set(col);
               parser.parse(featuresFile, new Importer(storage, sector, progress, row, col, maxRow, maxCol));
            }
         }

         System.out.println("- Optimizing storage...");
         storage.optimize();
         System.out.println("- Storage optimized");

         progress.finish();
      }


      System.out.println();

      try (final PointFeatureStorage storage = PointFeatureMapDBStorage.openReadOnly(storageDir, storageName)) {
         storage.getStatistics(true).show();
      }

      System.out.println("\n- done!");
   }


}
