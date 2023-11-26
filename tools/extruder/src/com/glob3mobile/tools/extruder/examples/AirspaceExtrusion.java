
package com.glob3mobile.tools.extruder.examples;

import java.io.*;
import java.util.*;

import org.glob3.mobile.generated.*;

import com.glob3mobile.tools.extruder.*;
import com.glob3mobile.tools.mesh.*;

public class AirspaceExtrusion {

   private static class AirspaceExtrusionHandler implements ExtrusionHandler<GEOFeature, Void> {

      private static final G3MeshMaterial MATERIAL_1 = new G3MeshMaterial(Color.fromRGBA(1, 1, 0, 0.5f));
      private static final G3MeshMaterial MATERIAL_2 = new G3MeshMaterial(Color.fromRGBA(0, 1, 1, 0.5f));
      private static final G3MeshMaterial MATERIAL_3 = new G3MeshMaterial(Color.fromRGBA(1, 0, 1, 0.5f));
      private static final G3MeshMaterial MATERIAL_4 = new G3MeshMaterial(Color.fromRGBA(1, 0, 0, 0.5f));

      private static double toMeter(final JSONObject properties, final String valueKey, final String unitKey) {
         return toMeters(properties.getAsNumber(valueKey, 0), properties.getAsString(unitKey, "FT"));
      }

      private static double toMeters(final double value, final String unit) {
         final double exageration   = 1;
         final double valueInMeters = (value / 3) * exageration;
         return unit.equalsIgnoreCase("FT") ? valueInMeters : (valueInMeters * 100);
      }

      //      @Override
      //      public boolean extrudes(final GEOFeature geoFeature,
      //                              final Void v) {
      //         final JSONObject properties = geoFeature.getProperties();
      //         final String type = properties.getAsString("type", "");
      //         return (!type.equalsIgnoreCase("CTA") && !type.equalsIgnoreCase("FIR"));
      //      }

      @Override
      public G3MeshMaterial getMaterialFor(final GEOFeature geoFeature, final Void v) {
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
      public boolean getDepthTestFor(final GEOFeature geoFeature, final Void v) {
         return false;
      }

      @Override
      public Heigths getHeightsFor(final GEOFeature geoFeature, final Void v) {
         final JSONObject properties  = geoFeature.getProperties();
         final double     lowerMeters = toMeter(properties, "lowerLimit", "lowerLimit_uom");
         final double     upperMeters = toMeter(properties, "upperLimit", "upperLimit_uom");
         return new Heigths(lowerMeters, upperMeters);
      }

      @Override
      public void onBuildings(final List<Building> buildings) {
      }

      @Override
      public void onPolygons(final List<ExtruderPolygon> polygons) {
      }

      @Override
      public void onMeshCollection(final G3MeshCollection meshes) {
      }
   }

   public static void main(final String[] args) throws IOException {
      System.out.println("AirspaceExtrusion 0.1");
      System.out.println("---------------------\n");

      final String name           = "airspace";
      final String inputFileName  = name + ".geojson";
      final String outputFileName = name + "_3d.json";

      final int floatPrecision = 6;

      final Planet  planet               = null; // cartesian
      final float   verticalExaggeration = 1;
      final double  deltaHeight          = 0;
      final boolean createNormals        = true;

      PolygonExtruder.process(inputFileName, outputFileName, new AirspaceExtrusionHandler(), createNormals, planet, verticalExaggeration, deltaHeight,
                              floatPrecision, true);
   }

}
