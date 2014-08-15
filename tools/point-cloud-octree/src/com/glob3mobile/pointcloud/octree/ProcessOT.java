

package com.glob3mobile.pointcloud.octree;


import java.awt.Color;
import java.util.List;

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
            //lodDB.acceptDepthFirstVisitor(null, new LODShowStatistics());


            System.out.println();
            final long start = System.currentTimeMillis();
            //final List<PersistentLOD.Level> levels = lodDB.getLODLevels("032010023013302133");
            final List<PersistentLOD.Level> levels = lodDB.getLODLevels("0320100230133021");
            //final List<PersistentLOD.Level> levels = lodDB.getLODLevels("0320100230133021332");
            //final List<PersistentLOD.Level> levels = lodDB.getLODLevels("0320100230133021333");
            System.out.println("== " + (System.currentTimeMillis() - start) + "ms");

            long totalPoints = 0;
            for (final PersistentLOD.Level level : levels) {
               final int levelSize = level.size();
               System.out.println(" Level=" + level.getLevel() + " points=" + levelSize);
               totalPoints += levelSize;
            }
            System.out.println("* Total Points=" + totalPoints);
         }
      }

   }

}
