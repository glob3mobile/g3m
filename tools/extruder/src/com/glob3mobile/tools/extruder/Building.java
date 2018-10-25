

package com.glob3mobile.tools.extruder;

import java.util.ArrayList;
import java.util.List;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.GEOFeature;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.Vector3D;
import org.glob3.mobile.generated.Vector3F;

import com.glob3mobile.tools.mesh.G3Mesh;
import com.glob3mobile.tools.mesh.G3MeshMaterial;

import poly2Tri.Triangle;


public class Building {

   private final GEOFeature     _geoFeature;
   private final List<Triangle> _roofTriangles;
   private final double[][]     _roofVertices;
   private final Wall           _exteriorWall;
   private final List<Wall>     _interiorWalls;
   private final G3MeshMaterial _material;


   Building(final GEOFeature geoFeature,
            final List<Triangle> roofTriangles,
            final double[][] roofVertices,
            final Wall exteriorWall,
            final List<Wall> interiorWalls,
            final G3MeshMaterial material) {
      _geoFeature = geoFeature;
      _roofTriangles = roofTriangles;
      _roofVertices = roofVertices;
      _exteriorWall = exteriorWall;
      _interiorWalls = interiorWalls;
      _material = material;
   }


   public G3Mesh createMesh(final Planet planet,
                            final int floatPrecision) {
      final double wallsLowerHeight = getWallsLowerHeight(_exteriorWall, _interiorWalls);

      final Vector3D center = getCenter(planet, floatPrecision, wallsLowerHeight);

      final List<Vector3F> vertices = new ArrayList<>(_roofVertices.length);
      for (final double[] vertex : _roofVertices) {
         final double x = vertex[0];
         final double y = vertex[1];
         final double z = vertex[2];
         addVertex(planet, vertices, center, x, y, z);
      }

      final List<Short> indices = new ArrayList<>();
      for (final Triangle triangle : _roofTriangles) {
         indices.add(toShort(triangle._vertex0));
         indices.add(toShort(triangle._vertex1));
         indices.add(toShort(triangle._vertex2));
      }

      final int lastCeilingVertexIndex = vertices.size() - 1; // get the indes of the last roof vertex before creating the walls
      {
         processWall(planet, vertices, indices, center, _exteriorWall);
         for (final Wall wall : _interiorWalls) {
            processWall(planet, vertices, indices, center, wall);
         }
      }

      final List<Vector3F> normals = createNormals(vertices, indices, lastCeilingVertexIndex);

      final G3Mesh.VerticesFormat verticesFormat = (planet == null) ? G3Mesh.VerticesFormat.GEODETIC
                                                                    : G3Mesh.VerticesFormat.CARTESIAN;


      return G3Mesh.createTrianglesMesh( //
               verticesFormat, //
               center, //
               vertices, //
               normals, //
               null, // colors
               null, // texCoords
               indices, //
               _material, //
               _material._depthTest //
      );

   }


   private static double getWallsLowerHeight(final Wall exteriorWall,
                                             final List<Wall> interiorWalls) {
      double result = getWallLowerHeight(exteriorWall);
      for (final Wall wall : interiorWalls) {
         result = Math.min(result, getWallLowerHeight(wall));
      }
      return result;
   }


   private static double getWallLowerHeight(final Wall wall) {
      double result = Double.POSITIVE_INFINITY;
      for (final WallQuad quad : wall._quads) {
         result = Math.min(result, quad._lowerHeight);
      }
      return result;
   }


   private Vector3D getCenter(final Planet planet,
                              final int floatPrecision,
                              final double lowerHeight) {
      double minX = Double.POSITIVE_INFINITY;
      double minY = Double.POSITIVE_INFINITY;
      double minZ = lowerHeight;
      double maxX = Double.NEGATIVE_INFINITY;
      double maxY = Double.NEGATIVE_INFINITY;
      double maxZ = Double.NEGATIVE_INFINITY;

      for (final double[] vertex : _roofVertices) {
         final double x = vertex[0];
         final double y = vertex[1];
         final double z = vertex[2];
         if (x < minX) {
            minX = x;
         }
         if (x > maxX) {
            maxX = x;
         }
         if (y < minY) {
            minY = y;
         }
         if (y > maxY) {
            maxY = y;
         }
         if (z < minZ) {
            minZ = z;
         }
         if (z > maxZ) {
            maxZ = z;
         }
      }

      final double x = (maxX + minX) / 2;
      final double y = (maxY + minY) / 2;
      final double z = (maxZ + minZ) / 2;
      final Vector3D center = createCenter(planet, x, y, z);

      final double factor = Math.pow(10, floatPrecision);
      return new Vector3D(round(center._x, factor), round(center._y, factor), round(center._z, factor));
   }


   private Vector3D createCenter(final Planet planet,
                                 final double x,
                                 final double y,
                                 final double z) {
      return (planet == null) ? new Vector3D(x, y, z) : planet.toCartesian(Angle.fromDegrees(y), Angle.fromDegrees(x), z);
   }


   private static double round(final double value,
                               final double factor) {
      final long i = Math.round(value * factor);
      return i / factor;
   }


