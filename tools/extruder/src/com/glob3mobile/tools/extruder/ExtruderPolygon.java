

package com.glob3mobile.tools.extruder;

import java.util.ArrayList;
import java.util.List;

import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.Vector3D;

import com.glob3mobile.tools.mesh.G3MeshMaterial;

import poly2Tri.Triangle;
import poly2Tri.Triangulation;
import poly2Tri.Triangulation.Data;
import poly2Tri.TriangulationException;


public abstract class ExtruderPolygon {

   private final double         _lowerHeight;
   private final G3MeshMaterial _material;
   private final boolean        _depthTest;
   private Geodetic2D           _average;
   private final double         _minHeight;


   protected ExtruderPolygon(final double lowerHeight,
                             final G3MeshMaterial material,
                             final boolean depthTest,
                             final double minHeight) {
      _lowerHeight = lowerHeight;
      _material = material;
      _depthTest = depthTest;
      _minHeight = minHeight;
   }


   public Geodetic2D getAverage() {
      if (_average == null) {
         _average = calculateAverage();
      }
      return _average;
   }


   //   public double getMinHeight() {
   //      return _minHeight;
   //   }


   public abstract Wall createExteriorWall(final double lowerHeight);


   public abstract List<Wall> createInteriorWalls(final double lowerHeight);


   public Building createBuilding(final PolygonExtruder.Statistics statistics,
                                  final long id) {

      final Triangulation.Data data = createRoofTriangulationData();

      try {
         final List<Triangle> roofTriangles = Triangulation.triangulate(data);
         if (roofTriangles == null) {
            System.err.println("Error triangulating polygon #" + id);
            statistics.countTriangulationError(PolygonExtruder.ErrorType.RETURN_NULL);
         }
         else {
            statistics.countTriangulation(roofTriangles.size());

            final Data doofSansHolesTriangulationData = createRoofSansHolesTriangulationData();
            final List<Triangle> roofSansHolesTriangles = Triangulation.triangulate(doofSansHolesTriangulationData);
            final double roofArea = calculateRoofArea(roofSansHolesTriangles, toVector3DList(doofSansHolesTriangulationData._vertices));

            final Wall exteriorWall = createExteriorWall(_lowerHeight);
            final List<Wall> interiorWalls = createInteriorWalls(_lowerHeight);
            return new Building(this, getAverage(), roofArea, _minHeight, toVector3DList(data._vertices), roofTriangles, exteriorWall, interiorWalls,
                     _material, _depthTest);
         }
      }
      catch (final NullPointerException e) {
         statistics.countTriangulationError(PolygonExtruder.ErrorType.NULL_POINTER_EXCEPTION);
      }
      catch (final TriangulationException e) {
         System.out.println(e.getMessage());
         statistics.countTriangulationError(PolygonExtruder.ErrorType.TRIANGULATION_EXCEPTION);
      }

      return null;
   }


   private static double calculateRoofArea(final List<Triangle> triangles,
                                           final List<Vector3D> vertices) {
      double area = 0;
      for (final Triangle triangle : triangles) {
         final Vector3D v0 = vertices.get(triangle._vertex0);
         final Vector3D v1 = vertices.get(triangle._vertex1);
         final Vector3D v2 = vertices.get(triangle._vertex2);
         area += Math.abs(triangleArea(v0, v1, v2));
      }
      return area;
   }


   private static double triangleArea(final Vector3D v0,
                                      final Vector3D v1,
                                      final Vector3D v2) {
      return (v1.sub(v0).cross(v2.sub(v0))).length() / 2;
   }


   private static List<Vector3D> toVector3DList(final double[][] vertices) {
      final List<Vector3D> result = new ArrayList<>(vertices.length);
      for (final double[] vertex : vertices) {
         final double x = vertex[0];
         final double y = vertex[1];
         final double z = vertex[2];
         result.add(new Vector3D(x, y, z));
      }
      return result;
   }


   protected abstract Triangulation.Data createRoofTriangulationData();


   protected abstract Triangulation.Data createRoofSansHolesTriangulationData();


   protected abstract Geodetic2D calculateAverage();


   public abstract List<Geodetic2D> getOuterRing();


   public abstract List<? extends List<Geodetic2D>> getHolesRings();


}
