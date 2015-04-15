

package com.glob3mobile.pointcloud.octree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import com.glob3mobile.pointcloud.octree.berkeleydb.BerkeleyDBOctree;
import com.glob3mobile.utils.Geodetic3D;

import es.igosoftware.euclid.projection.GProjection;
import es.igosoftware.euclid.vector.GVector2D;
import es.igosoftware.euclid.vector.IVector2;
import es.igosoftware.util.GUndeterminateProgress;
import es.igosoftware.util.XStringTokenizer;


public class CreateOT {


   public static void main(final String[] args) throws IOException {
      System.out.println("CreateOT 0.1");
      System.out.println("------------\n");

      //      final File sourceTXTDirectory = new File(System.getProperty("user.dir"));
      //      final GProjection sourceProjection = GProjection.EPSG_26918;
      //
      //
      //      final File cloudDirectory = new File(System.getProperty("user.dir"));
      //      //final File cloudDirectory = new File("/Volumes/My Passport/_LIDAR_COPY");
      //
      //      final String cloudName = "Loudoun-VA";


      //      final File sourceTXTDirectory = new File("/Volumes/My Passport/_belgium_lidar_/txt");
      //      final GProjection sourceProjection = GProjection.EPSG_31370;
      //
      //      final File cloudDirectory = new File("/Volumes/My Passport/_belgium_lidar_/db");
      //      final String cloudName = "Wallonia-Belgium";


      final GProjection sourceProjection = GProjection.EPSG_31370;

      //    final File sourceTXTDirectory = new File("/Volumes/My Passport/_minnesota_lidar_/txt");
      //    final File cloudDirectory = new File("/Volumes/My Passport/_minnesota_lidar_/db");
      //    final String cloudName = "minnesota";

      final File sourceTXTDirectory = new File(System.getProperty("user.dir"));
      final File cloudDirectory = new File(System.getProperty("user.dir"));
      final String cloudName = "Wallonia";


      final boolean deleteOT = true;
      final boolean loadOT = true;
      final boolean renameDone = false;
      final boolean visitOT = false;
      final boolean showStatisticsOT = true;

      if (deleteOT) {
         BerkeleyDBOctree.delete(cloudDirectory, cloudName);
      }

      if (loadOT) {
         loadOT(cloudDirectory, cloudName, renameDone, sourceProjection, getFilesToLoad(sourceTXTDirectory));
      }

      System.out.println();

      if (visitOT) {
         visitOT(cloudDirectory, cloudName);
      }

      System.out.println();

      if (showStatisticsOT) {
         showStatisticsOT(cloudDirectory, cloudName);
      }

   }


   private static String[] getFilesToLoad(final File sourceTXTDirectory) {
      final String[] filesNames = sourceTXTDirectory.list(new FilenameFilter() {
         @Override
         public boolean accept(final File dir,
                               final String name) {
            return name.endsWith(".txt") || name.endsWith(".txt.gz");
         }
      });

      final String[] result = new String[filesNames.length];
      for (int i = 0; i < filesNames.length; i++) {
         final String fileName = filesNames[i];
         result[i] = new File(sourceTXTDirectory, fileName).getAbsolutePath();
      }
      return result;
   }


   private static void loadOT(final PersistentOctree octree,
                              final GProjection projection,
                              final String fileName,
                              final String extraMsg) throws IOException {
      final GUndeterminateProgress progress = new GUndeterminateProgress(10, true) {
         @Override
         public void informProgress(final long stepsDone,
                                    final long elapsed) {
            System.out.println("- loading \"" + fileName + "\" " + extraMsg + progressString(stepsDone, elapsed));
         }
      };

      final GProjection targetProjection = GProjection.EPSG_4326;

      try (final BufferedReader reader = open(fileName)) {
         String line;
         while ((line = reader.readLine()) != null) {
            final String[] tokens = XStringTokenizer.getAllTokens(line, ",");

            final double x = Double.valueOf(tokens[0]);
            final double y = Double.valueOf(tokens[1]);
            final double z = Double.valueOf(tokens[2]);
            // final double intensity = Double.valueOf(tokens[2]);

            final IVector2 sourcePoint = new GVector2D(x, y);
            final IVector2 projectedPointInRadians = projection.transformPoint(targetProjection, sourcePoint);

            octree.addPoint(Geodetic3D.fromRadians(projectedPointInRadians.y(), projectedPointInRadians.x(), z));

            progress.stepDone();
         }
      }

      octree.flush();
      progress.finish();

      octree.getStatistics(false).show();
   }


