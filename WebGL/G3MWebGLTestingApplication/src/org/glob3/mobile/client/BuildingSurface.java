

package org.glob3.mobile.client;

import java.util.ArrayList;

import org.glob3.mobile.generated.AltitudeMode;
import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.DirectMesh;
import org.glob3.mobile.generated.FloatBufferBuilderFromCartesian3D;
import org.glob3.mobile.generated.FloatBufferBuilderFromGeodetic;
import org.glob3.mobile.generated.GLPrimitive;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IMathUtils;
import org.glob3.mobile.generated.Mark;
import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.generated.Mesh;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.Vector2D;
import org.glob3.mobile.generated.Vector3D;


class BuildingSurface {
   /**
    *
    */
   private final CityGMLBuilding  _cityGMLBuilding;
   public final ArrayList<Double> _coordinates;
   ArrayList<Geodetic3D>          _geodeticCoordinates  = null;
   double                         _baseHeightOfGeoCoors = 0;


   public double getBaseHeight() {
      double minHeight = 0;
      minHeight = Double.MAX_VALUE;
      for (int i = 0; i < _coordinates.size(); i += 3) {
         final double h = _coordinates.get(i + 2);
         if (h < minHeight) {
            minHeight = h;
         }
      }
      return minHeight;
   }


   public Vector3D getCounterClockWiseNormal(final Planet planet) {

      final ArrayList<Geodetic3D> co = getGeodetic3DCoordinates(0);
      final Vector3D a = planet.toCartesian(co.get(0));
      final Vector3D b = planet.toCartesian(co.get(1));
      final Vector3D c = planet.toCartesian(co.get(2));

      final Vector3D ab = a.sub(b);
      final Vector3D cb = c.sub(b);
      final Vector3D normal = ab.cross(cb);

      final ArrayList<Vector2D> c2D = createCoordinates2D(createCartesianCoordinates(planet, 0), normal);
      if (isClockwise(c2D)) {
         return normal.times(-1);
      }
      return normal;
   }


   public Geodetic3D getMin() {
      double minLat = Double.MAX_VALUE;
      double minLon = Double.MAX_VALUE;
      double minH = Double.MAX_VALUE;

      for (int i = 0; i < _coordinates.size(); i += 3) {
         final double lon = _coordinates.get(i);
         if (lon < minLon) {
            minLon = lon;
         }
         final double lat = _coordinates.get(i + 1);
         if (lat < minLat) {
            minLat = lat;
         }
         final double h = _coordinates.get(i + 2);
         if (h < minH) {
            minH = h;
         }
      }
      return Geodetic3D.fromDegrees(minLat, minLon, minH);
   }


   public Geodetic3D getMax() {
      double maxLat = Double.MIN_VALUE;
      double maxLon = Double.MIN_VALUE;
      double maxH = Double.MIN_VALUE;

      for (int i = 0; i < _coordinates.size(); i += 3) {
         final double lon = _coordinates.get(i);
         if (lon > maxLon) {
            maxLon = lon;
         }
         final double lat = _coordinates.get(i + 1);
         if (lat > maxLat) {
            maxLat = lat;
         }
         final double h = _coordinates.get(i + 2);
         if (h > maxH) {
            maxH = h;
         }
      }
      return Geodetic3D.fromDegrees(maxLat, maxLon, maxH);
   }


   public Geodetic3D getCenter() {
      final Geodetic3D min = getMin();
      final Geodetic3D max = getMax();

      return Geodetic3D.fromDegrees((min._latitude._degrees + max._latitude._degrees) / 2,
               (min._longitude._degrees + max._longitude._degrees) / 2, (min._height + max._height) / 2);
   }


   public final ArrayList<Geodetic3D> getGeodetic3DCoordinates(final double substractHeight) {
      if ((_geodeticCoordinates != null) && (substractHeight == _baseHeightOfGeoCoors)) {
         return _geodeticCoordinates;
      }

      _baseHeightOfGeoCoors = substractHeight;
      _geodeticCoordinates = new ArrayList<Geodetic3D>();

      for (int i = 0; i < _coordinates.size(); i += 3) {
         final double lat = _coordinates.get(i + 1);
         final double lon = _coordinates.get(i);
         final double h = _coordinates.get(i + 2);
         _geodeticCoordinates.add(Geodetic3D.fromDegrees(lat, lon, h - substractHeight));
      }
      return _geodeticCoordinates;
   }


