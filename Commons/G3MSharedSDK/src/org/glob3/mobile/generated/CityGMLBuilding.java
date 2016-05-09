

package org.glob3.mobile.generated;

public class CityGMLBuilding {

   private Mesh                                                      _containerMesh;
   private short                                                     _firstVertexIndexWithinContainerMesh;
   private short                                                     _lastVertexIndexWithinContainerMesh;

   private final java.util.ArrayList<CityGMLBuildingNumericProperty> _numericProperties = new java.util.ArrayList<CityGMLBuildingNumericProperty>();

   private final CityGMLBuildingTessellatorData                      _tessellatorData;


   public final String                                               _name;
   public final int                                                  _roofTypeCode;

   public final java.util.ArrayList<CityGMLBuildingSurface>          _surfaces;


   public CityGMLBuilding(final String name,
                          final int roofType,
                          final java.util.ArrayList<CityGMLBuildingSurface> walls) {
      _name = name;
      _roofTypeCode = roofType;
      _surfaces = walls;
      _tessellatorData = null;
   }


   public void dispose() {
      if (_tessellatorData != null) {
         _tessellatorData.dispose();
      }

      removeSurfaceData();

      for (int i = 0; i < _numericProperties.size(); i++) {
         if (_numericProperties.get(i) != null) {
            _numericProperties.get(i).dispose();
         }
      }
   }


   public final void removeSurfaceData() {
      for (int i = 0; i < _surfaces.size(); i++) {
         CityGMLBuildingSurface s = _surfaces.get(i);
         s = null;
      }

      _surfaces.clear();
   }


   public final java.util.ArrayList<CityGMLBuildingSurface> getSurfaces() {
      return _surfaces;
   }


   public final void addNumericProperty(final CityGMLBuildingNumericProperty value) {
      _numericProperties.add(value);
   }


   public final double getNumericProperty(final String name) {

      for (int i = 0; i < _numericProperties.size(); i++) {
         if (_numericProperties.get(i)._name.equals(name)) {
            return _numericProperties.get(i)._value;
         }
      }
      return java.lang.Double.NaN;
   }


   public final String getPropertiesDescription() {

      String d = "";
      for (int i = 0; i < _numericProperties.size(); i++) {
         d += _numericProperties.get(i)._name + ": " + IStringUtils.instance().toString(_numericProperties.get(i)._value);
         if (i < (_numericProperties.size() - 1)) {
            d += "\n";
         }
      }
      return d;
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


   public final void addMarkersToCorners(final MarksRenderer mr,
                                         final boolean fixOnGround) {

      final double deltaH = fixOnGround ? getBaseHeight() : 0;

      for (int i = 0; i < _surfaces.size(); i++) {
         final CityGMLBuildingSurface s = _surfaces.get(i);
         s.addMarkersToCorners(mr, deltaH);
      }
   }


   public final boolean markGroundAsNotVisible() {
      for (int i = 0; i < _surfaces.size(); i++) {
         final CityGMLBuildingSurface s1 = _surfaces.get(i);
         //Grounds are internal by definition
         if (s1.getType() == CityGMLBuildingSurfaceType.GROUND) {
            s1.setIsVisible(false);
            return true;
         }
      }
      return false;
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
}
