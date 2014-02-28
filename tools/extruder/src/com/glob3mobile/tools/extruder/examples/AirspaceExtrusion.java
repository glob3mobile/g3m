

package com.glob3mobile.tools.extruder.examples;

import java.io.IOException;

import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.GEOFeature;
import org.glob3.mobile.generated.JSONObject;

import com.glob3mobile.tools.extruder.ExtrusionHandler;
import com.glob3mobile.tools.extruder.Heigths;
import com.glob3mobile.tools.extruder.PolygonExtruder;
import com.glob3mobile.tools.mesh.G3MeshMaterial;


public class AirspaceExtrusion {


   private static class AirspaceExtrusionHandler
            implements
               ExtrusionHandler {


      private static double toMeter(final JSONObject properties,
                                    final String valueKey,
                                    final String unitKey) {
         return toMeters(properties.getAsNumber(valueKey, 0), properties.getAsString(unitKey, "FT"));
      }


      private static double toMeters(final double value,
                                     final String unit) {
         final double exageration = 1;
         final double valueInMeters = (value / 3) * exageration;
         return unit.equalsIgnoreCase("FT") ? valueInMeters : (valueInMeters * 100);
      }


      @Override
      public boolean extrudes(final GEOFeature geoFeature) {
         final JSONObject properties = geoFeature.getProperties();
         final String type = properties.getAsString("type", "");
         return (!type.equalsIgnoreCase("CTA") && !type.equalsIgnoreCase("FIR"));
      }


      @Override
      public boolean getDepthTestFor(final GEOFeature geoFeature) {
         return false;
      }


      @Override
      public G3MeshMaterial getMaterialFor(final GEOFeature geoFeature) {
         final JSONObject properties = geoFeature.getProperties();

         final String type = properties.getAsString("type", "");
         final Color color;
         if (type.equalsIgnoreCase("MTMA")) {
            color = Color.fromRGBA(1, 1, 0, 0.5f);
         }
         else if (type.equalsIgnoreCase("MCTR")) {
            color = Color.fromRGBA(0, 1, 1, 0.5f);
         }
         else if (type.equalsIgnoreCase("TMA")) {
            color = Color.fromRGBA(1, 0, 1, 0.5f);
         }
         else {
            color = Color.fromRGBA(1, 0, 0, 0.5f);
         }
         return new G3MeshMaterial(color);
      }


      @Override
      public Heigths getHeightsFor(final GEOFeature geoFeature) {
         final JSONObject properties = geoFeature.getProperties();
         final double lowerMeters = toMeter(properties, "lowerLimit", "lowerLimit_uom");
         final double upperMeters = toMeter(properties, "upperLimit", "upperLimit_uom");
         return new Heigths(lowerMeters, upperMeters);
      }


      @Override
      public void processTriangulationError(final GEOFeature geoFeature) {
         System.err.println("Error triangulation " + geoFeature);
      }
   }


   public static void main(final String[] args) throws IOException {
      System.out.println("AirspaceExtrusion 0.1");
      System.out.println("---------------------\n");

      final String inputFileName = "/Users/dgd/Desktop/extrussion/airspace.geojson";
      final String outputFileName = "3d_.json";

      PolygonExtruder.process(inputFileName, outputFileName, new AirspaceExtrusionHandler());
   }


}
