

package com.glob3mobile.pointcloud.octree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.glob3mobile.pointcloud.kdtree.KDInnerNode;
import com.glob3mobile.pointcloud.kdtree.KDLeafNode;
import com.glob3mobile.pointcloud.kdtree.KDTree;
import com.glob3mobile.pointcloud.kdtree.KDTreeVisitor;
import com.glob3mobile.pointcloud.octree.PersistentLOD.Transaction;
import com.glob3mobile.pointcloud.octree.berkeleydb.BerkeleyDBLOD;
import com.glob3mobile.pointcloud.octree.berkeleydb.TileHeader;

import es.igosoftware.util.GProgress;


class LODSortingTask
         implements
            PersistentOctree.Visitor {
   private final GProgress _progress;
   private final String    _lodCloudName;
   private PersistentLOD   _lodDB;
   private long            _sortedPointsCount;
   private long            _totalPointsCount;
   private final int       _maxPointsPerLeaf;
   private int             _counter;


   LODSortingTask(final String lodCloudName,
                  final String sourceCloudName,
                  final long pointsCount,
                  final int maxPointsPerLeaf) {
      _lodCloudName = lodCloudName;
      _progress = new GProgress(pointsCount, true) {
         @Override
         public void informProgress(final long stepsDone,
                                    final double percent,
                                    final long elapsed,
                                    final long estimatedMsToFinish) {
            System.out.println("- importing \"" + sourceCloudName + "\" "
                     + progressString(stepsDone, percent, elapsed, estimatedMsToFinish));
         }
      };
      _maxPointsPerLeaf = maxPointsPerLeaf;
   }


   @Override
   public boolean visit(final PersistentOctree.Node node) {
      final List<Geodetic3D> points = node.getPoints();
      final int pointsSize = points.size();

      final boolean keepWorking = _counter++ < 10;
      if (pointsSize == 0) {
         return keepWorking;
      }

      final PersistentLOD.Transaction transaction = _lodDB.createTransaction();
      final int sortedPointsCount;
      if (pointsSize > _maxPointsPerLeaf) {
         final byte[] binaryID = Utils.toBinaryID(node.getID());
         final Sector sector = TileHeader.sectorFor(binaryID);
         sortedPointsCount = splitPoints(transaction, _lodDB, binaryID, sector, points, _maxPointsPerLeaf);
      }
      else {
         sortedPointsCount = process(transaction, _lodDB, node.getID(), points);
      }
      transaction.commit();

      _sortedPointsCount += sortedPointsCount;
      _totalPointsCount += pointsSize;

      _progress.stepsDone(pointsSize);

      return keepWorking;
   }


   private static List<Geodetic3D> extractPoints(final Sector sector,
                                                 final List<Geodetic3D> points) {

      final List<Geodetic3D> extracted = new ArrayList<Geodetic3D>();

      final Iterator<Geodetic3D> iterator = points.iterator();
      while (iterator.hasNext()) {
         final Geodetic3D point = iterator.next();
         if (sector.contains(point._latitude, point._longitude)) {
            extracted.add(point);

            iterator.remove();
         }
      }

      final int extractedSize = extracted.size();
      if (extractedSize == 0) {
         return null;
      }

      return extracted;
   }


   private static int splitPoints(final Transaction transaction,
                                  final PersistentLOD lodDB,
                                  final byte[] nodeID,
                                  final Sector nodeSector,
                                  final List<Geodetic3D> nodePoints,
                                  final int maxPointsPerLeaf) {

      int sortedPointsCount = 0;

      final List<Geodetic3D> points = new ArrayList<Geodetic3D>(nodePoints);

      final TileHeader header = new TileHeader(nodeID, nodeSector);
      for (final TileHeader child : header.createChildren()) {
         final List<Geodetic3D> childPoints = extractPoints(child._sector, points);
         if (childPoints != null) {
            if (childPoints.size() > maxPointsPerLeaf) {
               sortedPointsCount += splitPoints(transaction, lodDB, child._id, child._sector, childPoints, maxPointsPerLeaf);
            }
            else {
               sortedPointsCount += process(transaction, lodDB, Utils.toIDString(child._id), childPoints);
            }
         }
      }

      if (!points.isEmpty()) {
         throw new RuntimeException("Logic error!");
      }

      return sortedPointsCount;
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

      System.out.println(nodeID + //
               " lodLevels=" + lodIndices.size() + //
               ", points=" + pointsSize + //
               ", lodIndices=" + lodIndices);
      final int _DGD_AT_WORK;
      //      while (lodIndices.size() > 1) {
      //         final int candidateLevel = lodIndices.peekFirst();
      //         if (candidateLevel > 128) {
      //            break;
      //         }
      //         lodIndices.pollFirst();
      //      }

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
      private final int       _iteration;


      private SortDirties(final PersistentLOD lodDB,
                          final int iteration,
                          final long totalPointsCount) {
         _lodDB = lodDB;
         _iteration = iteration;
         _progress = new GProgress(totalPointsCount, true) {
            @Override
            public void informProgress(final long stepsDone,
                                       final double percent,
                                       final long elapsed,
                                       final long estimatedMsToFinish) {
               System.out.println("- processing dirties \"" + lodDB.getCloudName() + "\" iteration #" + iteration + " "
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
         //final String id = node.getID();
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

         //         final long previousTotalPoints = _previousSortedPointsCount + _previousDirtyPointsCount;
         //         System.out.println("** processing dirties iteration #" + _iteration + " SortedInIteration=" + _sortedPointsCount + //
         //                            ", dirty=" + _dirtyPointsCount + "  (total=" + previousTotalPoints + ")");
      }
   }


   @Override
   public void start() {
      _lodDB = BerkeleyDBLOD.open(_lodCloudName, true);

      _totalPointsCount = 0;
      _sortedPointsCount = 0;

      _counter = 0;
   }


   @Override
   public void stop() {
      _progress.finish();

      long dirtyPointsCount = _totalPointsCount - _sortedPointsCount;
      //      System.out.println("** initial import: sortedPoints=" + _sortedPointsCount + //
      //                         ", dirtyPoints=" + dirtyPointsCount + //
      //                         ", total=" + _totalPointsCount);

      int iteration = 0;
      while (dirtyPointsCount > 0) {
         iteration++;
         final PersistentLOD.Transaction transaction = _lodDB.createTransaction();
         final SortDirties visitor = new SortDirties(_lodDB, iteration, _totalPointsCount);
         _lodDB.acceptDepthFirstVisitor(transaction, visitor);
         transaction.commit();

         dirtyPointsCount = visitor._dirtyPointsCount;
      }

      _lodDB.close();
      _lodDB = null;

   }

}
