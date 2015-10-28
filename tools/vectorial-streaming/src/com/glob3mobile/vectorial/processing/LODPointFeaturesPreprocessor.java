

package com.glob3mobile.vectorial.processing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
      private final PointFeatureLODStorage _lodStorage;
      private final boolean                _verbose;

      private Progress                     _progress;


      private LeafNodesImporter(final long nodesCount,
                                final PointFeatureLODStorage lodStorage,
                                final boolean verbose) {
         _nodesCount = nodesCount;
         _lodStorage = lodStorage;
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
                  System.out.println(_lodStorage.getName() + ": 1/4 Importing leaf nodes: "
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

         _lodStorage.addLeafNode( //
                  node.getID(), //
                  node.getNodeSector(), //
                  node.getMinimumSector(), //
                  features //
         );

         _progress.stepDone();
         return true;
      }

   }


   public static void process(final File storageDir,
                              final String storageName,
                              final File lodDir,
                              final String lodName,
                              final int maxFeaturesPerNode,
                              final Comparator<PointFeature> featuresComparator,
                              final boolean createClusters,
                              final boolean verbose) throws IOException {

      try (final PointFeatureStorage storage = PointFeatureMapDBStorage.openReadOnly(storageDir, storageName)) {


         try (final PointFeatureLODStorage lodStorage = PointFeatureLODMapDBStorage.createEmpty(storage.getSector(), lodDir,
                  lodName, maxFeaturesPerNode, featuresComparator, createClusters)) {
            final PointFeatureStorage.Statistics statistics = storage.getStatistics(verbose);
            if (verbose) {
               statistics.show();
               System.out.println();
            }

            final int nodesCount = statistics.getNodesCount();

            storage.acceptDepthFirstVisitor(new LeafNodesImporter(nodesCount, lodStorage, verbose));

            lodStorage.createLOD(verbose);

            if (verbose) {
               System.out.println(lodStorage.getName() + ": 4/4 Optimizing storage...");
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


      final File sourceDir = new File("PointFeaturesStorage");
      // final String sourceName = "Cities1000";
      // final String sourceName = "AR";
      // final String sourceName = "ES";
      //  final String sourceName = "GEONames-PopulatedPlaces";
      //      final String sourceName = "SpanishBars";
      final String sourceName = "Tornados";

      final File lodDir = new File("PointFeaturesLOD");
      final String lodName = sourceName + "_LOD";

      final int maxFeaturesPerNode = 64;
      // final int maxFeaturesPerNode = 96;

      final boolean createClusters = true;
      final Comparator<PointFeature> featuresComparator = createClusters ? null : new GEONamesComparator();


      final boolean verbose = true;

      LODPointFeaturesPreprocessor.process( //
               sourceDir, sourceName, //
               lodDir, lodName, //
               maxFeaturesPerNode, //
               featuresComparator, //
               createClusters, //
               verbose);

      System.out.println("\n- done!");
   }


}
