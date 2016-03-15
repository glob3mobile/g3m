

package org.glob3.mobile.client;

import java.util.ArrayList;

import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.CompositeMesh;
import org.glob3.mobile.generated.DirectMesh;
import org.glob3.mobile.generated.FloatBufferBuilderFromGeodetic;
import org.glob3.mobile.generated.GLPrimitive;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.Mesh;
import org.glob3.mobile.generated.Planet;


public class CityGMLBuilding {

   private class BuildingSurface {
      public final ArrayList<Double> _coordinates;


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


      public final ArrayList<Geodetic3D> createGeodetic3DCoordinates(final double substractHeight) {
         final ArrayList<Geodetic3D> array = new ArrayList<Geodetic3D>();

         for (int i = 0; i < _coordinates.size(); i += 3) {
            final double lat = _coordinates.get(i + 1);
            final double lon = _coordinates.get(i);
            final double h = _coordinates.get(i + 2);
            array.add(Geodetic3D.fromDegrees(lat, lon, h - substractHeight));
         }
         return array;
      }


      private Mesh createFlatMeshOf4Coor(final Planet planet,
                                         final ArrayList<Geodetic3D> c,
                                         final Color color) {
         final FloatBufferBuilderFromGeodetic fbb = FloatBufferBuilderFromGeodetic.builderWithFirstVertexAsCenter(planet);
         fbb.add(c.get(1));
         fbb.add(c.get(0));
         fbb.add(c.get(2));


         final DirectMesh verticesMesh = new DirectMesh(GLPrimitive.triangleStrip(), false, fbb.getCenter(), fbb.create(),
                  (float) 1.0, (float) 2.0, color, null, (float) 1.0, true, null);
         return verticesMesh;
      }


      private Mesh createFlatMeshOf5Coor(final Planet planet,
                                         final ArrayList<Geodetic3D> c,
                                         final Color color) {
         final FloatBufferBuilderFromGeodetic fbb = FloatBufferBuilderFromGeodetic.builderWithFirstVertexAsCenter(planet);
         fbb.add(c.get(1));
         fbb.add(c.get(0));
         fbb.add(c.get(2));
         fbb.add(c.get(3));


         final DirectMesh verticesMesh = new DirectMesh(GLPrimitive.triangleStrip(), false, fbb.getCenter(), fbb.create(),
                  (float) 1.0, (float) 2.0, color, null, (float) 1.0, true, null);
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

         final ArrayList<Geodetic3D> c = createGeodetic3DCoordinates(baseHeight);
         switch (c.size()) {
            case 5:
               return createFlatMeshOf5Coor(planet, c, color);
            case 4:
               return createFlatMeshOf4Coor(planet, c, color);
            default:
               ILogger.instance().logInfo("Problem with building surface");
               return createPointMeshOfWall(planet, c);
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
                          final boolean fixOnGround) {

      final double baseHeight = fixOnGround ? getBaseHeight() : 0;


      final CompositeMesh cm = new CompositeMesh();
      for (int w = 0; w < _walls.size(); w++) {
         final Color color = Color.red().wheelStep(_walls.size(), w);

         cm.addMesh(_walls.get(w).createMesh(planet, color, baseHeight));
      }


      return cm;

   }
}
