

package com.glob3mobile.tools.extruder;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.GEO2DMultiPolygonGeometry;
import org.glob3.mobile.generated.GEO2DPolygonData;
import org.glob3.mobile.generated.GEO2DPolygonGeometry;
import org.glob3.mobile.generated.GEO3DPolygonData;
import org.glob3.mobile.generated.GEO3DPolygonGeometry;
import org.glob3.mobile.generated.GEOFeature;
import org.glob3.mobile.generated.GEOFeatureCollection;
import org.glob3.mobile.generated.GEOGeometry;
import org.glob3.mobile.generated.GEOJSONParser;
import org.glob3.mobile.generated.GEOObject;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.IFactory;
import org.glob3.mobile.generated.IJSONParser;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IMathUtils;
import org.glob3.mobile.generated.IStringBuilder;
import org.glob3.mobile.generated.JSONGenerator;
import org.glob3.mobile.generated.LogLevel;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.specific.Factory_JavaDesktop;
import org.glob3.mobile.specific.JSONParser_JavaDesktop;
import org.glob3.mobile.specific.Logger_JavaDesktop;
import org.glob3.mobile.specific.MathUtils_JavaDesktop;
import org.glob3.mobile.specific.StringBuilder_JavaDesktop;

import com.glob3mobile.tools.mesh.G3Mesh;
import com.glob3mobile.tools.mesh.G3MeshCollection;
import com.glob3mobile.tools.mesh.G3MeshMaterial;
import com.seisw.util.geom.Clip;
import com.seisw.util.geom.Poly;
import com.seisw.util.geom.PolyDefault;
import com.seisw.util.geom.PolySimple;


public class PolygonExtruder {
   static {
      initSingletons();
   }


   private static void initSingletons() {
      IFactory.setInstance(new Factory_JavaDesktop());
      IMathUtils.setInstance(new MathUtils_JavaDesktop());
      IJSONParser.setInstance(new JSONParser_JavaDesktop());
      IStringBuilder.setInstance(new StringBuilder_JavaDesktop(IStringBuilder.DEFAULT_FLOAT_PRECISION));
      ILogger.setInstance(new Logger_JavaDesktop(LogLevel.InfoLevel));
   }


   public static enum ErrorType {
      RETURN_NULL,
      NULL_POINTER_EXCEPTION,
      TRIANGULATION_EXCEPTION
   }


   public static class Statistics {
      private long                       _triangulatedCounter;
      private long                       _allTrianglesCounter;
      private long                       _errorsCounter;
      private final Map<ErrorType, Long> _errorsCounterByType = new HashMap<>();


      private Statistics() {
         for (final ErrorType e : ErrorType.values()) {
            _errorsCounterByType.put(e, 0L);
         }
      }


      public void countTriangulation(final int trianglesCount) {
         _triangulatedCounter++;
         _allTrianglesCounter += trianglesCount;
      }


      public void countTriangulationError(final ErrorType errorType,
                                          final GEOFeature geoFeature,
                                          final ExtrusionHandler handler) {
         _errorsCounter++;

         _errorsCounterByType.put(errorType, _errorsCounterByType.get(errorType) + 1L);

         handler.processTriangulationError(geoFeature);
      }


      private void printStatistics() {
         logInfo("=========================================================================");

         final long processedCounter = _triangulatedCounter + _errorsCounter;
         logInfo(" Processed: " + processedCounter);

         final float triangulatedPercent = ((float) _triangulatedCounter / processedCounter) * 100;
         logInfo(" Triangulated: " + _triangulatedCounter + " (" + triangulatedPercent + "%)");

         final float average = (float) _allTrianglesCounter / _triangulatedCounter;
         logInfo(" Total triangles: " + _allTrianglesCounter + " (avr=" + average + "tri/pol)");

         final float errorPercent = ((float) _errorsCounter / processedCounter) * 100;
         logInfo(" Errors: " + _errorsCounter + " (" + errorPercent + "%)");

         for (final Map.Entry<ErrorType, Long> entry : _errorsCounterByType.entrySet()) {
            logInfo("   " + entry.getKey() + "=" + entry.getValue());
         }

         logInfo("=========================================================================");
      }
   }


   private static int _polygonsCounter = 0;


   private static void logInfo(final String str) {
      System.out.println("- " + str);
   }


