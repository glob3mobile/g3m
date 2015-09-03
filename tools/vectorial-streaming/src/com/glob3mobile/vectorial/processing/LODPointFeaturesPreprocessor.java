

package com.glob3mobile.vectorial.processing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

      private final long                     _nodesCount;
      private final PointFeatureLODStorage   _lodStorage;
      private final Comparator<PointFeature> _featuresComparator;
      private final boolean                  _verbose;

      private Progress                       _progress;


      private LeafNodesImporter(final long nodesCount,
                                final PointFeatureLODStorage lodStorage,
                                final Comparator<PointFeature> featuresComparator,
                                final boolean verbose) {
         _nodesCount = nodesCount;
         _lodStorage = lodStorage;
         _featuresComparator = featuresComparator;
         _verbose = verbose;
      }


      @Override
      public void start() {
         _progress = new Progress(_nodesCount) {
            @Override
            public void informProgress(final long stepsDone,
                                       final double percent,
                                       final long elapsed,
                                       final long estimatedMsToFinish) {
               if (_verbose) {
                  System.out.println(_lodStorage.getName() + " - 1/4 Importing leaf nodes: "
                                     + progressString(stepsDone, percent, elapsed, estimatedMsToFinish));
               }
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
         final List<PointFeature> sortedFeatures = new ArrayList<>(node.getFeatures());
         Collections.sort(sortedFeatures, _featuresComparator);

         _lodStorage.addLeafNode( //
                  node.getID(), //
                  node.getNodeSector(), //
                  node.getMinimumSector(), //
                  node.getAveragePosition(), //
                  sortedFeatures //
         );

         _progress.stepDone();
         return true;
      }

   }


   public static void process(final File storageDir,
                              final String storageName,
                              final File lodDir,
                              final String lodName,
                              final Comparator<PointFeature> featuresComparator,
                              final int maxFeaturesPerNode,
                              final boolean verbose) throws IOException {

      try (final PointFeatureStorage storage = PointFeatureMapDBStorage.openReadOnly(storageDir, storageName)) {


         try (final PointFeatureLODStorage lodStorage = PointFeatureLODMapDBStorage.createEmpty(storage.getSector(), lodDir,
                  lodName, maxFeaturesPerNode)) {
            final PointFeatureStorage.Statistics statistics = storage.getStatistics(verbose);
            if (verbose) {
               statistics.show();
               System.out.println();
            }

            final int nodesCount = statistics.getNodesCount();

            storage.acceptDepthFirstVisitor(new LeafNodesImporter(nodesCount, lodStorage, featuresComparator, verbose));

            lodStorage.processPendingNodes(featuresComparator, verbose);

            if (verbose) {
               System.out.println(lodStorage.getName() + " - 3/4 Optimizing storage...");
            }
            lodStorage.optimize();

            if (verbose) {
               System.out.println();
               final PointFeatureLODStorage.Statistics lodStatistics = lodStorage.getStatistics(verbose);
               lodStatistics.show();
            }
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
      final String lodName = storageName + "_LOD";

      final int maxFeaturesPerNode = 64;

      final boolean verbose = true;

      LODPointFeaturesPreprocessor.process( //
               storageDir, storageName, //
               lodDir, lodName, //
               featuresComparator, //
               maxFeaturesPerNode, //
               verbose);

      System.out.println("\n- done!");
   }


}
