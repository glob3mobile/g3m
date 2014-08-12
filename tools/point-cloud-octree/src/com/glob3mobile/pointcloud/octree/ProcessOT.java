

package com.glob3mobile.pointcloud.octree;


import com.glob3mobile.pointcloud.octree.PersistentOctree.Node;
import com.glob3mobile.pointcloud.octree.berkeleydb.BerkeleyDBOctree;

import es.igosoftware.util.GProgress;


public class ProcessOT {


   public static void main(final String[] args) {
      System.out.println("ProcessOT 0.1");
      System.out.println("-------------\n");

      final String sourceCloudName = "Loudoun-VA";
      final String targetCloudName = "Loudoun-VA-SORTED";
      final boolean recreateTargetOT = true;
      final int targetBufferSize = 1024 * 64;
      final int targetMaxPointsPerTitle = 1024 * 64;

      // 128K
      // ====
      //      loading "Loudoun-VA-SORTED" [######################################################################] 100.00% [Finished in 7m 25s] | 67.6kB/s l10=34.9kB/s avr=34.2kB/s |
      //               ======================================================================
      //                Loudoun-VA-SORTED
      //                  Points: 15571874
      //                  Nodes: 245
      //                  Levels: 14/18, Average=17.420408
      //                  Points/Node: Average=63558.668, Min=545, Max=128099
      //               ======================================================================
      //
      // 64K
      // ===
      //      loading "Loudoun-VA-SORTED" [######################################################################] 100.00% [Finished in 3m 27s] | 6.3MB/s l10=69.4kB/s avr=73.6kB/s |
      //               ======================================================================
      //                Loudoun-VA-SORTED
      //                  Points: 15571874
      //                  Nodes: 542
      //                  Levels: 14/19, Average=17.998156
      //                  Points/Node: Average=28730.395, Min=545, Max=64920
      //               ======================================================================


      if (recreateTargetOT) {
         BerkeleyDBOctree.delete(targetCloudName);
      }

      try (final PersistentOctree targetOctree = BerkeleyDBOctree.open(targetCloudName, recreateTargetOT, targetBufferSize,
               targetMaxPointsPerTitle)) {
         if (recreateTargetOT) {
            try (final PersistentOctree sourceOctree = BerkeleyDBOctree.open(sourceCloudName, false)) {
               final PersistentOctree.Statistics statistics = sourceOctree.getStatistics(true);
               final long pointsCount = statistics.getPointsCount();
               statistics.show();

               loadTargetOT(sourceOctree, pointsCount, targetOctree);
            }
         }

         targetOctree.getStatistics(true).show();
      }
   }


   private static void loadTargetOT(final PersistentOctree sourceOctree,
                                    final long pointsCount,
                                    final PersistentOctree targetOctree) {
      final GProgress progress = new GProgress(pointsCount, true) {
         @Override
         public void informProgress(final long stepsDone,
                                    final double percent,
                                    final long elapsed,
                                    final long estimatedMsToFinish) {
            System.out.println("  loading \"" + targetOctree.getCloudName() + "\" "
                               + progressString(stepsDone, percent, elapsed, estimatedMsToFinish));
         }
      };

      sourceOctree.acceptVisitor(new PersistentOctree.Visitor() {
         @Override
         public boolean visit(final Node node) {
            for (final Geodetic3D point : node.getPoints()) {
               targetOctree.addPoint(point);
               progress.stepDone();
            }
            return true;
         }


         @Override
         public void stop() {
            progress.finish();
         }


         @Override
         public void start() {
         }
      });

      targetOctree.flush();
   }


}