   private static String readFile(final String path,
                                  final Charset encoding) throws IOException {
      final byte[] encoded = Files.readAllBytes(Paths.get(path));
      return encoding.decode(ByteBuffer.wrap(encoded)).toString();
   }


   private static void getPolygonsFromFeatureCollection(final GEOFeatureCollection geoFeatureCollection,
                                                        final List<ExtruderPolygon> polygons,
                                                        final ExtrusionHandler handler) {
      final int size = geoFeatureCollection.size();
      for (int i = 0; i < size; i++) {
         getPolygonsFromFeature(geoFeatureCollection.get(i), polygons, handler);
      }
   }


   private static void getPolygonsFromFeature(final GEOFeature geoFeature,
                                              final List<ExtruderPolygon> polygons,
                                              final ExtrusionHandler handler) {
      if (handler.extrudes(geoFeature)) {
         processGEOGeometry(geoFeature, geoFeature.getGeometry(), polygons, handler);
      }
   }


   private static void processGEO2DPolygonGeometry(final GEOFeature geoFeature,
                                                   final GEO2DPolygonGeometry polygon,
                                                   final List<ExtruderPolygon> polygons,
                                                   final ExtrusionHandler handler) {
      processGEO2DPolygonData(geoFeature, polygon.getPolygonData(), polygons, handler);
   }


   private static void processGEO3DPolygonGeometry(final GEOFeature geoFeature,
                                                   final GEO3DPolygonGeometry polygon,
                                                   final List<ExtruderPolygon> polygons,
                                                   final ExtrusionHandler handler) {
      processGEO3DPolygonData(geoFeature, polygon.getPolygonData(), polygons, handler);
   }


   private static void processGEO2DMultiPolygonGeometry(final GEOFeature geoFeature,
                                                        final GEO2DMultiPolygonGeometry multiPolygon,
                                                        final List<ExtruderPolygon> polygons,
                                                        final ExtrusionHandler handler) {
      for (final GEO2DPolygonData data : multiPolygon.getPolygonsData()) {
         processGEO2DPolygonData(geoFeature, data, polygons, handler);
      }
   }


   private static List<Geodetic2D> removeLastDuplicated2DCoordinate(final List<Geodetic2D> coordinates) {
      final Geodetic2D firstCoordinate = coordinates.get(0);
      final int lastIndex = coordinates.size() - 1;
      final Geodetic2D lastCoordinate = coordinates.get(lastIndex);
      final List<Geodetic2D> result = firstCoordinate.isEquals(lastCoordinate) ? coordinates.subList(0, lastIndex) : coordinates;
      Collections.reverse(result);
      return result;
   }


   private static List<Geodetic3D> removeLastDuplicated3DCoordinate(final List<Geodetic3D> coordinates) {
      final Geodetic3D firstCoordinate = coordinates.get(0);
      final int lastIndex = coordinates.size() - 1;
      final Geodetic3D lastCoordinate = coordinates.get(lastIndex);
      final List<Geodetic3D> result = firstCoordinate.isEquals(lastCoordinate) ? coordinates.subList(0, lastIndex) : coordinates;
      Collections.reverse(result);
      return result;
   }


   private static List<List<Geodetic2D>> removeLastDuplicated2DCoordinates(final List<? extends List<Geodetic2D>> holesCoordinatesArray) {
      if (holesCoordinatesArray == null) {
         return Collections.emptyList();
      }

      final List<List<Geodetic2D>> result = new ArrayList<>(holesCoordinatesArray.size());
      for (final List<Geodetic2D> coordinates : holesCoordinatesArray) {
         result.add(removeLastDuplicated2DCoordinate(coordinates));
      }
      return result;
   }


   private static List<List<Geodetic3D>> removeLastDuplicated3DCoordinates(final List<? extends List<Geodetic3D>> holesCoordinatesArray) {
      if (holesCoordinatesArray == null) {
         return Collections.emptyList();
      }

      final List<List<Geodetic3D>> result = new ArrayList<>(holesCoordinatesArray.size());
      for (final List<Geodetic3D> coordinates : holesCoordinatesArray) {
         result.add(removeLastDuplicated3DCoordinate(coordinates));
      }
      return result;
   }


   private static class FixedPolygon2DData {
      private final List<Geodetic2D>       _coordinates;
      private final List<List<Geodetic2D>> _holesCoordinatesArray;


      private FixedPolygon2DData(final List<Geodetic2D> coordinates,
                                 final List<List<Geodetic2D>> holesCoordinatesArray) {
         _coordinates = coordinates;
         _holesCoordinatesArray = holesCoordinatesArray;
      }

   }


