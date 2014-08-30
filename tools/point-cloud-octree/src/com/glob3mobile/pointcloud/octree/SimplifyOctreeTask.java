

package com.glob3mobile.pointcloud.octree;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
      final List<Geodetic3D> points = sourceNode.getPoints();
      final int pointsSize = points.size();

      final List<Integer> sortedVertices = new ArrayList<Integer>(pointsSize);
      final LinkedList<Integer> lodIndices = new LinkedList<Integer>();

      if (pointsSize == 1) {
         // just one vertex, no need to sort
         lodIndices.add(0);
         sortedVertices.add(0);
      }
      else {
         sortPoints(points, sortedVertices, lodIndices);
      }

      final int targetPointsCount = Math.round(pointsSize * _resultSizeFactor);
      int maxIndex = 0;
      for (int i = 0; i < lodIndices.size(); i++) {
         final int lodIndex = lodIndices.get(i);
         if (lodIndex <= targetPointsCount) {
            maxIndex = lodIndex;
         }
         else {
            break;
         }
      }


      for (int i = 0; i < maxIndex; i++) {
         final int index = sortedVertices.get(i);
         final Geodetic3D point = points.get(index);
         _targetOctree.addPoint(point);
      }

      _progress.stepsDone(pointsSize);
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

}
