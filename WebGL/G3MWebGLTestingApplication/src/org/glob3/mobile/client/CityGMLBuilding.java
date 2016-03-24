

package org.glob3.mobile.client;

import java.util.ArrayList;

import org.glob3.mobile.generated.AltitudeMode;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.CompositeMesh;
import org.glob3.mobile.generated.FloatBufferBuilderFromCartesian3D;
import org.glob3.mobile.generated.FloatBufferBuilderFromColor;
import org.glob3.mobile.generated.GLPrimitive;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IndexedMesh;
import org.glob3.mobile.generated.Mark;
import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.generated.Mesh;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.ShortBufferBuilder;


public class CityGMLBuilding {

   public String                     _name;
   public int                        _roofTypeCode;
   public ArrayList<BuildingSurface> _walls = new ArrayList<BuildingSurface>();


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


   public Mesh createMeshesForSurfaces(final Planet planet,
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


   //   public Mesh createIndexedMesh(final Planet planet,
   //                                 final boolean fixOnGround,
   //                                 final Color color) {
   //
   //
   //      final double baseHeight = fixOnGround ? getBaseHeight() : 0;
   //
   //      final FloatBufferBuilderFromCartesian3D fbb = FloatBufferBuilderFromCartesian3D.builderWithFirstVertexAsCenter();
   //      final FloatBufferBuilderFromCartesian3D normals = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
   //      final ShortBufferBuilder indexes = new ShortBufferBuilder();
   //
   //      short firstIndex = 0;
   //      for (int w = 0; w < _walls.size(); w++) {
   //         firstIndex = _walls.get(w).addTrianglesCuttingEars(fbb, normals, indexes, baseHeight, planet, firstIndex);
   //      }
   //
   //      final IndexedMesh im = new IndexedMesh(GLPrimitive.triangles(), fbb.getCenter(), fbb.create(), true, indexes.create(),
   //               true, 1.0f, 1.0f, color, null, 1.0f, true, normals.create());
   //
   //      return im;
   //
   //   }

   private short addTrianglesCuttingEarsForAllWalls(final FloatBufferBuilderFromCartesian3D fbb,
                                                    final FloatBufferBuilderFromCartesian3D normals,
                                                    final ShortBufferBuilder indexes,
                                                    final FloatBufferBuilderFromColor colors,
                                                    final double baseHeight,
                                                    final Planet planet,
                                                    final short firstIndex,
                                                    final Color color) {
      short buildingFirstIndex = firstIndex;
      for (int w = 0; w < _walls.size(); w++) {
         buildingFirstIndex = _walls.get(w).addTrianglesCuttingEars(fbb, normals, indexes, colors, baseHeight, planet,
                  buildingFirstIndex, color);
      }
      return buildingFirstIndex;
   }


   public Mesh createIndexedMeshWithColorPerVertex(final Planet planet,
                                                   final boolean fixOnGround,
                                                   final Color color) {


      final double baseHeight = fixOnGround ? getBaseHeight() : 0;

      final FloatBufferBuilderFromCartesian3D fbb = FloatBufferBuilderFromCartesian3D.builderWithFirstVertexAsCenter();
      final FloatBufferBuilderFromCartesian3D normals = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
      final ShortBufferBuilder indexes = new ShortBufferBuilder();
      final FloatBufferBuilderFromColor colors = new FloatBufferBuilderFromColor();

      final short firstIndex = 0;
      addTrianglesCuttingEarsForAllWalls(fbb, normals, indexes, colors, baseHeight, planet, firstIndex, color);

      final IndexedMesh im = new IndexedMesh(GLPrimitive.triangles(), fbb.getCenter(), fbb.create(), true, indexes.create(),
               true, 1.0f, 1.0f, null, colors.create(), 1.0f, true, normals.create());

      return im;
   }


   public static Mesh createSingleIndexedMeshWithColorPerVertexForBuildings(final ArrayList<CityGMLBuilding> buildings,
                                                                            final Planet planet,
                                                                            final boolean fixOnGround) {

      CompositeMesh cm = null;
      int buildingCounter = 0;
      int meshesCounter = 0;


      FloatBufferBuilderFromCartesian3D fbb = FloatBufferBuilderFromCartesian3D.builderWithFirstVertexAsCenter();
      FloatBufferBuilderFromCartesian3D normals = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
      ShortBufferBuilder indexes = new ShortBufferBuilder();
      FloatBufferBuilderFromColor colors = new FloatBufferBuilderFromColor();

      final Color colorWheel = Color.red();

      short firstIndex = 0;
      for (int i = 0; i < buildings.size(); i++) {
         final CityGMLBuilding b = buildings.get(i);

         final double baseHeight = fixOnGround ? b.getBaseHeight() : 0;
         firstIndex = b.addTrianglesCuttingEarsForAllWalls(fbb, normals, indexes, colors, baseHeight, planet, firstIndex,
                  colorWheel.wheelStep(buildings.size(), buildingCounter));

         buildingCounter++;

         if (firstIndex > 30000) { //Max number of vertex per mesh (CHECK IT)
            if (cm == null) {
               cm = new CompositeMesh();
            }

            //Adding new mesh
            final IndexedMesh im = new IndexedMesh(GLPrimitive.triangles(), fbb.getCenter(), fbb.create(), true,
                     indexes.create(), true, 1.0f, 1.0f, null, colors.create(), 1.0f, true, normals.create());
            cm.addMesh(im);
            meshesCounter++;

            //Reset
            fbb = FloatBufferBuilderFromCartesian3D.builderWithFirstVertexAsCenter();
            normals = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
            indexes = new ShortBufferBuilder();
            colors = new FloatBufferBuilderFromColor();
            firstIndex = 0;
         }
      }

      if (cm == null) {

         final IndexedMesh im = new IndexedMesh(GLPrimitive.triangles(), fbb.getCenter(), fbb.create(), true, indexes.create(),
                  true, 1.0f, 1.0f, null, colors.create(), 1.0f, true, normals.create());

         ILogger.instance().logInfo("One single mesh created for %d buildings", buildingCounter);
         return im;
      }

      //Adding last mesh
      final IndexedMesh im = new IndexedMesh(GLPrimitive.triangles(), fbb.getCenter(), fbb.create(), true, indexes.create(),
               true, 1.0f, 1.0f, null, colors.create(), 1.0f, true, normals.create());
      cm.addMesh(im);
      meshesCounter++;

      ILogger.instance().logInfo("%d meshes created for %d buildings", meshesCounter, buildingCounter);
      return cm;
   }


   //   public Mesh createSingleTrianglesMesh(final Planet planet,
   //                                         final boolean fixOnGround,
   //                                         final Color color) {
   //
   //
   //      final double baseHeight = fixOnGround ? getBaseHeight() : 0;
   //
   //      final FloatBufferBuilderFromCartesian3D fbb = FloatBufferBuilderFromCartesian3D.builderWithFirstVertexAsCenter();
   //      final FloatBufferBuilderFromCartesian3D normals = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
   //
   //      for (int w = 0; w < _walls.size(); w++) {
   //         final boolean x = _walls.get(w).addTrianglesCuttingEars(fbb, normals, baseHeight, planet);
   //      }
   //
   //      final DirectMesh trianglesMesh = new DirectMesh(GLPrimitive.triangles(), false, fbb.getCenter(), fbb.create(), (float) 1.0,
   //               (float) 2.0, color, null, (float) 1.0, true, normals.create());
   //
   //      return trianglesMesh;
   //
   //   }


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


   public void addMarkersToCorners(final MarksRenderer mr,
                                   final boolean fixOnGround) {

      final double deltaH = fixOnGround ? getBaseHeight() : 0;

      for (int i = 0; i < _walls.size(); i++) {
         _walls.get(i).addMarkersToCorners(mr, deltaH);
      }
   }
}
