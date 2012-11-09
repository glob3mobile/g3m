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
	private static final String CLASE = "CLASE";


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
		JSONObject json = IJSONParser.instance().parse(String).asObject();
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
		JSONArray jsonFeatures = geojson.get(FEATURES).asArray();
		for (int i = 0; i < jsonFeatures.size(); i++)
		{
			JSONObject jsonFeature = jsonFeatures.getAsObject(i);
			JSONObject jsonGeometry = jsonFeature.getAsObject(GEOMETRY);
			String jsonType = jsonGeometry.getAsString(TYPE).value();
			if (jsonType.equals("Point"))
			{
				parsePointObject(jsonFeature);
			}
		}
	}
	private void parsePointObject(JSONObject point)
	{
		JSONObject jsonProperties = point.getAsObject(PROPERTIES);
		JSONObject jsonGeometry = point.getAsObject(GEOMETRY);
		JSONArray jsonCoordinates = jsonGeometry.getAsArray(COORDINATES);
    
		JSONBaseObject denominaci = jsonProperties.get(DENOMINATION);
		JSONBaseObject clase = jsonProperties.get(CLASE);
    
		if (denominaci != null && clase != null)
		{
    
			IStringBuilder iconUrl = IStringBuilder.newStringBuilder();
			iconUrl.addString("http://glob3m.glob3mobile.com/icons/markers/ayto/");
			iconUrl.addString(_icon);
			iconUrl.addString(".png");
    
			IStringBuilder name = IStringBuilder.newStringBuilder();
			name.addString(clase.asString().value());
			name.addString(" ");
			name.addString(denominaci.asString().value());
    
			final Angle latitude = Angle.fromDegrees(jsonCoordinates.getAsNumber(1).doubleValue());
			final Angle longitude = Angle.fromDegrees(jsonCoordinates.getAsNumber(0).doubleValue());
    
			Mark mark = new Mark(name.getString(), new URL(iconUrl.getString(),false), new Geodetic3D(latitude, longitude, 0),null,10000);
    
			_marksRenderer.addMark(mark);
		}
	}

}