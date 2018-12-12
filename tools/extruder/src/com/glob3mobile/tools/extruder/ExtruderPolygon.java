

package com.glob3mobile.tools.extruder;

import java.util.ArrayList;
import java.util.List;

import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.Vector3D;

import com.glob3mobile.tools.mesh.G3MeshMaterial;

import poly2Tri.Triangle;
import poly2Tri.Triangulation;
import poly2Tri.TriangulationException;


public abstract class ExtruderPolygon<T> {


   private final T              _source;
   private final double         _lowerHeight;
   private final G3MeshMaterial _material;
   private final boolean        _depthTest;
   private Geodetic2D           _average;
   private final double         _minHeight;


   protected ExtruderPolygon(final T source,
                             final double lowerHeight,
                             final G3MeshMaterial material,
                             final boolean depthTest,
                             final double minHeight) {
      _source = source;
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


   public double getMinHeight() {
      return _minHeight;
   }


   public abstract Wall createExteriorWall(final double lowerHeight);


   public abstract List<Wall> createInteriorWalls(final double lowerHeight);


   public Building<T> createBuilding(final PolygonExtruder.Statistics<T> statistics,
                                     final ExtrusionHandler<T> handler,
                                     final long id) {

      final Triangulation.Data data = createTriangulationData();

      try {
         final List<Triangle> roofTriangles = Triangulation.triangulate(data);
         if (roofTriangles == null) {
            System.err.println("Error triangulating polygon #" + id);
            statistics.countTriangulationError(PolygonExtruder.ErrorType.RETURN_NULL, _source, handler);
         }
         else {
            statistics.countTriangulation(roofTriangles.size());

            final Wall exteriorWall = createExteriorWall(_lowerHeight);
            final List<Wall> interiorWalls = createInteriorWalls(_lowerHeight);
            return new Building<T>(this, getAverage(), _minHeight, toVector3DList(data._vertices), roofTriangles, exteriorWall,
                     interiorWalls, _material, _depthTest);
         }
      }
      catch (final NullPointerException e) {
         statistics.countTriangulationError(PolygonExtruder.ErrorType.NULL_POINTER_EXCEPTION, _source, handler);
      }
      catch (final TriangulationException e) {
         System.out.println(e.getMessage());
         statistics.countTriangulationError(PolygonExtruder.ErrorType.TRIANGULATION_EXCEPTION, _source, handler);
      }

      return null;
   }


   private List<Vector3D> toVector3DList(final double[][] vertices) {
      final List<Vector3D> result = new ArrayList<>(vertices.length);
      for (final double[] vertex : vertices) {
         final double x = vertex[0];
         final double y = vertex[1];
         final double z = vertex[2];
         result.add(new Vector3D(x, y, z));
      }
      return result;
   }


   protected abstract Triangulation.Data createTriangulationData();


   protected abstract Geodetic2D calculateAverage();


   public abstract List<Geodetic2D> getOuterRing();


   public abstract List<? extends List<Geodetic2D>> getHolesRings();


}
