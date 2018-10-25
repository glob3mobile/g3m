

package com.glob3mobile.tools.extruder.examples;

import java.io.IOException;
import java.util.List;

import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.GEOFeature;
import org.glob3.mobile.generated.GEOObject;
import org.glob3.mobile.generated.JSONObject;
import org.glob3.mobile.generated.Planet;

import com.glob3mobile.tools.extruder.Building;
import com.glob3mobile.tools.extruder.ExtrusionHandler;
import com.glob3mobile.tools.extruder.Heigths;
import com.glob3mobile.tools.extruder.PolygonExtruder;
import com.glob3mobile.tools.mesh.G3MeshMaterial;


public class AirspaceExtrusion {


   private static class AirspaceExtrusionHandler
            implements
               ExtrusionHandler {


      private static final G3MeshMaterial MATERIAL_1 = new G3MeshMaterial(Color.fromRGBA(1, 1, 0, 0.5f), false);
      private static final G3MeshMaterial MATERIAL_2 = new G3MeshMaterial(Color.fromRGBA(0, 1, 1, 0.5f), false);
      private static final G3MeshMaterial MATERIAL_3 = new G3MeshMaterial(Color.fromRGBA(1, 0, 1, 0.5f), false);
      private static final G3MeshMaterial MATERIAL_4 = new G3MeshMaterial(Color.fromRGBA(1, 0, 0, 0.5f), false);


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
      public G3MeshMaterial getMaterialFor(final GEOFeature geoFeature) {
         final JSONObject properties = geoFeature.getProperties();

         final String type = properties.getAsString("type", "");
         if (type.equalsIgnoreCase("MTMA")) {
            return MATERIAL_1;
         }
         else if (type.equalsIgnoreCase("MCTR")) {
            return MATERIAL_2;
         }
         else if (type.equalsIgnoreCase("TMA")) {
            return MATERIAL_3;
         }
         else {
            return MATERIAL_4;
         }
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


      @Override
      public void onRootGEOObject(final GEOObject geoObject) {
      }


      @Override
      public void onFinish(final List<Building> buildings) {
      }
   }


   public static void main(final String[] args) throws IOException {
      System.out.println("AirspaceExtrusion 0.1");
      System.out.println("---------------------\n");

      final String name = "airspace";
      final String inputFileName = name + ".geojson";
      final String outputFileName = name + "_3d.json";

      final int floatPrecision = 6;

      final Planet planet = null; // cartesian

      PolygonExtruder.process(inputFileName, outputFileName, new AirspaceExtrusionHandler(), planet, floatPrecision);
   }


}