   private static void addVertex(final Planet planet,
                                 final List<Vector3F> vertices,
                                 final Vector3D center,
                                 final double x,
                                 final double y,
                                 final double z) {
      final Vector3F vertex;
      if (planet == null) {
         vertex = new Vector3F( //
                  (float) (x - (float) center._x), //
                  (float) (y - (float) center._y), //
                  (float) (z - (float) center._z));
      }
      else {
         final Vector3D projected = planet.toCartesian(Angle.fromDegrees(y), Angle.fromDegrees(x), z);
         vertex = new Vector3F( //
                  (float) (projected._x - (float) center._x), //
                  (float) (projected._y - (float) center._y), //
                  (float) (projected._z - (float) center._z));
      }
      vertices.add(vertex);
   }


   private static short toShort(final int index) {
      if ((index >= Short.MIN_VALUE) || (index <= Short.MAX_VALUE)) {
         return (short) index;
      }
      throw new RuntimeException("Invalid range for index #" + index);
   }


   private static void processWall(final Planet planet,
                                   final List<Vector3F> vertices,
                                   final List<Short> indices,
                                   final Vector3D center,
                                   final Wall wall) {
      for (final WallQuad quad : wall._quads) {
         final Geodetic3D topCorner0 = quad._topCorner0;
         final Geodetic3D topCorner1 = quad._topCorner1;
         final double lowerHeight = quad._lowerHeight;

         final int firstVertexIndex = vertices.size();
         addVertex(planet, vertices, center, topCorner0._longitude._degrees, topCorner0._latitude._degrees, topCorner0._height);
         addVertex(planet, vertices, center, topCorner0._longitude._degrees, topCorner0._latitude._degrees, lowerHeight);
         addVertex(planet, vertices, center, topCorner1._longitude._degrees, topCorner1._latitude._degrees, lowerHeight);
         addVertex(planet, vertices, center, topCorner1._longitude._degrees, topCorner1._latitude._degrees, topCorner1._height);

         indices.add(toShort(firstVertexIndex + 0));
         indices.add(toShort(firstVertexIndex + 1));
         indices.add(toShort(firstVertexIndex + 2));

         indices.add(toShort(firstVertexIndex + 0));
         indices.add(toShort(firstVertexIndex + 2));
         indices.add(toShort(firstVertexIndex + 3));
      }
   }


   private static List<Vector3F> createNormals(final List<Vector3F> vertices,
                                               final List<Short> indices,
                                               final int lastCeilingVertexIndex) {
      final int verticesSize = vertices.size();
      final List<List<Vector3F>> allNormals = new ArrayList<>(verticesSize);
      for (int i = 0; i < verticesSize; i++) {
         final List<Vector3F> vertexNormal = new ArrayList<>();
         //         if (i <= lastCeilingVertexIndex) {
         //            vertexNormal.add(new Vector3F(1, 0, 0));
         //         }
         allNormals.add(vertexNormal);
      }

      final int indicesSize = indices.size();
      for (int i = 0; i < indicesSize; i += 3) {
         final short index0 = indices.get(i + 0);
         final short index1 = indices.get(i + 1);
         final short index2 = indices.get(i + 2);

         final Vector3F vertex0 = vertices.get(index0);
         final Vector3F vertex1 = vertices.get(index1);
         final Vector3F vertex2 = vertices.get(index2);

         final Vector3F v10 = vertex1.sub(vertex0);
         final Vector3F v20 = vertex2.sub(vertex0);
         final Vector3F normal = v10.cross(v20).normalized();

         addNormal(allNormals, index0, normal);
         addNormal(allNormals, index1, normal);
         addNormal(allNormals, index2, normal);
      }

      final List<Vector3F> result = new ArrayList<>(allNormals.size());
      for (final List<Vector3F> currentNormals : allNormals) {
         result.add(smoothNormals(currentNormals));
      }
      //      for (int i = 0; i < result.size(); i++) {
      //         final Vector3F currentNormal = result.get(i);
      //         final boolean notHasNormal = (currentNormal == null) || currentNormal.isZero() || currentNormal.isNan();
      //         if (notHasNormal) {
      //            result.set(i, new Vector3F(1, 0, 0));
      //         }
      //      }

      return result;
   }


   private static Vector3F smoothNormals(final List<Vector3F> normals) {
      if ((normals == null) || normals.isEmpty()) {
         return new Vector3F(1, 0, 0);
      }
      Vector3F acum = Vector3F.zero();
      for (final Vector3F normal : normals) {
         acum = acum.add(normal);
      }
      return acum.div(normals.size());
   }


   private static void addNormal(final List<List<Vector3F>> allNormals,
                                 final int index,
                                 final Vector3F normal) {
      if ((normal != null) && !normal.isZero() && !normal.isNan()) {
         final List<Vector3F> currentNormals = allNormals.get(index);
         currentNormals.add(normal);
         //         final boolean notHasNormal = (currentNormal == null) || currentNormal.isZero() || currentNormal.isNan();
         //         final Vector3F updatedNormal = notHasNormal ? normal : currentNormal.add(normal).div(2);
         //         normals.set(index, updatedNormal);
      }
   }


}
