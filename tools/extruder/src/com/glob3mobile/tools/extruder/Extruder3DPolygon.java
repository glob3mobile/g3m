

package com.glob3mobile.tools.extruder;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.glob3.mobile.generated.GEOFeature;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.tools.utils.GEOBitmap;

import com.glob3mobile.tools.mesh.G3MeshMaterial;

import poly2Tri.Triangulation;


public class Extruder3DPolygon
         extends
            ExtruderPolygon {

   private final List<Geodetic3D>       _coordinates;
   private final List<List<Geodetic3D>> _holesCoordinatesArray;


   Extruder3DPolygon(final GEOFeature geoFeature,
                     final List<Geodetic3D> coordinates,
                     final List<List<Geodetic3D>> holesCoordinatesArray,
                     final double lowerHeight,
                     final G3MeshMaterial material,
                     final boolean depthTest) {
      super(geoFeature, lowerHeight, material, depthTest, minHeight(coordinates));
      _coordinates = coordinates;
      _holesCoordinatesArray = holesCoordinatesArray;
   }


   private static double minHeight(final List<Geodetic3D> coordinates) {
      double result = Double.POSITIVE_INFINITY;
      for (final Geodetic3D coordinate : coordinates) {
         if (coordinate._height < result) {
            result = coordinate._height;
         }
      }
      return result;
   }


   private static Wall createExteriorWall(final List<Geodetic3D> coordinates,
                                          final double lowerHeight) {
      final List<WallQuad> wallQuads = new ArrayList<WallQuad>(coordinates.size());

      Geodetic3D previousCoordinate = coordinates.get(coordinates.size() - 1);
      for (final Geodetic3D coordinate3D : coordinates) {
         final WallQuad quad = new WallQuad(previousCoordinate, coordinate3D, lowerHeight);
         wallQuads.add(quad);

         previousCoordinate = coordinate3D;
      }

      return new Wall(wallQuads);
   }


   private static Wall createInteriorWall(final List<Geodetic3D> coordinates,
                                          final double lowerHeight) {
      final ArrayList<Geodetic3D> reversed = new ArrayList<>(coordinates);
      Collections.reverse(reversed);
      return createExteriorWall(reversed, lowerHeight);
   }


   private static List<Wall> createInteriorWalls(final List<List<Geodetic3D>> holesCoordinatesArray,
                                                 final double lowerHeight) {
      final List<Wall> result = new ArrayList<>(holesCoordinatesArray.size());
      for (final List<Geodetic3D> holeCoordinates : holesCoordinatesArray) {
         result.add(createInteriorWall(holeCoordinates, lowerHeight));
      }
      return result;
   }


   @Override
   public Wall createExteriorWall(final double lowerHeight) {
      return createExteriorWall(_coordinates, lowerHeight);
   }


   @Override
   public List<Wall> createInteriorWalls(final double lowerHeight) {
      return createInteriorWalls(_holesCoordinatesArray, lowerHeight);
   }


   @Override
   protected Triangulation.Data createTriangulationData() {
      final int numHoles = _holesCoordinatesArray.size();

      final int numContures = 1 + numHoles;
      final int[] numVerticesInContures = new int[numContures];

      final int coordinatesSize = _coordinates.size();
      numVerticesInContures[0] = coordinatesSize;
      int totalVertices = coordinatesSize;

      for (int i = 0; i < numHoles; i++) {
         final List<Geodetic3D> holeCoordinates = _holesCoordinatesArray.get(i);
         final int holeCoordinatesSize = holeCoordinates.size();
         numVerticesInContures[1 + i] = holeCoordinatesSize;
         totalVertices += holeCoordinatesSize;
      }

      final double[][] roofVertices = new double[totalVertices][3];

      int verticesCursor = 0;
      for (final Geodetic3D coordinate : _coordinates) {
         verticesCursor = addVextex(roofVertices, verticesCursor, coordinate);
      }

      for (int i = 0; i < numHoles; i++) {
         final List<Geodetic3D> holeCoordinates = new ArrayList<>(_holesCoordinatesArray.get(i));
         Collections.reverse(holeCoordinates);
         for (final Geodetic3D coordinate : holeCoordinates) {
            verticesCursor = addVextex(roofVertices, verticesCursor, coordinate);
         }
      }

      return new Triangulation.Data(numContures, numVerticesInContures, roofVertices);
   }


   private static int addVextex(final double[][] roofVertices,
                                final int verticesCursor,
                                final Geodetic3D coordinate) {
      roofVertices[verticesCursor][0] = coordinate._longitude._degrees;
      roofVertices[verticesCursor][1] = coordinate._latitude._degrees;
      roofVertices[verticesCursor][2] = coordinate._height;
      return verticesCursor + 1;
   }


   //   @Override
   //   protected Geodetic2D createPosition() {
   //      Geodetic3D centroid = Geodetic3D.fromDegrees(0, 0, 0);
   //
   //      final int imax = _coordinates.size() - 1;
   //
   //      double area = 0;
   //      for (int i = 0; i < imax; ++i) {
   //         final Geodetic3D currentPoint = _coordinates.get(i);
   //         final Geodetic3D nextPoint = _coordinates.get(i + 1);
   //
   //         final double term = (currentPoint._longitude._radians * nextPoint._latitude._radians)
   //                             - (nextPoint._longitude._radians * currentPoint._latitude._radians);
   //         area += term;
   //         centroid = centroid.add(currentPoint.add(nextPoint).times(term));
   //      }
   //
   //      final double term = (_coordinates.get(imax)._longitude._radians * _coordinates.get(0)._latitude._radians)
   //                          - (_coordinates.get(0)._longitude._radians * _coordinates.get(imax)._latitude._radians);
   //      area += term;
   //      centroid = centroid.add(_coordinates.get(imax).add(_coordinates.get(0)).times(term));
   //
   //      area /= 2.0;
   //      centroid = centroid.div(6 * area);
   //
   //      return centroid.asGeodetic2D();
   //   }

   @Override
   protected Geodetic2D calculateAverage() {
      double totalLatRad = 0;
      double totalLonRad = 0;
      for (final Geodetic3D coordinate : _coordinates) {
         totalLatRad += coordinate._latitude._radians;
         totalLonRad += coordinate._longitude._radians;
      }
      return Geodetic2D.fromRadians(totalLatRad / _coordinates.size(), totalLonRad / _coordinates.size());
   }


   @Override
   public void drawOn(final GEOBitmap bitmap,
                      final Color fillColor,
                      final Color borderColor) {
      final List<Geodetic2D> coordinates = to2D(_coordinates);
      final List<? extends List<Geodetic2D>> holesCoordinatesArray = to2DList(_holesCoordinatesArray);
      bitmap.drawPolygon(coordinates, holesCoordinatesArray, fillColor, borderColor, false, null);
   }


   private static List<Geodetic2D> to2D(final List<Geodetic3D> coordinates) {
      final List<Geodetic2D> result = new ArrayList<>(coordinates.size());
      for (final Geodetic3D coordinate : coordinates) {
         result.add(coordinate.asGeodetic2D());
      }
      return result;
   }


   private List<List<Geodetic2D>> to2DList(final List<List<Geodetic3D>> coordinatesArray) {
      final List<List<Geodetic2D>> result = new ArrayList<>(coordinatesArray.size());
      for (final List<Geodetic3D> coordinates : coordinatesArray) {
         result.add(to2D(coordinates));
      }
      return result;
   }


}
