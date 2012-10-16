package org.glob3.mobile.generated; 
public class SceneParser
{

  private static SceneParser _instance = null;
  private java.util.HashMap<String, layer_type> mapLayerType = new java.util.HashMap<String, layer_type>();


  public static SceneParser instance()
  {
	if (_instance == null)
	{
	  _instance = new SceneParser();
	}
	return _instance;
  }
  public final void parse(LayerSet layerSet, String namelessParameter)
  {
  
	JSONObject json = IJSONParser.instance().parse(namelessParameter).getObject();
	parserJSONLayerList(layerSet, json.getObjectForKey(GlobalMembersSceneParser.layers).getObject());
	IJSONParser.instance().deleteJSONData(json);
  }
  public final void parserJSONLayerList(LayerSet layerSet, JSONObject jsonLayers)
  {
	for (int i = 0; i < jsonLayers.getObject().getSize(); i++)
	{
	  IStringBuilder isb = IStringBuilder.newStringBuilder();
	  isb.add(i);
	  JSONObject jsonLayer = jsonLayers.getObjectForKey(isb.getString()).getObject();
	  final layer_type layerType = mapLayerType.get(jsonLayer.getObjectForKey(GlobalMembersSceneParser.type).getString().getValue());
  
	  switch (layerType)
	  {
		case WMS:
		  parserJSONWMSLayer(layerSet, jsonLayer);
		  break;
		case THREED:
		  parserJSON3DLayer(layerSet, jsonLayer);
		  break;
		case PANO:
		  parserJSONPanoLayer(layerSet, jsonLayer);
		  break;
	  }
  
	}
  }
  public final void parserJSONWMSLayer(LayerSet layerSet, JSONObject jsonLayer)
  {
	System.out.print("Parsing WMS Layer ");
	System.out.print(jsonLayer.getObjectForKey(GlobalMembersSceneParser.name).getString().getValue());
	System.out.print("...");
	System.out.print("\n");
  
	final String jsonDatasource = jsonLayer.getObjectForKey(GlobalMembersSceneParser.datasource).getString().getValue();
	final int lastIndex = IStringUtils.instance().indexOf(jsonDatasource, "?");
	final String jsonURL = IStringUtils.instance().substring(jsonDatasource, 0, lastIndex+1);
	final String jsonVersion = jsonLayer.getObjectForKey(GlobalMembersSceneParser.version).getString().getValue();
  
	JSONArray jsonItems = jsonLayer.getObjectForKey(GlobalMembersSceneParser.items).getArray();
	IStringBuilder layersName = IStringBuilder.newStringBuilder();
  
	for (int i = 0; i<jsonItems.getSize(); i++)
	{
	  if (jsonItems.getElement(i).getObject().getObjectForKey(GlobalMembersSceneParser.status).getString().getValue().equals("true"))
	  {
		layersName.add(jsonItems.getElement(i).getObject().getObjectForKey(GlobalMembersSceneParser.name).getString().getValue());
		layersName.add(",");
	  }
	}
	String layersSecuence = layersName.getString();
	if (layersName.getString().length() > 0)
	{
	  layersSecuence = IStringUtils.instance().substring(layersSecuence, 0, layersSecuence.length()-1);
	}
  
	//TODO check if wms 1.1.1 is neccessary to have it in account
	WMSServerVersion wmsVersion = WMSServerVersion.WMS_1_1_0;
	if (jsonVersion.compareTo(GlobalMembersSceneParser.wms130)==0)
	{
	  wmsVersion = WMSServerVersion.WMS_1_3_0;
	}
  
	WMSLayer wmsLayer = new WMSLayer(layersSecuence, new URL(jsonURL), wmsVersion, Sector.fullSphere(), "image/png", "EPSG:4326", "", true, null);
	layerSet.addLayer(wmsLayer);
  }
  public final void parserJSON3DLayer(LayerSet layerSet, JSONObject jsonLayer)
  {
	System.out.print("Parsing 3D Layer ");
	System.out.print(jsonLayer.getObjectForKey(GlobalMembersSceneParser.name).getString().getValue());
	System.out.print("...");
	System.out.print("\n");
  }
  public final void parserJSONPanoLayer(LayerSet layerSet, JSONObject jsonLayer)
  {
	System.out.print("Parsing Pano Layer ");
	System.out.print(jsonLayer.getObjectForKey(GlobalMembersSceneParser.name).getString().getValue());
	System.out.print("...");
	System.out.print("\n");
  }

  protected SceneParser()
  {
	mapLayerType.put("WMS", layer_type.WMS);
	mapLayerType.put("THREED", layer_type.THREED);
	mapLayerType.put("PANO", layer_type.PANO);
  }
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  SceneParser(SceneParser NamelessParameter);
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  SceneParser operator = (SceneParser NamelessParameter);

}