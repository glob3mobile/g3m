

package com.glob3mobile.tools.extruder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.glob3.mobile.generated.Geodetic2D;

import poly2Tri.Triangle;

import com.glob3mobile.tools.mesh.G3MeshMaterial;


public class Building {
   public final List<Triangle> _ceilingTriangles;
   public final double[][]     _ceilingVertices;

   public final double         _lowerHeight;
   public final double         _upperHeight;
   public final Wall           _exteriorWall;
   public final List<Wall>     _interiorWalls;
   public final G3MeshMaterial _material;
   public final boolean        _depthTest;


   Building(final List<Triangle> ceilingTriangles,
            final double[][] ceilingVertices,
            final double lowerHeight,
            final double upperHeight,
            final ExtruderPolygon extruderPolygon,
            final G3MeshMaterial material,
            final boolean depthTest) {
      _material = material;
      _depthTest = depthTest;

      _ceilingTriangles = ceilingTriangles;
      _ceilingVertices = ceilingVertices;
      _lowerHeight = lowerHeight;
      _upperHeight = upperHeight;

      _exteriorWall = createExteriorWall(extruderPolygon._coordinates, lowerHeight, upperHeight);
      _interiorWalls = createInteriorWalls(extruderPolygon._holesCoordinatesArray, lowerHeight, upperHeight);
   }


   private Wall createExteriorWall(final List<Geodetic2D> coordinates,
                                   final double lowerHeight,
                                   final double upperHeight) {
      final List<WallQuad> wallQuads = new ArrayList<WallQuad>(coordinates.size());

      Geodetic2D previousCoordinate = coordinates.get(coordinates.size() - 1);
      for (final Geodetic2D coordinate : coordinates) {
         final WallQuad quad = new WallQuad(previousCoordinate, coordinate, lowerHeight, upperHeight);
         wallQuads.add(quad);

         previousCoordinate = coordinate;
      }

      return new Wall(wallQuads);
   }


   private Wall createInteriorWall(final List<Geodetic2D> coordinates,
                                   final double lowerHeight,
                                   final double upperHeight) {
      final ArrayList<Geodetic2D> reversed = new ArrayList<>(coordinates);
      Collections.reverse(reversed);
      return createExteriorWall(reversed, lowerHeight, upperHeight);
   }


   private List<Wall> createInteriorWalls(final List<List<Geodetic2D>> holesCoordinatesArray,
                                          final double lowerHeight,
                                          final double upperHeight) {
      final List<Wall> result = new ArrayList<>(holesCoordinatesArray.size());
      for (final List<Geodetic2D> holeCoordinates : holesCoordinatesArray) {
         result.add(createInteriorWall(holeCoordinates, lowerHeight, upperHeight));
      }
      return result;
   }


}
