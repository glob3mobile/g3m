

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

import poly2Tri.Triangle;
import poly2Tri.Triangulation;
import poly2Tri.TriangulationException;


public class PolygonExtruder {
   static {
      initSingletons();
   }


   private static void initSingletons() {
      IFactory.setInstance(new Factory_JavaDesktop());
      IMathUtils.setInstance(new MathUtils_JavaDesktop());
      IJSONParser.setInstance(new JSONParser_JavaDesktop());
      IStringBuilder.setInstance(new StringBuilder_JavaDesktop());
      ILogger.setInstance(new Logger_JavaDesktop(LogLevel.InfoLevel));
   }


   private static enum ErrorType {
      RETURN_NULL,
      NULL_POINTER_EXCEPTION,
      TRIANGULATION_EXCEPTION
   }


   private static class Statistics {
      private long                       _triangulatedCounter;
      private long                       _allTrianglesCounter;
      private long                       _errorsCounter;
      private final Map<ErrorType, Long> _errorsCounterByType = new HashMap<>();


      private Statistics() {
         for (final ErrorType e : ErrorType.values()) {
            _errorsCounterByType.put(e, 0L);
         }
      }


      private void countTriangulation(final int trianglesCount) {
         _triangulatedCounter++;
         _allTrianglesCounter += trianglesCount;
      }


