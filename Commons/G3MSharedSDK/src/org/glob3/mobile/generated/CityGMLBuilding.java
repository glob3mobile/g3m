

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


   public final String                                      _name;
   public final int                                         _roofTypeCode;

   public final java.util.ArrayList<CityGMLBuildingSurface> _surfaces;


   public CityGMLBuilding(final String name,
                          final int roofType,
                          final java.util.ArrayList<CityGMLBuildingSurface> walls) {
      _name = name;
      _roofTypeCode = roofType;
      _surfaces = walls;
   }


   public void dispose() {
      for (int i = 0; i < _surfaces.size(); i++) {
         CityGMLBuildingSurface s = _surfaces.get(i);
         s = null;
      }
   }


   public final double getBaseHeight() {
      double min = IMathUtils.instance().maxDouble();
      for (int i = 0; i < _surfaces.size(); i++) {
         final CityGMLBuildingSurface s = _surfaces.get(i);
         final double h = s.getBaseHeight();
         if (min > h) {
            min = h;
         }
      }
      return min;
   }


   public final String description() {

      final IStringBuilder isb = IStringBuilder.newStringBuilder();
      isb.addString("Building Name: " + _name + "\nRoof Type: ");
      isb.addInt(_roofTypeCode);
      for (int i = 0; i < _surfaces.size(); i++) {
         isb.addString("\n Wall: Coordinates: ");
         final CityGMLBuildingSurface s = _surfaces.get(i);
         for (int j = 0; j < s._geodeticCoordinates.size(); j += 3) {
            final Geodetic3D g = s._geodeticCoordinates.get(j);
            isb.addString(g.description());
         }
      }
      final String s = isb.getString();
      if (isb != null) {
         isb.dispose();
      }
      return s;
   }


   public final short addTrianglesCuttingEarsForAllWalls(final FloatBufferBuilderFromCartesian3D fbb,
                                                         final FloatBufferBuilderFromCartesian3D normals,
                                                         final ShortBufferBuilder indexes,
                                                         final FloatBufferBuilderFromColor colors,
                                                         final double baseHeight,
                                                         final Planet planet,
                                                         final short firstIndex,
                                                         final Color color,
                                                         final boolean includeGround) {
      short buildingFirstIndex = firstIndex;
      for (int w = 0; w < _surfaces.size(); w++) {
         final CityGMLBuildingSurface s = _surfaces.get(w);

         if ((!includeGround && (s.getType() == CityGMLBuildingSurfaceType.GROUND)) || !s.isVisible()) {
            continue;
         }

         buildingFirstIndex = s.addTrianglesByEarClipping(fbb, normals, indexes, colors, baseHeight, planet, buildingFirstIndex,
                  color);
      }
      return buildingFirstIndex;
   }


   public final Mesh createIndexedMeshWithColorPerVertex(final Planet planet,
                                                         final boolean fixOnGround,
                                                         final Color color,
                                                         final boolean includeGround) {


      final double baseHeight = fixOnGround ? getBaseHeight() : 0;

      final FloatBufferBuilderFromCartesian3D fbb = FloatBufferBuilderFromCartesian3D.builderWithFirstVertexAsCenter();
      final FloatBufferBuilderFromCartesian3D normals = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
      final ShortBufferBuilder indexes = new ShortBufferBuilder();
      final FloatBufferBuilderFromColor colors = new FloatBufferBuilderFromColor();

      final short firstIndex = 0;
      addTrianglesCuttingEarsForAllWalls(fbb, normals, indexes, colors, baseHeight, planet, firstIndex, color, includeGround);

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
                                                                            final boolean fixOnGround,
                                                                            final boolean includeGround) {

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
                  colorWheel.wheelStep(buildings.size(), buildingCounter), includeGround);

         buildingCounter++;

         if (firstIndex > 30000) //Max number of vertex per mesh (CHECK SHORT RANGE)
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

      if (fbb != null) {
         fbb.dispose();
      }
      if (normals != null) {
         normals.dispose();
      }
      colors = null;

      if (cm == null) {
         ILogger.instance().logInfo("One single mesh created for %d buildings", buildingCounter);
         return im;
      }

      cm.addMesh(im);
      meshesCounter++;

      ILogger.instance().logInfo("%d meshes created for %d buildings", meshesCounter, buildingCounter);
      return cm;
   }


   public final Geodetic3D getMin() {
      double minLat = IMathUtils.instance().maxDouble();
      double minLon = IMathUtils.instance().maxDouble();
      double minH = IMathUtils.instance().maxDouble();

      for (int i = 0; i < _surfaces.size(); i++) {
         final CityGMLBuildingSurface s = _surfaces.get(i);
         final Geodetic3D min = s.getMin();
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


   public final Geodetic3D getMax() {
      double maxLat = IMathUtils.instance().minDouble();
      double maxLon = IMathUtils.instance().minDouble();
      double maxH = IMathUtils.instance().minDouble();

      for (int i = 0; i < _surfaces.size(); i++) {
         final CityGMLBuildingSurface s = _surfaces.get(i);
         final Geodetic3D min = s.getMax();
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


   public final Geodetic3D getCenter() {
      final Geodetic3D min = getMin();
      final Geodetic3D max = getMax();

      return Geodetic3D.fromDegrees((min._latitude._degrees + max._latitude._degrees) / 2,
               (min._longitude._degrees + max._longitude._degrees) / 2, (min._height + max._height) / 2);
   }


   public final Mark createMark(final boolean fixOnGround) {
      final double deltaH = fixOnGround ? getBaseHeight() : 0;

      final Geodetic3D center = getCenter();
      final Geodetic3D pos = Geodetic3D.fromDegrees(center._latitude._degrees, center._longitude._degrees, center._height
                                                                                                           - deltaH);

      final Mark m = new Mark(_name, pos, AltitudeMode.ABSOLUTE, 100.0);
      return m;
   }


   public final void addMarkersToCorners(final MarksRenderer mr,
                                         final boolean fixOnGround) {

      final double deltaH = fixOnGround ? getBaseHeight() : 0;

      for (int i = 0; i < _surfaces.size(); i++) {
         final CityGMLBuildingSurface s = _surfaces.get(i);
         s.addMarkersToCorners(mr, deltaH);
      }
   }


   public static int checkWallsVisibility(final CityGMLBuilding b1,
                                          final CityGMLBuilding b2) {
      int nInvisibleWalls = 0;
      for (int i = 0; i < b1._surfaces.size(); i++) {
         final CityGMLBuildingSurface s1 = b1._surfaces.get(i);
         if (s1.getType() == CityGMLBuildingSurfaceType.WALL) {
            for (int j = 0; j < b2._surfaces.size(); j++) {
               final CityGMLBuildingSurface s2 = b2._surfaces.get(j);
               if (s2.getType() == CityGMLBuildingSurfaceType.WALL) {
                  if (s1.isEquivalentTo(s2)) {
                     s1.setIsVisible(false);
                     s2.setIsVisible(false);
                     nInvisibleWalls++;
                     //ILogger::instance()->logInfo("Two building surfaces are equivalent, so we mark them as invisible.");
                  }
               }
            }
         }
      }
      return nInvisibleWalls;
   }


   //  static int checkWallsVisibilityBetweenConsecutiveBuildings(const std::vector<CityGMLBuilding*> buildings){
   //    int nInvisibleWalls = 0;
   //    for (int i = 0; i < buildings.size() - 1; i++){
   //      CityGMLBuilding* b1 = buildings[i];
   //      CityGMLBuilding* b2 = buildings[i+1];
   //      nInvisibleWalls += checkWallsVisibility(b1, b2);
   //    }
   //    return nInvisibleWalls;
   //  }

   public static int checkWallsVisibility(final java.util.ArrayList<CityGMLBuilding> buildings) {
      int nInvisibleWalls = 0;
      for (int i = 0; i < (buildings.size() - 1); i++) {
         final CityGMLBuilding b1 = buildings.get(i);

         for (int j = i + 1; (j < (i + 30)) && (j < (buildings.size() - 1)); j++) {
            final CityGMLBuilding b2 = buildings.get(j);
            nInvisibleWalls += checkWallsVisibility(b1, b2);
         }

      }
      return nInvisibleWalls;
   }
}
