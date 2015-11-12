

package com.glob3mobile.vectorial.processing;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import com.glob3mobile.geo.Sector;
import com.glob3mobile.utils.Progress;
import com.glob3mobile.vectorial.lod.clustering.PointFeatureClusteringLODStorage;
import com.glob3mobile.vectorial.lod.clustering.mapdb.PointFeatureClusteringLODMapDBStorage;
import com.glob3mobile.vectorial.storage.PointFeature;
import com.glob3mobile.vectorial.storage.PointFeatureCluster;
import com.glob3mobile.vectorial.utils.GEOBitmap;


public class ClusteringLODPointFeaturesStatistics {


   public static final class ClusterDrawer
      implements
         PointFeatureClusteringLODStorage.NodeVisitor {

      private final PointFeatureClusteringLODStorage _clusterStorage;
      private final int                              _nodesCount;
      private final int                              _minDepth;
      private final int                              _maxDepth;
      private final long                             _featuresCount;

      private Progress                               _progress;
      private GEOBitmap                              _bitmap;


      private ClusterDrawer(final PointFeatureClusteringLODStorage clusterStorage,
                            final int nodesCount,
                            final int minDepth,
                            final int maxDepth,
                            final long featuresCount) {
         _clusterStorage = clusterStorage;
         _nodesCount = nodesCount;
         _minDepth = minDepth;
         _maxDepth = maxDepth;
         _featuresCount = featuresCount;
      }


      @Override
      public void start() {
         _progress = new Progress(_nodesCount) {
            @Override
            public void informProgress(final long stepsDone,
                                       final double percent,
                                       final long elapsed,
                                       final long estimatedMsToFinish) {
               System.out.println(_clusterStorage.getName() + " [" + _minDepth + "-" + _maxDepth + "] - Processing: "
                                  + progressString(stepsDone, percent, elapsed, estimatedMsToFinish));
            }
         };


         final int width = 2048;
         final Sector sector = _clusterStorage.getSector();
         // final Sector sector = Sector.FULL_SPHERE;
         final double sectorFactor = sector._deltaLatitude._radians / sector._deltaLongitude._radians;
         final int height = Math.round((float) sectorFactor * width);
         _bitmap = new GEOBitmap(sector, width, height, Color.BLACK);
      }


      @Override
      public void stop() {
         try {
            _bitmap.save(new File("Cluster_" + _clusterStorage.getName() + "_" + _minDepth + "-" + _maxDepth + ".png"));
         }
         catch (final IOException e) {
            throw new RuntimeException(e);
         }
         _bitmap = null;

         _progress.finish();
         _progress = null;
      }


      // final Color featureColor = new Color(1, 1, 0, 0.25f);
      //
      // final int level = node.getDepth();
      //
      // final int maxLevel = 15;
      // if (level < maxLevel) {
      //    // final Font font = new Font(Font.SERIF, Font.BOLD, 12 + ((maxLevel - level) * 3));
      //    // final Font font = new Font(Font.SERIF, Font.BOLD, 14);
      //    // final int pointSize = (maxLevel - level);
      //    final int pointSize = 2;
      //
      //    final Sector nodeSector = node.getNodeSector();
      //    _bitmap.drawSector(nodeSector, new Color(1, 1, 1, 0.05f), new Color(1, 1, 1, 0.2f));
      //
      //    final Sector minimumSector = node.getMinimumSector();
      //    _bitmap.drawSector(minimumSector, new Color(0, 1, 1, 0.05f), new Color(0, 1, 1, 0.2f));
      //
      //    for (final PointFeature feature : node.getFeatures()) {
      //       _bitmap.drawPoint(feature._position, pointSize, pointSize, featureColor);
      //       // final String featureName = (String) feature._properties.get("name");
      //       // _bitmap.drawPoint(featureName, feature._position, pointSize, pointSize, featureColor, font);
      //    }
      // }


      @Override
      public boolean visit(final PointFeatureClusteringLODStorage.InnerNode node) {
         final int depth = node.getDepth();
         if ((depth >= _minDepth) && (depth <= _maxDepth)) {
            final Color clusterColor = new Color(1, 1, 0, 0.9f);
            //            final Font font = new Font(Font.SERIF, Font.BOLD, 14);
            //            final Color fontColor = new Color(1, 1, 0, 1f);

            for (final PointFeatureCluster cluster : node.getClusters()) {
               final long clusterSize = cluster._size;

               final double area = ((200000.0 * clusterSize) / _featuresCount);
               final int pointSize = Math.round((float) Math.sqrt(area));

               //               _bitmap.drawPoint("C #" + depth + ", size:" + clusterSize, cluster._position, pointSize, pointSize, clusterColor,
               //                        font, fontColor);
               _bitmap.drawPoint(cluster._position, pointSize, pointSize, clusterColor);

            }
         }

         _progress.stepDone();
         return true;
      }


      @Override
      public boolean visit(final PointFeatureClusteringLODStorage.LeafNode node) {
         final int depth = node.getDepth();
         //         if ((depth >= _minDepth) && (depth <= _maxDepth)) {
         if (depth <= _maxDepth) {
            final int pointSize = 2;
            final Color clusterColor = new Color(1, 0, 1, 0.9f);

            for (final PointFeature feature : node.getFeatures()) {
               _bitmap.drawPoint(feature._position, pointSize, pointSize, clusterColor);

            }
         }

         _progress.stepDone();
         return true;
      }


   }


   public static void main(final String[] args) throws IOException {
      System.out.println("ClusteringLODPointFeaturesStatistics 0.1");
      System.out.println("----------------------------------------\n");


      final File directory = new File("PointFeaturesCluster");
      // final String name = "Cities1000_Cluster";
      // final String name = "AR_Cluster";
      // final String name = "ES_Cluster";
      final String name = "GEONames-PopulatedPlaces_Cluster";


      try (final PointFeatureClusteringLODStorage clusterStorage = PointFeatureClusteringLODMapDBStorage.openReadOnly(directory,
               name)) {
         final PointFeatureClusteringLODStorage.Statistics statistics = clusterStorage.getStatistics(true);
         statistics.show();

         final int nodesCount = statistics.getNodesCount();
         final long featuresCount = statistics.getFeaturesCount();
         final int maxNodeDepth = statistics.getMaxNodeDepth();

         for (int depth = 0; depth <= maxNodeDepth; depth++) {
            final int minDepth = depth;
            final int maxDepth = depth;

            clusterStorage.acceptDepthFirstVisitor(new ClusterDrawer(clusterStorage, nodesCount, minDepth, maxDepth,
                     featuresCount));
         }
      }

      System.out.println("\n- done!");
   }


}
