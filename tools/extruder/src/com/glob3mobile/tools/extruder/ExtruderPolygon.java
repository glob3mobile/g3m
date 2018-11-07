

package com.glob3mobile.tools.extruder;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.glob3.mobile.generated.GEOFeature;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.Vector3D;
import org.glob3.mobile.tools.utils.GEOBitmap;

import com.glob3mobile.tools.mesh.G3MeshMaterial;

import poly2Tri.Triangle;
import poly2Tri.Triangulation;
import poly2Tri.TriangulationException;


public abstract class ExtruderPolygon {

   private final GEOFeature     _geoFeature;
   private final double         _lowerHeight;
   private final G3MeshMaterial _material;
   private Geodetic2D           _average;
   private final double         _minHeight;


   protected ExtruderPolygon(final GEOFeature geoFeature,
                             final double lowerHeight,
                             final G3MeshMaterial material,
                             final double minHeight) {
      _geoFeature = geoFeature;
      _lowerHeight = lowerHeight;
      _material = material;
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


   public Building createBuilding(final PolygonExtruder.Statistics statistics,
                                  final ExtrusionHandler handler,
                                  final int id) {

      final Triangulation.Data data = createTriangulationData();

      try {
         final List<Triangle> roofTriangles = Triangulation.triangulate(data);
         if (roofTriangles == null) {
            System.err.println("Error triangulating polygon #" + id);
            statistics.countTriangulationError(PolygonExtruder.ErrorType.RETURN_NULL, _geoFeature, handler);
         }
         else {
            statistics.countTriangulation(roofTriangles.size());

            final Wall exteriorWall = createExteriorWall(_lowerHeight);
            final List<Wall> interiorWalls = createInteriorWalls(_lowerHeight);
            return new Building(this, getAverage(), toVector3DList(data._vertices), roofTriangles, exteriorWall, interiorWalls,
                     _material);
         }
      }
      catch (final NullPointerException e) {
         statistics.countTriangulationError(PolygonExtruder.ErrorType.NULL_POINTER_EXCEPTION, _geoFeature, handler);
      }
      catch (final TriangulationException e) {
         System.out.println(e.getMessage());
         statistics.countTriangulationError(PolygonExtruder.ErrorType.TRIANGULATION_EXCEPTION, _geoFeature, handler);
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


   public abstract void drawOn(final GEOBitmap bitmap,
                               final Color fillColor,
                               final Color borderColor);

}
