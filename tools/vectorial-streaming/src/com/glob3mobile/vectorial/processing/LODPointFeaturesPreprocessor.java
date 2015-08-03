

package com.glob3mobile.vectorial.processing;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;

import com.glob3mobile.utils.Progress;
import com.glob3mobile.vectorial.lod.PointFeatureLODStorage;
import com.glob3mobile.vectorial.lod.mapdb.PointFeatureLODMapDBStorage;
import com.glob3mobile.vectorial.storage.PointFeature;
import com.glob3mobile.vectorial.storage.PointFeatureStorage;
import com.glob3mobile.vectorial.storage.mapdb.PointFeatureMapDBStorage;


public class LODPointFeaturesPreprocessor {


   private static class LeafNodesImporter
      implements
         PointFeatureStorage.NodeVisitor {

      private final long                   _nodesCount;
      private final PointFeatureLODStorage _lodStorage;

      private Progress                     _progress;


      private LeafNodesImporter(final long nodesCount,
                                final PointFeatureLODStorage lodStorage) {
         _nodesCount = nodesCount;
         _lodStorage = lodStorage;
      }


      @Override
      public void start() {
         _progress = new Progress(_nodesCount) {
            @Override
            public void informProgress(final long stepsDone,
                                       final double percent,
                                       final long elapsed,
                                       final long estimatedMsToFinish) {
               System.out.println(_lodStorage.getName() + " - 1/4 Importing leaf nodes: "
                                  + progressString(stepsDone, percent, elapsed, estimatedMsToFinish));
            }
         };
      }


      @Override
      public void stop() {
         _progress.finish();
         _progress = null;
      }


      @Override
      public boolean visit(final PointFeatureStorage.Node node) {
         _lodStorage.addLeafNode(node.getID(), node.getSector(), node.getAveragePosition(), node.getFeatures());

         _progress.stepDone();
         return true;
      }

   }


   public static void process(final File storageDir,
                              final String storageName,
                              final File lodDir,
                              final Comparator<PointFeature> featuresComparator) throws IOException {

      try (final PointFeatureStorage storage = PointFeatureMapDBStorage.openReadOnly(storageDir, storageName)) {
         final String lodName = storage.getName() + "_LOD";

         final int maxFeaturesPerNode = 64;

         try (final PointFeatureLODStorage lodStorage = PointFeatureLODMapDBStorage.createEmpty(storage.getSector(), lodDir,
                  lodName, maxFeaturesPerNode)) {
            final PointFeatureStorage.Statistics statistics = storage.getStatistics(true);
            statistics.show();
            System.out.println();

            final int nodesCount = statistics.getNodesCount();

            storage.acceptDepthFirstVisitor(new LeafNodesImporter(nodesCount, lodStorage));

            lodStorage.processPendingNodes(featuresComparator);

            System.out.println(lodStorage.getName() + " - 3/4 Optimizing storage...");
            lodStorage.optimize();

            System.out.println();
            final PointFeatureLODStorage.Statistics lodStatistics = lodStorage.getStatistics(true);
            lodStatistics.show();
         }
      }
   }


   private LODPointFeaturesPreprocessor() {
   }


   public static void main(final String[] args) throws IOException {
      System.out.println("LODPointFeaturesPreprocessor 0.1");
      System.out.println("--------------------------------\n");


      final File storageDir = new File("PointFeaturesStorage");

      //      final String storageName = "PopulatedPlaces";
      //      final Comparator<PointFeature> featuresComparator = new PopulatedPlacesComparator();

      // final String storageName = "Cities1000";
      // final String storageName = "AR";
      // final String storageName = "ES";
      final String storageName = "GEONames-PopulatedPlaces";

      final Comparator<PointFeature> featuresComparator = new GEONamesComparator();

      final File lodDir = new File("PointFeaturesLOD");

      LODPointFeaturesPreprocessor.process( //
               storageDir, storageName, //
               lodDir, //
               featuresComparator);

      System.out.println("\n- done!");
   }


}
