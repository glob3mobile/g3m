

package com.glob3mobile.pointcloud.octree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.glob3mobile.pointcloud.kdtree.KDInnerNode;
import com.glob3mobile.pointcloud.kdtree.KDLeafNode;
import com.glob3mobile.pointcloud.kdtree.KDTree;
import com.glob3mobile.pointcloud.kdtree.KDTreeVisitor;
import com.glob3mobile.pointcloud.octree.PersistentLOD.Transaction;
import com.glob3mobile.pointcloud.octree.berkeleydb.BerkeleyDBLOD;

import es.igosoftware.util.GProgress;


class SortingTask
         implements
            PersistentOctree.Visitor {
   private final GProgress _progress;
   private final String    _lodCloudName;
   private PersistentLOD   _lodDB;
   private int             _sortedPointsCount;
   private int             _totalPointsCount;


   SortingTask(final String lodCloudName,
            final GProgress progress) {
      _lodCloudName = lodCloudName;
      _progress = progress;
   }


   @Override
   public boolean visit(final PersistentOctree.Node node) {
      final List<Geodetic3D> points = node.getPoints();
      final int pointsSize = points.size();

      if (pointsSize == 0) {
         return true;
      }

      final PersistentLOD.Transaction transaction = _lodDB.createTransaction();
      final int sortedPointsCount = process(transaction, _lodDB, node.getID(), points);
      transaction.commit();

      _sortedPointsCount += sortedPointsCount;
      _totalPointsCount += pointsSize;

      _progress.stepsDone(pointsSize);

      return true;
   }


   private static int process(final Transaction transaction,
                              final PersistentLOD lodDB,
                              final String nodeID,
                              final List<Geodetic3D> points) {
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

      //      System.out.println(node.getID() + //
      //                         " lodLevels=" + lodIndices.size() + //
      //                         ", points=" + pointsSize + //
      //                         ", lodIndices=" + lodIndices);

      while (lodIndices.size() > 1) {
         final int candidateLevel = lodIndices.peekFirst();
         if (candidateLevel > 128) {
            break;
         }
         lodIndices.pollFirst();
      }

      final int lodLevels = lodIndices.size();

      final String parentID = parentID(nodeID);

      //      System.out.println(nodeID + //
      //                         " lodLevels=" + lodLevels + //
      //                         ", points=" + pointsSize + //
      //                         ", lodIndices=" + lodIndices);

      int sortedPointsCount;
      if ((lodLevels == 1) || (parentID == null)) {
         final List<Geodetic3D> sortedPoints = new ArrayList<Geodetic3D>(pointsSize);
         for (final int index : sortedVertices) {
            sortedPoints.add(points.get(index));
         }
         lodDB.put(transaction, nodeID, false, sortedPoints);

         sortedPointsCount = sortedPoints.size();
      }
      else {
         final int lastLevelFrom = lodIndices.get(lodLevels - 2);
         final int lastLevelTo = lodIndices.get(lodLevels - 1);
         final int lastLevelPointsCount = lastLevelTo - lastLevelFrom;

         final List<Geodetic3D> parentLevelPoints = new ArrayList<Geodetic3D>(pointsSize - lastLevelPointsCount);
         final List<Geodetic3D> lastLevelPoints = new ArrayList<Geodetic3D>(lastLevelPointsCount);

         for (final int index : sortedVertices) {
            final Geodetic3D point = points.get(index);
            if (index <= lastLevelFrom) {
               parentLevelPoints.add(point);
            }
            else {
               lastLevelPoints.add(point);
            }
         }

         lodDB.put(transaction, nodeID, false, lastLevelPoints);
         lodDB.putOrMerge(transaction, parentID, true, parentLevelPoints);

         sortedPointsCount = lastLevelPoints.size();

         //         System.out.println("**** lastLevelFrom=" + lastLevelFrom + //
         //                            ",  lastLevelTo=" + lastLevelTo + //
         //                            ", count=" + lastLevelPointsCount + //
         //                            ", parentLevelPoints=" + parentLevelPoints.size() + //
         //                            ", lastLevelPoints=" + lastLevelPoints.size());
      }

      return sortedPointsCount;
   }


   //   private void createDebugImage(final Node node,
   //                                 final List<Geodetic3D> points,
   //                                 final List<Integer> sortedVertices,
   //                                 final List<Integer> lodIndices,
   //                                 final double minHeight,
   //                                 final double maxHeight) {
   //      final Sector sector = node.getSector();
   //
   //      final int width = 1024;
   //      final int height = 1024;
   //
   //
   //      //               final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
   //      //
   //      //               final Graphics2D g = image.createGraphics();
   //      //
   //      //               int cursor = 0;
   //      //               int lodLevel = 0;
   //      //
   //      //               final GColorF[] wheel = GColorF.RED.wheel(lodIndices.size());
   //      //
   //      //               for (final int lodIndex : lodIndices) {
   //      //                  //                  final float alpha = (float) lodLevel / lodIndices.size();
   //      //                  //                  final GColorF levelColor = GColorF.BLACK.mixedWidth(GColorF.WHITE, alpha);
   //      //                  final Color levelColor = new Color(wheel[lodLevel].getRed(), wheel[lodLevel].getGreen(),
   //      //                           wheel[lodLevel].getBlue(), 1);
   //      //
   //      //                  while (cursor <= lodIndex) {
   //      //                     // g.setColor(new Color(levelColor.getRed(), levelColor.getGreen(), levelColor.getBlue(), 1));
   //      //                     g.setColor(levelColor);
   //      //                     final Geodetic3D point = points.get(sortedVertices.get(cursor));
   //      //                     final int x = Math.round((float) (sector.getUCoordinate(point._longitude) * width));
   //      //                     final int y = Math.round((float) (sector.getVCoordinate(point._latitude) * height));
   //      //                     g.drawRect(x, y, 1, 1);
   //      //
   //      //                     cursor++;
   //      //                  }
   //      //                  lodLevel++;
   //      //               }
   //      //
   //      //               g.dispose();
   //      //
   //      //               try {
   //      //                  ImageIO.write(image, "png", new File("_DEBUG_" + node.getID() + ".png"));
   //      //               }
   //      //               catch (final IOException e) {
   //      //                  throw new RuntimeException(e);
   //      //               }
   //
   //
   //      int cursor = 0;
   //      int lodLevel = 0;
   //
   //      final double deltaHeight = maxHeight - minHeight;
   //
   //      for (final int maxLODIndex : lodIndices) {
   //         final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
   //         final Graphics2D g = image.createGraphics();
   //
   //         g.setColor(Color.WHITE);
   //         cursor = 0;
   //         while (cursor <= maxLODIndex) {
   //            final Geodetic3D point = points.get(sortedVertices.get(cursor));
   //
   //            final float alpha = (float) ((point._height - minHeight) / deltaHeight);
   //            final GColorF color = ProcessOT.interpolateColorFromRamp(GColorF.BLUE, ProcessOT.RAMP, alpha);
   //            g.setColor(ProcessOT.toAWTColor(color));
   //
   //
   //            final int x = Math.round((float) (sector.getUCoordinate(point._longitude) * width));
   //            final int y = Math.round((float) (sector.getVCoordinate(point._latitude) * height));
   //            g.drawRect(x, y, 1, 1);
   //
   //            cursor++;
   //         }
   //
   //         g.dispose();
   //
   //         try {
   //            ImageIO.write(image, "png", new File("_DEBUG_" + node.getID() + "_lod_" + lodLevel + ".png"));
   //         }
   //         catch (final IOException e) {
   //            throw new RuntimeException(e);
   //         }
   //
   //
   //         lodLevel++;
   //      }
   //
   //
   //   }


   private static String parentID(final String id) {
      return id.isEmpty() ? null : id.substring(0, id.length() - 1);
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


   private static class SortDirties
   implements
   PersistentLOD.Visitor {
      PersistentLOD           _lodDB;
      private final GProgress _progress;

      private long            _previousDirtyPointsCount;
      private long            _previousSortedPointsCount;
      private long            _sortedPointsCount;
      private long            _dirtyPointsCount;


      private SortDirties(final PersistentLOD lodDB,
                          final int iteration,
                          final long totalPointsCount) {
         _lodDB = lodDB;
         _progress = new GProgress(totalPointsCount, true) {
            @Override
            public void informProgress(final long stepsDone,
                                       final double percent,
                                       final long elapsed,
                                       final long estimatedMsToFinish) {
               System.out.println("  processing \"" + lodDB.getCloudName() + "\" iteration #" + iteration
                        + progressString(stepsDone, percent, elapsed, estimatedMsToFinish));
            }
         };
      }


      @Override
      public void start(final PersistentLOD.Transaction transaction) {
         _previousDirtyPointsCount = 0;
         _previousSortedPointsCount = 0;

         _sortedPointsCount = 0;
         _dirtyPointsCount = 0;
      }


      @Override
      public boolean visit(final PersistentLOD.Transaction transaction,
                           final PersistentLOD.Node node) {
         final String id = node.getID();
         final boolean isDirty = node.isDirty();
         final int pointsCounts = node.getPointsCount();
         //System.out.println("#" + id + " dirty=" + isDirty + ", pointsCount=" + pointsCounts);

         if (isDirty) {
            final List<Geodetic3D> points = node.getPoints();
            final int pointsSize = points.size();

            if (pointsSize == 0) {
               return true;
            }

            _previousDirtyPointsCount += pointsCounts;

            final int sortedPointsCount = process(transaction, _lodDB, node.getID(), points);
            _sortedPointsCount += sortedPointsCount;
            _dirtyPointsCount += (pointsSize - sortedPointsCount);
         }
         else {
            _previousSortedPointsCount += pointsCounts;
         }

         _progress.stepsDone(pointsCounts);

         return true;
      }


      @Override
      public void stop(final PersistentLOD.Transaction transaction) {
         _progress.finish();

         final long previousTotalPoints = _previousSortedPointsCount + _previousDirtyPointsCount;
         System.out.println("(2) FINISHED: previous (sortedPoints=" + _previousSortedPointsCount + //
                  ", dirtyPoints=" + _previousDirtyPointsCount + //
                  ", total=" + previousTotalPoints + //
                  ")  /  NEW= sorted " + _sortedPointsCount + //
                  " dirty=" + _dirtyPointsCount);
      }

   }


   @Override
   public void start() {
      _lodDB = BerkeleyDBLOD.open(_lodCloudName, true);

      _totalPointsCount = 0;
      _sortedPointsCount = 0;
   }


   @Override
   public void stop() {
      final int dirtyPointsCount = _totalPointsCount - _sortedPointsCount;
      System.out.println("** FINISHED: sortedPoints=" + _sortedPointsCount + //
               ", dirtyPoints=" + dirtyPointsCount + //
               ", total=" + _totalPointsCount);

      if (dirtyPointsCount > 0) {
         final int iteration = 1;
         final PersistentLOD.Transaction transaction = _lodDB.createTransaction();
         _lodDB.acceptDepthFirstVisitor(transaction, new SortDirties(_lodDB, iteration, _totalPointsCount));
         transaction.commit();
      }

      _lodDB.close();
      _lodDB = null;

      _progress.finish();
   }

}
