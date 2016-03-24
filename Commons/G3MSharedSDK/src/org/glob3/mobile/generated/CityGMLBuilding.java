

package org.glob3.mobile.generated;

//
//  CityGMLBuilding.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 24/3/16.
//
//

//
//  CityGMLBuilding.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 24/3/16.
//
//


public class CityGMLBuilding {

   private final String                                _name;
   private int                                         _roofTypeCode;
   private java.util.ArrayList<CityGMLBuildingSurface> _walls = new java.util.ArrayList<CityGMLBuildingSurface>();


   public CityGMLBuilding(final String name,
                          final int roofType,
                          final java.util.ArrayList<CityGMLBuildingSurface> walls) {
      _name = name;
      _roofTypeCode = roofType;
      _walls = walls;
   }


   private void setRoofTypeCode(final int i) {
      _roofTypeCode = i;
   }


   private double getBaseHeight() {
      double min = IMathUtils.instance().maxDouble();
      for (int i = 0; i < _walls.size(); i++) {
         final double h = _walls.get(i).getBaseHeight();
         if (min > h) {
            min = h;
         }
      }
      return min;
   }


   private String description() {

      final IStringBuilder isb = IStringBuilder.newStringBuilder();
      isb.addString("Building Name: " + _name + "\nRoof Type: ");
      isb.addInt(_roofTypeCode);
      for (int i = 0; i < _walls.size(); i++) {
         isb.addString("\n Wall: Coordinates: ");
         for (int j = 0; j < _walls.get(i)._geodeticCoordinates.size(); j += 3) {
            isb.addString(_walls.get(i)._geodeticCoordinates.get(j).description());
         }
      }
      final String s = isb.getString();
      if (isb != null) {
         isb.dispose();
      }
      return s;
   }


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
         buildingFirstIndex = _walls.get(w).addTrianglesByEarClipping(fbb, normals, indexes, colors, baseHeight, planet,
                  buildingFirstIndex, color);
      }
      return buildingFirstIndex;
   }


   private Mesh createIndexedMeshWithColorPerVertex(final Planet planet,
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

      if (fbb != null) {
         fbb.dispose();
      }
      if (normals != null) {
         normals.dispose();
      }

      return im;
   }


   public static Mesh createSingleIndexedMeshWithColorPerVertexForBuildings(final java.util.ArrayList<CityGMLBuilding> buildings,
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

         if (firstIndex > 30000) //Max number of vertex per mesh (CHECK IT)
         {
            if (cm == null) {
               cm = new CompositeMesh();
            }

            //Adding new mesh
            final IndexedMesh im = new IndexedMesh(GLPrimitive.triangles(), fbb.getCenter(), fbb.create(), true,
                     indexes.create(), true, 1.0f, 1.0f, null, colors.create(), 1.0f, true, normals.create());

            cm.addMesh(im);
            meshesCounter++;

            //Reset
            if (fbb != null) {
               fbb.dispose();
            }
            if (normals != null) {
               normals.dispose();
            }
            colors = null;
            fbb = FloatBufferBuilderFromCartesian3D.builderWithFirstVertexAsCenter();
            normals = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
            indexes = new ShortBufferBuilder();
            colors = new FloatBufferBuilderFromColor();
            firstIndex = 0;
         }
      }

      //Adding last mesh
      final IndexedMesh im = new IndexedMesh(GLPrimitive.triangles(), fbb.getCenter(), fbb.create(), true, indexes.create(),
               true, 1.0f, 1.0f, null, colors.create(), 1.0f, true, normals.create());
      if (cm == null) {
         ILogger.instance().logInfo("One single mesh created for %d buildings", buildingCounter);
         return im;
      }

      cm.addMesh(im);
      meshesCounter++;

      ILogger.instance().logInfo("%d meshes created for %d buildings", meshesCounter, buildingCounter);
      return cm;
   }


   private Geodetic3D getMin() {
      double minLat = IMathUtils.instance().maxDouble();
      double minLon = IMathUtils.instance().maxDouble();
      double minH = IMathUtils.instance().maxDouble();

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


   private Geodetic3D getMax() {
      double maxLat = IMathUtils.instance().minDouble();
      double maxLon = IMathUtils.instance().minDouble();
      double maxH = IMathUtils.instance().minDouble();

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


   private Geodetic3D getCenter() {
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


   private void addMarkersToCorners(final MarksRenderer mr,
                                    final boolean fixOnGround) {

      final double deltaH = fixOnGround ? getBaseHeight() : 0;

      for (int i = 0; i < _walls.size(); i++) {
         _walls.get(i).addMarkersToCorners(mr, deltaH);
      }
   }
}
