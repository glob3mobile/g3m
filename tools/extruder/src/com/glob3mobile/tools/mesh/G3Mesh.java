

package com.glob3mobile.tools.mesh;

import java.util.ArrayList;
import java.util.List;

import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.JSONArray;
import org.glob3.mobile.generated.JSONBaseObject;
import org.glob3.mobile.generated.JSONObject;
import org.glob3.mobile.generated.Vector2F;
import org.glob3.mobile.generated.Vector3F;

import poly2Tri.Triangle;

import com.glob3mobile.tools.extruder.Building;
import com.glob3mobile.tools.extruder.Wall;
import com.glob3mobile.tools.extruder.WallQuad;


public class G3Mesh {


   public static enum Primitive {
      TRIANGLES("Triangles"),
      TRIANGLE_STRIP("TriangleStrip"),
      TRIANGLE_FAN("TriangleFan"),
      LINES("Lines"),
      LINE_STRIP("LineStrip"),
      LINE_LOOP("LineLoop"),
      POINTS("Points");

      private String _name;


      private Primitive(final String name) {
         _name = name;
      }
   }


   public static enum VerticesFormat {
      CARTESIAN("Cartesian"),
      GEODETIC("Geodetic");

      private String _name;


      private VerticesFormat(final String name) {
         _name = name;
      }
   }


   private final G3MeshMaterial        _material;
   private final G3Mesh.Primitive      _primitive;
   private final float                 _pointSize;
   private final float                 _lineWidth;
   private final boolean               _depthTest;
   private final G3Mesh.VerticesFormat _verticesFormat;
   private Vector3F                    _center;
   private final List<Vector3F>        _vertices;
   private final List<Vector3F>        _normals;
   private final List<Color>           _colors;
   private final List<Vector2F>        _texCoords;
   private final List<Short>           _indices;


   public G3Mesh(final G3MeshMaterial material,
                 final G3Mesh.Primitive primitive,
                 final float pointSize,
                 final float lineWidth,
                 final boolean depthTest,
                 final G3Mesh.VerticesFormat verticesFormat,
                 final Vector3F center,
                 final boolean hasNormals,
                 final boolean hasColors,
                 final boolean hasTexCoords,
                 final boolean hasIndices) {
      _material = material;
      _primitive = primitive;
      _pointSize = pointSize;
      _lineWidth = lineWidth;
      _depthTest = depthTest;
      _verticesFormat = verticesFormat;
      _center = center;
      _vertices = new ArrayList<>();
      _normals = hasNormals ? new ArrayList<Vector3F>() : null;
      _colors = hasColors ? new ArrayList<Color>() : null;
      _texCoords = hasTexCoords ? new ArrayList<Vector2F>() : null;
      _indices = hasIndices ? new ArrayList<Short>() : null;
   }


   public G3Mesh(final Building building) {
      _material = building._material;
      _primitive = G3Mesh.Primitive.TRIANGLES;
      _pointSize = 1;
      _lineWidth = 1;
      _depthTest = building._depthTest;
      _verticesFormat = G3Mesh.VerticesFormat.GEODETIC;


      double minX = Double.MAX_VALUE;
      double minY = Double.MAX_VALUE;
      double maxX = -Double.MAX_VALUE;
      double maxY = -Double.MAX_VALUE;

      for (final double[] vertex : building._ceilingVertices) {
         final double x = vertex[0];
         final double y = vertex[1];
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
      }

      final float centerX = (float) ((maxX + minX) / 2);
      final float centerY = (float) ((maxY + minY) / 2);
      final float centerZ = (float) ((building._lowerHeight + building._upperHeight) / 2);
      _center = new Vector3F(centerX, centerY, centerZ);
      _vertices = new ArrayList<>(building._ceilingVertices.length);
      //      _normals = new ArrayList<>(building._ceilingVertices.length);
      for (final double[] vertex : building._ceilingVertices) {
         final double x = vertex[0];
         final double y = vertex[1];
         final double z = building._upperHeight;
         addVertex(centerX, centerY, centerZ, x, y, z);

         //         _normals.add(new Vector3F(1, 0, 0));
      }
      _colors = null;
      _texCoords = null;

      _indices = new ArrayList<>();
      for (final Triangle triangle : building._ceilingTriangles) {
         _indices.add(toShort(triangle._vertex0));
         _indices.add(toShort(triangle._vertex1));
         _indices.add(toShort(triangle._vertex2));
      }

      final int lastCeilingVertexIndex = _vertices.size() - 1;

      processWall(centerX, centerY, centerZ, building._exteriorWall);
      for (final Wall wall : building._interiorWalls) {
         processWall(centerX, centerY, centerZ, wall);
      }

      _normals = initializeNormals(lastCeilingVertexIndex);
   }


