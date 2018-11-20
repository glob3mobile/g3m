

package com.glob3mobile.tools.extruder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.Vector3D;
import org.glob3.mobile.generated.Vector3F;

import com.glob3mobile.tools.mesh.G3Mesh;
import com.glob3mobile.tools.mesh.G3MeshMaterial;

import poly2Tri.Triangle;


public class Building {

   private final ExtruderPolygon _extruderPolygon;
   private final Geodetic2D      _position;
   private final double          _minHeight;
   private final List<Vector3D>  _roofVertices;
   private final List<Triangle>  _roofTriangles;
   private final Wall            _exteriorWall;
   private final List<Wall>      _interiorWalls;
   private final G3MeshMaterial  _material;


   Building(final ExtruderPolygon extruderPolygon,
            final Geodetic2D position,
            final double minHeight,
            final List<Vector3D> roofVertices,
            final List<Triangle> roofTriangles,
            final Wall exteriorWall,
            final List<Wall> interiorWalls,
            final G3MeshMaterial material) {
      _extruderPolygon = extruderPolygon;
      _position = position;
      _minHeight = minHeight;
      _roofVertices = roofVertices;
      _roofTriangles = consolidate(roofTriangles, roofVertices);
      _exteriorWall = exteriorWall;
      _interiorWalls = interiorWalls;
      _material = material;
   }


   private double calculateSize(final double minHeight) {
      double area = 0;
      double maxHeight = Double.NEGATIVE_INFINITY;
      //double sumHeight = 0;
      for (final Triangle triangle : _roofTriangles) {
         final Vector3D v0 = _roofVertices.get(triangle._vertex0);
         final Vector3D v1 = _roofVertices.get(triangle._vertex1);
         final Vector3D v2 = _roofVertices.get(triangle._vertex2);
         area += Math.abs(triangleArea(v0, v1, v2));

         maxHeight = max(maxHeight, v0._z, v1._z, v2._z);
      }

      if (maxHeight == Double.NEGATIVE_INFINITY) {
         throw new RuntimeException("Oops!");
      }

      if (minHeight > maxHeight) {
         throw new RuntimeException("Oops!");
      }


      final double height = Math.max((maxHeight - minHeight) + 1, 1);
      return area * height;
   }


   private static double max(final double v,
                             final double... d) {
      double max = v;
      for (final double e : d) {
         if (e > max) {
            max = e;
         }
      }
      return max;
   }


   private double triangleArea(final Vector3D v0,
                               final Vector3D v1,
                               final Vector3D v2) {
      return (v1.sub(v0).cross(v2.sub(v0))).length() / 2;
   }


   public ExtruderPolygon getExtruderPolygon() {
      return _extruderPolygon;
   }


   public Geodetic2D getPosition() {
      return _position;
   }


   private static List<Triangle> consolidate(final List<Triangle> triangles,
                                             final List<Vector3D> roofVertices) {
      final List<Triangle> result = new ArrayList<>(triangles.size());

      for (final Triangle triangle : triangles) {
         if (pointsToSky(triangle, roofVertices)) {
            result.add(triangle);
         }
         else {
            result.add(triangle.flipped());
         }
      }

      return result;
   }


   private static boolean pointsToSky(final Triangle triangle,
                                      final List<Vector3D> vertices) {
      final Vector3D vertex0 = vertices.get(triangle._vertex0);
      final Vector3D vertex1 = vertices.get(triangle._vertex1);
      final Vector3D vertex2 = vertices.get(triangle._vertex2);

      final Vector3D v10 = vertex1.sub(vertex0);
      final Vector3D v20 = vertex2.sub(vertex0);
      final Vector3D normal = v10.cross(v20).normalized();
      if (normal.isEquals(Vector3D.DOWN_Z)) {
         return false;
      }
      if (normal.isEquals(Vector3D.UP_Z)) {
         return true;
      }

      final Angle angleToUp = normal.angleBetween(Vector3D.UP_Z);
      final Angle angleToDown = normal.angleBetween(Vector3D.DOWN_Z);
      final boolean pointsToSkye = !angleToDown.lowerThan(angleToUp);
      return pointsToSkye;
   }


   public G3Mesh createMesh(final Planet planet,
                            final float verticalExaggeration,
                            final double deltaHeight,
                            final int floatPrecision) {
      return createMesh( //
               _roofVertices, //
               _roofTriangles, //
               _exteriorWall, //
               _interiorWalls, //
               _material, //
               planet, //
               verticalExaggeration, //
               deltaHeight, floatPrecision);
   }


