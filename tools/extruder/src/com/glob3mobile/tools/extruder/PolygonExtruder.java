

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
import org.glob3.mobile.generated.Planet;

import com.glob3mobile.json.JSONUtils;
import com.glob3mobile.tools.mesh.G3Mesh;
import com.glob3mobile.tools.mesh.G3MeshCollection;
import com.glob3mobile.tools.mesh.G3MeshMaterial;


public class PolygonExtruder {
   //   static {
   //      initSingletons();
   //   }


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


      public Statistics() {
         for (final ErrorType e : ErrorType.values()) {
            _errorsCounterByType.put(e, 0L);
         }
      }


      public void countTriangulation(final int trianglesCount) {
         _triangulatedCounter++;
         _allTrianglesCounter += trianglesCount;
      }


      public void countTriangulationError(final ErrorType errorType) {
         _errorsCounter++;

         _errorsCounterByType.put(errorType, _errorsCounterByType.get(errorType) + 1L);
      }


      public void printStatistics() {
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
                                                        final ExtrusionHandler<GEOFeature, Void> handler) {
      final int size = geoFeatureCollection.size();
      for (int i = 0; i < size; i++) {
         getPolygonsFromFeature(geoFeatureCollection.get(i), polygons, handler);
      }
   }


   private static void getPolygonsFromFeature(final GEOFeature geoFeature,
                                              final List<ExtruderPolygon> polygons,
                                              final ExtrusionHandler<GEOFeature, Void> handler) {
      //if (handler.extrudes(geoFeature, null)) {
      processGEOGeometry(geoFeature, geoFeature.getGeometry(), polygons, handler);
      //}
   }


   private static void processGEO2DPolygonGeometry(final GEOFeature geoFeature,
                                                   final GEO2DPolygonGeometry polygon,
                                                   final List<ExtruderPolygon> polygons,
                                                   final ExtrusionHandler<GEOFeature, Void> handler) {
      processGEO2DPolygonData(geoFeature, polygon.getPolygonData(), polygons, handler);
   }


   private static void processGEO3DPolygonGeometry(final GEOFeature geoFeature,
                                                   final GEO3DPolygonGeometry polygon,
                                                   final List<ExtruderPolygon> polygons,
                                                   final ExtrusionHandler<GEOFeature, Void> handler) {
      processGEO3DPolygonData(geoFeature, polygon.getPolygonData(), polygons, handler);
   }


