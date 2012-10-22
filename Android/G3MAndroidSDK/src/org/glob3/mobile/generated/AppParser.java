

package org.glob3.mobile.generated;

public class AppParser {

   private static AppParser _instance = null;


   private void parseWorldConfiguration(final LayerSet layerSet,
                                        final MarksRenderer marks,
                                        final JSONObject jsonWorld) {
      final String jsonBaseLayer = jsonWorld.getObjectForKey(GlobalMembersAppParser.baselayer).getString().getValue();
      final JSONArray jsonBbox = jsonWorld.getObjectForKey(GlobalMembersAppParser.bbox).getArray();

      if (jsonBaseLayer.equals("BING")) {
         final WMSLayer bing = new WMSLayer("ve", new URL("http://worldwind27.arc.nasa.gov/wms/virtualearth?"),
                  WMSServerVersion.WMS_1_1_0, Sector.fromDegrees(jsonBbox.getElement(1).getNumber().getDoubleValue(),
                           jsonBbox.getElement(0).getNumber().getDoubleValue(),
                           jsonBbox.getElement(3).getNumber().getDoubleValue(),
                           jsonBbox.getElement(2).getNumber().getDoubleValue()), "image/jpeg", "EPSG:4326", "", false, null);
         layerSet.addLayer(bing);
      }
      else {
         final WMSLayer osm = new WMSLayer("osm", new URL("http://wms.latlon.org/"), WMSServerVersion.WMS_1_1_0,
                  Sector.fromDegrees(jsonBbox.getElement(1).getNumber().getDoubleValue(),
                           jsonBbox.getElement(0).getNumber().getDoubleValue(),
                           jsonBbox.getElement(3).getNumber().getDoubleValue(),
                           jsonBbox.getElement(2).getNumber().getDoubleValue()), "image/jpeg", "EPSG:4326", "", false, null);
         layerSet.addLayer(osm);
      }
      parseCustomData(marks, jsonWorld.getObjectForKey(GlobalMembersAppParser.customdata).getObject());
   }


   private void parseCustomData(final MarksRenderer marks,
                                final JSONObject jsonCustomData) {
      final JSONArray jsonFeatures = jsonCustomData.getObjectForKey(GlobalMembersAppParser.features).getArray();
      for (int i = 0; i < jsonFeatures.getSize(); i++) {
         final JSONObject jsonFeature = jsonFeatures.getElement(i).getObject();
         final JSONObject jsonGeometry = jsonFeature.getObjectForKey(GlobalMembersAppParser.geometry).getObject();
         final String jsonType = jsonGeometry.getObjectForKey(GlobalMembersAppParser.type).getString().getValue();
         if (jsonType.equals("Point")) {
            parseGEOJSONPointObject(marks, jsonFeature);
         }
      }
   }


   private void parseGEOJSONPointObject(final MarksRenderer marks,
                                        final JSONObject point) {
      final JSONObject jsonProperties = point.getObjectForKey(GlobalMembersAppParser.properties).getObject();
      final JSONObject jsonGeometry = point.getObjectForKey(GlobalMembersAppParser.geometry).getObject();
      final JSONArray jsonCoordinates = jsonGeometry.getObjectForKey(GlobalMembersAppParser.coordinates).getArray();

      final Mark mark = new Mark(jsonProperties.getObjectForKey(GlobalMembersAppParser.name).getString().getValue(), new URL(
               "http://glob3m.glob3mobile.com/icons/markers/g3m.png"), new Geodetic3D(
               Angle.fromDegrees(jsonCoordinates.getElement(1).getNumber().getDoubleValue()),
               Angle.fromDegrees(jsonCoordinates.getElement(0).getNumber().getDoubleValue()), 0));
      marks.addMark(mark);
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
      final JSONObject json = IJSONParser.instance().parse(namelessParameter).getObject();
      parseWorldConfiguration(layerSet, marks, json.getObjectForKey(GlobalMembersAppParser.world).getObject());
      IJSONParser.instance().deleteJSONData(json);
   }


   protected AppParser() {
   }
   //C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
   //  AppParser(AppParser NamelessParameter);
   //C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
   //  AppParser operator = (AppParser NamelessParameter);

}