   public static G3Mesh createMesh(final List<Vector3D> roofVertices,
                                   final List<Triangle> roofTriangles,
                                   final Wall exteriorWall,
                                   final List<Wall> interiorWalls,
                                   final G3MeshMaterial material,
                                   final Planet planet,
                                   final float verticalExaggeration,
                                   final double deltaHeight,
                                   final int floatPrecision) {

      final double wallsLowerHeight = getWallsLowerHeight(exteriorWall, interiorWalls);

      final Vector3D center = getCenter(roofVertices, planet, verticalExaggeration, deltaHeight, floatPrecision,
               wallsLowerHeight);

      final List<Vector3F> vertices = new ArrayList<>(roofVertices.size());
      for (final Vector3D vertex : roofVertices) {
         addVertex(planet, verticalExaggeration, deltaHeight, vertices, center, vertex._x, vertex._y, vertex._z);
      }

      final List<Short> indices = new ArrayList<>();
      for (final Triangle triangle : roofTriangles) {
         indices.add(toShort(triangle._vertex0));
         indices.add(toShort(triangle._vertex1));
         indices.add(toShort(triangle._vertex2));
      }

      //      final int lastCeilingVertexIndex = vertices.size() - 1; // get the indes of the last roof vertex before creating the walls
      {
         processWall(planet, verticalExaggeration, deltaHeight, vertices, indices, center, exteriorWall);
         for (final Wall wall : interiorWalls) {
            processWall(planet, verticalExaggeration, deltaHeight, vertices, indices, center, wall);
         }
      }

      final List<Vector3F> normals = createNormals(planet, vertices, indices, roofVertices);

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
               material //
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


   private static Vector3D getCenter(final List<Vector3D> roofVertices,
                                     final Planet planet,
                                     final float verticalExaggeration,
                                     final double deltaHeight,
                                     final int floatPrecision,
                                     final double lowerHeight) {
      double minX = Double.POSITIVE_INFINITY;
      double minY = Double.POSITIVE_INFINITY;
      double minZ = lowerHeight;
      double maxX = Double.NEGATIVE_INFINITY;
      double maxY = Double.NEGATIVE_INFINITY;
      double maxZ = Double.NEGATIVE_INFINITY;

      for (final Vector3D vertex : roofVertices) {
         final double x = vertex._x;
         final double y = vertex._y;
         final double z = vertex._z;
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
      final Vector3D center = createCenter(planet, verticalExaggeration, deltaHeight, x, y, z);

      final double factor = Math.pow(10, floatPrecision);
      return new Vector3D(round(center._x, factor), round(center._y, factor), round(center._z, factor));
   }


   private static Vector3D createCenter(final Planet planet,
                                        final float verticalExaggeration,
                                        final double deltaHeight,
                                        final double x,
                                        final double y,
                                        final double z) {
      return (planet == null) ? new Vector3D(x, y, z) : planet.toCartesian(Angle.fromDegrees(y), Angle.fromDegrees(x),
               (z * verticalExaggeration) + deltaHeight);
   }


   private static double round(final double value,
                               final double factor) {
      final long i = Math.round(value * factor);
      return i / factor;
   }


   private static void addVertex(final Planet planet,
                                 final float verticalExaggeration,
                                 final double deltaHeight,
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
                  (float) ((z + deltaHeight) - (float) center._z));
      }
      else {
         final Vector3D projected = planet.toCartesian(Angle.fromDegrees(y), Angle.fromDegrees(x),
                  (z * verticalExaggeration) + deltaHeight);
         vertex = new Vector3F( //
                  (float) (projected._x - (float) center._x), //
                  (float) (projected._y - (float) center._y), //
                  (float) (projected._z - (float) center._z));
      }
      vertices.add(vertex);
   }


   private static short toShort(final int index) {
      if ((index >= 0) && (index <= Short.MAX_VALUE)) {
         return (short) index;
      }
      throw new RuntimeException("Invalid range for index #" + index);
   }