   private static class FixedPolygon3DData {
      private final List<Geodetic3D>       _coordinates;
      private final List<List<Geodetic3D>> _holesCoordinatesArray;


      private FixedPolygon3DData(final List<Geodetic3D> coordinates,
                                 final List<List<Geodetic3D>> holesCoordinatesArray) {
         _coordinates = coordinates;
         _holesCoordinatesArray = holesCoordinatesArray;
      }

   }


   private static void processGEO2DPolygonData(final GEOFeature geoFeature,
                                               final List<Geodetic2D> coordinates,
                                               final List<List<Geodetic2D>> holesCoordinatesArray,
                                               final List<ExtruderPolygon> polygons,
                                               final ExtrusionHandler handler) {
      final Heigths heights = handler.getHeightsFor(geoFeature);
      final G3MeshMaterial material = handler.getMaterialFor(geoFeature);
      final boolean depthTest = handler.getDepthTestFor(geoFeature);

      final FixedPolygon2DData fixedPolygon = fixPolygon2DData(coordinates, holesCoordinatesArray);
      polygons.add(new Extruder2DPolygon(geoFeature, fixedPolygon._coordinates, fixedPolygon._holesCoordinatesArray,
               heights._lowerHeight, heights._upperHeight, material, depthTest));
   }


   private static void processGEO3DPolygonData(final GEOFeature geoFeature,
                                               final List<Geodetic3D> coordinates,
                                               final List<List<Geodetic3D>> holesCoordinatesArray,
                                               final List<ExtruderPolygon> polygons,
                                               final ExtrusionHandler handler) {
      final Heigths heights = handler.getHeightsFor(geoFeature);
      final G3MeshMaterial material = handler.getMaterialFor(geoFeature);
      final boolean depthTest = handler.getDepthTestFor(geoFeature);

      final FixedPolygon3DData fixedPolygon = fixPolygon3DData(coordinates, holesCoordinatesArray);
      polygons.add(new Extruder3DPolygon(geoFeature, fixedPolygon._coordinates, fixedPolygon._holesCoordinatesArray,
               heights._lowerHeight, material, depthTest));
   }


   private static FixedPolygon2DData fixPolygon2DData(final List<Geodetic2D> coordinates,
                                                      final List<List<Geodetic2D>> holesCoordinatesArray) {
      final Poly p1 = polygon2DToPoly(coordinates, holesCoordinatesArray);
      final Poly p2 = polygon2DToPoly(coordinates, holesCoordinatesArray);

      //final Poly fixedPoly = Clip.intersection(p1, p2);

      // final Poly union = Clip.union(p1, p2);
      // final Poly fixedPoly = Clip.intersection(union, union);

      final Poly fixedPoly = Clip.union(p1, p2);

      return toFixedPolygon2DData(fixedPoly);
   }


   private static FixedPolygon3DData fixPolygon3DData(final List<Geodetic3D> coordinates,
                                                      final List<List<Geodetic3D>> holesCoordinatesArray) {
      final Poly p1 = polygon3DToPoly(coordinates, holesCoordinatesArray);
      final Poly p2 = polygon3DToPoly(coordinates, holesCoordinatesArray);

      //final Poly fixedPoly = Clip.intersection(p1, p2);

      // final Poly union = Clip.union(p1, p2);
      // final Poly fixedPoly = Clip.intersection(union, union);

      final Poly fixedPoly = Clip.union(p1, p2);

      return toFixedPolygon3DData(fixedPoly, coordinates, holesCoordinatesArray);
   }


   private static FixedPolygon2DData toFixedPolygon2DData(final Poly poly) {
      final List<Geodetic2D> fixedCoordinates = toGeodetic2DList(poly);

      final List<List<Geodetic2D>> fixedHolesCoordinatesArray = new ArrayList<>();
      final int numInnerPoly = poly.getNumInnerPoly();
      for (int polyIndex = 1; polyIndex < numInnerPoly; polyIndex++) {
         final Poly hole = poly.getInnerPoly(polyIndex);
         fixedHolesCoordinatesArray.add(toGeodetic2DList(hole));
      }

      return new FixedPolygon2DData(fixedCoordinates, fixedHolesCoordinatesArray);
   }


