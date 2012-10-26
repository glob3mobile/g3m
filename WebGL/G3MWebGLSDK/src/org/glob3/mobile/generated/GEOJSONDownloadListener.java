package org.glob3.mobile.generated; 
//
//  SceneParserDownloadListener.cpp
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 26/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


//
//  SceneParserDownloadListener.h
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 26/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class MarksRenderer;
public class GEOJSONDownloadListener implements IBufferDownloadListener
{

	private static final String FEATURES = "features";
	private static final String GEOMETRY = "geometry";
	private static final String TYPE = "type";
	private static final String COORDINATES = "coordinates";
	private static final String PROPERTIES = "properties";
	private static final String DENOMINATION = "DENOMINACI";


	private MarksRenderer _marksRenderer;
	private String _icon;

	public GEOJSONDownloadListener(MarksRenderer marksRenderer, String icon)
	{
		_marksRenderer = marksRenderer;
		_icon = icon;
	}

	public final void onDownload(URL url, IByteBuffer buffer)
	{
		String String = buffer.getAsString();
		JSONObject json = IJSONParser.instance().parse(String).getObject();
		ILogger.instance().logInfo(url.getPath());
		parseGEOJSON(json);
    
	}

	public final void onError(URL url)
	{
		ILogger.instance().logError("The requested geojson file could not be found!");
	}

	public final void onCancel(URL url)
	{
	}
	public final void onCanceledDownload(URL url, IByteBuffer data)
	{
	}

	public void dispose()
	{
	}
	private void parseGEOJSON(JSONObject geojson)
	{
		JSONArray jsonFeatures = geojson.getObjectForKey(FEATURES).getArray();
		for (int i = 0; i < jsonFeatures.getSize(); i++)
		{
			JSONObject jsonFeature = jsonFeatures.getElement(i).getObject();
			JSONObject jsonGeometry = jsonFeature.getObjectForKey(GEOMETRY).getObject();
			String jsonType = jsonGeometry.getObjectForKey(TYPE).getString().getValue();
			if (jsonType.equals("Point"))
			{
				parsePointObject(jsonFeature);
			}
		}
	}
	private void parsePointObject(JSONObject point)
	{
		JSONObject jsonProperties = point.getObjectForKey(PROPERTIES).getObject();
		JSONObject jsonGeometry = point.getObjectForKey(GEOMETRY).getObject();
		JSONArray jsonCoordinates = jsonGeometry.getObjectForKey(COORDINATES).getArray();
    
		JSONBaseObject denominaci = jsonProperties.getObjectForKey(DENOMINATION);
		if (denominaci != null)
		{
			ILogger.instance().logInfo(denominaci.getString().getValue());
			IStringBuilder iconUrl = IStringBuilder.newStringBuilder();
			iconUrl.addString("http://glob3m.glob3mobile.com/icons/markers/ayto/");
			iconUrl.addString(_icon);
			iconUrl.addString(".png");
    
			Mark mark = new Mark(denominaci.getString().getValue(), new URL(iconUrl.getString(),false), new Geodetic3D(Angle.fromDegrees(jsonCoordinates.getElement(1).getNumber().getDoubleValue()), Angle.fromDegrees(jsonCoordinates.getElement(0).getNumber().getDoubleValue()), 0));
    
			_marksRenderer.addMark(mark);
		}
	}

}