   private List<Vector3F> initializeNormals(final int lastCeilingVertexIndex) {
      final int verticesSize = _vertices.size();
      final ArrayList<Vector3F> result = new ArrayList<>(verticesSize);
      for (int i = 0; i < verticesSize; i++) {
         if (i <= lastCeilingVertexIndex) {
            result.add(new Vector3F(1, 0, 0));
         }
         else {
            result.add(null);
         }
      }

      final int indicesSize = _indices.size();
      for (int i = 0; i < indicesSize; i += 3) {
         final short index0 = _indices.get(i + 0);
         final short index1 = _indices.get(i + 1);
         final short index2 = _indices.get(i + 2);

         final Vector3F vertex0 = _vertices.get(index0);
         final Vector3F vertex1 = _vertices.get(index1);
         final Vector3F vertex2 = _vertices.get(index2);

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


   private void setNormal(final ArrayList<Vector3F> normals,
                          final short index,
                          final Vector3F normal) {
      final Vector3F currentNormal = normals.get(index);
      if ((currentNormal == null) || currentNormal.isZero() || currentNormal.isNan()) {
         normals.set(index, normal);
      }
   }


   private void processWall(final float centerX,
                            final float centerY,
                            final float centerZ,
                            final Wall wall) {
      for (final WallQuad quad : wall._quads) {
         final Geodetic2D coordinate0 = quad._coordinate0;
         final Geodetic2D coordinate1 = quad._coordinate1;
         final double lowerHeight = quad._lowerHeight;
         final double upperHeight = quad._upperHeight;

         final int firstVertexIndex = _vertices.size();
         addVertex(centerX, centerY, centerZ, coordinate0, upperHeight);
         addVertex(centerX, centerY, centerZ, coordinate0, lowerHeight);
         addVertex(centerX, centerY, centerZ, coordinate1, lowerHeight);
         addVertex(centerX, centerY, centerZ, coordinate1, upperHeight);

         _indices.add((short) (firstVertexIndex + 0));
         _indices.add((short) (firstVertexIndex + 1));
         _indices.add((short) (firstVertexIndex + 2));

         _indices.add((short) (firstVertexIndex + 0));
         _indices.add((short) (firstVertexIndex + 2));
         _indices.add((short) (firstVertexIndex + 3));
      }
   }


   private void addVertex(final float centerX,
                          final float centerY,
                          final float centerZ,
                          final Geodetic2D coordinate,
                          final double height) {
      addVertex(centerX, centerY, centerZ, coordinate._longitude._degrees, coordinate._latitude._degrees, height);
   }


   private void addVertex(final float centerX,
                          final float centerY,
                          final float centerZ,
                          final double x,
                          final double y,
                          final double z) {
      _vertices.add(new Vector3F( //
               (float) (x - centerX), //
               (float) (y - centerY), //
               (float) (z - centerZ)));
   }


   private static short toShort(final int index) {
      if ((index >= Short.MIN_VALUE) || (index <= Short.MAX_VALUE)) {
         return (short) index;
      }
      throw new RuntimeException("Invalid range for index #" + index);
   }


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


   public void addVertex(final Vector3F vertice) {
      if (_center == null) {
         _center = vertice;
      }
      _vertices.add(vertice.sub(_center));
   }


   public void addNormal(final Vector3F normal) {
      _normals.add(normal.normalized());
   }


   public void addColor(final Color color) {
      _colors.add(color);
   }


   public void addTexCoord(final Vector2F texCoord) {
      _texCoords.add(texCoord);
   }


   public void addIndex(final short index) {
      _indices.add(index);
   }


   private JSONBaseObject toColorJSON(final List<Color> colors) {
      final JSONArray result = new JSONArray();
      for (final Color color : colors) {
         result.add(color._red);
         result.add(color._green);
         result.add(color._blue);
         result.add(color._alpha);
      }
      return result;
   }


   private JSONArray toJSON(final Vector3F point) {
      final JSONArray result = new JSONArray();
      result.add(point._x);
      result.add(point._y);
      result.add(point._z);
      return result;
   }


   private JSONArray toVector3FJSON(final List<Vector3F> vertices) {
      final JSONArray result = new JSONArray();
      for (final Vector3F vertex : vertices) {
         result.add(vertex._x);
         result.add(vertex._y);
         result.add(vertex._z);
      }
      return result;
   }


   private JSONArray toVector2FJSON(final List<Vector2F> texCoords) {
      final JSONArray result = new JSONArray();
      for (final Vector2F vertex : texCoords) {
         result.add(vertex._x);
         result.add(vertex._y);
      }
      return result;
   }


   private JSONArray toShortJSON(final List<Short> indices) {
      final JSONArray result = new JSONArray();
      for (final short index : indices) {
         result.add(index);
      }
      return result;
   }


   public JSONObject toG3MeshJSON() {
      validate();

      final JSONObject result = new JSONObject();

      result.put("material", _material.getID());
      result.put("primitive", _primitive._name);
      if (_pointSize != 1) {
         result.put("pointSize", _pointSize);
      }
      if (_lineWidth != 1) {
         result.put("lineWidth", _lineWidth);
      }
      if (_depthTest != true) {
         result.put("depthTest", _depthTest);
      }
      result.put("verticesFormat", _verticesFormat._name);
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
