

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
