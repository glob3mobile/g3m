

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


public class Extruder2DPolygon
         extends
            ExtruderPolygon {

   private final double                 _upperHeight;
   private final List<Geodetic2D>       _coordinates;
   private final List<List<Geodetic2D>> _holesCoordinatesArray;


   Extruder2DPolygon(final GEOFeature geoFeature,
                     final List<Geodetic2D> coordinates,
                     final List<List<Geodetic2D>> holesCoordinatesArray,
                     final double lowerHeight,
                     final double upperHeight,
                     final G3MeshMaterial material) {
      super(geoFeature, lowerHeight, material, lowerHeight);
      _upperHeight = upperHeight;
      _coordinates = coordinates;
      _holesCoordinatesArray = holesCoordinatesArray;
   }


   private static Wall createExteriorWall(final List<Geodetic2D> coordinates,
                                          final double lowerHeight,
                                          final double upperHeight) {
      final List<WallQuad> wallQuads = new ArrayList<WallQuad>(coordinates.size());

      Geodetic3D previousCoordinate = new Geodetic3D(coordinates.get(coordinates.size() - 1), upperHeight);
      for (final Geodetic2D coordinate2D : coordinates) {
         final Geodetic3D coordinate3D = new Geodetic3D(coordinate2D, upperHeight);
         final WallQuad quad = new WallQuad(previousCoordinate, coordinate3D, lowerHeight);
         wallQuads.add(quad);

         previousCoordinate = coordinate3D;
      }

      return new Wall(wallQuads);
   }


   private static Wall createInteriorWall(final List<Geodetic2D> coordinates,
                                          final double lowerHeight,
                                          final double upperHeight) {
      final ArrayList<Geodetic2D> reversed = new ArrayList<>(coordinates);
      Collections.reverse(reversed);
      return createExteriorWall(reversed, lowerHeight, upperHeight);
   }


   private static List<Wall> createInteriorWalls(final List<List<Geodetic2D>> holesCoordinatesArray,
                                                 final double lowerHeight,
                                                 final double upperHeight) {
      final List<Wall> result = new ArrayList<>(holesCoordinatesArray.size());
      for (final List<Geodetic2D> holeCoordinates : holesCoordinatesArray) {
         result.add(createInteriorWall(holeCoordinates, lowerHeight, upperHeight));
      }
      return result;
   }


   @Override
   public Wall createExteriorWall(final double lowerHeight) {
      return createExteriorWall(_coordinates, lowerHeight, _upperHeight);
   }


   @Override
   public List<Wall> createInteriorWalls(final double lowerHeight) {
      return createInteriorWalls(_holesCoordinatesArray, lowerHeight, _upperHeight);
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
         final List<Geodetic2D> holeCoordinates = _holesCoordinatesArray.get(i);
         final int holeCoordinatesSize = holeCoordinates.size();
         numVerticesInContures[1 + i] = holeCoordinatesSize;
         totalVertices += holeCoordinatesSize;
      }

      final double[][] roofVertices = new double[totalVertices][3];

      int verticesCursor = 0;
      for (final Geodetic2D coordinate : _coordinates) {
         verticesCursor = addVextex(roofVertices, verticesCursor, coordinate, _upperHeight);
      }

      for (int i = 0; i < numHoles; i++) {
         final List<Geodetic2D> holeCoordinates = new ArrayList<>(_holesCoordinatesArray.get(i));
         Collections.reverse(holeCoordinates);
         for (final Geodetic2D coordinate : holeCoordinates) {
            verticesCursor = addVextex(roofVertices, verticesCursor, coordinate, _upperHeight);
         }
      }

      return new Triangulation.Data(numContures, numVerticesInContures, roofVertices);
   }


   private static int addVextex(final double[][] roofVertices,
                                final int verticesCursor,
                                final Geodetic2D coordinate,
                                final double height) {
      roofVertices[verticesCursor][0] = coordinate._longitude._degrees;
      roofVertices[verticesCursor][1] = coordinate._latitude._degrees;
      roofVertices[verticesCursor][2] = height;
      return verticesCursor + 1;
   }


   //   @Override
   //   protected Geodetic2D createPosition() {
   //      Geodetic2D centroid = Geodetic2D.fromDegrees(0, 0);
   //
   //      final int imax = _coordinates.size() - 1;
   //
   //      double area = 0;
   //      for (int i = 0; i < imax; ++i) {
   //         final Geodetic2D currentPoint = _coordinates.get(i);
   //         final Geodetic2D nextPoint = _coordinates.get(i + 1);
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
   //      return centroid;
   //   }


   @Override
   protected Geodetic2D calculateAverage() {
      double totalLatRad = 0;
      double totalLonRad = 0;
      for (final Geodetic2D coordinate : _coordinates) {
         totalLatRad += coordinate._latitude._radians;
         totalLonRad += coordinate._longitude._radians;
      }
      return Geodetic2D.fromRadians(totalLatRad / _coordinates.size(), totalLonRad / _coordinates.size());
   }


   @Override
   public void drawOn(final GEOBitmap bitmap,
                      final Color fillColor,
                      final Color borderColor) {
      bitmap.drawPolygon(_coordinates, _holesCoordinatesArray, fillColor, borderColor, false, null);
   }


}
