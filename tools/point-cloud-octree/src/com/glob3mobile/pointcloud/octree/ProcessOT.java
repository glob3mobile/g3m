

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

import es.igosoftware.euclid.colors.GColorF;
import es.igosoftware.util.GMath;


public class ProcessOT {

   static final GColorF[] RAMP = new GColorF[] { GColorF.CYAN, GColorF.GREEN, GColorF.YELLOW, GColorF.RED };


   static GColorF interpolateColorFromRamp(final GColorF colorFrom,
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


   static Color toAWTColor(final GColorF color) {
      return new Color(color.getRed(), color.getGreen(), color.getBlue(), 1);
   }


   public static void main(final String[] args) {
      System.out.println("ProcessOT 0.1");
      System.out.println("-------------\n");


      final String sourceCloudName = "Loudoun-VA";
      final String lodCloudName = sourceCloudName + "_LOD";

      //      try (final PersistentOctree sourceOctree = BerkeleyDBOctree.openReadOnly(sourceCloudName)) {
      //         final PersistentOctree.Statistics statistics = sourceOctree.getStatistics(false, true);
      //         final long pointsCount = statistics.getPointsCount();
      //         statistics.show();
      //
      //         final boolean createLOD = true;
      //         if (createLOD) {
      //            BerkeleyDBLOD.delete(lodCloudName);
      //            final int maxPointsPerLeaf = 24 * 1024;
      //            sourceOctree.acceptDepthFirstVisitor(new LODSortingTask(lodCloudName, sourceCloudName, pointsCount, maxPointsPerLeaf));
      //         }
      //
      //         //sourceOctree.acceptVisitor(new CreateMapTask(progress, statistics, 2048 * 2));
      //      }

      System.out.println();

      final boolean showLODStats = true;
      if (showLODStats) {
         try (final PersistentLOD lodDB = BerkeleyDBLOD.openReadOnly(lodCloudName)) {
            //            lodDB.acceptDepthFirstVisitor(null, new LODShowStatistics());


            final boolean drawSampleNode = true;
            if (drawSampleNode) {
               System.out.println();

               //final String id = "032010023013302133";
               final String id = "0320100233212300003";

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
                  generateImage(id, level.getLevel(), sector, accumulatedPoints);
               }
               System.out.println("* Total Points=" + totalPoints);
            }
         }
      }

   }


   private static void generateImage(final String id,
                                     final int level,
                                     final Sector sector,
                                     final List<Geodetic3D> points) {
      final int imageWidth = 1024;
      final int imageHeight = 1024;

      final BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_4BYTE_ABGR);
      final Graphics2D g = image.createGraphics();

      g.setColor(Color.BLACK);
      g.fillRect(0, 0, imageWidth, imageHeight);

      g.setColor(Color.WHITE);
      for (final Geodetic3D point : points) {
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
