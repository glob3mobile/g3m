

package com.glob3mobile.tools.mesh;

import java.util.List;

import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.JSONArray;
import org.glob3.mobile.generated.JSONBaseObject;
import org.glob3.mobile.generated.JSONObject;
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
                                            final G3MeshMaterial material,
                                            final boolean depthTest) {
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
               material, //
               depthTest //
      );

   }


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
                  final G3MeshMaterial material,
                  final boolean depthTest) {
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
      _depthTest = depthTest;
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
   private final boolean               _depthTest;


   void validate() {
      final int verticesSize = _vertices.size();
      if ((_normals != null) && (_normals.size() != verticesSize)) {
         throw new RuntimeException("Normals doesn't match vertices size");
      }
      if ((_colors != null) && (_colors.size() != verticesSize)) {
         throw new RuntimeException("Colors doesn't match vertices size");
      }
      if ((_texCoords != null) && (_texCoords.size() != verticesSize)) {
         throw new RuntimeException("TexCoords doesn't match vertices size");
      }
      if (_indices != null) {
         for (final short index : _indices) {
            if ((index < 0) || (index >= verticesSize)) {
               throw new RuntimeException("Invalid index " + index);
            }
         }
      }

      _material.validate();
   }


   private static JSONBaseObject toColorJSON(final List<Color> colors) {
      final JSONArray result = new JSONArray();
      for (final Color color : colors) {
         result.add(color._red);
         result.add(color._green);
         result.add(color._blue);
         result.add(color._alpha);
      }
      return result;
   }


   private static JSONArray toJSON(final Vector3D point) {
      final JSONArray result = new JSONArray();
      result.add(point._x);
      result.add(point._y);
      result.add(point._z);
      return result;
   }


   private static JSONArray toVector3FJSON(final List<Vector3F> vertices) {
      final JSONArray result = new JSONArray();
      for (final Vector3F vertex : vertices) {
         result.add(vertex._x);
         result.add(vertex._y);
         result.add(vertex._z);
      }
      return result;
   }


   private static JSONArray toVector2FJSON(final List<Vector2F> texCoords) {
      final JSONArray result = new JSONArray();
      for (final Vector2F vertex : texCoords) {
         result.add(vertex._x);
         result.add(vertex._y);
      }
      return result;
   }


   private static JSONArray toShortJSON(final List<Short> indices) {
      final JSONArray result = new JSONArray();
      for (final short index : indices) {
         result.add(index);
      }
      return result;
   }


   public JSONObject toJSON() {
      validate();

      final JSONObject result = new JSONObject();

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
      if (!_depthTest) {
         result.put("depthTest", _depthTest);
      }
      if (_verticesFormat != G3Mesh.VerticesFormat.CARTESIAN) {
         result.put("verticesFormat", _verticesFormat._name);
      }
      if (!_center.isZero()) {
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


   public G3MeshMaterial getMaterial() {
      return _material;
   }


}