      private void countTriangulationError(final ErrorType errorType,
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


   private static void processGEOFeatureCollection(final GEOFeatureCollection geoFeatureCollection,
                                                   final List<ExtruderPolygon> polygons,
                                                   final ExtrusionHandler handler) {
      final int size = geoFeatureCollection.size();
      for (int i = 0; i < size; i++) {
         processGEOFeature(geoFeatureCollection.get(i), polygons, handler);
      }
   }


   private static void processGEOFeature(final GEOFeature geoFeature,
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


   private static List<List<Geodetic2D>> removeLastDuplicated2DCoordinates(final List<ArrayList<Geodetic2D>> holesCoordinatesArray) {
      if (holesCoordinatesArray == null) {
         return Collections.emptyList();
      }

      final List<List<Geodetic2D>> result = new ArrayList<>(holesCoordinatesArray.size());
      for (final ArrayList<Geodetic2D> coordinates : holesCoordinatesArray) {
         result.add(removeLastDuplicated2DCoordinate(coordinates));
      }
      return result;
   }


   private static List<List<Geodetic3D>> removeLastDuplicated3DCoordinates(final List<ArrayList<Geodetic3D>> holesCoordinatesArray) {
      if (holesCoordinatesArray == null) {
         return Collections.emptyList();
      }

      final List<List<Geodetic3D>> result = new ArrayList<>(holesCoordinatesArray.size());
      for (final ArrayList<Geodetic3D> coordinates : holesCoordinatesArray) {
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

      final List<FixedPolygon2DData> fixedPolygons = fixPolygon2DData(coordinates, holesCoordinatesArray);
      for (final FixedPolygon2DData fixed : fixedPolygons) {
         polygons.add(new Extruder2DPolygon(geoFeature, fixed._coordinates, fixed._holesCoordinatesArray, heights._lowerHeight,
                  heights._upperHeight, material, depthTest));
      }
   }


   private static void processGEO3DPolygonData(final GEOFeature geoFeature,
                                               final List<Geodetic3D> coordinates,
                                               final List<List<Geodetic3D>> holesCoordinatesArray,
                                               final List<ExtruderPolygon> polygons,
                                               final ExtrusionHandler handler) {
      final Heigths heights = handler.getHeightsFor(geoFeature);
      final G3MeshMaterial material = handler.getMaterialFor(geoFeature);
      final boolean depthTest = handler.getDepthTestFor(geoFeature);

      final List<FixedPolygon3DData> fixedPolygons = fixPolygon3DData(coordinates, holesCoordinatesArray);
      for (final FixedPolygon3DData fixed : fixedPolygons) {
         polygons.add(new Extruder3DPolygon(geoFeature, fixed._coordinates, fixed._holesCoordinatesArray, heights._lowerHeight,
                  heights._upperHeight, material, depthTest));
      }
   }


   private static List<FixedPolygon2DData> fixPolygon2DData(final List<Geodetic2D> coordinates,
                                                            final List<List<Geodetic2D>> holesCoordinatesArray) {
      final Poly p1 = toPoly(coordinates, holesCoordinatesArray);
      final Poly p2 = toPoly(coordinates, holesCoordinatesArray);
      //final Poly fixedPoly = Clip.intersection(p1, p2);
      final Poly fixedPoly = Clip.union(p1, p2);

      final int numInnerPoly = fixedPoly.getNumInnerPoly();

      final List<FixedPolygon2DData> result = new ArrayList<>(numInnerPoly);

      for (int polyIndex = 0; polyIndex < numInnerPoly; polyIndex++) {
         final Poly poly = fixedPoly.getInnerPoly(polyIndex);
         result.add(toFixedPolygon2DData(poly));
      }

      return result;
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


   private static List<Geodetic2D> toGeodetic2DList(final Poly poly) {
      final List<Geodetic2D> result = new ArrayList<>();
      final int numPoints = poly.getNumPoints();
      for (int i = 0; i < numPoints; i++) {
         final Angle latitude = Angle.fromDegrees(poly.getY(i));
         final Angle longitude = Angle.fromDegrees(poly.getX(i));
         result.add(new Geodetic2D(latitude, longitude));
      }
      return result;
   }


   private static Poly toPoly(final List<Geodetic2D> coordinates,
                              final List<List<Geodetic2D>> holesCoordinatesArray) {
      final PolySimple outer = new PolySimple();
      for (final Geodetic2D coordinate : coordinates) {
         final double x = coordinate._longitude._degrees;
         final double y = coordinate._latitude._degrees;
         outer.add(x, y);
      }

      if (holesCoordinatesArray.isEmpty()) {
         return outer;
      }

      final PolyDefault complex = new PolyDefault(false);
      complex.add(outer);

      for (final List<Geodetic2D> holesCoordinates : holesCoordinatesArray) {
         final PolyDefault hole = new PolyDefault(true);
         //hole.setIsHole(true);
         for (final Geodetic2D coordinate : holesCoordinates) {
            final double x = coordinate._longitude._degrees;
            final double y = coordinate._latitude._degrees;
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
      final List<Geodetic2D> coordinates = removeConsecutiveDuplicates2DCoordinates(
               removeLastDuplicated2DCoordinate(data.getCoordinates()));

      final List<List<Geodetic2D>> holesCoordinates = removeConsecutiveDuplicates2DCoordinatesArray(
               removeLastDuplicated2DCoordinates(data.getHolesCoordinatesArray()));

      processGEO2DPolygonData(geoFeature, coordinates, holesCoordinates, polygons, handler);
   }


   private static void processGEO3DPolygonData(final GEOFeature geoFeature,
                                               final GEO3DPolygonData data,
                                               final List<ExtruderPolygon> polygons,
                                               final ExtrusionHandler handler) {
      final List<Geodetic3D> coordinates = removeConsecutiveDuplicates3DCoordinates(
               removeLastDuplicated3DCoordinate(data.getCoordinates()));

      final List<List<Geodetic3D>> holesCoordinates = removeConsecutiveDuplicates3DCoordinatesArray(
               removeLastDuplicated3DCoordinates(data.getHolesCoordinatesArray()));

      processGEO3DPolygonData(geoFeature, coordinates, holesCoordinates, polygons, handler);
   }


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
         //throw new RuntimeException("GEOGeometry " + geoGeometry  + " not supported");
         System.out.println("GEOGeometry " + geoGeometry + " not supported");
      }
   }


   private static void process(final String outputFileName,
                               final GEOObject geoObject,
                               final ExtrusionHandler handler) {

      final List<ExtruderPolygon> polygons = new LinkedList<>();
      process(geoObject, polygons, handler);
      System.out.println("Found " + polygons.size() + " polygons");


      final Statistics statistics = new Statistics();

      createG3MMeshJSON(outputFileName, polygons, statistics, handler);


      statistics.printStatistics();

   }


   private static void createG3MMeshJSON(final String fileName,
                                         final Collection<ExtruderPolygon> extruderPolygons,
                                         final Statistics statistics,
                                         final ExtrusionHandler handler) {

      final List<Building> buildings = new ArrayList<>(extruderPolygons.size());

      for (final ExtruderPolygon extruderPolygon : extruderPolygons) {
         _polygonsCounter++;

         //System.out.println("-- Processing Polygon #" + _polygonsCounter);

         final GEOFeature geoFeature = extruderPolygon._geoFeature;
         final List<Geodetic2D> coordinates = extruderPolygon._coordinates;
         final List<List<Geodetic2D>> holesCoordinatesArray = extruderPolygon._holesCoordinatesArray;

         final int numHoles = holesCoordinatesArray.size();

         final int[] numVerticesInContures = new int[1 + numHoles];

         final int coordinatesSize = coordinates.size();
         numVerticesInContures[0] = coordinatesSize;
         int totalVertices = coordinatesSize;

         if (numHoles != 0) {
            System.out.println("Polygon #" + _polygonsCounter + " has " + numHoles + " holes");

            for (int i = 0; i < numHoles; i++) {
               final List<Geodetic2D> holeCoordinates = holesCoordinatesArray.get(i);
               final int holeCoordinatesSize = holeCoordinates.size();
               numVerticesInContures[1 + i] = holeCoordinatesSize;
               totalVertices += holeCoordinatesSize;
            }
         }

         final int numContures = 1 + numHoles;
         final double[][] ceilingVertices = new double[totalVertices][2];

         int verticesCursor = 0;
         for (final Geodetic2D coordinate : coordinates) {
            ceilingVertices[verticesCursor][0] = coordinate._longitude._degrees;
            ceilingVertices[verticesCursor][1] = coordinate._latitude._degrees;
            verticesCursor++;
         }

         for (int i = 0; i < numHoles; i++) {
            final List<Geodetic2D> holeCoordinates = holesCoordinatesArray.get(i);
            // Collections.reverse(holeCoordinates);
            for (final Geodetic2D holeCoordinate : holeCoordinates) {
               ceilingVertices[verticesCursor][0] = holeCoordinate._longitude._degrees;
               ceilingVertices[verticesCursor][1] = holeCoordinate._latitude._degrees;
               verticesCursor++;
            }
         }

         try {
            final List<Triangle> ceilingTriangles = Triangulation.triangulate(numContures, numVerticesInContures,
                     ceilingVertices);
            if (ceilingTriangles == null) {
               System.out.println("Error triangulating polygon #" + _polygonsCounter);
               statistics.countTriangulationError(ErrorType.RETURN_NULL, geoFeature, handler);
            }
            else {
               //               if (ceilingTriangles.size() >= 1000) {
               //                  System.out.println("Polygon #" + _polygonsCounter + " triangulated with " + ceilingTriangles.size()
               //                                     + " triangles." + " Vertices=" + coordinates.size() + ", Holes="
               //                                     + holesCoordinatesArray.size() + "\n   Feature:"
               //                                     + extruderPolygon._geoFeature.getProperties());
               //               }
               // final G3MeshMaterial material = G3MeshMaterial.defaultMaterial();
               final G3MeshMaterial material = extruderPolygon._material;

               //System.out.println("Polygon #" + _polygonsCounter + " triangulated with " + triangles.size() + " triangles.");
               buildings.add(new Building(ceilingTriangles, ceilingVertices, extruderPolygon._lowerHeight,
                        extruderPolygon._upperHeight, extruderPolygon, material, extruderPolygon._depthTest));
               statistics.countTriangulation(ceilingTriangles.size());
            }
         }
         catch (final NullPointerException e) {
            statistics.countTriangulationError(ErrorType.NULL_POINTER_EXCEPTION, geoFeature, handler);
         }
         catch (final TriangulationException e) {
            statistics.countTriangulationError(ErrorType.TRIANGULATION_EXCEPTION, geoFeature, handler);
         }
      }

      writeG3MMeshJSON(fileName, buildings);
   }


   private static void writeG3MMeshJSON(final String fileName,
                                        final List<Building> buildings) {
      final G3MeshCollection meshCollection = new G3MeshCollection();
      for (final Building building : buildings) {
         meshCollection.add(new G3Mesh(building));
      }

      final String sceneJSON = JSONGenerator.generate(meshCollection.toG3MeshJSON());

      // final IByteBuffer bson = BSONGenerator.generate(meshCollection.toG3MeshJSON());
      // System.out.println("BSON: " + bson.size());

      try {
         final PrintWriter out = new PrintWriter(fileName);

         out.println(sceneJSON);

         out.close();
      }
      catch (final IOException e) {
         e.printStackTrace();
      }
   }


   private static void process(final GEOObject geoObject,
                               final List<ExtruderPolygon> polygons,
                               final ExtrusionHandler handler) {
      if (geoObject instanceof GEOFeatureCollection) {
         processGEOFeatureCollection((GEOFeatureCollection) geoObject, polygons, handler);
      }
      else if (geoObject instanceof GEOFeature) {
         processGEOFeature((GEOFeature) geoObject, polygons, handler);
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
         if (!coordinate.isEquals(lastCoordinate)) {
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
         if (!coordinate.isEquals(lastCoordinate)) {
            result.add(coordinate);
            lastCoordinate = coordinate;
         }
      }

      result.trimToSize();

      return result;
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


   public static void process(final String inputFileName,
                              final String outputFileName,
                              final ExtrusionHandler handler) throws IOException {
      logInfo("Starting...");
      final long now = System.currentTimeMillis();

      logInfo("Reading file...");
      final String json = PolygonExtruder.readFile(inputFileName, Charset.forName("UTF-8"));

      logInfo("Parsing...");
      final GEOObject geoObject = GEOJSONParser.parseJSON(json);

      process(outputFileName, geoObject, handler);

      final long elapsed = System.currentTimeMillis() - now;
      logInfo("done! (" + elapsed + "ms)");
   }


}
