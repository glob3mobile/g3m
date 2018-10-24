

package com.glob3mobile.tools.extruder;

import java.util.ArrayList;
import java.util.List;

import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.Vector3D;
import org.glob3.mobile.generated.Vector3F;

import com.glob3mobile.tools.mesh.G3Mesh;
import com.glob3mobile.tools.mesh.G3MeshMaterial;

import poly2Tri.Triangle;


public class Building {

   private final List<Triangle> _ceilingTriangles;
   private final double[][]     _ceilingVertices;
   private final Wall           _exteriorWall;
   private final List<Wall>     _interiorWalls;
   private final G3MeshMaterial _material;
   private final boolean        _depthTest;


   Building(final List<Triangle> ceilingTriangles,
            final double[][] ceilingVertices,
            final Wall exteriorWall,
            final List<Wall> interiorWalls,
            final G3MeshMaterial material,
            final boolean depthTest) {
      _ceilingTriangles = ceilingTriangles;
      _ceilingVertices = ceilingVertices;
      _exteriorWall = exteriorWall;
      _interiorWalls = interiorWalls;
      _material = material;
      _depthTest = depthTest;
   }


   public G3Mesh createMesh(final int floatPrecision) {
      final double wallsLowerHeight = getWallsLowerHeight(_exteriorWall, _interiorWalls);
      final Vector3D center = getCenter(floatPrecision, wallsLowerHeight);

      final List<Vector3F> vertices = new ArrayList<>(_ceilingVertices.length);
      for (final double[] vertex : _ceilingVertices) {
         final double x = vertex[0];
         final double y = vertex[1];
         final double z = vertex[2];
         addVertex(vertices, center, x, y, z);
      }

      final List<Short> indices = new ArrayList<>();
      for (final Triangle triangle : _ceilingTriangles) {
         indices.add(toShort(triangle._vertex0));
         indices.add(toShort(triangle._vertex1));
         indices.add(toShort(triangle._vertex2));
      }

      final int lastCeilingVertexIndex = vertices.size() - 1;

      processWall(vertices, indices, center, _exteriorWall);
      for (final Wall wall : _interiorWalls) {
         processWall(vertices, indices, center, wall);
      }

      final List<Vector3F> normals = createNormals(vertices, indices, lastCeilingVertexIndex);

      return G3Mesh.createTrianglesMesh( //
               G3Mesh.VerticesFormat.GEODETIC, //
               center, //
               vertices, //
               normals, //
               null, // colors
               null, // texCoords
               indices, //
               _material, //
               _depthTest //
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


   private Vector3D getCenter(final int floatPrecision,
                              final double lowerHeight) {
      double minX = Double.POSITIVE_INFINITY;
      double minY = Double.POSITIVE_INFINITY;
      double minZ = lowerHeight;
      double maxX = Double.NEGATIVE_INFINITY;
      double maxY = Double.NEGATIVE_INFINITY;
      double maxZ = Double.NEGATIVE_INFINITY;

      for (final double[] vertex : _ceilingVertices) {
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

      final double centerX = (maxX + minX) / 2;
      final double centerY = (maxY + minY) / 2;
      final double centerZ = (maxZ + minZ) / 2;

      final double factor = Math.pow(10, floatPrecision);

      return new Vector3D(round(centerX, factor), round(centerY, factor), round(centerZ, factor));
   }


   private static double round(final double value,
                               final double factor) {
      final long i = Math.round(value * factor);
      return i / factor;
   }


   private static void addVertex(final List<Vector3F> vertices,
                                 final Vector3D center,
                                 final double x,
                                 final double y,
                                 final double z) {
      vertices.add(new Vector3F( //
               (float) (x - center._x), //
               (float) (y - center._y), //
               (float) (z - center._z)));
   }


   private static short toShort(final int index) {
      if ((index >= Short.MIN_VALUE) || (index <= Short.MAX_VALUE)) {
         return (short) index;
      }
      throw new RuntimeException("Invalid range for index #" + index);
   }


   private static void processWall(final List<Vector3F> vertices,
                                   final List<Short> indices,
                                   final Vector3D center,
                                   final Wall wall) {
      for (final WallQuad quad : wall._quads) {
         final Geodetic3D topCorner0 = quad._coordinate0;
         final Geodetic3D topCorner1 = quad._coordinate1;
         final double lowerHeight = quad._lowerHeight;

         final int firstVertexIndex = vertices.size();
         addVertex(vertices, center, topCorner0._longitude._degrees, topCorner0._latitude._degrees, topCorner0._height);
         addVertex(vertices, center, topCorner0._longitude._degrees, topCorner0._latitude._degrees, lowerHeight);
         addVertex(vertices, center, topCorner1._longitude._degrees, topCorner1._latitude._degrees, lowerHeight);
         addVertex(vertices, center, topCorner1._longitude._degrees, topCorner1._latitude._degrees, topCorner1._height);

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
      final ArrayList<Vector3F> result = new ArrayList<>(verticesSize);
      for (int i = 0; i < verticesSize; i++) {
         if (i <= lastCeilingVertexIndex) {
            result.add(new Vector3F(1, 0, 0));
         }
         else {
            result.add(null);
         }
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

         if (normal.isNan()) {
            setNormal(result, index0, Vector3F.zero());
            setNormal(result, index1, Vector3F.zero());
            setNormal(result, index2, Vector3F.zero());
         }
         else {
            setNormal(result, index0, normal);
            setNormal(result, index1, normal);
            setNormal(result, index2, normal);
         }
      }

      return result;
   }


   private static void setNormal(final ArrayList<Vector3F> normals,
                                 final short index,
                                 final Vector3F normal) {
      final Vector3F currentNormal = normals.get(index);
      if ((currentNormal == null) || currentNormal.isZero() || currentNormal.isNan()) {
         normals.set(index, normal);
      }
   }


}