   private Mesh createFlatMeshOf4Coor(final Planet planet,
                                      final ArrayList<Geodetic3D> c,
                                      final Color color) {
      final FloatBufferBuilderFromGeodetic fbb = FloatBufferBuilderFromGeodetic.builderWithFirstVertexAsCenter(planet);
      final FloatBufferBuilderFromCartesian3D normals = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
      final Vector3D normal = getCounterClockWiseNormal(planet);
      fbb.add(c.get(1));
      fbb.add(c.get(0));
      fbb.add(c.get(2));

      normals.add(normal);
      normals.add(normal);
      normals.add(normal);


      final DirectMesh verticesMesh = new DirectMesh(GLPrimitive.triangleStrip(), false, fbb.getCenter(), fbb.create(),
               (float) 1.0, (float) 2.0, color, null, (float) 1.0, true, normals.create());
      return verticesMesh;
   }


   private Mesh createFlatMeshOf5Coor(final Planet planet,
                                      final ArrayList<Geodetic3D> c,
                                      final Color color) {
      final FloatBufferBuilderFromGeodetic fbb = FloatBufferBuilderFromGeodetic.builderWithFirstVertexAsCenter(planet);
      final FloatBufferBuilderFromCartesian3D normals = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
      final Vector3D normal = getCounterClockWiseNormal(planet);
      fbb.add(c.get(1));
      fbb.add(c.get(0));
      fbb.add(c.get(2));
      fbb.add(c.get(3));

      normals.add(normal);
      normals.add(normal);
      normals.add(normal);
      normals.add(normal);


      final DirectMesh verticesMesh = new DirectMesh(GLPrimitive.triangleStrip(), false, fbb.getCenter(), fbb.create(),
               (float) 1.0, (float) 2.0, color, null, (float) 1.0, true, normals.create());
      return verticesMesh;
   }


   private Mesh createFlatMeshOfConvex(final Planet planet,
                                       final ArrayList<Geodetic3D> c,
                                       final Color color) {
      final FloatBufferBuilderFromGeodetic fbb = FloatBufferBuilderFromGeodetic.builderWithFirstVertexAsCenter(planet);
      final FloatBufferBuilderFromCartesian3D normals = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
      final Vector3D normal = getCounterClockWiseNormal(planet);
      for (int i = 0; i < c.size(); i++) {
         fbb.add(c.get(i));
         normals.add(normal);
      }

      final DirectMesh verticesMesh = new DirectMesh(GLPrimitive.triangleFan(), false, fbb.getCenter(), fbb.create(),
               (float) 1.0, (float) 2.0, color, null, (float) 1.0, true, normals.create());
      return verticesMesh;
   }


   private Mesh createPointMeshOfWall(final Planet planet,
                                      final ArrayList<Geodetic3D> c) {
      final FloatBufferBuilderFromGeodetic fbb = FloatBufferBuilderFromGeodetic.builderWithFirstVertexAsCenter(planet);
      for (int i = 0; i < c.size(); i++) {
         fbb.add(c.get(i));
      }

      final DirectMesh verticesMesh = new DirectMesh(GLPrimitive.points(), false, fbb.getCenter(), fbb.create(), (float) 1.0,
               (float) 2.0, new Color(Color.red()), null, (float) 1.0, false, null);
      return verticesMesh;
   }


   public void addTriangles(final FloatBufferBuilderFromGeodetic fbb,
                            final FloatBufferBuilderFromCartesian3D normals,
                            final double baseHeight,
                            final Planet planet) {

      final Vector3D normal = getCounterClockWiseNormal(planet);

      final ArrayList<Geodetic3D> c = getGeodetic3DCoordinates(baseHeight);

      for (int i = 0; i < (c.size() - 2); i++) {
         fbb.add(c.get(0));
         fbb.add(c.get(i + 1));
         fbb.add(c.get(i + 2));

         normals.add(normal);
         normals.add(normal);
         normals.add(normal);
      }

   }


   public BuildingSurface reversePolygon() {
      final ArrayList<Double> rcs = new ArrayList<Double>();
      for (int i = _coordinates.size() - 1; i > -1; i++) {
         rcs.add(_coordinates.get(i));
      }

      return new BuildingSurface(_cityGMLBuilding, rcs);
   }


   public ArrayList<Vector2D> createCoordinates2D(final ArrayList<Vector3D> c3D,
                                                  final Vector3D normal) {
      final Vector3D z = Vector3D.upZ();
      final Vector3D rotationAxis = z.cross(normal);

      Angle a = null;
      if (rotationAxis.isZero()) {
         a = Angle.fromDegrees(180);
      }
      else {
         a = normal.signedAngleBetween(rotationAxis, z);
      }

      final ArrayList<Vector2D> c2D = new ArrayList<Vector2D>();
      for (int i = 0; i < c3D.size(); i++) {
         Vector3D v3D = null;
         if (a.isNan() || a.isZero()) {
            v3D = c3D.get(i);
         }
         else {
            if ((a._degrees == 180) || (a._degrees == -180)) {
               v3D = c3D.get(i).rotateAroundAxis(Vector3D.upX(), a);
            }
            else {
               v3D = c3D.get(i).rotateAroundAxis(rotationAxis, a);
            }
         }
         c2D.add(new Vector2D(v3D._x, v3D._y));
      }
      return c2D;
   }


