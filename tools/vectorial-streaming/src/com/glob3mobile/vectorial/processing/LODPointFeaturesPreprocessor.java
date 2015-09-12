

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

      private final long                   _nodesCount;
      private final PointFeatureLODStorage _clusterStorage;
      private final boolean                _verbose;

      private Progress                     _progress;


      private LeafNodesImporter(final long nodesCount,
                                final PointFeatureLODStorage clusterStorage,
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
                  Collections.emptyList(), //
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
                              final Comparator<PointFeature> featuresComparator,
                              final boolean createClusters,
                              final boolean verbose) throws IOException {

      try (final PointFeatureStorage storage = PointFeatureMapDBStorage.openReadOnly(storageDir, storageName)) {


         try (final PointFeatureLODStorage clusterStorage = PointFeatureLODMapDBStorage.createEmpty(storage.getSector(),
                  clusterDir, clusterName, maxFeaturesPerNode, featuresComparator, createClusters)) {
            final PointFeatureStorage.Statistics statistics = storage.getStatistics(verbose);
            if (verbose) {
               statistics.show();
               System.out.println();
            }

            final int nodesCount = statistics.getNodesCount();

            storage.acceptDepthFirstVisitor(new LeafNodesImporter(nodesCount, clusterStorage, verbose));

            clusterStorage.createLOD(verbose);

            if (verbose) {
               System.out.println(clusterStorage.getName() + ": 4/4 Optimizing storage...");
            }
            clusterStorage.optimize();

            if (verbose) {
               System.out.println();
               final PointFeatureLODStorage.Statistics clusterStatistics = clusterStorage.getStatistics(verbose);
               clusterStatistics.show();
            }
         }
      }
   }


   private LODPointFeaturesPreprocessor() {
   }


   public static void main(final String[] args) throws IOException {
      System.out.println("LODPointFeaturesPreprocessor 0.1");
      System.out.println("--------------------------------\n");


      final File sourceDir = new File("PointFeaturesStorage");
      // final String sourceName = "Cities1000";
      // final String sourceName = "AR";
      // final String sourceName = "ES";
      final String sourceName = "GEONames-PopulatedPlaces";


      final File clusterDir = new File("PointFeaturesLOD");
      final String clusterName = sourceName + "_LOD";

      final int maxFeaturesPerNode = 64;
      final Comparator<PointFeature> featuresComparator = null;
      final boolean createClusters = true;

      final boolean verbose = true;

      LODPointFeaturesPreprocessor.process( //
               sourceDir, sourceName, //
               clusterDir, clusterName, //
               maxFeaturesPerNode, //
               featuresComparator, //
               createClusters, //
               verbose);

      System.out.println("\n- done!");
   }


}
