

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
import com.glob3mobile.pointcloud.kdtree.KDMonoLeafNode;
import com.glob3mobile.pointcloud.kdtree.KDTree;
import com.glob3mobile.pointcloud.kdtree.KDTreeVisitor;
import com.glob3mobile.pointcloud.octree.PersistentOctree.Node;
import com.glob3mobile.pointcloud.octree.berkeleydb.BerkeleyDBOctree;

import es.igosoftware.euclid.colors.GColorF;
import es.igosoftware.util.GProgress;


public class ProcessOT {


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

      try (final PersistentOctree sourceOctree = BerkeleyDBOctree.open(sourceCloudName, false)) {
         final PersistentOctree.Statistics statistics = sourceOctree.getStatistics(true);
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

         sourceOctree.acceptVisitor(new PersistentOctree.Visitor() {
            @Override
            public boolean visit(final Node node) {
               //               for (final Geodetic3D point : node.getPoints()) {
               //                  progress.stepDone();
               //               }

               //               System.out.println("=> " + node.getID() + " level=" + node.getLevel() + ", points=" + node.getPoints().size());

               final List<Geodetic3D> points = node.getPoints();

               final int pointsSize = points.size();
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

               progress.stepsDone(pointsSize);

               System.out.println(lodIndices);

               final Sector sector = node.getSector();

               final int width = 512;
               final int height = 512;
               final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);

               final Graphics2D g = image.createGraphics();

               int cursor = 0;
               int lodLevel = 0;

               final GColorF[] wheel = GColorF.RED.wheel(lodIndices.size());

               for (final int lodIndex : lodIndices) {
                  //                  final float alpha = (float) lodLevel / lodIndices.size();
                  //                  final GColorF levelColor = GColorF.RED.mixedWidth(GColorF.BLUE, alpha);
                  final Color levelColor = new Color(wheel[lodLevel].getRed(), wheel[lodLevel].getGreen(),
                           wheel[lodLevel].getBlue(), 1);

                  while (cursor <= lodIndex) {
                     //                     g.setColor(new Color(levelColor.getRed(), levelColor.getGreen(), levelColor.getBlue(), 1));
                     g.setColor(levelColor);
                     final Geodetic3D point = points.get(cursor);
                     final int x = Math.round((float) (sector.getUCoordinate(point._longitude) * width));
                     final int y = Math.round((float) (sector.getVCoordinate(point._latitude) * height));
                     g.drawRect(x, y, 1, 1);

                     cursor++;
                  }
                  lodLevel++;
               }

               g.dispose();

               try {
                  ImageIO.write(image, "png", new File(node.getID() + ".png"));
               }
               catch (final IOException e) {
                  throw new RuntimeException(e);
               }

               final boolean keepWorking = false;
               return keepWorking;
            }


            private void sortPoints(final List<Geodetic3D> points,
                                    final List<Integer> sortedVertices,
                                    final List<Integer> lodIndices) {
               final KDTree tree = new KDTree(points);

               final KDTreeVisitor visitor = new KDTreeVisitor() {
                  private int _lastDepth = 0;


                  @Override
                  public void startVisiting(final KDTree tree1) {
                  }


                  @Override
                  public void visitInnerNode(final KDInnerNode innerNode) {
                     pushVertexIndex(innerNode.getVertexIndex(), innerNode.getDepth());
                  }


                  @Override
                  public void visitLeafNode(final KDMonoLeafNode leafNode) {
                     pushVertexIndex(leafNode.getVertexIndex(), leafNode.getDepth());
                  }


                  private void pushVertexIndex(final int vertexIndex,
                                               final int depth) {
                     if (_lastDepth != depth) {
                        _lastDepth = depth;

                        final int sortedVerticesCount = sortedVertices.size();
                        if (sortedVerticesCount > 0) {
                           lodIndices.add(sortedVerticesCount - 1);
                        }
                     }

                     sortedVertices.add(vertexIndex);
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
               progress.finish();
            }


            @Override
            public void start() {
            }
         });


      }
   }

   //   private static void loadTargetOT(final PersistentOctree sourceOctree,
   //                                    final long pointsCount,
   //                                    final PersistentOctree targetOctree) {
   //      final GProgress progress = new GProgress(pointsCount, true) {
   //         @Override
   //         public void informProgress(final long stepsDone,
   //                                    final double percent,
   //                                    final long elapsed,
   //                                    final long estimatedMsToFinish) {
   //            System.out.println("  loading \"" + targetOctree.getCloudName() + "\" "
   //                     + progressString(stepsDone, percent, elapsed, estimatedMsToFinish));
   //         }
   //      };
   //
   //      sourceOctree.acceptVisitor(new PersistentOctree.Visitor() {
   //         @Override
   //         public boolean visit(final Node node) {
   //            for (final Geodetic3D point : node.getPoints()) {
   //               targetOctree.addPoint(point);
   //               progress.stepDone();
   //            }
   //            return true;
   //         }
   //
   //
   //         @Override
   //         public void stop() {
   //            progress.finish();
   //         }
   //
   //
   //         @Override
   //         public void start() {
   //         }
   //      });
   //
   //      targetOctree.flush();
   //   }


}