   static private Vector2D to2D(final Vector3D c3D,
                                final Vector3D normal) {


      final Vector3D z = Vector3D.upZ();
      final Vector3D rotationAxis = z.cross(normal);

      final Angle a = normal.signedAngleBetween(rotationAxis, z);

      final Vector3D v3D = c3D.rotateAroundAxis(rotationAxis, a);
      return new Vector2D(v3D._x, v3D._y);
   }


   static private boolean isInsideTriangle(final Vector2D p,
                                           final Vector2D cornerA,
                                           final Vector2D cornerB,
                                           final Vector2D cornerC) {

      final double alpha = (((cornerB._y - cornerC._y) * (p._x - cornerC._x)) + ((cornerC._x - cornerB._x) * (p._y - cornerC._y)))
                           / (((cornerB._y - cornerC._y) * (cornerA._x - cornerC._x)) + ((cornerC._x - cornerB._x) * (cornerA._y - cornerC._y)));
      final double beta = (((cornerC._y - cornerA._y) * (p._x - cornerC._x)) + ((cornerA._x - cornerC._x) * (p._y - cornerC._y)))
                          / (((cornerB._y - cornerC._y) * (cornerA._x - cornerC._x)) + ((cornerC._x - cornerB._x) * (cornerA._y - cornerC._y)));
      final double gamma = 1.0 - alpha - beta;

      if ((alpha > 0) && (beta > 0) && (gamma > 0)) {
         return true;
      }
      return false;
   }


   private static boolean isClockwise(final ArrayList<Vector2D> c2D) {
      double sum = 0;
      for (int j = 0; j < (c2D.size() - 1); j++) {
         final double slopeEdge = (c2D.get(j + 1)._x - c2D.get(j)._x) * (c2D.get(j + 1)._y - c2D.get(j)._y);
         sum += slopeEdge;
      }
      return sum > 0;
   }


   private ArrayList<Vector3D> extractEarAndAddTriangle(final ArrayList<Vector3D> cartesianC,
                                                        final FloatBufferBuilderFromCartesian3D fbb,
                                                        final FloatBufferBuilderFromCartesian3D normals,
                                                        final Vector3D normal,
            final Vector3D antinormal,
            final boolean isClockwise) {

      //As seen in http://www.geometrictools.com/Documentation/TriangulationByEarClipping.pdf

      int i1 = 0, i2 = 0, i3 = 0;
      double angleInDegrees = 0;
      boolean acceptableAngle = false;
      ILogger.instance().logInfo("Looking for ears");

      final ArrayList<Vector2D> c2D = createCoordinates2D(cartesianC, normal);

      final Vector3D axisForAngles = antinormal;
      //               if (isClockwise) {
      //                  axisForAngles = axisForAngles.times(-1);
      //               }

      double angleSum = 0;
      for (int i = 0; i < cartesianC.size(); i++) {
         i1 = i;
         i2 = (i + 1) % (cartesianC.size());
         i3 = (i + 2) % (cartesianC.size());

         final Vector3D v1 = cartesianC.get(i1);
         final Vector3D v2 = cartesianC.get(i2);
         final Vector3D v3 = cartesianC.get(i3);

         final Vector3D v21 = v1.sub(v2);
         final Vector3D v23 = v3.sub(v2);

         final double angle = v21.signedAngleBetween(v23, axisForAngles)._degrees;
         if (!Double.isNaN(angle)) {
            angleSum += angle;
         }

      }


      final boolean[] removed = new boolean[cartesianC.size()];
      for (int i = 0; i < (cartesianC.size() - 1); i++) {
         removed[i] = false;
      }

      while (true) {


         for (int i = 0; i < (cartesianC.size() - 1); i++) {

            int q = i;
            while (removed[q]) {
               q = (q + 1) % (cartesianC.size());
            }
            i1 = q;
            q = (q + 1) % (cartesianC.size());

            while (removed[q]) {
               q = (q + 1) % (cartesianC.size());
            }
            i2 = q;
            q = (q + 1) % (cartesianC.size());

            while (removed[q]) {
               q = (q + 1) % (cartesianC.size());
            }
            i3 = q;
            q = (q + 1) % (cartesianC.size());

            //         i1 = i;
            //         i2 = (i + 1) % (cartesianC.size());
            //         i3 = (i + 2) % (cartesianC.size());

            final Vector3D v1 = cartesianC.get(i1);
            final Vector3D v2 = cartesianC.get(i2);
            final Vector3D v3 = cartesianC.get(i3);

            final Vector3D v21 = v1.sub(v2);
            final Vector3D v23 = v3.sub(v2);

            angleInDegrees = v21.signedAngleBetween(v23, axisForAngles)._degrees;

            if (angleSum < 0) {
               angleInDegrees *= -1;
            }

            if (angleInDegrees < 0) {
               angleInDegrees += 360;
            }

            acceptableAngle = IMathUtils.instance().isBetween((float) angleInDegrees, (float) 0.0, (float) 180.0)
                     || Double.isNaN(angleInDegrees);

            if (acceptableAngle) {
               //Internal angle is concave
               //Checking inclusion of other vertices

               final Vector2D cornerA = c2D.get(i1);
               final Vector2D cornerB = c2D.get(i2);
               final Vector2D cornerC = c2D.get(i3);

               for (int j = 0; j < cartesianC.size(); j++) {
                  if ((j != i1) && (j != i2) && (j != i3)) {
                     final Vector2D p = c2D.get(j);
                     if (isInsideTriangle(p, cornerA, cornerB, cornerC)) {
                        acceptableAngle = false;
                        break;
                     }
                  }
               }

               if (acceptableAngle) {
                  break;
               }

            }
         }
         //ILogger.instance().logInfo("!!!! Angle %f", angleInDegrees);


         if (acceptableAngle) { //Valid triangle (ear)
            fbb.add(cartesianC.get(i1));
            fbb.add(cartesianC.get(i2));
            fbb.add(cartesianC.get(i3));
            normals.add(normal);
            normals.add(normal);
            normals.add(normal);

            ILogger.instance().logInfo("T: %d, %d, %d -> Angle %f", i1, i2, i3, angleInDegrees);
         }
         else {
            ILogger.instance().logInfo("NO EAR!!!!");
         }


         //Removing ear
         removed[i2] = true;

         int notRemoved = 0;
         for (int i = 0; i < (cartesianC.size() - 1); i++) {
            if (!removed[i]) {
               notRemoved++;
            }
         }

         if (notRemoved < 3) {
            break;
         }

         //      final ArrayList<Vector3D> newCartesianC = new ArrayList<Vector3D>();
         //      for (int i = 0; i < cartesianC.size(); i++) {
         //         if (i != i2) {
         //            newCartesianC.add(cartesianC.get(i));
         //         }
         //      }

      }


      return null;

   }


