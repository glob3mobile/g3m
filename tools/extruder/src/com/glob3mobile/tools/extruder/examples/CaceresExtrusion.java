

package com.glob3mobile.tools.extruder.examples;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.EllipsoidalPlanet;
import org.glob3.mobile.generated.GEOFeature;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.tools.utils.GEOBitmap;

import com.glob3mobile.tools.extruder.Building;
import com.glob3mobile.tools.extruder.ExtruderPolygon;
import com.glob3mobile.tools.extruder.ExtrusionHandler;
import com.glob3mobile.tools.extruder.Heigths;
import com.glob3mobile.tools.extruder.PolygonExtruder;
import com.glob3mobile.tools.mesh.G3MeshCollection;
import com.glob3mobile.tools.mesh.G3MeshMaterial;


public class CaceresExtrusion {


   private static class CaceresExtrusionHandler
            implements
               ExtrusionHandler<GEOFeature, Void> {


      private static final G3MeshMaterial MATERIAL = new G3MeshMaterial(Color.YELLOW);

      private GEOBitmap _bitmap;


      @Override
      public G3MeshMaterial getMaterialFor(final GEOFeature geoFeature,
                                           final Void v) {
         return MATERIAL;
      }


      @Override
      public Heigths getHeightsFor(final GEOFeature geoFeature,
                                   final Void v) {
         return new Heigths(0, 100);
      }


      @Override
      public void onBuildings(final List<Building> buildings) {
      }


      private void save(final String fileName) throws IOException {
         if (_bitmap != null) {
            _bitmap.save(new File(fileName));
         }
      }


      @Override
      public void onPolygons(final List<ExtruderPolygon> polygons) {
      }


      @Override
      public void onMeshCollection(final G3MeshCollection meshes) {
      }


      @Override
      public boolean getDepthTestFor(final GEOFeature geoFeature,
                                     final Void v) {
         return true;
      }


   }


   public static void main(final String[] args) throws IOException {
      System.out.println("CaceresExtrusion 0.1");
      System.out.println("--------------------\n");

      // final String name = "deportivo";
      // final String name = "cortijos";
      final String name = "casco_historico";
      // final String name = "nucleo_urbano";
      // final String name = "muralla";

      final String inputFileName = name + ".geojson";
      final String outputFileName = name + "_3d.json";

      final Planet planet = EllipsoidalPlanet.createEarth();
      final float verticalExaggeration = 1;
      final double deltaHeight = 0;
      final int floatPrecision = 3;
      final boolean createNormals = true;

      System.out.println(name);
      final CaceresExtrusionHandler handler = new CaceresExtrusionHandler();
      PolygonExtruder.process(inputFileName, outputFileName, handler, createNormals, planet, verticalExaggeration, deltaHeight, floatPrecision, true);
      handler.save(name + "_debug.png");
   }


}
