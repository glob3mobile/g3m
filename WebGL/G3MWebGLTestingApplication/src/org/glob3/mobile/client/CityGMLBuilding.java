

package org.glob3.mobile.client;

import java.util.ArrayList;

import org.glob3.mobile.generated.AltitudeMode;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.CompositeMesh;
import org.glob3.mobile.generated.DirectMesh;
import org.glob3.mobile.generated.FloatBufferBuilderFromCartesian3D;
import org.glob3.mobile.generated.FloatBufferBuilderFromGeodetic;
import org.glob3.mobile.generated.GLPrimitive;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.Mark;
import org.glob3.mobile.generated.Mesh;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.Vector3D;


public class CityGMLBuilding {

   private class BuildingSurface {
      public final ArrayList<Double> _coordinates;
      ArrayList<Geodetic3D>          _geodeticCoordinates = null;


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


      public Vector3D getNormal(final Planet planet) {

         final ArrayList<Geodetic3D> co = getGeodetic3DCoordinates(0);
         final Vector3D a = planet.toCartesian(co.get(0));
         final Vector3D b = planet.toCartesian(co.get(1));
         final Vector3D c = planet.toCartesian(co.get(2));

         final Vector3D ab = a.sub(b);
         final Vector3D ac = a.sub(c);
         return ab.cross(ac);
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
         if (_geodeticCoordinates != null) {
            return _geodeticCoordinates;
         }

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
         final Vector3D normal = getNormal(planet);
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
         final Vector3D normal = getNormal(planet);
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
         final Vector3D normal = getNormal(planet);
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


      public BuildingSurface(final ArrayList<Double> coordinates) {
         _coordinates = coordinates;
      }
   }

   public String                     _name;
   public int                        _roofTypeCode;
   public ArrayList<BuildingSurface> _walls = new ArrayList<CityGMLBuilding.BuildingSurface>();


   public CityGMLBuilding(final String name) {
      _name = name;
   }


   public void addSurfaceWithPosLis(final ArrayList<Double> coordinates) {
      _walls.add(new BuildingSurface(coordinates));
   }


   public void setRoofTypeCode(final int i) {
      _roofTypeCode = i;
   }


   double getBaseHeight() {
      double min = Double.MAX_VALUE;
      for (int i = 0; i < _walls.size(); i++) {
         final double h = _walls.get(i).getBaseHeight();
         if (min > h) {
            min = h;
         }
      }
      return min;
   }


   public String description() {
      String s = "Building Name: " + _name;
      s += "\nRoof Type: " + _roofTypeCode;
      for (int i = 0; i < _walls.size(); i++) {
         s += "\n Wall: Coordinates: ";
         for (int j = 0; j < _walls.get(i)._coordinates.size(); j += 3) {
            s += "(" + _walls.get(i)._coordinates.get(j) + ", " + _walls.get(i)._coordinates.get(j + 1) + ", "
                 + _walls.get(i)._coordinates.get(j + 2) + ") ";
         }
      }
      return s;
   }


   public Mesh createMesh(final Planet planet,
                          final boolean fixOnGround,
                          final Color color) {

      final double baseHeight = fixOnGround ? getBaseHeight() : 0;

      final CompositeMesh cm = new CompositeMesh();
      for (int w = 0; w < _walls.size(); w++) {
         final Color colorW = color == null ? Color.red().wheelStep(_walls.size(), w) : color;

         cm.addMesh(_walls.get(w).createMesh(planet, colorW, baseHeight));
      }


      return cm;

   }


   public Geodetic3D getMin() {
      double minLat = Double.MAX_VALUE;
      double minLon = Double.MAX_VALUE;
      double minH = Double.MAX_VALUE;

      for (int i = 0; i < _walls.size(); i++) {
         final Geodetic3D min = _walls.get(i).getMin();
         if (min._longitude._degrees < minLon) {
            minLon = min._longitude._degrees;
         }
         if (min._latitude._degrees < minLat) {
            minLat = min._latitude._degrees;
         }
         if (min._height < minH) {
            minH = min._height;
         }
      }
      return Geodetic3D.fromDegrees(minLat, minLon, minH);
   }


   public Geodetic3D getMax() {
      double maxLat = Double.MIN_VALUE;
      double maxLon = Double.MIN_VALUE;
      double maxH = Double.MIN_VALUE;

      for (int i = 0; i < _walls.size(); i++) {
         final Geodetic3D min = _walls.get(i).getMax();
         if (min._longitude._degrees > maxLon) {
            maxLon = min._longitude._degrees;
         }
         if (min._latitude._degrees > maxLat) {
            maxLat = min._latitude._degrees;
         }
         if (min._height > maxH) {
            maxH = min._height;
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


   public Mark createMark(final boolean fixOnGround) {
      final double deltaH = fixOnGround ? getBaseHeight() : 0;

      final Geodetic3D center = getCenter();
      final Geodetic3D pos = Geodetic3D.fromDegrees(center._latitude._degrees, center._longitude._degrees, center._height
                                                                                                           - deltaH);

      final Mark m = new Mark(_name, pos, AltitudeMode.ABSOLUTE, 100.0);
      return m;
   }
}