   public ArrayList<Vector3D> createCartesianCoordinates(final Planet planet,
                                                         final double baseHeight) {
      final ArrayList<Geodetic3D> c = getGeodetic3DCoordinates(baseHeight);
      final ArrayList<Vector3D> cartesianC = new ArrayList<Vector3D>();

      for (int i = 0; i < c.size(); i++) {
         cartesianC.add(planet.toCartesian(c.get(i)));
      }
      return cartesianC;
   }


   public boolean addTrianglesCuttingEars(final FloatBufferBuilderFromCartesian3D fbb,
                                          final FloatBufferBuilderFromCartesian3D normals,
                                          final double baseHeight,
                                          final Planet planet,
                                          final Vector3D normal) {

      final Vector3D antinormal = normal.times(-1);

      ArrayList<Vector3D> cartesianC = createCartesianCoordinates(planet, baseHeight);
      final ArrayList<Vector2D> c2D = createCoordinates2D(cartesianC, normal);
      final boolean isClockwise = isClockwise(c2D);

      int nTriangles = 0;

      //      while (cartesianC.size() > 3) {
      cartesianC = extractEarAndAddTriangle(cartesianC, fbb, normals, normal, antinormal, isClockwise);
      nTriangles++;
      //      }
      return true;
   }


   public Mesh createMesh(final Planet planet,
                          final Color color,
                          final double baseHeight) {

      final ArrayList<Geodetic3D> c = getGeodetic3DCoordinates(baseHeight);
      switch (c.size()) {
         case 5:
            return createFlatMeshOf5Coor(planet, c, color);
         case 4:
            return createFlatMeshOf4Coor(planet, c, color);
         default:
            ILogger.instance().logInfo("Problem with building surface");
            return createFlatMeshOfConvex(planet, c, color); //Assuming all surfaces are convex (WRONG!)
            // return createPointMeshOfWall(planet, c);
      }
   }


   public BuildingSurface(final CityGMLBuilding cityGMLBuilding,
                          final ArrayList<Double> coordinates) {
      _cityGMLBuilding = cityGMLBuilding;
      _coordinates = coordinates;
   }


   public void addMarkersToCorners(final MarksRenderer mr,
                                   final double substractHeight) {

      final ArrayList<Geodetic3D> c = getGeodetic3DCoordinates(substractHeight);

      for (int i = 0; i < (c.size() - 1); i++) {
         final Geodetic3D pos = Geodetic3D.fromDegrees(c.get(i)._latitude._degrees, c.get(i)._longitude._degrees,
                  c.get(i)._height);
         final Mark m = new Mark("" + i, pos, AltitudeMode.ABSOLUTE, 10000.0);
         mr.addMark(m);
      }

   }
}
