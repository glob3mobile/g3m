

package com.glob3mobile.pointcloud.octree;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

   private static final String CLOUD_NAME = "Loudoun-VA";


   private static Geodetic3D fromRadians(final double latitudeInRadians,
                                         final double longitudeInRadians,
                                         final double height) {
      return new Geodetic3D( //
               Angle.fromRadians(latitudeInRadians), //
               Angle.fromRadians(longitudeInRadians), //
               height);
   }


   public static void main(final String[] args) throws IOException {
      System.out.println("PointClout OcTree 0.1");
      System.out.println("---------------------\n");


      BerkeleyDBOctree.delete(CLOUD_NAME);


      final boolean loadPoints = true;
      if (loadPoints) {
         final String fileName = "18STJ6448.txt.gz";
         final boolean createIfNotExists = true;
         final boolean compress = true;

         /*
          NO compress / NO keyPrefix
             - loaded in 40757ms
             - 140M

          YES compress / NO keyPrefix
             - loaded in 39416ms
             - 109M

          YES compress / YES keyPrefix
             - loaded in 39851ms
             - 109M
          */

         final PersistentOctree octree = BerkeleyDBOctree.open(CLOUD_NAME, createIfNotExists, compress);
         final long start = System.currentTimeMillis();
         load(octree, fileName);
         final long elapsed = System.currentTimeMillis() - start;
         System.out.println("\n- loaded in " + elapsed + "ms");

         octree.close();
      }
   }


   private static void load(final PersistentOctree octree,
                            final String fileName) throws IOException, FileNotFoundException {
      final GUndeterminateProgress progress = new GUndeterminateProgress(5, true) {
         @Override
         public void informProgress(final long stepsDone,
                                    final long elapsed) {
            System.out.println("Loading \"" + fileName + "\" " + progressString(stepsDone, elapsed));
         }
      };

      final GProjection projection = GProjection.EPSG_26918;
      final GProjection targetProjection = GProjection.EPSG_4326;

      try (final BufferedReader reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(
               fileName))))) {
         String line;
         while ((line = reader.readLine()) != null) {
            final String[] tokens = XStringTokenizer.getAllTokens(line, ",");

            final double x = Double.valueOf(tokens[0]);
            final double y = Double.valueOf(tokens[1]);
            final double z = Double.valueOf(tokens[2]);
            final double intensity = Double.valueOf(tokens[2]);

            final IVector2 sourcePoint = new GVector2D(x, y);
            final IVector2 projectedPointInRadians = projection.transformPoint(targetProjection, sourcePoint);


            final Geodetic3D point = fromRadians(projectedPointInRadians.y(), projectedPointInRadians.x(), z);
            octree.addPoint(point);

            progress.stepDone();
         }
      }

      progress.finish();

      octree.optimize();
   }


}
