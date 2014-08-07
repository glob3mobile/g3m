

package com.glob3mobile.pointcloud.octree;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.zip.GZIPInputStream;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.Geodetic3D;

import com.glob3mobile.pointcloud.octree.postgresql.PostgreSQLOctree;

import es.igosoftware.euclid.projection.GProjection;
import es.igosoftware.euclid.vector.GVector2D;
import es.igosoftware.euclid.vector.IVector2;
import es.igosoftware.util.GUndeterminateProgress;
import es.igosoftware.util.XStringTokenizer;


public class Main {
   private static final String SERVER     = "192.168.1.12";
   private static final String DB_NAME    = "TESTING";
   private static final String USER       = "postgres";
   private static final String PASSWORD   = "postgres";

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


      cleanupOctree();


      final boolean createIfNotExists = true;
      final PersistentOctree octree = PostgreSQLOctree.get(SERVER, DB_NAME, USER, PASSWORD, CLOUD_NAME, createIfNotExists);

      //      createRandomPoints(octree, 100000);

      final String fileName = "18STJ6448.txt.gz";

      final GUndeterminateProgress progress = new GUndeterminateProgress() {
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

      //      final GXYZLoader loader =new GXYZLoader(final GVectorPrecision vectorPrecision,
      //               final GColorPrecision colorPrecision,
      //               final GProjection projection,
      //               final boolean intensitiesFromColor,
      //               final int flags,
      //               final GFileName... fileNames);

      //      final GVectorPrecision vectorPrecision = GVectorPrecision.DOUBLE;
      //      final GColorPrecision colorPrecision = GColorPrecision.INT;
      //      final GProjection projection = GProjection.EPSG_26918;
      //      final boolean intensitiesFromColor = false;
      //      final int flags = GXYZLoader.VERBOSE;
      //      final GFileName fileName = GFileName.relative("18STJ6448.txt");
      //      final GXYZLoader loader = new GXYZLoader(vectorPrecision, colorPrecision, projection, intensitiesFromColor, flags, fileName);
      //
      //      loader.load();
      //      final IUnstructuredVertexContainer<IVector3, Vertex<IVector3>, ?> vertices = loader.getVertices();


      octree.close();


   }


   private static void cleanupOctree() {
      final boolean createIfNotExists = true;
      final PersistentOctree octree = PostgreSQLOctree.get(SERVER, DB_NAME, USER, PASSWORD, CLOUD_NAME, createIfNotExists);
      octree.remove();
      octree.close();
   }


   private static void createRandomPoints(final PersistentOctree octree,
                                          final int pointsCount) {
      final Random rnd = new Random(0);
      for (int i = 0; i < pointsCount; i++) {
         final Angle latitude = Angle.fromDegrees((rnd.nextDouble() * 180) - 90);
         final Angle longitude = Angle.fromDegrees((rnd.nextDouble() * 360) - 180);
         final double height = (rnd.nextDouble() * 500) + 250;

         octree.addPoint(new Geodetic3D(latitude, longitude, height));
      }
   }

}