   private static void processGEO2DMultiPolygonGeometry(final GEOFeature geoFeature,
                                                        final GEO2DMultiPolygonGeometry multiPolygon,
                                                        final List<ExtruderPolygon> polygons,
                                                        final ExtrusionHandler<GEOFeature, Void> handler) {
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


   private static void processGEO2DPolygonData(final GEOFeature geoFeature,
                                               final List<Geodetic2D> coordinates,
                                               final List<List<Geodetic2D>> holesCoordinatesArray,
                                               final List<ExtruderPolygon> polygons,
                                               final ExtrusionHandler<GEOFeature, Void> handler) {
      final Heigths heights = handler.getHeightsFor(geoFeature, null);
      final G3MeshMaterial material = handler.getMaterialFor(geoFeature, null);
      final boolean depthTest = handler.getDepthTestFor(geoFeature, null);

      final PolygonData<Geodetic2D> fixedPolygon = PolygonData.fixPolygon2DData(coordinates, holesCoordinatesArray);
      polygons.add(new Extruder2DPolygon(fixedPolygon._coordinates, fixedPolygon._holesCoordinatesArray, heights._lowerHeight,
               heights._upperHeight, material, depthTest));
   }


   private static void processGEO3DPolygonData(final GEOFeature geoFeature,
                                               final List<Geodetic3D> coordinates,
                                               final List<List<Geodetic3D>> holesCoordinatesArray,
                                               final List<ExtruderPolygon> polygons,
                                               final ExtrusionHandler<GEOFeature, Void> handler) {
      final Heigths heights = handler.getHeightsFor(geoFeature, null);
      final G3MeshMaterial material = handler.getMaterialFor(geoFeature, null);
      final boolean depthTest = handler.getDepthTestFor(geoFeature, null);

      final PolygonData<Geodetic3D> fixedPolygon = PolygonData.fixPolygon3DData(coordinates, holesCoordinatesArray);
      polygons.add(new Extruder3DPolygon(fixedPolygon._coordinates, fixedPolygon._holesCoordinatesArray, heights._lowerHeight,
               material, depthTest));
   }


   private static void processGEO2DPolygonData(final GEOFeature geoFeature,
                                               final GEO2DPolygonData data,
                                               final List<ExtruderPolygon> polygons,
                                               final ExtrusionHandler<GEOFeature, Void> handler) {
      final List<Geodetic2D> coordinates = cleanup2DCoordinates(data.getCoordinates());
      final List<List<Geodetic2D>> holesCoordinates = cleanup2DCoordinatesArray(data.getHolesCoordinatesArray());
      processGEO2DPolygonData(geoFeature, coordinates, holesCoordinates, polygons, handler);
   }


   public static List<Geodetic2D> cleanup2DCoordinates(final List<Geodetic2D> coordinates) {
      if ((coordinates == null) || coordinates.isEmpty()) {
         return coordinates;
      }
      return sort2DCoordinates(removeDuplicates2DCoordinates(removeLastDuplicated2DCoordinate(coordinates)));
   }


   public static List<List<Geodetic2D>> cleanup2DCoordinatesArray(final List<? extends List<Geodetic2D>> coordinatesArray) {
      return sort2DCoordinatesArray(removeDuplicates2DCoordinatesArray(removeLastDuplicated2DCoordinates(coordinatesArray)));
   }


   public static List<Geodetic3D> cleanup3DCoordinates(final List<Geodetic3D> coordinates) {
      if ((coordinates == null) || coordinates.isEmpty()) {
         return coordinates;
      }
      return sort3DCoordinates(removeDuplicates3DCoordinates(removeLastDuplicated3DCoordinate(coordinates)));
   }


   public static List<List<Geodetic3D>> cleanup3DCoordinatesArray(final List<? extends List<Geodetic3D>> coordinatesArray) {
      return sort3DCoordinatesArray(removeDuplicates3DCoordinatesArray(removeLastDuplicated3DCoordinates(coordinatesArray)));
   }


   private static void processGEO3DPolygonData(final GEOFeature geoFeature,
                                               final GEO3DPolygonData data,
                                               final List<ExtruderPolygon> polygons,
                                               final ExtrusionHandler<GEOFeature, Void> handler) {
      final List<Geodetic3D> coordinates = cleanup3DCoordinates(data.getCoordinates());
      final List<List<Geodetic3D>> holesCoordinates = cleanup3DCoordinatesArray(data.getHolesCoordinatesArray());
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
                                          final ExtrusionHandler<GEOFeature, Void> handler) {
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
                                              final boolean verbose) {
      final Statistics statistics = new Statistics();
      final List<Building> buildings = getBuildings(polygons, statistics);
      if (verbose) {
         statistics.printStatistics();
      }
      return buildings;
   }


   private static List<ExtruderPolygon> getPolygons(final GEOObject geoObject,
                                                    final ExtrusionHandler<GEOFeature, Void> handler,
                                                    final boolean verbose) {
      final List<ExtruderPolygon> polygons = new LinkedList<>();
      getPolygons(geoObject, polygons, handler);
      if (verbose) {
         System.out.println("Found " + polygons.size() + " polygons");
      }
      return polygons;
   }


   private static List<Building> getBuildings(final Collection<ExtruderPolygon> extruderPolygons,
                                              final Statistics statistics) {
      final List<Building> result = new ArrayList<>(extruderPolygons.size());
      for (final ExtruderPolygon extruderPolygon : extruderPolygons) {
         _polygonsCounter++;
         final Building building = extruderPolygon.createBuilding(statistics, _polygonsCounter);
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

      //final String json = JSONGenerator.generate(meshes.toJSON(), floatPrecision);
      final String json = JSONUtils.toJSON(meshes.toJSON(), floatPrecision);

      try (final PrintWriter out = new PrintWriter(fileName)) {
         out.println(json);
      }

      final long elapsed = System.currentTimeMillis() - now;
      logInfo("done! (" + elapsed + "ms)");
   }


   private static G3MeshCollection getMeshCollection(final boolean createNormals,
                                                     final Planet planet,
                                                     final float verticalExaggeration,
                                                     final double deltaHeight,
                                                     final int floatPrecision,
                                                     final List<Building> buildings) {
      logInfo("Starting meshing...");
      final long now = System.currentTimeMillis();

      final List<G3Mesh> meshes = getMeshes(createNormals, planet, verticalExaggeration, deltaHeight, floatPrecision, buildings);
      final G3MeshCollection result = new G3MeshCollection(meshes);

      final long elapsed = System.currentTimeMillis() - now;
      logInfo("done! (" + elapsed + "ms)");

      return result;
   }


   private static List<G3Mesh> getMeshes(final boolean createNormals,
                                         final Planet planet,
                                         final float verticalExaggeration,
                                         final double deltaHeight,
                                         final int floatPrecision,
                                         final List<Building> buildings) {
      final List<G3Mesh> result = new ArrayList<>(buildings.size());
      for (final Building building : buildings) {
         final G3Mesh mesh = building.createMesh(createNormals, planet, verticalExaggeration, deltaHeight, floatPrecision);
         if (mesh != null) {
            result.add(mesh);
         }
      }
      return result;
   }


   private static void getPolygons(final GEOObject geoObject,
                                   final List<ExtruderPolygon> polygons,
                                   final ExtrusionHandler<GEOFeature, Void> handler) {
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


   private static List<Geodetic2D> removeDuplicates2DCoordinates(final List<Geodetic2D> coordinates) {
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


   private static List<Geodetic3D> removeDuplicates3DCoordinates(final List<Geodetic3D> coordinates) {
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


   private static List<List<Geodetic2D>> removeDuplicates2DCoordinatesArray(final List<List<Geodetic2D>> coordinatesArray) {
      if ((coordinatesArray == null) || coordinatesArray.isEmpty()) {
         return Collections.emptyList();
      }

      final List<List<Geodetic2D>> result = new ArrayList<>(coordinatesArray.size());
      for (final List<Geodetic2D> coordinates : coordinatesArray) {
         result.add(removeDuplicates2DCoordinates(coordinates));
      }
      return result;
   }


   private static List<List<Geodetic3D>> removeDuplicates3DCoordinatesArray(final List<List<Geodetic3D>> coordinatesArray) {
      if ((coordinatesArray == null) || coordinatesArray.isEmpty()) {
         return Collections.emptyList();
      }

      final List<List<Geodetic3D>> result = new ArrayList<>(coordinatesArray.size());
      for (final List<Geodetic3D> coordinates : coordinatesArray) {
         result.add(removeDuplicates3DCoordinates(coordinates));
      }
      return result;
   }


   private static GEOObject getGEOObject(final String inputFileName,
                                         final boolean verbose) throws IOException {
      final String json = PolygonExtruder.readFile(inputFileName, Charset.forName("UTF-8"));
      return GEOJSONParser.parseJSON(json, verbose);
   }


   public static List<Building> getBuildings(final String inputFileName,
                                             final ExtrusionHandler<GEOFeature, Void> handler,
                                             final boolean verbose) throws IOException {
      if (verbose) {
         logInfo("Building...");
      }
      final long now = System.currentTimeMillis();

      final List<ExtruderPolygon> polygons = getPolygons(inputFileName, handler, verbose);

      final List<Building> buildings = getBuildings(polygons, verbose);
      handler.onBuildings(buildings);

      final long elapsed = System.currentTimeMillis() - now;
      if (verbose) {
         logInfo("done! (" + elapsed + "ms)");
      }

      return buildings;
   }


   public static List<ExtruderPolygon> getPolygons(final String inputFileName,
                                                   final ExtrusionHandler<GEOFeature, Void> handler,
                                                   final boolean verbose) throws IOException {
      final GEOObject geoObject = getGEOObject(inputFileName, verbose);

      final List<ExtruderPolygon> polygons = getPolygons(geoObject, handler, verbose);
      handler.onPolygons(polygons);

      return polygons;
   }


   public static void process(final String inputFileName,
                              final String outputFileName,
                              final ExtrusionHandler<GEOFeature, Void> handler,
                              final boolean createNormals,
                              final Planet planet,
                              final float verticalExaggeration,
                              final double deltaHeight,
                              final int floatPrecision,
                              final boolean verbose) throws IOException {
      final List<Building> buildings = getBuildings(inputFileName, handler, verbose);
      final G3MeshCollection meshCollection = getMeshCollection(createNormals, planet, verticalExaggeration, deltaHeight, floatPrecision, buildings);
      handler.onMeshCollection(meshCollection);
      writeMeshCollectionJSON(outputFileName, meshCollection, floatPrecision);
   }


}