   private static void processWall(final Planet planet,
                                   final float verticalExaggeration,
                                   final double deltaHeight,
                                   final List<Vector3F> vertices,
                                   final List<Short> indices,
                                   final Vector3D center,
                                   final Wall wall) {
      for (final WallQuad quad : wall._quads) {
         final Geodetic3D topCorner0 = quad._topCorner0;
         final Geodetic3D topCorner1 = quad._topCorner1;
         final double lowerHeight = quad._lowerHeight;

         final int firstVertexIndex = vertices.size();
         addVertex(planet, verticalExaggeration, deltaHeight, vertices, center, topCorner0._longitude._degrees,
                  topCorner0._latitude._degrees, topCorner0._height);
         addVertex(planet, verticalExaggeration, deltaHeight, vertices, center, topCorner0._longitude._degrees,
                  topCorner0._latitude._degrees, lowerHeight);
         addVertex(planet, verticalExaggeration, deltaHeight, vertices, center, topCorner1._longitude._degrees,
                  topCorner1._latitude._degrees, lowerHeight);
         addVertex(planet, verticalExaggeration, deltaHeight, vertices, center, topCorner1._longitude._degrees,
                  topCorner1._latitude._degrees, topCorner1._height);

         indices.add(toShort(firstVertexIndex + 0));
         indices.add(toShort(firstVertexIndex + 1));
         indices.add(toShort(firstVertexIndex + 2));

         indices.add(toShort(firstVertexIndex + 0));
         indices.add(toShort(firstVertexIndex + 2));
         indices.add(toShort(firstVertexIndex + 3));
      }
   }


   private static List<Vector3F> createNormals(final Planet planet,
                                               final List<Vector3F> vertices,
                                               final List<Short> indices,
                                               final List<Vector3D> roofVertices) {
      final int verticesSize = vertices.size();
      final List<List<Vector3F>> allNormals = new ArrayList<>(verticesSize);
      for (int i = 0; i < verticesSize; i++) {
         final List<Vector3F> vertexNormal = new ArrayList<>();
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
      for (int i = 0; i < allNormals.size(); i++) {
         final List<Vector3F> currentNormals = allNormals.get(i);
         final Vector3D originalVertex = i < roofVertices.size() ? roofVertices.get(i) : null;
         final Vector3F smoothedNormal = smoothNormals(planet, originalVertex, currentNormals);
         if (smoothedNormal.isNan()) {
            throw new RuntimeException();
         }
         result.add(smoothedNormal);
      }

      return result;
   }


   private static Vector3F smoothNormals(final Planet planet,
                                         final Vector3D originalVertex,
                                         final List<Vector3F> normals) {
      if ((normals == null) || normals.isEmpty()) {
         return normalAt(planet, originalVertex);
      }
      Vector3F acum = Vector3F.zero();
      for (final Vector3F normal : normals) {
         acum = acum.add(normal);
      }
      if (acum.isZero()) {
         return normals.get(0);
      }
      return acum.div(normals.size()).normalized();
   }


   private static Vector3F normalAt(final Planet planet,
                                    final Vector3D originalVertex) {
      if ((planet == null) || (originalVertex == null)) {
         return new Vector3F(0, 0, 1);
      }
      final Vector3D normal = planet.geodeticSurfaceNormal(Geodetic2D.fromDegrees(originalVertex._y, originalVertex._x));
      return new Vector3F((float) normal._x, (float) normal._y, (float) normal._z);
   }


   private static void addNormal(final List<List<Vector3F>> normals,
                                 final int index,
                                 final Vector3F normal) {
      if ((normal != null) && !normal.isZero() && !normal.isNan()) {
         normals.get(index).add(normal);
      }
   }


   public Map<String, Object> createFeatureProperties(final float priority) {
      final Map<String, Object> result = new LinkedHashMap<>();

      result.put("roof_vertices", ExtruderJSON.verticesToJSON(_roofVertices));
      result.put("roof_triangles", ExtruderJSON.trianglesToJSON(_roofTriangles));

      result.put("exterior_wall", ExtruderJSON.wallToJSON(_exteriorWall));
      result.put("interior_walls", ExtruderJSON.wallsToJSON(_interiorWalls));

      result.put("material", ExtruderJSON.materialToJSON(_material));

      result.put("size", calculateSize(_minHeight));

      result.put("min_height", _minHeight);
      result.put("max_height", calculateMaxHeight());

      if (!Float.isNaN(priority)) {
         result.put("priority", priority);
      }

      return result;
   }


   private double calculateMaxHeight() {
      double max = _roofVertices.get(0)._z;
      for (int i = 1; i < _roofVertices.size(); i++) {
         final Vector3D vertex = _roofVertices.get(i);
         if (vertex._z > max) {
            max = vertex._z;
         }
      }
      return max;
   }


}
