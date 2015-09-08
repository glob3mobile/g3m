

package com.glob3mobile.vectorial.processing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.glob3mobile.utils.Progress;
import com.glob3mobile.vectorial.cluster.PointFeatureClusterStorage;
import com.glob3mobile.vectorial.cluster.mapdb.PointFeatureClusterMapDBStorage;
import com.glob3mobile.vectorial.storage.PointFeature;
import com.glob3mobile.vectorial.storage.PointFeatureStorage;
import com.glob3mobile.vectorial.storage.mapdb.PointFeatureMapDBStorage;


public class ClusterPointFeaturesPreprocessor {


   private static class LeafNodesImporter
      implements
         PointFeatureStorage.NodeVisitor {

      private final long                       _nodesCount;
      private final PointFeatureClusterStorage _clusterStorage;
      private final boolean                    _verbose;

      private Progress                         _progress;


      private LeafNodesImporter(final long nodesCount,
                                final PointFeatureClusterStorage clusterStorage,
                                final boolean verbose) {
         _nodesCount = nodesCount;
         _clusterStorage = clusterStorage;
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
                  System.out.println(_clusterStorage.getName() + ": 1/4 Importing leaf nodes: "
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
         final List<PointFeature> features = new ArrayList<>(node.getFeatures());

         _clusterStorage.addLeafNode( //
                  node.getID(), //
                  node.getNodeSector(), //
                  node.getMinimumSector(), //
                  node.getAveragePosition(), //
                  features //
         );

         _progress.stepDone();
         return true;
      }

   }


   public static void process(final File storageDir,
                              final String storageName,
                              final File clusterDir,
                              final String clusterName,
                              final int maxFeaturesPerNode,
                              final boolean verbose) throws IOException {

      try (final PointFeatureStorage storage = PointFeatureMapDBStorage.openReadOnly(storageDir, storageName)) {


         try (final PointFeatureClusterStorage clusterStorage = PointFeatureClusterMapDBStorage.createEmpty(storage.getSector(),
                  clusterDir, clusterName, maxFeaturesPerNode)) {
            final PointFeatureStorage.Statistics statistics = storage.getStatistics(verbose);
            if (verbose) {
               statistics.show();
               System.out.println();
            }

            final int nodesCount = statistics.getNodesCount();

            storage.acceptDepthFirstVisitor(new LeafNodesImporter(nodesCount, clusterStorage, verbose));

            if (false) {
               // clusterStorage.processPendingNodes(featuresComparator, verbose);
            }

            if (verbose) {
               System.out.println(clusterStorage.getName() + ": 3/4 Optimizing storage...");
            }
            clusterStorage.optimize();

            if (verbose) {
               System.out.println();
               final PointFeatureClusterStorage.Statistics clusterStatistics = clusterStorage.getStatistics(verbose);
               clusterStatistics.show();
            }
         }
      }
   }


   private ClusterPointFeaturesPreprocessor() {
   }


   public static void main(final String[] args) throws IOException {
      System.out.println("ClusterPointFeaturesPreprocessor 0.1");
      System.out.println("--------------------------------\n");


      final File storageDir = new File("PointFeaturesStorage");


      // final String storageName = "Cities1000";
      // final String storageName = "AR";
      // final String storageName = "ES";
      final String storageName = "GEONames-PopulatedPlaces";


      final File clusterDir = new File("PointFeaturesCluster");
      final String clusterName = storageName + "_Cluster";

      final int maxFeaturesPerNode = 64;

      final boolean verbose = true;

      ClusterPointFeaturesPreprocessor.process( //
               storageDir, storageName, //
               clusterDir, clusterName, //
               maxFeaturesPerNode, //
               verbose);

      System.out.println("\n- done!");
   }


}
