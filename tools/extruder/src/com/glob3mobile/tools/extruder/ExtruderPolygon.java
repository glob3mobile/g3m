

package com.glob3mobile.tools.extruder;

import java.util.List;

import org.glob3.mobile.generated.GEOFeature;

import com.glob3mobile.tools.mesh.G3MeshMaterial;

import poly2Tri.Triangle;
import poly2Tri.Triangulation;
import poly2Tri.TriangulationException;


public abstract class ExtruderPolygon {

   private final GEOFeature     _geoFeature;
   protected final double       _lowerHeight;
   private final G3MeshMaterial _material;


   protected ExtruderPolygon(final GEOFeature geoFeature,
                             final double lowerHeight,
                             final G3MeshMaterial material) {
      _geoFeature = geoFeature;
      _lowerHeight = lowerHeight;
      _material = material;
   }


   public abstract Wall createExteriorWall();


   public abstract List<Wall> createInteriorWalls();


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

            final Wall exteriorWall = createExteriorWall();
            final List<Wall> interiorWalls = createInteriorWalls();
            return new Building(_geoFeature, data._vertices, roofTriangles, exteriorWall, interiorWalls, _material);
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


   protected abstract Triangulation.Data createTriangulationData();

}
