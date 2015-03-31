

package com.glob3mobile.pointcloud.octree;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.glob3mobile.pointcloud.kdtree.KDInnerNode;
import com.glob3mobile.pointcloud.kdtree.KDLeafNode;
import com.glob3mobile.pointcloud.kdtree.KDTree;
import com.glob3mobile.pointcloud.kdtree.KDTreeVisitor;
import com.glob3mobile.pointcloud.octree.berkeleydb.BerkeleyDBLOD;
import com.glob3mobile.pointcloud.octree.berkeleydb.TileHeader;
import com.glob3mobile.utils.Geodetic3D;
import com.glob3mobile.utils.Sector;
import com.glob3mobile.utils.Utils;

import es.igosoftware.util.GProgress;


class LODSortingTask
         implements
            PersistentOctree.Visitor {
   private final GProgress _progress;
   private final String    _lodCloudName;
   private PersistentLOD   _lodDB;
   private final long      _pointsCount;
   private final int       _maxPointsPerLeaf;
   private long            _sumLevelsCount;

   private long            _processedPointsCount;
   private final File      _cloudDirectory;


   LODSortingTask(final File cloudDirectory,
            final String lodCloudName,
                  final String sourceCloudName,
                  final long pointsCount,
                  final int maxPointsPerLeaf) {
      _cloudDirectory = cloudDirectory;
      _lodCloudName = lodCloudName;
      _pointsCount = pointsCount;
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

      if (pointsSize > 0) {
         final PersistentLOD.Transaction transaction = _lodDB.createTransaction();
         if (pointsSize > _maxPointsPerLeaf) {
            final byte[] binaryID = Utils.toBinaryID(node.getID());
            final Sector sector = TileHeader.sectorFor(binaryID);
            splitPoints(transaction, _lodDB, binaryID, sector, new ArrayList<Geodetic3D>(points), _maxPointsPerLeaf);
         }
         else {
            process(transaction, _lodDB, node.getID(), points);
         }
         transaction.commit();

         _processedPointsCount += pointsSize;

         _progress.stepsDone(pointsSize);
      }

      final boolean keepWorking = true;
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


   private void splitPoints(final PersistentLOD.Transaction transaction,
                            final PersistentLOD lodDB,
                            final byte[] nodeID,
                            final Sector nodeSector,
                            final List<Geodetic3D> points,
                            final int maxPointsPerLeaf) {

      final TileHeader header = new TileHeader(nodeID, nodeSector);
      for (final TileHeader child : header.createChildren()) {
         final List<Geodetic3D> childPoints = extractPoints(child._sector, points);
         if (childPoints != null) {
            if (childPoints.size() > maxPointsPerLeaf) {
               splitPoints(transaction, lodDB, child._id, child._sector, childPoints, maxPointsPerLeaf);
            }
            else {
               process(transaction, lodDB, Utils.toIDString(child._id), childPoints);
            }
         }
      }

      if (!points.isEmpty()) {
         throw new RuntimeException("Logic error!");
      }
   }


   private void process(final PersistentLOD.Transaction transaction,
                        final PersistentLOD lodDB,
                        final String nodeID,
                        final List<Geodetic3D> points) {
      final int pointsSize = points.size();

      final List<Integer> sortedIndices = new ArrayList<Integer>(pointsSize);
      final LinkedList<Integer> lodIndices = new LinkedList<Integer>();

      if (pointsSize == 1) {
         // just one vertex, no need to sort
         lodIndices.add(0);
         sortedIndices.add(0);
      }
      else {
         sortPoints(points, sortedIndices, lodIndices);
      }

      //      System.out.println(nodeID + //
      //               " lodLevels=" + lodIndices.size() + //
      //               ", points=" + pointsSize + //
      //               ", lodIndices=" + lodIndices);


      final int lodLevels = lodIndices.size();
      _sumLevelsCount += lodLevels;

      //      final String parentID = parentID(nodeID);

      //      System.out.println(nodeID + //
      //                         " lodLevels=" + lodLevels + //
      //                         ", points=" + pointsSize + //
      //                         ", lodIndices=" + lodIndices);

      final List<List<Geodetic3D>> levelsPoints = new ArrayList<List<Geodetic3D>>(lodLevels);
      int pointsCounter = 0;
      int fromIndexI = 0;
      for (int level = 0; level < lodLevels; level++) {
         final int toIndexI = lodIndices.get(level);

         final int levelPointsSize = (toIndexI - fromIndexI) + 1;
         final List<Geodetic3D> levelPoints = new ArrayList<Geodetic3D>(levelPointsSize);
         //         System.out.println("Extracting point for level " + level + " range=" + fromIndexI + "->" + toIndexI + " (size="
         //                            + levelPointsSize + ")");
         for (int indexI = fromIndexI; indexI <= toIndexI; indexI++) {
            final int index = sortedIndices.get(indexI);
            final Geodetic3D point = points.get(index);
            levelPoints.add(point);
         }
         pointsCounter += levelPoints.size();

         if (levelPointsSize != levelPoints.size()) {
            throw new RuntimeException("Logic error!");
         }

         //lodDB.put(transaction, nodeID, level, levelPoints);
         levelsPoints.add(levelPoints);

         fromIndexI = toIndexI + 1;
      }

      if (pointsCounter != pointsSize) {
         throw new RuntimeException("Logic error!");
      }

      lodDB.put(transaction, nodeID, levelsPoints);
   }


   //   private void createDebugImage(final Node node,
   //                                 final List<Geodetic3D> points,
   //                                 final List<Integer> sortedIndices,
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
   //      //                     final Geodetic3D point = points.get(sortedIndices.get(cursor));
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
   //            final Geodetic3D point = points.get(sortedIndices.get(cursor));
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


   //   private static String parentID(final String id) {
   //      return id.isEmpty() ? null : id.substring(0, id.length() - 1);
   //   }


   private static void sortPoints(final List<Geodetic3D> points,
                                  final List<Integer> sortedIndices,
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

               final int sortedIndicesCount = sortedIndices.size();
               if (sortedIndicesCount > 0) {
                  lodIndices.add(sortedIndicesCount - 1);
               }
            }

            for (final int vertexIndex : vertexIndexes) {
               sortedIndices.add(vertexIndex);
            }
         }


         @Override
         public void endVisiting(final KDTree tree1) {
         }
      };
      tree.breadthFirstAcceptVisitor(visitor);

      final int sortedIndicesCount = sortedIndices.size();
      if (sortedIndicesCount > 0) {
         lodIndices.add(sortedIndicesCount - 1);
      }
   }


   @Override
   public void start() {
      final int cacheSizeInBytes = 1024 * 1024 * 1024;
      _lodDB = BerkeleyDBLOD.open(_cloudDirectory, _lodCloudName, true, cacheSizeInBytes);

      _processedPointsCount = 0;
      _sumLevelsCount = 0;
   }


   @Override
   public void stop() {
      _lodDB.close();
      _lodDB = null;

      _progress.finish();

      System.out.println("levels count=" + _sumLevelsCount);

      if (_processedPointsCount != _pointsCount) {
         throw new RuntimeException("Logic error");
      }
   }

}