   private static FixedPolygon3DData toFixedPolygon3DData(final Poly poly,
                                                          final List<Geodetic3D> coordinates,
                                                          final List<List<Geodetic3D>> holesCoordinatesArray) {
      final List<Geodetic3D> fixedCoordinates = toGeodetic3DList(poly, coordinates, holesCoordinatesArray);

      final List<List<Geodetic3D>> fixedHolesCoordinatesArray = new ArrayList<>();
      final int numInnerPoly = poly.getNumInnerPoly();
      for (int polyIndex = 1; polyIndex < numInnerPoly; polyIndex++) {
         final Poly hole = poly.getInnerPoly(polyIndex);
         fixedHolesCoordinatesArray.add(toGeodetic3DList(hole, coordinates, holesCoordinatesArray));
      }

      return new FixedPolygon3DData(fixedCoordinates, fixedHolesCoordinatesArray);
   }


   private static List<Geodetic2D> toGeodetic2DList(final Poly poly) {
      final List<Geodetic2D> result = new ArrayList<>();
      final int numPoints = poly.getNumPoints();
      for (int i = 0; i < numPoints; i++) {
         final Angle latitude = Angle.fromRadians(poly.getY(i));
         final Angle longitude = Angle.fromRadians(poly.getX(i));
         result.add(new Geodetic2D(latitude, longitude));
      }
      return result;
   }


   private static List<Geodetic3D> toGeodetic3DList(final Poly poly,
                                                    final List<Geodetic3D> coordinates,
                                                    final List<List<Geodetic3D>> holesCoordinatesArray) {
      final List<Geodetic3D> result = new ArrayList<>();
      final int numPoints = poly.getNumPoints();
      for (int i = 0; i < numPoints; i++) {
         final Angle latitude = Angle.fromRadians(poly.getY(i));
         final Angle longitude = Angle.fromRadians(poly.getX(i));
         result.add(getGeodetic3D(latitude, longitude, coordinates, holesCoordinatesArray));
      }
      return result;
   }


   private static Geodetic3D getGeodetic3D(final Angle latitude,
                                           final Angle longitude,
                                           final List<Geodetic3D> firstCandidates,
                                           final List<List<Geodetic3D>> candidatesArray) {
      Geodetic3D closest = firstCandidates.get(0);
      double closestDistance = sqDistance(closest, latitude, longitude);
      {
         for (int i = 1; i < firstCandidates.size(); i++) {
            final Geodetic3D candidate = firstCandidates.get(i);
            final double candidateDistance = sqDistance(candidate, latitude, longitude);
            if (candidateDistance < closestDistance) {
               closest = candidate;
               closestDistance = candidateDistance;
            }
         }
      }
      for (final List<Geodetic3D> candidates : candidatesArray) {
         for (final Geodetic3D candidate : candidates) {
            final double candidateDistance = sqDistance(candidate, latitude, longitude);
            if (candidateDistance < closestDistance) {
               closest = candidate;
               closestDistance = candidateDistance;
            }
         }
      }
      return closest;
   }


   private static double sqDistance(final Geodetic3D closest,
                                    final Angle latitude,
                                    final Angle longitude) {
      final double deltaLat = (latitude._radians - closest._latitude._radians) * 2;
      final double deltaLon = longitude._radians - closest._longitude._radians;
      return (deltaLat * deltaLat) + (deltaLon * deltaLon);
   }


   private static Poly polygon2DToPoly(final List<Geodetic2D> coordinates,
                                       final List<List<Geodetic2D>> holesCoordinatesArray) {
      final PolySimple outer = new PolySimple();
      for (final Geodetic2D coordinate : coordinates) {
         final double x = coordinate._longitude._radians;
         final double y = coordinate._latitude._radians;
         outer.add(x, y);
      }

      if (holesCoordinatesArray.isEmpty()) {
         return outer;
      }

      final PolyDefault complex = new PolyDefault(false);
      complex.add(outer);

      for (final List<Geodetic2D> holesCoordinates : holesCoordinatesArray) {
         final PolyDefault hole = new PolyDefault(true);
         for (final Geodetic2D coordinate : holesCoordinates) {
            final double x = coordinate._longitude._radians;
            final double y = coordinate._latitude._radians;
            hole.add(x, y);
         }
         complex.add(hole);
      }

      return complex;
   }


