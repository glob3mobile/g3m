

package com.glob3mobile.tools.mesh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.Vector2F;
import org.glob3.mobile.generated.Vector3D;
import org.glob3.mobile.generated.Vector3F;


public class G3Mesh {


   public static enum Primitive {
      TRIANGLES("Triangles"),
      TRIANGLE_STRIP("TriangleStrip"),
      TRIANGLE_FAN("TriangleFan"),
      LINES("Lines"),
      LINE_STRIP("LineStrip"),
      LINE_LOOP("LineLoop"),
      POINTS("Points");

      private final String _name;


      private Primitive(final String name) {
         _name = name;
      }
   }


   public static enum VerticesFormat {
      CARTESIAN("Cartesian"),
      GEODETIC("Geodetic");

      private final String _name;


      private VerticesFormat(final String name) {
         _name = name;
      }
   }


   public static G3Mesh createTrianglesMesh(final VerticesFormat verticesFormat,
                                            final Vector3D center,
                                            final List<Vector3F> vertices,
                                            final List<Vector3F> normals,
                                            final List<Color> colors,
                                            final List<Vector2F> texCoords,
                                            final List<Short> indices,
                                            final G3MeshMaterial material) {
      return new G3Mesh( //
               Primitive.TRIANGLES, //
               1, // pointSize
               1, // lineWidth
               verticesFormat, //
               center, //
               vertices, //
               normals, //
               colors, //
               texCoords, //
               indices, //
               material //
      );

   }


   public static List<G3Mesh> consolidate(final List<G3Mesh> meshes) {
      if ((meshes == null) || meshes.isEmpty()) {
         return Collections.emptyList();
      }

      final G3Mesh first = meshes.get(0);

      if (first._primitive != G3Mesh.Primitive.TRIANGLES) {
         throw new RuntimeException("Primitive not supported: " + first._primitive);
      }

      final List<G3Mesh> newMeshes = new ArrayList<>();

      final List<Vector3D> newVertices = new ArrayList<>();
      final List<Vector3F> newNormals = new ArrayList<>();
      final List<Color> newColors = new ArrayList<>();
      final List<Vector2F> newTexCoords = new ArrayList<>();
      final List<Short> newIndices = new ArrayList<>();

      double sumX = 0;
      double sumY = 0;
      double sumZ = 0;

      for (final G3Mesh mesh : meshes) {
         if (!mesh.isHomomorphic(first)) {
            throw new RuntimeException("Inconsistency");
         }

         int indicesOffset = newVertices.size();

         final boolean fitsMesh = fits(mesh, indicesOffset);
         if (!fitsMesh) {
            final int newVerticesSize = newVertices.size();
            if (newVerticesSize > 0) {
               final Vector3D center = new Vector3D(sumX / newVerticesSize, sumY / newVerticesSize, sumZ / newVerticesSize);
               final G3Mesh newMesh = new G3Mesh( //
                        first._primitive, //
                        first._pointSize, //
                        first._lineWidth, //
                        first._verticesFormat, //
                        center, //
                        convert(center, newVertices), //
                        newNormals.isEmpty() ? null : newNormals, //
                        newColors.isEmpty() ? null : newColors, //
                        newTexCoords.isEmpty() ? null : newTexCoords, //
                        newIndices, //
                        first._material);
               newMeshes.add(newMesh);
            }

            sumX = 0;
            sumY = 0;
            sumZ = 0;
            newVertices.clear();
            newNormals.clear();
            newColors.clear();
            newTexCoords.clear();
            newIndices.clear();
            indicesOffset = 0;
         }

         final Vector3D center = mesh._center;
         final List<Vector3F> vertices = mesh._vertices;
         for (int i = 0; i < vertices.size(); i++) {
            final Vector3F vertex = vertices.get(i);
            final double x = center._x + vertex._x;
            final double y = center._y + vertex._y;
            final double z = center._z + vertex._z;

            sumX += x;
            sumY += y;
            sumZ += z;

            newVertices.add(new Vector3D(x, y, z));
         }

         if (mesh._normals != null) {
            newNormals.addAll(mesh._normals);
         }
         if (mesh._colors != null) {
            newColors.addAll(mesh._colors);
         }
         if (mesh._texCoords != null) {
            newTexCoords.addAll(mesh._texCoords);
         }

         for (final short index : mesh._indices) {
            newIndices.add(toShort(indicesOffset + index));
         }

      }


      final int newVerticesSize = newVertices.size();
      if (newVerticesSize > 0) {
         final Vector3D center = new Vector3D(sumX / newVerticesSize, sumY / newVerticesSize, sumZ / newVerticesSize);
         final G3Mesh newMesh = new G3Mesh( //
                  first._primitive, //
                  first._pointSize, //
                  first._lineWidth, //
                  first._verticesFormat, //
                  center, //
                  convert(center, newVertices), //
                  newNormals.isEmpty() ? null : newNormals, //
                  newColors.isEmpty() ? null : newColors, //
                  newTexCoords.isEmpty() ? null : newTexCoords, //
                  newIndices, //
                  first._material);
         newMeshes.add(newMesh);
      }


      return newMeshes;
   }


