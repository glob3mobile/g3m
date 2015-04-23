

package com.glob3mobile.pointcloud.octree;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.glob3mobile.pointcloud.octree.berkeleydb.BerkeleyDBLOD;
import com.glob3mobile.pointcloud.octree.berkeleydb.BerkeleyDBOctree;
import com.glob3mobile.utils.Geodetic3D;
import com.glob3mobile.utils.Sector;

import es.igosoftware.util.GUndeterminateProgress;


public class ProcessOT {

   //   private static final GColorF[] RAMP = new GColorF[] { GColorF.CYAN, GColorF.GREEN, GColorF.YELLOW, GColorF.RED };
   //
   //
   //   private static GColorF interpolateColorFromRamp(final GColorF colorFrom,
   //                                                   final GColorF[] ramp,
   //                                                   final float alpha) {
   //      final float rampStep = 1f / ramp.length;
   //
   //      final int toI;
   //      if (GMath.closeTo(alpha, 1)) {
   //         toI = ramp.length - 1;
   //      }
   //      else {
   //         toI = (int) (alpha / rampStep);
   //      }
   //
   //      final GColorF from;
   //      if (toI == 0) {
   //         from = colorFrom;
   //      }
   //      else {
   //         from = ramp[toI - 1];
   //      }
   //
   //      final float colorAlpha = (alpha % rampStep) / rampStep;
   //      return from.mixedWidth(ramp[toI], colorAlpha);
   //   }


