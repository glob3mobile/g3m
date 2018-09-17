

package org.glob3.mobile.generated;


//
//  AppParser.cpp
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 18/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


//
//  AppParser.hpp
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 18/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


public class AppParser {

   private static final String WORLD       = "_world";
   private static final String BASELAYER   = "_baselayer";
   private static final String BBOX        = "_bbox";
   private static final String CUSTOMDATA  = "_customdata";

   private static final String FEATURES    = "features";
   private static final String GEOMETRY    = "geometry";
   private static final String TYPE        = "type";
   private static final String COORDINATES = "coordinates";
   private static final String PROPERTIES  = "properties";
   private static final String NAME        = "name";

   private static AppParser    _instance   = null;


   private void parseWorldConfiguration(final LayerSet layerSet,
                                        final MarksRenderer marks,
                                        final JSONObject jsonWorld) {
      final String jsonBaseLayer = jsonWorld.getAsString(BASELAYER).value();
      final JSONArray jsonBbox = jsonWorld.getAsArray(BBOX);

      if (jsonBaseLayer.equals("BING")) {
         final WMSLayer bing = new WMSLayer("ve", new URL("http://worldwind27.arc.nasa.gov/wms/virtualearth?", true),
                  WMSServerVersion.WMS_1_1_0, "", null, WMSServerVersion.WMS_1_1_0, Sector.fromDegrees(
                           jsonBbox.getAsNumber(1).value(), jsonBbox.getAsNumber(0).value(), jsonBbox.getAsNumber(3).value(),
                           jsonBbox.getAsNumber(2).value()), "image/jpeg", "EPSG:4326", "", false, null, null, false);
         layerSet.addLayer(bing);
      }
      else {
         final WMSLayer osm = new WMSLayer("osm", new URL("http://wms.latlon.org/", true), WMSServerVersion.WMS_1_1_0, "", null,
                  WMSServerVersion.WMS_1_1_0, Sector.fromDegrees(jsonBbox.getAsNumber(1).value(),
                           jsonBbox.getAsNumber(0).value(), jsonBbox.getAsNumber(3).value(), jsonBbox.getAsNumber(2).value()),
                  "image/jpeg", "EPSG:4326", "", false, null, null, false);
         layerSet.addLayer(osm);
      }
      parseCustomData(marks, jsonWorld.getAsObject(CUSTOMDATA));
   }


   private void parseGEOJSONPointObject(final MarksRenderer marks,
                                        final JSONObject point) {
      final JSONObject jsonProperties = point.getAsObject(PROPERTIES);
      final JSONObject jsonGeometry = point.getAsObject(GEOMETRY);
      final JSONArray jsonCoordinates = jsonGeometry.getAsArray(COORDINATES);

      /*
            final Mark mark = new Mark(jsonProperties.getAsString(NAME).value(), new Geodetic3D(
                     Angle.fromDegrees(jsonCoordinates.getAsNumber(1).value()),
                     Angle.fromDegrees(jsonCoordinates.getAsNumber(0).value()), 0), new URL(
                     "http://glob3m.glob3mobile.com/icons/markers/g3m.png", false), AltitudeMode.RELATIVE_TO_GROUND, 10000);
      
      marks.addMark(mark);
       */
   }


   public static AppParser instance() {
      if (_instance == null) {
         _instance = new AppParser();
      }
      return _instance;
   }


   public final void parse(final LayerSet layerSet,
                           final MarksRenderer marks,
                           final String namelessParameter) {
      final JSONObject json = IJSONParser.instance().parse(namelessParameter).asObject();
      parseWorldConfiguration(layerSet, marks, json.get(WORLD).asObject());
      IJSONParser.instance().deleteJSONData(json);
   }


   public final void parseCustomData(final MarksRenderer marks,
                                     final JSONObject jsonCustomData) {
      final JSONArray jsonFeatures = jsonCustomData.getAsArray(FEATURES);
      for (int i = 0; i < jsonFeatures.size(); i++) {
         final JSONObject jsonFeature = jsonFeatures.getAsObject(i);
         final JSONObject jsonGeometry = jsonFeature.getAsObject(GEOMETRY);
         final String jsonType = jsonGeometry.getAsString(TYPE).value();
         if (jsonType.equals("Point")) {
            parseGEOJSONPointObject(marks, jsonFeature);
         }
      }
   }


   protected AppParser() {
   }
   //C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
   //  AppParser(AppParser NamelessParameter);
   //C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
   //  AppParser operator = (AppParser NamelessParameter);

}
