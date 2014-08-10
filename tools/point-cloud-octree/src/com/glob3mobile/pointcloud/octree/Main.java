

package com.glob3mobile.pointcloud.octree;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.Geodetic3D;

import com.glob3mobile.pointcloud.octree.berkeleydb.BerkeleyDBOctree;

import es.igosoftware.euclid.projection.GProjection;
import es.igosoftware.euclid.vector.GVector2D;
import es.igosoftware.euclid.vector.IVector2;
import es.igosoftware.util.GUndeterminateProgress;
import es.igosoftware.util.XStringTokenizer;


public class Main {


   public static void main(final String[] args) throws IOException {
      System.out.println("PointClout OcTree 0.1");
      System.out.println("---------------------\n");


      final String cloudName = "Loudoun-VA";

      final boolean createOT = true;
      final boolean visitOT = true;

      if (createOT) {
         createOT(cloudName);
      }

      System.out.println();

      if (visitOT) {
         visitOT(cloudName);
      }

   }


   private static Geodetic3D fromRadians(final double latitudeInRadians,
                                         final double longitudeInRadians,
                                         final double height) {
      return new Geodetic3D( //
               Angle.fromRadians(latitudeInRadians), //
               Angle.fromRadians(longitudeInRadians), //
               height);
   }


   private static void loadOT(final PersistentOctree octree,
                              final String fileName,
                              final GProjection projection) throws IOException {
      final GUndeterminateProgress progress = new GUndeterminateProgress(5, true) {
         @Override
         public void informProgress(final long stepsDone,
                                    final long elapsed) {
            System.out.println("- loading \"" + fileName + "\"" + progressString(stepsDone, elapsed));
         }
      };

      final GProjection targetProjection = GProjection.EPSG_4326;

      try (final BufferedReader reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(
               fileName))))) {
         String line;
         while ((line = reader.readLine()) != null) {
            final String[] tokens = XStringTokenizer.getAllTokens(line, ",");

            final double x = Double.valueOf(tokens[0]);
            final double y = Double.valueOf(tokens[1]);
            final double z = Double.valueOf(tokens[2]);
            // final double intensity = Double.valueOf(tokens[2]);

            final IVector2 sourcePoint = new GVector2D(x, y);
            final IVector2 projectedPointInRadians = projection.transformPoint(targetProjection, sourcePoint);

            octree.addPoint(fromRadians(projectedPointInRadians.y(), projectedPointInRadians.x(), z));

            progress.stepDone();
         }
      }

      progress.finish();

      octree.optimize();
   }


   private static void createOT(final String cloudName) throws IOException {
      BerkeleyDBOctree.delete(cloudName);

      final boolean loadPoints = true;
      if (loadPoints) {
         final boolean createIfNotExists = true;
         try (final PersistentOctree octree = BerkeleyDBOctree.open(cloudName, createIfNotExists)) {
            final GProjection projection = GProjection.EPSG_26918;
            loadOT(octree, "18STJ6448.txt.gz", projection);
         }
      }
   }


   private static void visitOT(final String cloudName) {
      final boolean createIfNotExists = false;
      try (final PersistentOctree octree = BerkeleyDBOctree.open(cloudName, createIfNotExists);) {
         octree.acceptVisitor(new PersistentOctree.Visitor() {
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

               final Geodetic3D averagePoint = node.getAveragePoint();
               System.out.println(" node=" + node.getID() + ", points=" + pointsCount + ", average="
                                  + averagePoint._latitude._degrees + "/" + averagePoint._longitude._degrees + "/"
                                  + averagePoint._height);
               _counter++;
               _totalPoints += pointsCount;
               return true;
            }


            @Override
            public void stop() {
               final long elapsed = System.currentTimeMillis() - _started;
               System.out.println("** Visited " + _counter + " nodes with " + _totalPoints + " points in " + elapsed + "ms");
               System.out.println("** Averge Points per Tile=" + ((float) _totalPoints / _counter));
            }
         });
      }
   }


}
