

package org.glob3.mobile.generated;

public class SceneParser {
   private static final String                         LAYERS             = "layers";
   private static final String                         TYPE               = "type";
   private static final String                         DATASOURCE         = "datasource";
   private static final String                         VERSION            = "version";
   private static final String                         ITEMS              = "items";
   private static final String                         STATUS             = "status";
   private static final String                         NAME               = "name";
   private static final String                         ICON               = "icon";

   private static final String                         WMS110             = "1.1.0";
   private static final String                         WMS111             = "1.1.1";
   private static final String                         WMS130             = "1.3.0";

   private static SceneParser                          _instance          = null;
   private final java.util.HashMap<String, layer_type> _mapLayerType      = new java.util.HashMap<String, layer_type>();
   private final java.util.HashMap<String, String>     _mapGeoJSONSources = new java.util.HashMap<String, String>();


   public static SceneParser instance() {
      if (_instance == null) {
         _instance = new SceneParser();
      }
      return _instance;
   }


   public final void parse(final LayerSet layerSet,
                           final String namelessParameter) {

      _mapGeoJSONSources.clear();

      final JSONObject json = IJSONParser.instance().parse(namelessParameter).asObject();
      parserJSONLayerList(layerSet, json.getAsObject(LAYERS));
      IJSONParser.instance().deleteJSONData(json);
   }


   public final java.util.HashMap<String, String> getMapGeoJSONSources() {
      return _mapGeoJSONSources;
   }


   private void parserJSONLayerList(final LayerSet layerSet,
                                    final JSONObject jsonLayers) {
      for (int i = 0; i < jsonLayers.size(); i++) {
         final IStringBuilder isb = IStringBuilder.newStringBuilder();
         isb.addInt(i);
         final JSONObject jsonLayer = jsonLayers.getAsObject(isb.getString());
         final layer_type layerType = _mapLayerType.get(jsonLayer.getAsString(TYPE).value());

         switch (layerType) {
            case WMS:
               parserJSONWMSLayer(layerSet, jsonLayer);
               break;
            case THREED:
               parserJSON3DLayer(layerSet, jsonLayer);
               break;
            case PANO:
               parserJSONPanoLayer(layerSet, jsonLayer);
               break;
            case GEOJSON:
               parserGEOJSONLayer(layerSet, jsonLayer);
               break;
         }

      }
   }


   private void parserJSONWMSLayer(final LayerSet layerSet,
                                   final JSONObject jsonLayer) {
      System.out.print("Parsing WMS Layer ");
      System.out.print(jsonLayer.getAsString(NAME).value());
      System.out.print("...");
      System.out.print("\n");

      final String jsonDatasource = jsonLayer.getAsString(DATASOURCE).value();
      final int lastIndex = IStringUtils.instance().indexOf(jsonDatasource, "?");
      final String jsonURL = IStringUtils.instance().substring(jsonDatasource, 0, lastIndex + 1);
      final String jsonVersion = jsonLayer.getAsString(VERSION).value();

      final JSONArray jsonItems = jsonLayer.getAsArray(ITEMS);
      final IStringBuilder layersName = IStringBuilder.newStringBuilder();

      for (int i = 0; i < jsonItems.size(); i++) {
         if (jsonItems.getAsObject(i).getAsBoolean(STATUS).value()) {
            layersName.addString(jsonItems.getAsObject(i).getAsString(NAME).value());
            layersName.addString(",");
         }
      }
      String layersSecuence = layersName.getString();
      if (layersName.getString().length() > 0) {
         layersSecuence = IStringUtils.instance().substring(layersSecuence, 0, layersSecuence.length() - 1);
      }

      //TODO check if wms 1.1.1 is neccessary to have it in account
      WMSServerVersion wmsVersion = WMSServerVersion.WMS_1_1_0;
      if (jsonVersion.compareTo(WMS130) == 0) {
         wmsVersion = WMSServerVersion.WMS_1_3_0;
      }


      final WMSLayer wmsLayer = new WMSLayer(URL.escape(layersSecuence), new URL(jsonURL, false), wmsVersion, "", null,
               wmsVersion, Sector.fullSphere(), "image/png", "EPSG:4326", "", true, null, null, true);
      layerSet.addLayer(wmsLayer);
   }


   private void parserJSON3DLayer(final LayerSet layerSet,
                                  final JSONObject jsonLayer) {
      System.out.print("Parsing 3D Layer ");
      System.out.print(jsonLayer.getAsString(NAME).value());
      System.out.print("...");
      System.out.print("\n");
   }


   private void parserJSONPanoLayer(final LayerSet layerSet,
                                    final JSONObject jsonLayer) {
      System.out.print("Parsing Pano Layer ");
      System.out.print(jsonLayer.getAsString(NAME).value());
      System.out.print("...");
      System.out.print("\n");
   }


   private void parserGEOJSONLayer(final LayerSet layerSet,
                                   final JSONObject jsonLayer) {
      System.out.print("Parsing GEOJSON Layer ");
      System.out.print(jsonLayer.getAsString(NAME).value());
      System.out.print("...");
      System.out.print("\n");

      final String geojsonDatasource = jsonLayer.getAsString(DATASOURCE).value();

      final JSONArray jsonItems = jsonLayer.getAsArray(ITEMS);
      for (int i = 0; i < jsonItems.size(); i++) {

         final String namefile = jsonItems.getAsObject(i).getAsString(NAME).value();
         final String icon = jsonItems.getAsObject(i).getAsString(ICON).value();

         final IStringBuilder url = IStringBuilder.newStringBuilder();
         url.addString(geojsonDatasource);
         url.addString("/");
         url.addString(namefile);

         _mapGeoJSONSources.put(url.getString(), icon);
      }
   }


   protected SceneParser() {
      _mapLayerType.put("WMS", layer_type.WMS);
      _mapLayerType.put("THREED", layer_type.THREED);
      _mapLayerType.put("PANO", layer_type.PANO);
      _mapLayerType.put("GEOJSON", layer_type.GEOJSON);
   }
   //C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
   //	SceneParser(SceneParser NamelessParameter);
   //C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
   //	SceneParser operator = (SceneParser NamelessParameter);

}
