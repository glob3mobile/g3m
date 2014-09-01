

package com.glob3mobile.pointcloud.octree;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import com.glob3mobile.pointcloud.kdtree.KDInnerNode;
import com.glob3mobile.pointcloud.kdtree.KDLeafNode;
import com.glob3mobile.pointcloud.kdtree.KDTree;
import com.glob3mobile.pointcloud.kdtree.KDTreeVisitor;
import com.glob3mobile.pointcloud.octree.berkeleydb.BerkeleyDBOctree;

import es.igosoftware.util.GProgress;
import es.igosoftware.util.GStringUtils;


public class SimplifyOctreeTask
         implements
PersistentOctree.Visitor {

   private final String     _sourceCloudName;
   private final File       _cloudDirectory;
   private final String     _simplifiedCloudName;
   private final float      _resultSizeFactor;
   private final long       _sourcePointsCount;
   private final long       _cacheSizeInBytes;
   private PersistentOctree _targetOctree;
   private GProgress        _progress;


   SimplifyOctreeTask(final String sourceCloudName,
                      final File cloudDirectory,
                      final String simplifiedCloudName,
                      final long cacheSizeInBytes,
                      final long sourcePointsCount,
                      final float resultSizeFactor) {
      _sourceCloudName = sourceCloudName;
      _cloudDirectory = cloudDirectory;
      _simplifiedCloudName = simplifiedCloudName;
      _cacheSizeInBytes = cacheSizeInBytes;
      _sourcePointsCount = sourcePointsCount;
      _resultSizeFactor = resultSizeFactor;
   }


   @Override
   public void start() {
      BerkeleyDBOctree.delete(_cloudDirectory, _simplifiedCloudName);

      _targetOctree = BerkeleyDBOctree.open(_cloudDirectory, _simplifiedCloudName, true, _cacheSizeInBytes);

      _progress = new GProgress(_sourcePointsCount, true) {
         @Override
         public void informProgress(final long stepsDone,
                                    final double percent,
                                    final long elapsed,
                                    final long estimatedMsToFinish) {
            System.out.println("- Simplifying \"" + _sourceCloudName + "\" "
                               + progressString(stepsDone, percent, elapsed, estimatedMsToFinish));
         }
      };
   }


   @Override
   public void stop() {
      _targetOctree.close();
      _targetOctree = null;

      _progress.finish();
      _progress = null;

      System.out.println();
      try (PersistentOctree targetOctree = BerkeleyDBOctree.openReadOnly(_cloudDirectory, _simplifiedCloudName, _cacheSizeInBytes)) {
         final PersistentOctree.Statistics statistics = targetOctree.getStatistics(false, true);
         statistics.show();
         final long simplifiedPointsCount = statistics.getPointsCount();

         System.out.println();
         System.out.println("Source points=" + _sourcePointsCount + ", Simplified Points=" + simplifiedPointsCount + " ("
                  + GStringUtils.formatPercent(simplifiedPointsCount, _sourcePointsCount) + ")");
      }
   }


   @Override
   public boolean visit(final PersistentOctree.Node sourceNode) {
      //      final List<Geodetic3D> points = sourceNode.getPoints();
      //      final int pointsSize = points.size();
      //
      //      final List<Integer> sortedVertices = new ArrayList<Integer>(pointsSize);
      //      final LinkedList<Integer> lodIndices = new LinkedList<Integer>();
      //
      //      if (pointsSize == 1) {
      //         // just one vertex, no need to sort
      //         lodIndices.add(0);
      //         sortedVertices.add(0);
      //      }
      //      else {
      //         sortPoints(points, sortedVertices, lodIndices);
      //      }
      //
      //      final int targetPointsCount = Math.round(pointsSize * _resultSizeFactor);
      //      int maxIndex = 0;
      //      for (int i = 0; i < lodIndices.size(); i++) {
      //         final int lodIndex = lodIndices.get(i);
      //         if (lodIndex <= targetPointsCount) {
      //            maxIndex = lodIndex;
      //         }
      //         else {
      //            break;
      //         }
      //      }
      //
      //
      //      for (int i = 0; i < maxIndex; i++) {
      //         final int index = sortedVertices.get(i);
      //         final Geodetic3D point = points.get(index);
      //         _targetOctree.addPoint(point);
      //      }


      final long pointsSize = sourceNode.getPointsCount();

      final List<Geodetic3D> points = sourceNode.getPoints();
      final int targetPointsCount = Math.round(pointsSize * _resultSizeFactor);
      final List<Geodetic3D> simplifiedPoints = KMeans.cluster(points, targetPointsCount);

      for (final Geodetic3D point : simplifiedPoints) {
         _targetOctree.addPoint(point);
      }

      _progress.stepsDone(pointsSize);

      //      final String id = sourceNode.getID();
      //      if (pointsSize == 64920) {
      //         System.out.println("found #" + id);
      //         final Sector sector = sourceNode.getSector();
      //         final List<Geodetic3D> points = sourceNode.getPoints();
      //         generateImage(id, sector, points);
      //
      //         final int targetPointsCount = Math.round(pointsSize * _resultSizeFactor);
      //         final List<Geodetic3D> simplifiedPoints = KMeans.cluster(points, targetPointsCount);
      //         generateImage(id + "_simplified", sector, simplifiedPoints);
      //
      //         return false;
      //      }

      final boolean keepWorking = true;
      return keepWorking;
   }


   private static void sortPoints(final List<Geodetic3D> points,
                                  final List<Integer> sortedVertices,
                                  final List<Integer> lodIndices) {

      final KDTree tree = new KDTree(points, 2);

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

      final int sortedVerticesCount = sortedVertices.size();
      if (sortedVerticesCount > 0) {
         lodIndices.add(sortedVerticesCount - 1);
      }
   }


   private static void generateImage(final String id,
                                     //final int level,
                                     final Sector sector,
                                     final List<Geodetic3D> points
   //final double minHeight,
   //final double maxHeight
   ) {
      final int imageWidth = 1024;
      final int imageHeight = 1024;

      final BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_4BYTE_ABGR);
      final Graphics2D g = image.createGraphics();

      //      g.setColor(Color.WHITE);
      //      g.fillRect(0, 0, imageWidth, imageHeight);


      g.setColor(Color.WHITE);

      //      final double deltaHeight = maxHeight - minHeight;

      for (final Geodetic3D point : points) {
         //         final float alpha = (float) ((point._height - minHeight) / deltaHeight);
         //         //final GColorF color = GColorF.BLACK.mixedWidth(GColorF.WHITE, alpha);
         //         final GColorF color = ProcessOT.interpolateColorFromRamp(GColorF.BLUE, ProcessOT.RAMP, alpha);
         //         g.setColor(Utils.toAWTColor(color));

         final int x = Math.round((float) (sector.getUCoordinate(point._longitude) * imageWidth));
         final int y = Math.round((float) (sector.getVCoordinate(point._latitude) * imageHeight));
         g.fillRect(x, y, 1, 1);
      }

      g.dispose();

      //      final String imageName = "_DEBUG_" + id + "_L" + level + ".png";
      final String imageName = "_DEBUG_" + id + ".png";
      try {
         ImageIO.write(image, "png", new File(imageName));
      }
      catch (final IOException e) {
         throw new RuntimeException(e);
      }
   }
}
