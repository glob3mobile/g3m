

package com.glob3mobile.pointcloud.octree;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.glob3mobile.pointcloud.kdtree.KDInnerNode;
import com.glob3mobile.pointcloud.kdtree.KDLeafNode;
import com.glob3mobile.pointcloud.kdtree.KDTree;
import com.glob3mobile.pointcloud.kdtree.KDTreeVisitor;
import com.glob3mobile.pointcloud.octree.PersistentOctree.Node;
import com.glob3mobile.pointcloud.octree.PersistentOctree.Statistics;
import com.glob3mobile.pointcloud.octree.berkeleydb.BerkeleyDBOctree;

import es.igosoftware.euclid.colors.GColorF;
import es.igosoftware.util.GMath;
import es.igosoftware.util.GProgress;


public class ProcessOT {

   private static final GColorF[] RAMP = new GColorF[] { GColorF.CYAN, GColorF.GREEN, GColorF.YELLOW, GColorF.RED };


   private static GColorF interpolateColorFromRamp(final GColorF colorFrom,
                                                   final GColorF[] ramp,
                                                   final float alpha) {
      final float rampStep = 1f / ramp.length;

      final int toI;
      if (GMath.closeTo(alpha, 1)) {
         toI = ramp.length - 1;
      }
      else {
         toI = (int) (alpha / rampStep);
      }

      final GColorF from;
      if (toI == 0) {
         from = colorFrom;
      }
      else {
         from = ramp[toI - 1];
      }

      final float colorAlpha = (alpha % rampStep) / rampStep;
      return from.mixedWidth(ramp[toI], colorAlpha);
   }


   private static Color toAWTColor(final GColorF color) {
      return new Color(color.getRed(), color.getGreen(), color.getBlue(), 1);
   }


