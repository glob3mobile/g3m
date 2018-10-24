

package com.glob3mobile.tools.extruder.examples;

import java.io.File;
import java.io.IOException;

import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.GEO3DPolygonGeometry;
import org.glob3.mobile.generated.GEOFeature;
import org.glob3.mobile.generated.GEOGeometry;
import org.glob3.mobile.generated.GEOObject;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.tools.utils.GEOBitmap;

import com.glob3mobile.tools.extruder.ExtrusionHandler;
import com.glob3mobile.tools.extruder.Heigths;
import com.glob3mobile.tools.extruder.PolygonExtruder;
import com.glob3mobile.tools.mesh.G3MeshMaterial;


public class CaceresExtrusion {


   private static class CaceresExtrusionHandler
            implements
               ExtrusionHandler {


      private static final G3MeshMaterial MATERIAL_1 = new G3MeshMaterial(Color.fromRGBA(1, 1, 0, 1));

      private GEOBitmap _bitmap;


      @Override
      public boolean extrudes(final GEOFeature geoFeature) {
         return true;
      }


      @Override
      public boolean getDepthTestFor(final GEOFeature geoFeature) {
         return true;
      }


      @Override
      public G3MeshMaterial getMaterialFor(final GEOFeature geoFeature) {
         return MATERIAL_1;
      }


      @Override
      public Heigths getHeightsFor(final GEOFeature geoFeature) {
         return new Heigths(0, 100);
      }


      @Override
      public void processTriangulationError(final GEOFeature geoFeature) {
         final GEOGeometry geometry = geoFeature.getGeometry();
         System.err.println("Error triangulation " + geoFeature + ", geometry:  " + geometry);
         if (geometry instanceof GEO3DPolygonGeometry) {
            final GEO3DPolygonGeometry polygon3D = (GEO3DPolygonGeometry) geometry;

            _bitmap.drawPolygon( //
                     polygon3D.getCoordinates(), //
                     polygon3D.getHolesCoordinatesArray(), //
                     new java.awt.Color(1, 0, 0, 0.5f), //
                     new java.awt.Color(1, 0, 0, 0.9f), //
                     true, //  drawVertices
                     new java.awt.Color(1, 1, 0, 0.5f) //
            );
         }
      }


      @Override
      public void onRootGEOObject(final GEOObject geoObject) {
         final Sector sector = geoObject.getSector();

         final int width = 2048;
         final int height = (int) Math.round((width / sector._deltaLongitude._radians) * sector._deltaLatitude._radians);
         _bitmap = new GEOBitmap(sector, width, height, java.awt.Color.BLACK);
      }


      @Override
      public void onFinish() {
         try {
            _bitmap.save(new File("debug.png"));
         }
         catch (final IOException e) {
            throw new RuntimeException(e);
         }
      }


   }


   public static void main(final String[] args) throws IOException {
      System.out.println("CaceresExtrusion 0.1");
      System.out.println("--------------------\n");


      final String name = "casco_historico"; // "nucleo_urbano"; // "muralla";
      final String inputFileName = name + ".geojson";
      final String outputFileName = name + "_3d.json";

      final int floatPrecision = 6;

      PolygonExtruder.process(inputFileName, outputFileName, new CaceresExtrusionHandler(), floatPrecision);
   }


}
