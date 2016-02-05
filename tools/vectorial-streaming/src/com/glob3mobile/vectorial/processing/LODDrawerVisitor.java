

package com.glob3mobile.vectorial.processing;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;

import com.glob3mobile.geo.Geodetic2D;
import com.glob3mobile.geo.Sector;
import com.glob3mobile.utils.Progress;
import com.glob3mobile.vectorial.lod.PointFeatureLODStorage;
import com.glob3mobile.vectorial.storage.PointFeature;
import com.glob3mobile.vectorial.storage.PointFeatureCluster;
import com.glob3mobile.vectorial.utils.GEOBitmap;


public final class LODDrawerVisitor
   implements
      PointFeatureLODStorage.NodeVisitor {

   private final PointFeatureLODStorage _clusterStorage;
   private final int                    _nodesCount;
   private final int                    _minDepth;
   private final int                    _maxDepth;
   private final long                   _featuresCount;

   private Progress                     _progress;
   private GEOBitmap                    _bitmap;

   private long                         _visitedFeatures;
   private final Sector                 _sectorToProcess;


   public LODDrawerVisitor(final Sector sectorToProcess,
                           final PointFeatureLODStorage clusterStorage,
                           final int nodesCount,
                           final int minDepth,
                           final int maxDepth,
                           final long featuresCount) {
      _sectorToProcess = sectorToProcess;
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

      _visitedFeatures = 0;
   }


   @Override
   public void stop() {
      try {
         _bitmap.save(new File("LOD_" + _clusterStorage.getName() + "_" + _minDepth + "-" + _maxDepth + ".png"));
      }
      catch (final IOException e) {
         throw new RuntimeException(e);
      }
      _bitmap = null;

      _progress.finish();
      _progress = null;

      System.out.println("- Visited featues: " + _visitedFeatures);
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
   public boolean visit(final PointFeatureLODStorage.Node node) {

      if (!node.getNodeSector().touchesWith(_sectorToProcess)) {
         return true;
      }

      final int depth = node.getDepth();
      if (depth <= _maxDepth) {
         if (depth >= _minDepth) {
            final Color clusterColor = new Color(1, 1, 0, 0.9f);
            //            final Color fontColor = new Color(1, 1, 0, 1f);

            for (final PointFeatureCluster cluster : node.getClusters()) {
               final Geodetic2D clusterPosition = cluster._position;
               if (!_sectorToProcess.contains(clusterPosition)) {
                  break;
               }

               final long clusterSize = cluster._size;

               final double area = ((200000.0 * clusterSize) / _featuresCount);
               final int pointSize = Math.round((float) Math.sqrt(area));

               //               _bitmap.drawPoint("C #" + depth + ", size:" + clusterSize, cluster._position, pointSize, pointSize, clusterColor,
               //                        font, fontColor);
               _bitmap.drawPoint(clusterPosition, pointSize, pointSize, clusterColor);
            }
         }


         final int pointSize = 10;
         final Color featureColor = new Color(1, 0, 1, 0.9f);
         final Font font = new Font(Font.SERIF, Font.BOLD, 14);

         for (final PointFeature feature : node.getFeatures()) {
            final Geodetic2D featurePosition = feature._position;
            if (!_sectorToProcess.contains(featurePosition)) {
               break;
            }

            //               _bitmap.drawPoint(featurePosition, pointSize, pointSize, featureColor);
            final String featureName = (String) feature._properties.get("name");
            _bitmap.drawPoint(featureName, featurePosition, pointSize, pointSize, featureColor, font, featureColor);

            _visitedFeatures++;
         }
      }

      _progress.stepDone();
      return true;
   }


   //      @Override
   //      public boolean visit(final PointFeatureLODStorage.LeafNode node) {
   //         final int depth = node.getDepth();
   //         //         if ((depth >= _minDepth) && (depth <= _maxDepth)) {
   //         if (depth <= _maxDepth) {
   //            final int pointSize = 2;
   //            final Color clusterColor = new Color(1, 0, 1, 0.9f);
   //
   //            for (final PointFeature feature : node.getFeatures()) {
   //               _bitmap.drawPoint(feature._position, pointSize, pointSize, clusterColor);
   //            }
   //         }
   //
   //         _progress.stepDone();
   //         return true;
   //      }


}
