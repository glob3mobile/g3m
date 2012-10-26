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



public class AppParser
{

	private static final String WORLD = "_world";
	private static final String BASELAYER = "_baselayer";
	private static final String BBOX = "_bbox";
	private static final String CUSTOMDATA = "_customdata";

	private static final String FEATURES = "features";
	private static final String GEOMETRY = "geometry";
	private static final String TYPE = "type";
	private static final String COORDINATES = "coordinates";
	private static final String PROPERTIES = "properties";
	private static final String NAME = "name";

	private static AppParser _instance = null;


  private void parseWorldConfiguration(LayerSet layerSet, MarksRenderer marks, JSONObject jsonWorld)
  {
	String jsonBaseLayer = jsonWorld.getObjectForKey(BASELAYER).getString().getValue();
	JSONArray jsonBbox = jsonWorld.getObjectForKey(BBOX).getArray();
  
	if (jsonBaseLayer.equals("BING"))
	{
	  WMSLayer bing = new WMSLayer("ve", new URL("http://worldwind27.arc.nasa.gov/wms/virtualearth?",true), WMSServerVersion.WMS_1_1_0, Sector.fromDegrees(jsonBbox.getElement(1).getNumber().getDoubleValue(), jsonBbox.getElement(0).getNumber().getDoubleValue(), jsonBbox.getElement(3).getNumber().getDoubleValue(), jsonBbox.getElement(2).getNumber().getDoubleValue()), "image/jpeg", "EPSG:4326", "", false, null);
	  layerSet.addLayer(bing);
	}
	else
	{
	  WMSLayer osm = new WMSLayer("osm", new URL("http://wms.latlon.org/",true), WMSServerVersion.WMS_1_1_0, Sector.fromDegrees(jsonBbox.getElement(1).getNumber().getDoubleValue(), jsonBbox.getElement(0).getNumber().getDoubleValue(), jsonBbox.getElement(3).getNumber().getDoubleValue(), jsonBbox.getElement(2).getNumber().getDoubleValue()), "image/jpeg", "EPSG:4326", "", false, null);
	  layerSet.addLayer(osm);
	}
	parseCustomData(marks, jsonWorld.getObjectForKey(CUSTOMDATA).getObject());
  }
  private void parseGEOJSONPointObject(MarksRenderer marks, JSONObject point)
  {
	  JSONObject jsonProperties = point.getObjectForKey(PROPERTIES).getObject();
	  JSONObject jsonGeometry = point.getObjectForKey(GEOMETRY).getObject();
	  JSONArray jsonCoordinates = jsonGeometry.getObjectForKey(COORDINATES).getArray();
  
	  Mark mark = new Mark(jsonProperties.getObjectForKey(NAME).getString().getValue(), new URL("http://glob3m.glob3mobile.com/icons/markers/g3m.png",false), new Geodetic3D(Angle.fromDegrees(jsonCoordinates.getElement(1).getNumber().getDoubleValue()), Angle.fromDegrees(jsonCoordinates.getElement(0).getNumber().getDoubleValue()), 0));
	  marks.addMark(mark);
  }


  public static AppParser instance()
  {
	if (_instance == null)
	{
	  _instance = new AppParser();
	}
	return _instance;
  }
  public final void parse(LayerSet layerSet, MarksRenderer marks, String namelessParameter)
  {
	JSONObject json = IJSONParser.instance().parse(namelessParameter).getObject();
	parseWorldConfiguration(layerSet, marks, json.getObjectForKey(WORLD).getObject());
	IJSONParser.instance().deleteJSONData(json);
  }
	public final void parseCustomData(MarksRenderer marks, JSONObject jsonCustomData)
	{
	  JSONArray jsonFeatures = jsonCustomData.getObjectForKey(FEATURES).getArray();
	  for (int i = 0; i < jsonFeatures.getSize(); i++)
	  {
		JSONObject jsonFeature = jsonFeatures.getElement(i).getObject();
		  JSONObject jsonGeometry = jsonFeature.getObjectForKey(GEOMETRY).getObject();
		String jsonType = jsonGeometry.getObjectForKey(TYPE).getString().getValue();
		if (jsonType.equals("Point"))
		{
		  parseGEOJSONPointObject(marks, jsonFeature);
		}
	  }
	}


  protected AppParser()
  {
  }
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  AppParser(AppParser NamelessParameter);
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  AppParser operator = (AppParser NamelessParameter);

}