

package com.glob3mobile.pointcloud.octree;


import java.awt.Color;

import com.glob3mobile.pointcloud.octree.berkeleydb.BerkeleyDBLOD;
import com.glob3mobile.pointcloud.octree.berkeleydb.BerkeleyDBOctree;

import es.igosoftware.euclid.colors.GColorF;
import es.igosoftware.util.GMath;
import es.igosoftware.util.GProgress;


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
         final PersistentOctree.Statistics statistics = sourceOctree.getStatistics(false, true);
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


         final String lodCloudName = sourceCloudName + "_LOD";
         BerkeleyDBLOD.delete(lodCloudName);
         sourceOctree.acceptDepthFirstVisitor(new SortingTask(lodCloudName, progress));

         //sourceOctree.acceptVisitor(new CreateMapTask(progress, statistics, 2048 * 2));
      }
   }


}
