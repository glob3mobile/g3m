

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

               final int pointsSize = node.getPointsCount();

               //final boolean isExemplar = node.getID().equals("032010023013302231");
               final boolean isExemplar = pointsSize == 64920;

               if (isExemplar) {

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

                  System.out.println(node.getID() + " " + lodIndices);

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

                  createDebugImage(node, points, sortedVertices, lodIndices, minHeight, maxHeight);
               }

               progress.stepsDone(pointsSize);


               final boolean keepWorking = !isExemplar;
               //final boolean keepWorking = false;
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
               progress.finish();
            }


            @Override
            public void start() {
            }
         });


      }
   }


}
