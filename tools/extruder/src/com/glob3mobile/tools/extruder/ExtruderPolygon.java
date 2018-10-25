

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
   private final boolean        _depthTest;


   protected ExtruderPolygon(final GEOFeature geoFeature,
                             final double lowerHeight,
                             final G3MeshMaterial material,
                             final boolean depthTest) {
      _geoFeature = geoFeature;
      _lowerHeight = lowerHeight;
      _material = material;
      _depthTest = depthTest;
   }


   public abstract Wall createExteriorWall();


   public abstract List<Wall> createInteriorWalls();


   public Building createBuilding(final PolygonExtruder.Statistics statistics,
                                  final ExtrusionHandler handler,
                                  final int id) {

      final Triangulation.Data data = createTriangulationData();

      try {
         final List<Triangle> ceilingTriangles = Triangulation.triangulate(data);
         if (ceilingTriangles == null) {
            System.out.println("Error triangulating polygon #" + id);
            statistics.countTriangulationError(PolygonExtruder.ErrorType.RETURN_NULL, _geoFeature, handler);
         }
         else {
            statistics.countTriangulation(ceilingTriangles.size());

            return new Building(ceilingTriangles, data._vertices, createExteriorWall(), createInteriorWalls(), _material,
                     _depthTest);
         }
      }
      catch (final NullPointerException e) {
         statistics.countTriangulationError(PolygonExtruder.ErrorType.NULL_POINTER_EXCEPTION, _geoFeature, handler);
      }
      catch (final TriangulationException e) {
         statistics.countTriangulationError(PolygonExtruder.ErrorType.TRIANGULATION_EXCEPTION, _geoFeature, handler);
      }

      return null;
   }


   protected abstract Triangulation.Data createTriangulationData();

}
