

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

import es.igosoftware.euclid.colors.GColorF;
import es.igosoftware.util.GMath;


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


   public static void main(final String[] args) {
      System.out.println("ProcessOT 0.1");
      System.out.println("-------------\n");


      final String sourceCloudName = "Loudoun-VA";
      final String lodCloudName = sourceCloudName + "_LOD";

      final boolean createMapForSourceOT = false;
      final boolean createLOD = true;
      final boolean showLODStats = true;
      final boolean drawSampleLODNode = false;

      if (createMapForSourceOT) {
         try (final PersistentOctree sourceOctree = BerkeleyDBOctree.openReadOnly(sourceCloudName)) {
            final PersistentOctree.Statistics statistics = sourceOctree.getStatistics(false, true);
            statistics.show();

            sourceOctree.acceptDepthFirstVisitor(new CreateMapTask(sourceCloudName, statistics, 2048 * 2));
         }
         System.out.println();
      }

      if (createLOD) {
         try (final PersistentOctree sourceOctree = BerkeleyDBOctree.openReadOnly(sourceCloudName)) {
            final PersistentOctree.Statistics statistics = sourceOctree.getStatistics(false, true);
            final long pointsCount = statistics.getPointsCount();
            statistics.show();

            BerkeleyDBLOD.delete(lodCloudName);
            //final int maxPointsPerLeaf = 4 * 1024;
            final int maxPointsPerLeaf = Integer.MAX_VALUE;
            sourceOctree.acceptDepthFirstVisitor(new LODSortingTask(lodCloudName, sourceCloudName, pointsCount, maxPointsPerLeaf));
         }
         System.out.println();
      }

      if (showLODStats) {
         try (final PersistentLOD lodDB = BerkeleyDBLOD.openReadOnly(lodCloudName)) {
            final PersistentLOD.Statistics statistics = lodDB.getStatistics(false, true);
            statistics.show();
            //lodDB.acceptDepthFirstVisitor(null, new LODShowStatistics());
         }
         System.out.println();
      }

      if (drawSampleLODNode) {
         try (final PersistentLOD lodDB = BerkeleyDBLOD.openReadOnly(lodCloudName)) {
            final PersistentLOD.Statistics statistics = lodDB.getStatistics(false, true);

            final double minHeight = statistics.getMinHeight();
            final double maxHeight = statistics.getMaxHeight();


            final String id = "032010023321230000"; // FoundSelf -> OK
            // final String id = "333333333333333"; // FoundNothing -> OK

            //            final String id = "03201002332123000000"; // NotFoundSelfNorDescendants **** PENDING ***

            // final String id = "03201002332123000"; // FoundDescendants -> OK
            // final String id = "0320100233212300"; // FoundDescendants -> OK
            //final String id = "03201002332"; // FoundDescendants -> OK

            final Sector sector = lodDB.getSector(id);

            final long start = System.currentTimeMillis();
            final List<PersistentLOD.Level> levels = lodDB.getLODLevels(id);
            final long elapsed = System.currentTimeMillis() - start;
            System.out.println("== " + elapsed + "ms");

            final List<Geodetic3D> accumulatedPoints = new ArrayList<Geodetic3D>();
            long totalPoints = 0;
            for (final PersistentLOD.Level level : levels) {
               System.out.println(level);
               totalPoints += level.size();

               accumulatedPoints.addAll(level.getPoints());
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
      final int imageWidth = 1024;
      final int imageHeight = 1024;

      final BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_4BYTE_ABGR);
      final Graphics2D g = image.createGraphics();

      //      g.setColor(Color.WHITE);
      //      g.fillRect(0, 0, imageWidth, imageHeight);


      g.setColor(Color.WHITE);

      //final double deltaHeight = maxHeight - minHeight;

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