   private static BufferedReader open(final String fileName) throws IOException {
      if (fileName.toLowerCase().endsWith(".gz")) {
         return new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(fileName))));
      }
      return new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
   }


   private static void loadOT(final File cloudDirectory,
                              final String cloudName,
                              final boolean renameDone,
                              final GProjection projection,
                              final String... filesNames) throws IOException {

      //      final int bufferSize = 512 * 1024;
      //      final int maxPointsPerTitle = 512 * 1024;
      final int bufferSize = 192 * 1024;
      final int maxPointsPerTitle = 192 * 1024;

      final boolean loadPoints = true;
      if (loadPoints) {
         final boolean createIfNotExists = true;
         final int cacheSizeInBytes = 1024 * 1024 * 1024;
         try (final PersistentOctree octree = BerkeleyDBOctree.open(cloudDirectory, cloudName, createIfNotExists,
                  maxPointsPerTitle, bufferSize, cacheSizeInBytes)) {

            final int filesNamesLength = filesNames.length;
            for (int i = 0; i < filesNamesLength; i++) {
               final String fileName = filesNames[i];
               final String extraMsg = (i + 1) + "/" + filesNamesLength;
               loadOT(octree, projection, fileName, extraMsg);

               if (renameDone) {
                  new File(fileName).renameTo(new File(fileName + ".DONE"));
               }
            }

            octree.optimize();
         }
      }
   }


   private static void visitOT(final File cloudDirectory,
                               final String cloudName) {
      final boolean createIfNotExists = false;
      final int cacheSizeInBytes = 1024 * 1024 * 1024;

      try (final PersistentOctree octree = BerkeleyDBOctree.open(cloudDirectory, cloudName, createIfNotExists, cacheSizeInBytes)) {
         octree.acceptDepthFirstVisitor(new PersistentOctree.Visitor() {
            private int  _counter;
            private long _started;
            private long _totalPoints;


            @Override
            public void start() {
               _counter = 0;
               _started = System.currentTimeMillis();
               _totalPoints = 0;
            }


            @Override
            public boolean visit(final PersistentOctree.Node node) {
               final int pointsCount = node.getPoints().size();
               //final int pointsCount = node.getPointsCount();

               // final Geodetic3D avrPoint = node.getAveragePoint();
               // final String avrPointStr = avrPoint._latitude._degrees + "/" + avrPoint._longitude._degrees + "/"
               // + avrPoint._height;

               //               System.out.println(" node=" + node.getID() + //
               //                        ", level=" + node.getLevel() + //
               //                        ", points=" + pointsCount //
               //                        // ", average=" + avrPointStr //
               //                        );

               _counter++;
               _totalPoints += pointsCount;
               return true;
            }


            @Override
            public void stop() {
               final long elapsed = System.currentTimeMillis() - _started;
               System.out.println("** Visited " + _counter + " nodes with " + _totalPoints + " points in " + elapsed + "ms");
               System.out.println("** Average Points per Tile=" + ((float) _totalPoints / _counter));
            }
         });
      }
   }


   private static void showStatisticsOT(final File cloudDirectory,
                                        final String cloudName) {
      final boolean createIfNotExists = false;
      final int cacheSizeInBytes = 1024 * 1024 * 1024;
      try (final PersistentOctree octree = BerkeleyDBOctree.open(cloudDirectory, cloudName, createIfNotExists, cacheSizeInBytes)) {
         octree.getStatistics(false).show();
      }
   }


}
