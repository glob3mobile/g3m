

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


public class ProcessOT {


   public static void main(final String[] args) {
      System.out.println("ProcessOT 0.1");
      System.out.println("-------------\n");


      final String sourceCloudName = "Loudoun-VA";
      final String lodCloudName = sourceCloudName + "_LOD";

      final boolean createSourceBitmapMap = false;
      final boolean createLOD = true;
      final boolean showLODStats = true;
      final boolean drawSampleLODNode = true;

      if (createSourceBitmapMap) {
         try (final PersistentOctree sourceOctree = BerkeleyDBOctree.openReadOnly(sourceCloudName)) {
            final PersistentOctree.Statistics statistics = sourceOctree.getStatistics(false, true);
            statistics.show();

            sourceOctree.acceptDepthFirstVisitor(new CreateMapTask(sourceCloudName, statistics, 2048 * 2));
         }
      }

      if (createLOD) {
         try (final PersistentOctree sourceOctree = BerkeleyDBOctree.openReadOnly(sourceCloudName)) {
            final PersistentOctree.Statistics statistics = sourceOctree.getStatistics(false, true);
            final long pointsCount = statistics.getPointsCount();
            statistics.show();

            BerkeleyDBLOD.delete(lodCloudName);
            final int maxPointsPerLeaf = 4 * 1024;
            //final int maxPointsPerLeaf = Integer.MAX_VALUE;
            sourceOctree.acceptDepthFirstVisitor(new LODSortingTask(lodCloudName, sourceCloudName, pointsCount, maxPointsPerLeaf));
         }
         System.out.println();
      }


      if (showLODStats) {
         try (final PersistentLOD lodDB = BerkeleyDBLOD.openReadOnly(lodCloudName)) {
            lodDB.acceptDepthFirstVisitor(null, new LODShowStatistics());
         }
         System.out.println();
      }

      if (drawSampleLODNode) {
         try (final PersistentLOD lodDB = BerkeleyDBLOD.openReadOnly(lodCloudName)) {
            final String id = "0320100230133020333";

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
         System.out.println();
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