   private static boolean fits(final G3Mesh mesh,
                               final int indicesOffset) {
      for (final short index : mesh._indices) {
         final int newIndex = indicesOffset + index;
         if (newIndex > Short.MAX_VALUE) {
            return false;
         }
      }
      return true;
   }


   private static List<Vector3F> convert(final Vector3D center,
                                         final List<Vector3D> verticesD) {
      final List<Vector3F> result = new ArrayList<>(verticesD.size());
      for (final Vector3D vertexD : verticesD) {
         final Vector3F vertexF = new Vector3F( //
                  (float) (vertexD._x - (float) center._x), //
                  (float) (vertexD._y - (float) center._y), //
                  (float) (vertexD._z - (float) center._z));
         result.add(vertexF);
      }
      return result;

   }


   private static short toShort(final int index) {
      if ((index >= 0) && (index <= Short.MAX_VALUE)) {
         return (short) index;
      }
      throw new RuntimeException("Invalid range for index #" + index);
   }


   private final G3Mesh.Primitive      _primitive;
   private final float                 _pointSize;
   private final float                 _lineWidth;
   private final G3Mesh.VerticesFormat _verticesFormat;
   private final Vector3D              _center;
   private final List<Vector3F>        _vertices;
   private final List<Vector3F>        _normals;
   private final List<Color>           _colors;
   private final List<Vector2F>        _texCoords;
   private final List<Short>           _indices;
   private final G3MeshMaterial        _material;


   private G3Mesh(final Primitive primitive,
                  final float pointSize,
                  final float lineWidth,
                  final VerticesFormat verticesFormat,
                  final Vector3D center,
                  final List<Vector3F> vertices,
                  final List<Vector3F> normals,
                  final List<Color> colors,
                  final List<Vector2F> texCoords,
                  final List<Short> indices,
                  final G3MeshMaterial material) {
      _primitive = primitive;
      _pointSize = pointSize;
      _lineWidth = lineWidth;
      _verticesFormat = verticesFormat;
      _center = center;
      _vertices = vertices;
      _normals = normals;
      _colors = colors;
      _texCoords = texCoords;
      _indices = indices;
      _material = material;

      validate();
   }


   private void validate() {
      final int verticesSize = _vertices.size();

      if (_primitive == Primitive.TRIANGLES) {
         if (_indices == null) {
            if ((verticesSize % 3) != 0) {
               throw new RuntimeException("TRIANGLES: vertices count is not multiple of 3 (" + verticesSize + ")");
            }
         }
         else {
            if ((_indices.size() % 3) != 0) {
               throw new RuntimeException("TRIANGLES: indices count is not multiple of 3 (" + _indices.size() + ")");
            }
         }
      }

      if ((_normals != null) && (_normals.size() != verticesSize)) {
         throw new RuntimeException("Normals doesn't match vertices size (" + verticesSize + ")");
      }

      if ((_colors != null) && (_colors.size() != verticesSize)) {
         throw new RuntimeException("Colors doesn't match vertices size (" + verticesSize + ")");
      }

      if ((_texCoords != null) && (_texCoords.size() != verticesSize)) {
         throw new RuntimeException("TexCoords doesn't match vertices size (" + verticesSize + ")");
      }

      if (_indices != null) {
         for (final short index : _indices) {
            if ((index < 0) || (index >= verticesSize)) {
               throw new RuntimeException("Invalid index " + index + " (" + verticesSize + ")");
            }
         }
      }
   }


