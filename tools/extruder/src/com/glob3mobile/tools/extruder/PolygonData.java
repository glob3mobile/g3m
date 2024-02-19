
package com.glob3mobile.tools.extruder;

import java.util.*;
import org.glob3.mobile.generated.*;
import org.glob3.mobile.specific.*;
import com.seisw.util.geom.*;

public class PolygonData<T> {
   static {
      G3M_JavaDesktop.initialize();
   }

   public static PolygonData<Geodetic2D> fixPolygon2DData(final List<Geodetic2D> rawCoordinates,
                                                          final List<List<Geodetic2D>> rawHolesCoordinatesArray) {
      final List<Geodetic2D>       coordinates           = PolygonExtruder.cleanup2DCoordinates(rawCoordinates);
      final List<List<Geodetic2D>> holesCoordinatesArray = PolygonExtruder.cleanup2DCoordinatesArray(rawHolesCoordinatesArray);

      if (coordinates.size() < 3) {
         return null;
      }

      final Poly p1 = polygon2DToPoly(coordinates, holesCoordinatesArray);
      final Poly p2 = polygon2DToPoly(coordinates, holesCoordinatesArray);

      final Poly fixedPoly = Clip.union(p1, p2);

      return toFixedPolygon2DData(fixedPoly);
   }

   public static PolygonData<Geodetic3D> fixPolygon3DData(final List<Geodetic3D> rawCoordinates,
                                                          final List<List<Geodetic3D>> rawHolesCoordinatesArray) {
      final List<Geodetic3D>       coordinates           = PolygonExtruder.cleanup3DCoordinates(rawCoordinates);
      final List<List<Geodetic3D>> holesCoordinatesArray = PolygonExtruder.cleanup3DCoordinatesArray(rawHolesCoordinatesArray);

      if (coordinates.size() < 3) {
         return null;
      }

      final Poly p1 = polygon3DToPoly(coordinates, holesCoordinatesArray);
      final Poly p2 = polygon3DToPoly(coordinates, holesCoordinatesArray);

      final Poly fixedPoly = Clip.union(p1, p2);

      return toFixedPolygon3DData(fixedPoly, coordinates, holesCoordinatesArray);
   }

   private static Poly polygon2DToPoly(final List<Geodetic2D> coordinates, final List<List<Geodetic2D>> holesCoordinatesArray) {
      final Poly outer = createPoly(coordinates);

      if ((holesCoordinatesArray == null) || holesCoordinatesArray.isEmpty()) {
         return outer;
      }

      Poly result = outer;

      for (final List<Geodetic2D> holesCoordinates : holesCoordinatesArray) {
         final Poly hole = createPoly(holesCoordinates);
         result = Clip.xor(result, hole);
      }

      return result;

      //      final PolyDefault complex = new PolyDefault(false);
      //      complex.add(outer);
      //
      //      for (final List<Geodetic2D> holesCoordinates : holesCoordinatesArray) {
      //         final PolyDefault hole = new PolyDefault(true);
      //         for (final Geodetic2D coordinate : holesCoordinates) {
      //            final double x = coordinate._longitude._radians;
      //            final double y = coordinate._latitude._radians;
      //            hole.add(x, y);
      //         }
      //         complex.add(hole);
      //      }
      //
      //      return complex;
   }

   //   private static <T> List<T> reversed(final List<T> list) {
   //      final int size = list.size();
   //      final List<T> result = new ArrayList<>(size);
   //      for (int i = size - 1; i >= 0; i--) {
   //         result.add(list.get(i));
   //      }
   //      return result;
   //   }
   //

   private static Poly createPoly(final List<Geodetic2D> coordinates) {
      final PolySimple poly1 = new PolySimple();
      final PolySimple poly2 = new PolySimple();
      for (final Geodetic2D coordinate : coordinates) {
         final double x = coordinate._longitude._radians;
         final double y = coordinate._latitude._radians;
         poly1.add(x, y);
         poly2.add(x, y);
      }
      return Clip.union(poly1, poly2);
   }

   private static Poly polygon3DToPoly(final List<Geodetic3D> coordinates, final List<List<Geodetic3D>> holesCoordinatesArray) {
      final PolySimple outer = new PolySimple();
      for (final Geodetic3D coordinate : coordinates) {
         final double x = coordinate._longitude._radians;
         final double y = coordinate._latitude._radians;
         outer.add(x, y);
      }

      if (holesCoordinatesArray.isEmpty()) {
         return outer;
      }

      final PolyDefault complex = new PolyDefault(false);
      complex.add(outer);

      for (final List<Geodetic3D> holesCoordinates : holesCoordinatesArray) {
         final PolyDefault hole = new PolyDefault(true);
         for (final Geodetic3D coordinate : holesCoordinates) {
            final double x = coordinate._longitude._radians;
            final double y = coordinate._latitude._radians;
            hole.add(x, y);
         }
         complex.add(hole);
      }

      return complex;
   }