   private static class SortingTask
            implements
               PersistentOctree.Visitor {
      private final GProgress _progress;


      private SortingTask(final GProgress progress) {
         _progress = progress;
      }


      @Override
      public boolean visit(final Node node) {
         //               System.out.println("=> " + node.getID() + " level=" + node.getLevel() + ", points=" + node.getPoints().size());

         final int pointsSize = node.getPointsCount();

         //final boolean isExemplar = node.getID().equals("032010023013302231");
         //final boolean isExemplar = pointsSize == 64920;

         //               if (isExemplar) {

         final List<Geodetic3D> points = node.getPoints();

         //final int pointsSize = points.size();
         final List<Integer> sortedVertices = new ArrayList<Integer>(pointsSize);
         final List<Integer> lodIndices = new ArrayList<Integer>();

         if (pointsSize == 1) {
            // just one vertex, no need to sort
            lodIndices.add(0);
            sortedVertices.add(0);
         }
         else {
            sortPoints(points, sortedVertices, lodIndices);
         }

         //System.out.println(node.getID() + " " + lodIndices);

         double minHeight = Double.POSITIVE_INFINITY;
         double maxHeight = Double.NEGATIVE_INFINITY;
         for (final Geodetic3D point : points) {
            final double height = point._height;
            if (height < minHeight) {
               minHeight = height;
            }
            if (height > maxHeight) {
               maxHeight = height;
            }
         }

         //createDebugImage(node, points, sortedVertices, lodIndices, minHeight, maxHeight);
         //               }

         _progress.stepsDone(pointsSize);


         //final boolean keepWorking = !isExemplar;
         final boolean keepWorking = true;
         return keepWorking;
      }


      private void createDebugImage(final Node node,
                                    final List<Geodetic3D> points,
                                    final List<Integer> sortedVertices,
                                    final List<Integer> lodIndices,
                                    final double minHeight,
                                    final double maxHeight) {
         final Sector sector = node.getSector();

         final int width = 1024;
         final int height = 1024;


         //               final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
         //
         //               final Graphics2D g = image.createGraphics();
         //
         //               int cursor = 0;
         //               int lodLevel = 0;
         //
         //               final GColorF[] wheel = GColorF.RED.wheel(lodIndices.size());
         //
         //               for (final int lodIndex : lodIndices) {
         //                  //                  final float alpha = (float) lodLevel / lodIndices.size();
         //                  //                  final GColorF levelColor = GColorF.BLACK.mixedWidth(GColorF.WHITE, alpha);
         //                  final Color levelColor = new Color(wheel[lodLevel].getRed(), wheel[lodLevel].getGreen(),
         //                           wheel[lodLevel].getBlue(), 1);
         //
         //                  while (cursor <= lodIndex) {
         //                     // g.setColor(new Color(levelColor.getRed(), levelColor.getGreen(), levelColor.getBlue(), 1));
         //                     g.setColor(levelColor);
         //                     final Geodetic3D point = points.get(sortedVertices.get(cursor));
         //                     final int x = Math.round((float) (sector.getUCoordinate(point._longitude) * width));
         //                     final int y = Math.round((float) (sector.getVCoordinate(point._latitude) * height));
         //                     g.drawRect(x, y, 1, 1);
         //
         //                     cursor++;
         //                  }
         //                  lodLevel++;
         //               }
         //
         //               g.dispose();
         //
         //               try {
         //                  ImageIO.write(image, "png", new File("_DEBUG_" + node.getID() + ".png"));
         //               }
         //               catch (final IOException e) {
         //                  throw new RuntimeException(e);
         //               }


         int cursor = 0;
         int lodLevel = 0;

         final double deltaHeight = maxHeight - minHeight;

         for (final int maxLODIndex : lodIndices) {
            final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            final Graphics2D g = image.createGraphics();

            g.setColor(Color.WHITE);
            cursor = 0;
            while (cursor <= maxLODIndex) {
               final Geodetic3D point = points.get(sortedVertices.get(cursor));

               final float alpha = (float) ((point._height - minHeight) / deltaHeight);
               final GColorF color = interpolateColorFromRamp(GColorF.BLUE, RAMP, alpha);
               g.setColor(toAWTColor(color));


               final int x = Math.round((float) (sector.getUCoordinate(point._longitude) * width));
               final int y = Math.round((float) (sector.getVCoordinate(point._latitude) * height));
               g.drawRect(x, y, 1, 1);

               cursor++;
            }

            g.dispose();

            try {
               ImageIO.write(image, "png", new File("_DEBUG_" + node.getID() + "_lod_" + lodLevel + ".png"));
            }
            catch (final IOException e) {
               throw new RuntimeException(e);
            }


            lodLevel++;
         }


      }


      private void sortPoints(final List<Geodetic3D> points,
                              final List<Integer> sortedVertices,
                              final List<Integer> lodIndices) {


         final KDTree tree = new KDTree(points, 2);

         /*
            arity=2
            032010023321221112 [0, 2, 6, 14, 30, 62, 126, 254, 510, 1022, 2046, 4094, 8190, 16382, 33381, 64919]

            arity=3
            032010023321221112 [1, 7, 25, 79, 241, 727, 2185, 6559, 19681, 64919]

            arity=4
            032010023321221112 [2, 14, 62, 254, 1022, 4094, 16382, 62777, 64919]
          */

         final KDTreeVisitor visitor = new KDTreeVisitor() {
            private int _lastDepth = 0;


            @Override
            public void startVisiting(final KDTree tree1) {
            }


            @Override
            public void visitInnerNode(final KDInnerNode innerNode) {
               pushVertexIndex(innerNode.getVertexIndexes(), innerNode.getDepth());
            }


            @Override
            public void visitLeafNode(final KDLeafNode leafNode) {
               pushVertexIndex(leafNode.getVertexIndexes(), leafNode.getDepth());
            }


            private void pushVertexIndex(final int[] vertexIndexes,
                                         final int depth) {
               if (_lastDepth != depth) {
                  _lastDepth = depth;

                  final int sortedVerticesCount = sortedVertices.size();
                  if (sortedVerticesCount > 0) {
                     lodIndices.add(sortedVerticesCount - 1);
                  }
               }

               for (final int vertexIndex : vertexIndexes) {
                  sortedVertices.add(vertexIndex);
               }
            }


            @Override
            public void endVisiting(final KDTree tree1) {
            }
         };
         tree.breadthFirstAcceptVisitor(visitor);
         lodIndices.add(sortedVertices.size() - 1);
      }


      @Override
      public void stop() {
         _progress.finish();
      }


      @Override
      public void start() {
      }
   }