   public static void main(final String[] args) {
      System.out.println("ProcessOT 0.1");
      System.out.println("-------------\n");

      //      // final File cloudDirectory = new File(System.getProperty("user.dir"));
      //      final File cloudDirectory = new File("/Volumes/My Passport/_LIDAR_COPY");
      //
      //
      //      final String completeSourceCloudName = "Loudoun-VA";
      //      //final String sourceCloudName = "Loudoun-VA_simplified";
      //      final String simplifiedCloudName = completeSourceCloudName + "_simplified2";
      //      //final String sourceCloudName = simplifiedCloudName;
      //      final String fragmentCloudName = completeSourceCloudName + "_fragment";
      //      final String sourceCloudName = fragmentCloudName;
      //      final String lodCloudName = sourceCloudName + "_LOD";


      //      final File cloudDirectory = new File("/Volumes/My Passport/_belgium_lidar_/db");
      //
      //      final String sourceCloudName = "Wallonia-Belgium_simplified2";
      //      final String lodCloudName = sourceCloudName + "_LOD";

      final File cloudDirectory = new File(System.getProperty("user.dir"));

      //      final String sourceCloudName = "Wallonia_simplified";
      final String sourceCloudName = "Wallonia";
      final String lodCloudName = sourceCloudName + "_LOD";


      //      final File cloudDirectory = new File("/Volumes/My Passport/_minnesota_lidar_/db");
      //
      //      final String sourceCloudName = "minnesota";
      //      final String lodCloudName = sourceCloudName + "_LOD";


      final long cacheSizeInBytes = 4 * 1024 * 1024 * 1024;

      //      final boolean createSimplifiedCloudName = false;
      final boolean createMapForSourceOT = false;
      final boolean createLOD = true;
      final boolean showLODStats = true;
      final boolean drawSampleLODNode = false;

      //      final boolean createFragmentCloudName = false;
      //
      //      if (createFragmentCloudName) {
      //         try (final PersistentOctree sourceOctree = BerkeleyDBOctree.openReadOnly(cloudDirectory, completeSourceCloudName,
      //                  cacheSizeInBytes)) {
      //            final PersistentOctree.Statistics statistics = sourceOctree.getStatistics(true);
      //            statistics.show();
      //
      //
      //            //            // cantera
      //            //            final Sector sector = Sector.fromDegrees( //
      //            //                     39.051968051473274102, -77.5404852494428809, //
      //            //                     39.095519409073318684, -77.497507593275656745);
      //            //            // - processing for "Loudoun-VA" [ done ] 45819 steps [Finished in 5s] 8.9kB/sec (avr=8.9kB/sec)
      //            //            // Total points: 43447637, nodes: 45819, full: 1285, edges: 148
      //
      //
      //            final Sector sector = Sector.fromDegrees( //
      //                     39.058452085532550768, -77.692590169281558587, //
      //                     39.107394637653797531, -77.594653167458375265);
      //            // - processing "Loudoun-VA" [ done ] 88296 steps [Finished in 11m 57s] 123.2B/sec (avr=123.2B/sec)
      //            // Total points: 119809579, nodes: 88296, full: 3757, edges: 244
      //            // Avr Density=5.933766718645362E-10 / 7.644235651407404E-10
      //
      //            final int maxPointsPerTitle = 256 * 1024;
      //
      //            sourceOctree.acceptDepthFirstVisitor( //
      //                     sector, //
      //                     new FragmentCreator(cloudDirectory, fragmentCloudName, sector, cacheSizeInBytes, maxPointsPerTitle));
      //         }
      //      }

      //      if (createSimplifiedCloudName) {
      //         try (final PersistentOctree sourceOctree = BerkeleyDBOctree.openReadOnly(cloudDirectory, completeSourceCloudName,
      //                  cacheSizeInBytes)) {
      //            final PersistentOctree.Statistics statistics = sourceOctree.getStatistics(true);
      //            statistics.show();
      //
      //            final long sourcePointsCount = statistics.getPointsCount();
      //
      //            final int maxPointsPerTitle = 256 * 1024;
      //            final float resultSizeFactor = 0.06f;
      //            //            final float resultSizeFactor = 0.1f;
      //
      //            final SimplifyOctreeTask visitor = new SimplifyOctreeTask( //
      //                     completeSourceCloudName, //
      //                     cloudDirectory, //
      //                     simplifiedCloudName, //
      //                     cacheSizeInBytes, //
      //                     sourcePointsCount, //
      //                     resultSizeFactor, //
      //                     maxPointsPerTitle);
      //            sourceOctree.acceptDepthFirstVisitor(visitor);
      //         }
      //         System.out.println();
      //      }

      if (createMapForSourceOT) {
         try (final PersistentOctree sourceOctree = BerkeleyDBOctree.openReadOnly(cloudDirectory, sourceCloudName,
                  cacheSizeInBytes)) {
            final PersistentOctree.Statistics statistics = sourceOctree.getStatistics(true);
            statistics.show();

            sourceOctree.acceptDepthFirstVisitor(new CreateMapTask(sourceCloudName, statistics, 2048 * 2));
         }
         System.out.println();
      }

      if (createLOD) {
         try (final PersistentOctree sourceOctree = BerkeleyDBOctree.openReadOnly(cloudDirectory, sourceCloudName,
                  cacheSizeInBytes)) {
            final PersistentOctree.Statistics statistics = sourceOctree.getStatistics(true);
            final long pointsCount = statistics.getPointsCount();
            statistics.show();

            BerkeleyDBLOD.delete(cloudDirectory, lodCloudName);
            //final int maxPointsPerLeaf = 4 * 1024;
            final int maxPointsPerLeaf = Integer.MAX_VALUE;
            sourceOctree.acceptDepthFirstVisitor(new LODSortingTask(cloudDirectory, lodCloudName, sourceCloudName, pointsCount,
                     maxPointsPerLeaf));
         }
         System.out.println();
      }

      if (showLODStats) {
         try (final PersistentLOD lodDB = BerkeleyDBLOD.openReadOnly(cloudDirectory, lodCloudName, cacheSizeInBytes)) {
            final PersistentLOD.Statistics statistics = lodDB.getStatistics(true);
            statistics.show();

            //            final Sector wholeSector = statistics.getSector();
            //            final TileHeader rootHeader = TileHeader.deepestEnclosingTileHeader(wholeSector);
            //
            //            System.out.println(rootHeader);
            //
            //
            //            lodDB.acceptDepthFirstVisitor(null, new PersistentLOD.Visitor() {
            //               private final List<String> _nodesIDs = new ArrayList<String>((int) statistics.getNodesCount());
            //               private long               _sumIDLengths;
            //
            //
            //               @Override
            //               public void start(final PersistentLOD.Transaction transaction) {
            //                  _sumIDLengths = 0;
            //               }
            //
            //
            //               @Override
            //               public void stop(final PersistentLOD.Transaction transaction) {
            //                  System.out.println(_nodesIDs.size());
            //
            //                  System.out.println((_nodesIDs.size() * 3) + _sumIDLengths);
            //               }
            //
            //
            //               @Override
            //               public boolean visit(final PersistentLOD.Transaction transaction,
            //                                    final PersistentLOD.Node node) {
            //                  final String nodeID = node.getID();
            //                  //System.out.println(nodeID);
            //                  _nodesIDs.add(nodeID);
            //                  _sumIDLengths += nodeID.length();
            //                  return true;
            //               }
            //            });


            final boolean showStatisticsForSector = false;
            if (showStatisticsForSector) {
               final Sector sector = Sector.fromDegrees( //
                        39.198205348894802569, -77.673339843749985789, //
                        39.249270846223389242, -77.607421875);


               final PersistentLOD.Visitor visitor = new PersistentLOD.Visitor() {
                  private GUndeterminateProgress _progress;
                  private long                   _nodesCounter;
                  private long                   _levelsCounter;
                  private long                   _pointsCounter;
                  private double                 _sumDensity;
                  private double                 _minDensity;
                  private double                 _maxDensity;


                  @Override
                  public void start(final PersistentLOD.Transaction transaction) {
                     _nodesCounter = 0;
                     _levelsCounter = 0;
                     _pointsCounter = 0;
                     _sumDensity = 0;

                     _minDensity = Double.POSITIVE_INFINITY;
                     _maxDensity = Double.NEGATIVE_INFINITY;

                     _progress = new GUndeterminateProgress(10, true) {
                        @Override
                        public void informProgress(final long stepsDone,
                                                   final long elapsed) {
                           System.out.println("- gathering statistics for \"" + lodDB.getCloudName() + "\""
                                              + progressString(stepsDone, elapsed));
                        }
                     };
                  }


                  @Override
                  public boolean visit(final PersistentLOD.Transaction transaction,
                                       final PersistentLOD.Node node) {

                     final int nodePointsCount = node.getPointsCount();

                     _nodesCounter++;
                     _pointsCounter += nodePointsCount;
                     _levelsCounter += node.getLevelsCount();

                     final Sector nodeSector = node.getSector();
                     final double squaredDegrees = nodeSector._deltaLongitude._degrees * nodeSector._deltaLongitude._degrees;
                     final double nodeDensity = nodePointsCount / squaredDegrees;
                     _sumDensity += nodeDensity;
                     _minDensity = Math.min(_minDensity, nodeDensity);
                     _maxDensity = Math.max(_maxDensity, nodeDensity);

                     _progress.stepDone();

                     // System.out.println(" ==> " + node.getID());

                     final boolean keepWorking = true;
                     // final boolean keepWorking = _nodesCounter < 50;
                     return keepWorking;
                  }


                  @Override
                  public void stop(final PersistentLOD.Transaction transaction) {
                     _progress.finish();
                     System.out.println("======================================================================");
                     System.out.println(" Sector: " + sector);
                     System.out.println("   Nodes: " + _nodesCounter);
                     System.out.println("   Levels: " + _levelsCounter);
                     System.out.println("     Levels/Node: " + ((float) _levelsCounter / _nodesCounter));
                     System.out.println("   Points: " + _pointsCounter);
                     System.out.println("     Points/Node: " + ((float) _pointsCounter / _nodesCounter));
                     System.out.println("     Points/Level: " + ((float) _pointsCounter / _levelsCounter));
                     System.out.println("   Density/Node: Average=" + (_sumDensity / _nodesCounter) + //
                                        ", Min=" + _minDensity + //
                                        ", Max=" + _maxDensity);
                     System.out.println("======================================================================");
                  }
               };


               final PersistentLOD.Transaction transaction = null;
               lodDB.acceptVisitor(transaction, visitor, sector);
               //lodDB.acceptDepthFirstVisitor(null, new LODShowStatistics());
            }
         }
         System.out.println();
      }

      if (drawSampleLODNode) {
         try (final PersistentLOD lodDB = BerkeleyDBLOD.openReadOnly(cloudDirectory, lodCloudName, cacheSizeInBytes)) {
            final PersistentLOD.Statistics statistics = lodDB.getStatistics(true);
            statistics.show();

            final double minHeight = statistics.getMinHeight();
            final double maxHeight = statistics.getMaxHeight();


            //            final String id = "032010023321230000"; // FoundSelf -> OK
            final String id = "0320100233212030"; // FoundSelf -> OK
            // final String id = "333333333333333"; // FoundNothing -> OK

            //            final String id = "03201002332123000000"; // NotFoundSelfNorDescendants **** PENDING ***

            // final String id = "03201002332123000"; // FoundDescendants -> OK
            // final String id = "0320100233212300"; // FoundDescendants -> OK
            //final String id = "03201002332"; // FoundDescendants -> OK

            final Sector sector = lodDB.getSector(id);

            final long start = System.currentTimeMillis();
            final PersistentLOD.Node node = lodDB.getNode(id, true);
            final long elapsed = System.currentTimeMillis() - start;
            System.out.println("== " + elapsed + "ms");

            final List<Geodetic3D> accumulatedPoints = new ArrayList<Geodetic3D>();
            long totalPoints = 0;
            for (final PersistentLOD.NodeLevel level : node.getLevels()) {
               System.out.println(level);
               totalPoints += level.getPointsCount();

               accumulatedPoints.addAll(level.getPoints(null));
               generateImage(id, level.getLevel(), sector, accumulatedPoints, minHeight, maxHeight);
            }
            System.out.println("* Total Points=" + totalPoints);
         }
         System.out.println();
      }

   }


   private static void generateImage(final String id,
                                     final int level,
                                     final Sector sector,
                                     final List<Geodetic3D> points,
                                     final double minHeight,
                                     final double maxHeight) {
      final int imageWidth = 1024 / 2;
      final int imageHeight = 1024 / 2;

      final BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_4BYTE_ABGR);
      final Graphics2D g = image.createGraphics();

      //      g.setColor(Color.WHITE);
      //      g.fillRect(0, 0, imageWidth, imageHeight);


      g.setColor(Color.WHITE);

      @SuppressWarnings("unused")
      final double deltaHeight = maxHeight - minHeight;

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

      final String imageName = "_DEBUG_" + id + "_L" + level + ".png";
      try {
         ImageIO.write(image, "png", new File(imageName));
      }
      catch (final IOException e) {
         throw new RuntimeException(e);
      }
   }
}