   private static PolygonData<Geodetic2D> toFixedPolygon2DData(final Poly poly) {
      final List<Geodetic2D> fixedCoordinates = toGeodetic2DList(poly);

      final List<List<Geodetic2D>> fixedHolesCoordinatesArray = new ArrayList<>();
      final int                    numInnerPoly               = poly.getNumInnerPoly();
      for (int polyIndex = 1; polyIndex < numInnerPoly; polyIndex++) {
         final Poly hole = poly.getInnerPoly(polyIndex);
         fixedHolesCoordinatesArray.add(toGeodetic2DList(hole));
      }

      return new PolygonData<>(fixedCoordinates, fixedHolesCoordinatesArray);
   }

   private static PolygonData<Geodetic3D> toFixedPolygon3DData(final Poly poly, final List<Geodetic3D> coordinates,
                                                               final List<List<Geodetic3D>> holesCoordinatesArray) {
      final List<Geodetic3D> fixedCoordinates = toGeodetic3DList(poly, coordinates, holesCoordinatesArray);

      final List<List<Geodetic3D>> fixedHolesCoordinatesArray = new ArrayList<>();
      final int                    numInnerPoly               = poly.getNumInnerPoly();
      for (int polyIndex = 1; polyIndex < numInnerPoly; polyIndex++) {
         final Poly hole = poly.getInnerPoly(polyIndex);
         fixedHolesCoordinatesArray.add(toGeodetic3DList(hole, coordinates, holesCoordinatesArray));
      }

      return new PolygonData<>(fixedCoordinates, fixedHolesCoordinatesArray);
   }

   private static List<Geodetic2D> toGeodetic2DList(final Poly poly) {
      final List<Geodetic2D> result = new ArrayList<>();

      try {
         final int numPoints = poly.getNumPoints();
         for (int i = 0; i < numPoints; i++) {
            final Angle latitude  = Angle.fromRadians(poly.getY(i));
            final Angle longitude = Angle.fromRadians(poly.getX(i));
            result.add(new Geodetic2D(latitude, longitude));
         }
      }
      catch (final IndexOutOfBoundsException e) {
         System.err.println(e);
      }

      return result;
   }

   private static List<Geodetic3D> toGeodetic3DList(final Poly poly, final List<Geodetic3D> coordinates,
                                                    final List<List<Geodetic3D>> holesCoordinatesArray) {
      final List<Geodetic3D> result    = new ArrayList<>();
      final int              numPoints = poly.getNumPoints();
      for (int i = 0; i < numPoints; i++) {
         final Angle latitude  = Angle.fromRadians(poly.getY(i));
         final Angle longitude = Angle.fromRadians(poly.getX(i));
         result.add(getGeodetic3D(latitude, longitude, coordinates, holesCoordinatesArray));
      }
      return result;
   }

   private static Geodetic3D getGeodetic3D(final Angle latitude, final Angle longitude, final List<Geodetic3D> firstCandidates,
                                           final List<List<Geodetic3D>> candidatesArray) {
      Geodetic3D closest         = firstCandidates.get(0);
      double     closestDistance = sqDistance(closest, latitude, longitude);
      {
         for (int i = 1; i < firstCandidates.size(); i++) {
            final Geodetic3D candidate         = firstCandidates.get(i);
            final double     candidateDistance = sqDistance(candidate, latitude, longitude);
            if (candidateDistance < closestDistance) {
               closest         = candidate;
               closestDistance = candidateDistance;
            }
         }
      }
      for (final List<Geodetic3D> candidates : candidatesArray) {
         for (final Geodetic3D candidate : candidates) {
            final double candidateDistance = sqDistance(candidate, latitude, longitude);
            if (candidateDistance < closestDistance) {
               closest         = candidate;
               closestDistance = candidateDistance;
            }
         }
      }
      return closest;
   }

   private static double sqDistance(final Geodetic3D closest, final Angle latitude, final Angle longitude) {
      final double deltaLat = (latitude._radians - closest._latitude._radians) * 2;
      final double deltaLon = longitude._radians - closest._longitude._radians;
      return (deltaLat * deltaLat) + (deltaLon * deltaLon);
   }

   public final List<T>       _coordinates;
   public final List<List<T>> _holesCoordinatesArray;

   private PolygonData(final List<T> coordinates, final List<List<T>> holesCoordinatesArray) {
      _coordinates           = coordinates;
      _holesCoordinatesArray = holesCoordinatesArray;

   }

}