   public G3MeshMaterial getMaterial() {
      return _material;
   }


   private static List<Float> toColorJSON(final List<Color> colors) {
      final List<Float> result = new ArrayList<>();
      for (final Color color : colors) {
         result.add(color._red);
         result.add(color._green);
         result.add(color._blue);
         result.add(color._alpha);
      }
      return result;
   }


   private static List<Double> toJSON(final Vector3D point) {
      final List<Double> result = new ArrayList<>();
      result.add(point._x);
      result.add(point._y);
      result.add(point._z);
      return result;
   }


   private static List<Float> toVector3FJSON(final List<Vector3F> vertices) {
      final List<Float> result = new ArrayList<>();
      for (final Vector3F vertex : vertices) {
         result.add(vertex._x);
         result.add(vertex._y);
         result.add(vertex._z);
      }
      return result;
   }


   private static List<Float> toVector2FJSON(final List<Vector2F> texCoords) {
      final List<Float> result = new ArrayList<>();
      for (final Vector2F vertex : texCoords) {
         result.add(vertex._x);
         result.add(vertex._y);
      }
      return result;
   }


   private static List<Short> toShortJSON(final List<Short> indices) {
      final List<Short> result = new ArrayList<>();
      for (final short index : indices) {
         result.add(index);
      }
      return result;
   }


   public Map<String, Object> toJSON() {
      final Map<String, Object> result = new LinkedHashMap<>();

      result.put("material", _material.getID());
      if (_primitive != G3Mesh.Primitive.TRIANGLES) {
         result.put("primitive", _primitive._name);
      }
      if (_pointSize != 1) {
         result.put("pointSize", _pointSize);
      }
      if (_lineWidth != 1) {
         result.put("lineWidth", _lineWidth);
      }
      final boolean depthTest = _material._depthTest;
      if (!depthTest) {
         result.put("depthTest", depthTest);
      }
      if (_verticesFormat != G3Mesh.VerticesFormat.CARTESIAN) {
         result.put("verticesFormat", _verticesFormat._name);
      }
      if ((_center != null) && !_center.isZero()) {
         result.put("center", toJSON(_center));
      }
      result.put("vertices", toVector3FJSON(_vertices));
      if (_normals != null) {
         result.put("normals", toVector3FJSON(_normals));
      }
      if (_colors != null) {
         result.put("colors", toColorJSON(_colors));
      }
      if (_texCoords != null) {
         result.put("texCoords", toVector2FJSON(_texCoords));
      }
      if (_indices != null) {
         result.put("indices", toShortJSON(_indices));
      }

      return result;
   }


   public boolean isHomomorphic(final G3Mesh that) {
      if (this == that) {
         return true;
      }

      if (that == null) {
         return false;
      }

      if (_primitive != that._primitive) {
         return false;
      }

      if (Float.floatToIntBits(_pointSize) != Float.floatToIntBits(that._pointSize)) {
         return false;
      }

      if (Float.floatToIntBits(_lineWidth) != Float.floatToIntBits(that._lineWidth)) {
         return false;
      }

      if (_verticesFormat != that._verticesFormat) {
         return false;
      }

      if (_material == null) {
         if (that._material != null) {
            return false;
         }
      }
      else if (!_material.getID().equals(that._material.getID())) {
         return false;
      }

      if ((_normals != null) && (that._normals == null)) {
         return false;
      }
      if ((_normals == null) && (that._normals != null)) {
         return false;
      }

      if ((_colors != null) && (that._colors == null)) {
         return false;
      }
      if ((_colors == null) && (that._colors != null)) {
         return false;
      }

      if ((_texCoords != null) && (that._texCoords == null)) {
         return false;
      }
      if ((_texCoords == null) && (that._texCoords != null)) {
         return false;
      }

      return true;
   }


}
