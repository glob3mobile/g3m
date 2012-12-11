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
	String jsonBaseLayer = jsonWorld.getAsString(BASELAYER).value();
	JSONArray jsonBbox = jsonWorld.getAsArray(BBOX);
  
	if (jsonBaseLayer.equals("BING"))
	{
	  WMSLayer bing = new WMSLayer("ve", new URL("http://worldwind27.arc.nasa.gov/wms/virtualearth?",true), WMSServerVersion.WMS_1_1_0, Sector.fromDegrees(jsonBbox.getAsNumber(1).doubleValue(), jsonBbox.getAsNumber(0).doubleValue(), jsonBbox.getAsNumber(3).doubleValue(), jsonBbox.getAsNumber(2).doubleValue()), "image/jpeg", "EPSG:4326", "", false, null);
	  layerSet.addLayer(bing);
	}
	else
	{
	  WMSLayer osm = new WMSLayer("osm", new URL("http://wms.latlon.org/",true), WMSServerVersion.WMS_1_1_0, Sector.fromDegrees(jsonBbox.getAsNumber(1).doubleValue(), jsonBbox.getAsNumber(0).doubleValue(), jsonBbox.getAsNumber(3).doubleValue(), jsonBbox.getAsNumber(2).doubleValue()), "image/jpeg", "EPSG:4326", "", false, null);
	  layerSet.addLayer(osm);
	}
	parseCustomData(marks, jsonWorld.getAsObject(CUSTOMDATA));
  }
  private void parseGEOJSONPointObject(MarksRenderer marks, JSONObject point)
  {
	  JSONObject jsonProperties = point.getAsObject(PROPERTIES);
	  JSONObject jsonGeometry = point.getAsObject(GEOMETRY);
	  JSONArray jsonCoordinates = jsonGeometry.getAsArray(COORDINATES);
  
	  Mark mark = new Mark(jsonProperties.getAsString(NAME).value(), new URL("http://glob3m.glob3mobile.com/icons/markers/g3m.png",false), new Geodetic3D(Angle.fromDegrees(jsonCoordinates.getAsNumber(1).doubleValue()), Angle.fromDegrees(jsonCoordinates.getAsNumber(0).doubleValue()), 0), null, 0, null);
  
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
	JSONObject json = IJSONParser.instance().parse(namelessParameter).asObject();
	parseWorldConfiguration(layerSet, marks, json.get(WORLD).asObject());
	IJSONParser.instance().deleteJSONData(json);
  }
	public final void parseCustomData(MarksRenderer marks, JSONObject jsonCustomData)
	{
	  JSONArray jsonFeatures = jsonCustomData.getAsArray(FEATURES);
	  for (int i = 0; i < jsonFeatures.size(); i++)
	  {
		JSONObject jsonFeature = jsonFeatures.getAsObject(i);
		  JSONObject jsonGeometry = jsonFeature.getAsObject(GEOMETRY);
		String jsonType = jsonGeometry.getAsString(TYPE).value();
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