   private static Poly polygon3DToPoly(final List<Geodetic3D> coordinates,
                                       final List<List<Geodetic3D>> holesCoordinatesArray) {
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


   private static void processGEO2DPolygonData(final GEOFeature geoFeature,
                                               final GEO2DPolygonData data,
                                               final List<ExtruderPolygon> polygons,
                                               final ExtrusionHandler handler) {
      //      final List<Geodetic2D> coordinates = removeConsecutiveDuplicates2DCoordinates(
      //               removeLastDuplicated2DCoordinate(data.getCoordinates()));
      //
      //      final List<List<Geodetic2D>> holesCoordinates = removeConsecutiveDuplicates2DCoordinatesArray(
      //               removeLastDuplicated2DCoordinates(data.getHolesCoordinatesArray()));
      final List<Geodetic2D> coordinates = sort2DCoordinates(
               removeConsecutiveDuplicates2DCoordinates(removeLastDuplicated2DCoordinate(data.getCoordinates())));

      final List<List<Geodetic2D>> holesCoordinates = sort2DCoordinatesArray(
               removeConsecutiveDuplicates2DCoordinatesArray(removeLastDuplicated2DCoordinates(data.getHolesCoordinatesArray())));

      processGEO2DPolygonData(geoFeature, coordinates, holesCoordinates, polygons, handler);
   }


   private static void processGEO3DPolygonData(final GEOFeature geoFeature,
                                               final GEO3DPolygonData data,
                                               final List<ExtruderPolygon> polygons,
                                               final ExtrusionHandler handler) {
      final List<Geodetic3D> coordinates = sort3DCoordinates(
               removeConsecutiveDuplicates3DCoordinates(removeLastDuplicated3DCoordinate(data.getCoordinates())));

      final List<List<Geodetic3D>> holesCoordinates = sort3DCoordinatesArray(
               removeConsecutiveDuplicates3DCoordinatesArray(removeLastDuplicated3DCoordinates(data.getHolesCoordinatesArray())));

      processGEO3DPolygonData(geoFeature, coordinates, holesCoordinates, polygons, handler);
   }


   private static List<List<Geodetic2D>> sort2DCoordinatesArray(final List<? extends List<Geodetic2D>> coordinatesArray) {
      if (coordinatesArray == null) {
         return null;
      }

      final List<List<Geodetic2D>> result = new ArrayList<>(coordinatesArray.size());
      for (final List<Geodetic2D> coordinates : coordinatesArray) {
         result.add(sort2DCoordinates(coordinates));
      }
      return result;
   }


   private static List<List<Geodetic3D>> sort3DCoordinatesArray(final List<? extends List<Geodetic3D>> coordinatesArray) {
      if (coordinatesArray == null) {
         return null;
      }

      final List<List<Geodetic3D>> result = new ArrayList<>(coordinatesArray.size());
      for (final List<Geodetic3D> coordinates : coordinatesArray) {
         result.add(sort3DCoordinates(coordinates));
      }
      return result;
   }


   private static List<Geodetic2D> sort2DCoordinates(final List<Geodetic2D> coordinates) {
      final List<Geodetic2D> result = new ArrayList<>(coordinates);
      //      sortClockwise(get2DCenter(coordinates), coordinates);
      return result;
   }


   private static List<Geodetic3D> sort3DCoordinates(final List<Geodetic3D> coordinates) {
      final List<Geodetic3D> result = new ArrayList<>(coordinates);
      //      sortClockwise(get3DCenter(coordinates), coordinates);
      return result;
   }


   //   private static Geodetic2D get2DCenter(final List<Geodetic2D> coordinates) {
   //      double totalLatRad = 0;
   //      double totalLonRad = 0;
   //      for (final Geodetic2D coordinate : coordinates) {
   //         totalLatRad += coordinate._latitude._radians;
   //         totalLonRad += coordinate._longitude._radians;
   //      }
   //
   //      final int coordinatesSize = coordinates.size();
   //      return new Geodetic2D( //
   //               Angle.fromRadians(totalLatRad / coordinatesSize), //
   //               Angle.fromRadians(totalLonRad / coordinatesSize));
   //   }
   //
   //
   //   private static Geodetic3D get3DCenter(final List<Geodetic3D> coordinates) {
   //      double totalLatRad = 0;
   //      double totalLonRad = 0;
   //      double totalHeight = 0;
   //      for (final Geodetic3D coordinate : coordinates) {
   //         totalLatRad += coordinate._latitude._radians;
   //         totalLonRad += coordinate._longitude._radians;
   //         totalHeight += coordinate._height;
   //      }
   //
   //      final int coordinatesSize = coordinates.size();
   //      return new Geodetic3D( //
   //               Angle.fromRadians(totalLatRad / coordinatesSize), //
   //               Angle.fromRadians(totalLonRad / coordinatesSize), //
   //               totalHeight / coordinatesSize);
   //   }


   //   private static void sortClockwise(final Geodetic3D center,
   //                                     final List<Geodetic3D> coordinates) {
   //      //      final Vector3D centerV = toVector3D(center);
   //      //
   //      //      try {
   //      //         Collections.sort(coordinates, new Comparator<>() {
   //      //            @Override
   //      //            public int compare(final Geodetic3D p1,
   //      //                               final Geodetic3D p2) {
   //      //               final Vector3D p1V = toVector3D(p1);
   //      //               final Vector3D p2V = toVector3D(p2);
   //      //               final double angle = getSignedAngle(centerV, p1V, p2V);
   //      //
   //      //               if (angle < 0) {
   //      //                  return -1;
   //      //               }
   //      //               else if (angle > 0) {
   //      //                  return 1;
   //      //               }
   //      //               else {
   //      //                  return 0;
   //      //               }
   //      //            }
   //      //         });
   //      //      }
   //      //      catch (final java.lang.IllegalArgumentException e) {
   //      //         System.err.println(e);
   //      //      }
   //   }


   //   private static Vector3D toVector3D(final Geodetic2D geodetic) {
   //      return new Vector3D(geodetic._longitude._degrees, geodetic._latitude._degrees, 0);
   //   }
   //
   //
   //   private static Vector3D toVector3D(final Geodetic3D geodetic) {
   //      return new Vector3D(geodetic._longitude._degrees, geodetic._latitude._degrees, geodetic._height);
   //   }


   //   private static void sortClockwise(final Geodetic2D center,
   //                                     final List<Geodetic2D> coordinates) {
   //      //      final Vector3D centerV = toVector3D(center);
   //      //
   //      //      try {
   //      //         Collections.sort(coordinates, new Comparator<>() {
   //      //            @Override
   //      //            public int compare(final Geodetic2D p1,
   //      //                               final Geodetic2D p2) {
   //      //               final Vector3D p1V = toVector3D(p1);
   //      //               final Vector3D p2V = toVector3D(p2);
   //      //               final double angle = getSignedAngle(centerV, p1V, p2V);
   //      //
   //      //               if (angle < 0) {
   //      //                  return -1;
   //      //               }
   //      //               else if (angle > 0) {
   //      //                  return 1;
   //      //               }
   //      //               else {
   //      //                  return 0;
   //      //               }
   //      //            }
   //      //         });
   //      //      }
   //      //      catch (final java.lang.IllegalArgumentException e) {
   //      //         System.err.println(e);
   //      //      }
   //   }


   //   private static double getSignedAngle(final Vector3D normal,
   //                                        final Vector3D v1,
   //                                        final Vector3D v2) {
   //      final Vector3D tempCross = v1.cross(v2);
   //
   //      final double angle = Math.atan2(tempCross.length(), v1.dot(v2));
   //
   //      return tempCross.dot(normal) < 0 ? -angle : angle;
   //   }


   private static void processGEOGeometry(final GEOFeature geoFeature,
                                          final GEOGeometry geoGeometry,
                                          final List<ExtruderPolygon> polygons,
                                          final ExtrusionHandler handler) {
      if (geoGeometry instanceof GEO2DPolygonGeometry) {
         processGEO2DPolygonGeometry(geoFeature, (GEO2DPolygonGeometry) geoGeometry, polygons, handler);
      }
      else if (geoGeometry instanceof GEO3DPolygonGeometry) {
         processGEO3DPolygonGeometry(geoFeature, (GEO3DPolygonGeometry) geoGeometry, polygons, handler);
      }
      else if (geoGeometry instanceof GEO2DMultiPolygonGeometry) {
         processGEO2DMultiPolygonGeometry(geoFeature, (GEO2DMultiPolygonGeometry) geoGeometry, polygons, handler);
      }
      else {
         System.out.println("GEOGeometry " + geoGeometry + " not supported");
      }
   }


   private static List<Building> getBuildings(final List<ExtruderPolygon> polygons,
                                              final ExtrusionHandler handler,
                                              final boolean verbose) {
      final Statistics statistics = new Statistics();
      final List<Building> buildings = getBuildings(polygons, statistics, handler);
      if (verbose) {
         statistics.printStatistics();
      }
      return buildings;
   }


   private static List<ExtruderPolygon> getPolygons(final GEOObject geoObject,
                                                    final ExtrusionHandler handler,
                                                    final boolean verbose) {
      final List<ExtruderPolygon> polygons = new LinkedList<>();
      getPolygons(geoObject, polygons, handler);
      if (verbose) {
         System.out.println("Found " + polygons.size() + " polygons");
      }
      return polygons;
   }


   private static List<Building> getBuildings(final Collection<ExtruderPolygon> extruderPolygons,
                                              final Statistics statistics,
                                              final ExtrusionHandler handler) {
      final List<Building> result = new ArrayList<>(extruderPolygons.size());
      for (final ExtruderPolygon extruderPolygon : extruderPolygons) {
         _polygonsCounter++;
         final Building building = extruderPolygon.createBuilding(statistics, handler, _polygonsCounter);
         if (building != null) {
            result.add(building);
         }
      }
      return result;
   }


   private static void writeMeshCollectionJSON(final String fileName,
                                               final G3MeshCollection meshes,
                                               final int floatPrecision) throws IOException {
      logInfo("Saving meshes...");
      final long now = System.currentTimeMillis();

      final String json = JSONGenerator.generate(meshes.toJSON(), floatPrecision);
      //final String json = JSONUtils.toJSON(meshes.toJSON(), floatPrecision);

      try (final PrintWriter out = new PrintWriter(fileName)) {
         out.println(json);
      }

      final long elapsed = System.currentTimeMillis() - now;
      logInfo("done! (" + elapsed + "ms)");
   }


   private static G3MeshCollection getMeshCollection(final Planet planet,
                                                     final float verticalExaggeration,
                                                     final double deltaHeight,
                                                     final int floatPrecision,
                                                     final List<Building> buildings) {
      logInfo("Starting meshing...");
      final long now = System.currentTimeMillis();

      final List<G3Mesh> meshes = getMeshes(planet, verticalExaggeration, deltaHeight, floatPrecision, buildings);
      final G3MeshCollection result = new G3MeshCollection(meshes);

      final long elapsed = System.currentTimeMillis() - now;
      logInfo("done! (" + elapsed + "ms)");

      return result;
   }


   private static List<G3Mesh> getMeshes(final Planet planet,
                                         final float verticalExaggeration,
                                         final double deltaHeight,
                                         final int floatPrecision,
                                         final List<Building> buildings) {
      final List<G3Mesh> result = new ArrayList<>(buildings.size());
      for (final Building building : buildings) {
         final G3Mesh mesh = building.createMesh(planet, verticalExaggeration, deltaHeight, floatPrecision);
         if (mesh != null) {
            result.add(mesh);
         }
      }
      return result;
   }


   private static void getPolygons(final GEOObject geoObject,
                                   final List<ExtruderPolygon> polygons,
                                   final ExtrusionHandler handler) {
      if (geoObject instanceof GEOFeatureCollection) {
         getPolygonsFromFeatureCollection((GEOFeatureCollection) geoObject, polygons, handler);
      }
      else if (geoObject instanceof GEOFeature) {
         getPolygonsFromFeature((GEOFeature) geoObject, polygons, handler);
      }
      else {
         throw new RuntimeException("GEOObject subclass " + geoObject.getClass() + " not supported");
      }
   }


   private static List<Geodetic2D> removeConsecutiveDuplicates2DCoordinates(final List<Geodetic2D> coordinates) {
      if ((coordinates == null) || coordinates.isEmpty()) {
         return Collections.emptyList();
      }

      final int coordinatesSize = coordinates.size();
      final ArrayList<Geodetic2D> result = new ArrayList<>(coordinatesSize);

      Geodetic2D lastCoordinate = coordinates.get(0);
      result.add(lastCoordinate);

      for (int i = 1; i < coordinatesSize; i++) {
         final Geodetic2D coordinate = coordinates.get(i);
         if (!equals(coordinate, lastCoordinate)) {
            result.add(coordinate);
            lastCoordinate = coordinate;
         }
      }

      result.trimToSize();

      return result;
   }


   private static List<Geodetic3D> removeConsecutiveDuplicates3DCoordinates(final List<Geodetic3D> coordinates) {
      if ((coordinates == null) || coordinates.isEmpty()) {
         return Collections.emptyList();
      }

      final int coordinatesSize = coordinates.size();
      final ArrayList<Geodetic3D> result = new ArrayList<>(coordinatesSize);

      Geodetic3D lastCoordinate = coordinates.get(0);
      result.add(lastCoordinate);

      for (int i = 1; i < coordinatesSize; i++) {
         final Geodetic3D coordinate = coordinates.get(i);
         if (!equals(coordinate, lastCoordinate)) {
            result.add(coordinate);
            lastCoordinate = coordinate;
         }
      }

      result.trimToSize();

      return result;
   }


   private static boolean equals(final Geodetic2D one,
                                 final Geodetic2D two) {
      return equals( //
               one._latitude, one._longitude, //
               two._latitude, two._longitude);
   }


   private static boolean equals(final Geodetic3D one,
                                 final Geodetic3D two) {
      return equals( //
               one._latitude, one._longitude, //
               two._latitude, two._longitude);
   }


   private static boolean equals(final Angle latitude1,
                                 final Angle longitude1,
                                 final Angle latitude2,
                                 final Angle longitude2) {
      return equals(latitude1, latitude2) && equals(longitude1, longitude2);
   }


   private static boolean equals(final Angle angle1,
                                 final Angle angle2) {
      return (angle1._radians == angle2._radians);
   }


   private static List<List<Geodetic2D>> removeConsecutiveDuplicates2DCoordinatesArray(final List<List<Geodetic2D>> coordinatesArray) {
      if ((coordinatesArray == null) || coordinatesArray.isEmpty()) {
         return Collections.emptyList();
      }

      final List<List<Geodetic2D>> result = new ArrayList<>(coordinatesArray.size());
      for (final List<Geodetic2D> coordinates : coordinatesArray) {
         result.add(removeConsecutiveDuplicates2DCoordinates(coordinates));
      }
      return result;
   }


   private static List<List<Geodetic3D>> removeConsecutiveDuplicates3DCoordinatesArray(final List<List<Geodetic3D>> coordinatesArray) {
      if ((coordinatesArray == null) || coordinatesArray.isEmpty()) {
         return Collections.emptyList();
      }

      final List<List<Geodetic3D>> result = new ArrayList<>(coordinatesArray.size());
      for (final List<Geodetic3D> coordinates : coordinatesArray) {
         result.add(removeConsecutiveDuplicates3DCoordinates(coordinates));
      }
      return result;
   }


   private static GEOObject getGEOObject(final String inputFileName,
                                         final boolean verbose) throws IOException {
      final String json = PolygonExtruder.readFile(inputFileName, Charset.forName("UTF-8"));
      return GEOJSONParser.parseJSON(json, verbose);
   }


   public static List<Building> getBuildings(final String inputFileName,
                                             final ExtrusionHandler handler,
                                             final boolean verbose) throws IOException {
      if (verbose) {
         logInfo("Building...");
      }
      final long now = System.currentTimeMillis();

      final List<ExtruderPolygon> polygons = getPolygons(inputFileName, handler, verbose);

      final List<Building> buildings = getBuildings(polygons, handler, verbose);
      handler.onBuildings(buildings);

      final long elapsed = System.currentTimeMillis() - now;
      if (verbose) {
         logInfo("done! (" + elapsed + "ms)");
      }

      return buildings;
   }


   public static List<ExtruderPolygon> getPolygons(final String inputFileName,
                                                   final ExtrusionHandler handler,
                                                   final boolean verbose) throws IOException {
      final GEOObject geoObject = getGEOObject(inputFileName, verbose);
      handler.onRootGEOObject(geoObject);

      final List<ExtruderPolygon> polygons = getPolygons(geoObject, handler, verbose);
      handler.onPolygons(polygons);

      return polygons;
   }


   public static void process(final String inputFileName,
                              final String outputFileName,
                              final ExtrusionHandler handler,
                              final Planet planet,
                              final float verticalExaggeration,
                              final double deltaHeight,
                              final int floatPrecision,
                              final boolean verbose) throws IOException {
      final List<Building> buildings = getBuildings(inputFileName, handler, verbose);
      final G3MeshCollection meshCollection = getMeshCollection(planet, verticalExaggeration, deltaHeight, floatPrecision,
               buildings);
      handler.onMeshCollection(meshCollection);
      writeMeshCollectionJSON(outputFileName, meshCollection, floatPrecision);
   }


}
