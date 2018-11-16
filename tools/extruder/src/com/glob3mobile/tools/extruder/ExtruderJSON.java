

package com.glob3mobile.tools.extruder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.Vector3D;

import com.glob3mobile.tools.mesh.G3MeshMaterial;

import poly2Tri.Triangle;


public class ExtruderJSON {


   private ExtruderJSON() {
   }


   public static List<Double> verticesToJSON(final List<Vector3D> vertices) {
      final List<Double> result = new ArrayList<>(vertices.size() * 3);
      for (final Vector3D vertex : vertices) {
         result.add(vertex._x);
         result.add(vertex._y);
         result.add(vertex._z);
      }
      return result;
   }


   public static List<Vector3D> jsonToVertices(final Map<String, Object> properties,
                                               final String name) {
      @SuppressWarnings("unchecked")
      final List<Double> json = (List<Double>) properties.get(name);

      final List<Vector3D> result = new ArrayList<>(json.size() / 3);
      for (int i = 0; i < json.size(); i += 3) {
         final double x = json.get(i + 0);
         final double y = json.get(i + 1);
         final double z = json.get(i + 2);
         result.add(new Vector3D(x, y, z));
      }
      return result;
   }


   public static List<Integer> trianglesToJSON(final List<Triangle> triangles) {
      final List<Integer> result = new ArrayList<>(triangles.size() * 3);
      for (final Triangle triangle : triangles) {
         result.add(triangle._vertex0);
         result.add(triangle._vertex1);
         result.add(triangle._vertex2);
      }
      return result;
   }


   public static List<Triangle> jsonToTriangles(final Map<String, Object> properties,
                                                final String name) {
      @SuppressWarnings("unchecked")
      final List<Integer> json = (List<Integer>) properties.get(name);
      final List<Triangle> result = new ArrayList<>(json.size() / 3);
      for (int i = 0; i < json.size(); i += 3) {
         final int vertex0 = json.get(i + 0);
         final int vertex1 = json.get(i + 1);
         final int vertex2 = json.get(i + 2);
         result.add(new Triangle(vertex0, vertex1, vertex2));
      }
      return result;
   }


   public static List<Map<String, Object>> wallToJSON(final Wall wall) {
      final List<WallQuad> quads = wall._quads;

      final List<Map<String, Object>> result = new ArrayList<>(quads.size());
      for (final WallQuad quad : quads) {
         result.add(wallQuadToJSON(quad));
      }
      return result;
   }


   public static Wall jsonToWall(final Map<String, Object> properties,
                                 final String name) {
      @SuppressWarnings("unchecked")
      final List<Map<String, Object>> json = (List<Map<String, Object>>) properties.get(name);
      return jsonToWall(json);
   }


   private static Wall jsonToWall(final List<Map<String, Object>> json) {
      final List<WallQuad> quads = new ArrayList<>(json.size());
      for (final Map<String, Object> each : json) {
         quads.add(jsonToWallQuaD(each));
      }
      return new Wall(quads);
   }


   @SuppressWarnings("unchecked")
   private static WallQuad jsonToWallQuaD(final Map<String, Object> json) {
      final Geodetic3D topCorner0 = jsonToGeodetic3D((List<Double>) json.get("top_corner_0"));
      final Geodetic3D topCorner1 = jsonToGeodetic3D((List<Double>) json.get("top_corner_1"));
      final double lowerHeight = (Double) json.get("lower_height");
      return new WallQuad(topCorner0, topCorner1, lowerHeight);
   }


   private static Map<String, Object> wallQuadToJSON(final WallQuad quad) {
      final Map<String, Object> result = new LinkedHashMap<>(3);

      result.put("top_corner_0", geodetic3DToJSON(quad._topCorner0));
      result.put("top_corner_1", geodetic3DToJSON(quad._topCorner1));

      result.put("lower_height", quad._lowerHeight);

      return result;
   }


   private static List<Double> geodetic3DToJSON(final Geodetic3D position) {
      return Arrays.asList( //
               position._latitude._degrees, //
               position._longitude._degrees, //
               position._height);
   }


   private static Geodetic3D jsonToGeodetic3D(final List<Double> json) {
      return Geodetic3D.fromDegrees(json.get(0), json.get(1), json.get(2));
   }


   public static List<List<Map<String, Object>>> wallsToJSON(final List<Wall> walls) {
      final List<List<Map<String, Object>>> result = new ArrayList<>(walls.size());
      for (final Wall wall : walls) {
         result.add(wallToJSON(wall));
      }
      return result;
   }


   public static List<Wall> jsonToWalls(final Map<String, Object> properties,
                                        final String name) {
      @SuppressWarnings("unchecked")
      final List<List<Map<String, Object>>> json = (List<List<Map<String, Object>>>) properties.get(name);

      final List<Wall> result = new ArrayList<>(json.size());
      for (final List<Map<String, Object>> e : json) {
         result.add(jsonToWall(e));
      }
      return result;
   }


   public static Map<String, Object> materialToJSON(final G3MeshMaterial material) {
      final Map<String, Object> result = new LinkedHashMap<>(1);
      result.put("color", colorToJSON(material._color));
      result.put("depth_test", material._depthTest);
      return result;
   }


   @SuppressWarnings("unchecked")
   public static G3MeshMaterial jsonToMaterial(final Map<String, Object> properties,
                                               final String name) {
      final Map<String, Object> json = (Map<String, Object>) properties.get(name);
      final Color color = jsonToColor((List<Float>) json.get("color"));
      final boolean depthTest = (Boolean) json.get("depth_test");
      return new G3MeshMaterial(color, depthTest);
   }


   private static List<Float> colorToJSON(final Color color) {
      return Arrays.asList(color._red, color._green, color._blue, color._alpha);
   }


   private static Color jsonToColor(final List<Float> list) {
      return Color.fromRGBA(list.get(0), list.get(1), list.get(2), list.get(3));
   }


}