   private static class CreateMapTask
            implements
               PersistentOctree.Visitor {

      private final GProgress _progress;
      private final Sector    _mapSector;
      private final int       _imageWidth;
      private final int       _imageHeight;
      private BufferedImage   _image;
      private Graphics2D      _g;
      private final int       _minPointsPerNode;
      private final int       _maxPointsPerNode;


      private CreateMapTask(final GProgress progress,
                            final Statistics statistics,
                            final int imageWidth) {
         _progress = progress;
         _mapSector = statistics.getSector();
         _minPointsPerNode = statistics.getMinPointsPerNode();
         _maxPointsPerNode = statistics.getMaxPointsPerNode();

         _imageWidth = imageWidth;
         final float ratio = (float) (_mapSector._deltaLatitude._radians / _mapSector._deltaLongitude._radians);
         _imageHeight = Math.round(imageWidth * ratio);
      }


      @Override
      public void start() {
         _image = new BufferedImage(_imageWidth, _imageHeight, BufferedImage.TYPE_4BYTE_ABGR);
         _g = _image.createGraphics();
      }


      @Override
      public boolean visit(final Node node) {
         final Sector nodeSector = node.getSector();

         final int pointsCount = node.getPointsCount();

         final int deltaPoints = _maxPointsPerNode - _minPointsPerNode;
         final float alpha = (float) (pointsCount - _minPointsPerNode) / deltaPoints;
         final GColorF color = GColorF.BLACK.mixedWidth(GColorF.WHITE, alpha);


         final int xFrom = Math.round((float) (_mapSector.getUCoordinate(nodeSector._lower._longitude) * _imageWidth));
         final int yFrom = Math.round((float) (_mapSector.getVCoordinate(nodeSector._upper._latitude) * _imageHeight));
         final int xTo = Math.round((float) (_mapSector.getUCoordinate(nodeSector._upper._longitude) * _imageWidth));
         final int yTo = Math.round((float) (_mapSector.getVCoordinate(nodeSector._lower._latitude) * _imageHeight));
         final int width = xTo - xFrom;
         final int height = yTo - yFrom;
         _g.setColor(toAWTColor(color));
         _g.fillRect(xFrom, yFrom, width, height);

         _g.setColor(toAWTColor(GColorF.YELLOW));
         _g.drawRect(xFrom, yFrom, width, height);

         //         _g.drawString("" + pointsCount, (xTo + xFrom) / 2, (yTo + yFrom) / 2);

         _progress.stepsDone(pointsCount);

         final boolean keepVisiting = true;
         return keepVisiting;
      }


      @Override
      public void stop() {
         _g.dispose();

         try {
            ImageIO.write(_image, "png", new File("_DEBUG_ot_map.png"));
         }
         catch (final IOException e) {
            throw new RuntimeException(e);
         }

         _progress.finish();
      }

   }


   public static void main(final String[] args) {
      System.out.println("ProcessOT 0.1");
      System.out.println("-------------\n");

      final String sourceCloudName = "Loudoun-VA";
      //      final String targetCloudName = "Loudoun-VA-SORTED";
      //      final boolean recreateTargetOT = true;
      //      final int targetBufferSize = 1024 * 64;
      //      final int targetMaxPointsPerTitle = 1024 * 64;


      //      if (recreateTargetOT) {
      //         BerkeleyDBOctree.delete(targetCloudName);
      //      }
      //
      //      try (final PersistentOctree targetOctree = BerkeleyDBOctree.open(targetCloudName, recreateTargetOT, targetBufferSize,
      //               targetMaxPointsPerTitle)) {
      //         if (recreateTargetOT) {
      //            try (final PersistentOctree sourceOctree = BerkeleyDBOctree.open(sourceCloudName, false)) {
      //               final PersistentOctree.Statistics statistics = sourceOctree.getStatistics(true);
      //               final long pointsCount = statistics.getPointsCount();
      //               statistics.show();
      //
      //               loadTargetOT(sourceOctree, pointsCount, targetOctree);
      //            }
      //         }
      //
      //         targetOctree.getStatistics(true).show();
      //      }

      try (final PersistentOctree sourceOctree = BerkeleyDBOctree.openReadOnly(sourceCloudName)) {
         final PersistentOctree.Statistics statistics = sourceOctree.getStatistics(false, true);
         final long pointsCount = statistics.getPointsCount();
         statistics.show();

         final GProgress progress = new GProgress(pointsCount, true) {
            @Override
            public void informProgress(final long stepsDone,
                                       final double percent,
                                       final long elapsed,
                                       final long estimatedMsToFinish) {
               System.out.println("  processing \"" + sourceOctree.getCloudName() + "\" "
                                  + progressString(stepsDone, percent, elapsed, estimatedMsToFinish));
            }
         };

         //sourceOctree.acceptVisitor(new SortingTask(progress));

         sourceOctree.acceptVisitor(new CreateMapTask(progress, statistics, 2048 * 2));

      }
   }


}
