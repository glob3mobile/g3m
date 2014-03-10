

package com.glob3mobile.tools.extruder;

import java.util.ArrayList;
import java.util.List;

import org.glob3.mobile.generated.GEOFeature;
import org.glob3.mobile.generated.Geodetic2D;

import com.glob3mobile.tools.mesh.G3MeshMaterial;

import es.igosoftware.euclid.shape.GComplexPolygon2D;
import es.igosoftware.euclid.shape.IPolygon2D;
import es.igosoftware.euclid.shape.ISimplePolygon2D;
import es.igosoftware.euclid.utils.GShapeUtils;
import es.igosoftware.euclid.vector.GVector2D;
import es.igosoftware.euclid.vector.IVector2;
import es.igosoftware.util.GCollections;
import es.igosoftware.util.IFunction;


class ExtruderPolygon {
   final GEOFeature             _geoFeature;
   final List<Geodetic2D>       _coordinates;
   final List<List<Geodetic2D>> _holesCoordinatesArray;
   final double                 _lowerHeight;
   final double                 _upperHeight;
   final G3MeshMaterial         _material;
   final boolean                _depthTest;


   //private final Sector         _sector;


   ExtruderPolygon(final GEOFeature geoFeature,
                   final List<Geodetic2D> coordinates,
                   final List<List<Geodetic2D>> holesCoordinatesArray,
                   final double lowerHeight,
                   final double upperHeight,
                   final G3MeshMaterial material,
                   final boolean depthTest) {
      _geoFeature = geoFeature;
      _coordinates = coordinates;
      _holesCoordinatesArray = holesCoordinatesArray;
      _lowerHeight = lowerHeight;
      _upperHeight = upperHeight;
      _material = material;
      _depthTest = depthTest;
      //_sector =  calculateSector(coordinates, holesCoordinatesArray);
   }


   //   private static Sector calculateSector(final List<Geodetic2D> coordinates,
   //                                         final List<List<Geodetic2D>> holesCoordinatesArray) {
   //
   //      double minLat = Double.MAX_VALUE;
   //      double maxLat = -Double.MAX_VALUE;
   //      double minLon = Double.MAX_VALUE;
   //      double maxLon = -Double.MAX_VALUE;
   //
   //      for (final Geodetic2D coordinate : coordinates) {
   //         final double lat = coordinate._latitude._degrees;
   //         if (lat < minLat) {
   //            minLat = lat;
   //         }
   //         if (lat > maxLat) {
   //            maxLat = lat;
   //         }
   //
   //         final double lon = coordinate._longitude._degrees;
   //         if (lon < minLon) {
   //            minLon = lon;
   //         }
   //         if (lon > maxLon) {
   //            maxLon = lon;
   //         }
   //      }
   //
   //
   //      for (final List<Geodetic2D> holesCoordinates : holesCoordinatesArray) {
   //         for (final Geodetic2D coordinate : holesCoordinates) {
   //            final double lat = coordinate._latitude._degrees;
   //            if (lat < minLat) {
   //               minLat = lat;
   //            }
   //            if (lat > maxLat) {
   //               maxLat = lat;
   //            }
   //
   //            final double lon = coordinate._longitude._degrees;
   //            if (lon < minLon) {
   //               minLon = lon;
   //            }
   //            if (lon > maxLon) {
   //               maxLon = lon;
   //            }
   //         }
   //      }
   //
   //      final Geodetic2D lower = Geodetic2D.fromDegrees(minLat, minLon);
   //      final Geodetic2D upper = Geodetic2D.fromDegrees(maxLat, maxLon);
   //      return new Sector(lower, upper);
   //   }


   private static ISimplePolygon2D createPolygon2D(final List<Geodetic2D> coordinates) {
      final List<IVector2> points = GCollections.collect(coordinates, new IFunction<Geodetic2D, IVector2>() {
         @Override
         public IVector2 apply(final Geodetic2D coordinate) {
            return new GVector2D(coordinate._longitude._degrees, coordinate._latitude._degrees);
         }
      });
      return GShapeUtils.createPolygon2(false, points);
   }


   IPolygon2D asPolygon2D() {
      final List<Geodetic2D> coordinates = _coordinates;
      final List<List<Geodetic2D>> holesCoordinatesArray = _holesCoordinatesArray;

      final ISimplePolygon2D hull = createPolygon2D(coordinates);
      if (holesCoordinatesArray.isEmpty()) {
         return hull;
      }
      final List<ISimplePolygon2D> holes = new ArrayList<>(holesCoordinatesArray.size());
      for (final List<Geodetic2D> holeCoordinates : holesCoordinatesArray) {
         final ISimplePolygon2D polygon2D = createPolygon2D(holeCoordinates);
         if (polygon2D != null) {
            holes.add(polygon2D);
         }
      }
      return new GComplexPolygon2D(hull, holes);
   }

}
