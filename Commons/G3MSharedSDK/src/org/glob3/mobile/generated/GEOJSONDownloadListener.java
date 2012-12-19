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
	private static final String URLICON = "URLICON";
	private static final String URLWEB = "URL";


	private MarksRenderer _marksRenderer;
	private String _icon;
	private double _minDistance;

	public GEOJSONDownloadListener(MarksRenderer marksRenderer, String icon, double minDistance)
	{
		_marksRenderer = marksRenderer;
		_icon = icon;
		_minDistance = minDistance;
	}

	public final void onDownload(URL url, IByteBuffer buffer)
	{
		String String = buffer.getAsString();
		JSONBaseObject json = IJSONParser.instance().parse(String);
		ILogger.instance().logInfo(url.getPath());
		parseGEOJSON(json.asObject());
		IJSONParser.instance().deleteJSONData(json);
    
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
		final JSONArray jsonFeatures = geojson.get(FEATURES).asArray();
		for (int i = 0; i < jsonFeatures.size(); i++)
		{
			final JSONObject jsonFeature = jsonFeatures.getAsObject(i);
			final JSONObject jsonGeometry = jsonFeature.getAsObject(GEOMETRY);
			String jsonType = jsonGeometry.getAsString(TYPE).value();
			if (jsonType.equals("Point"))
			{
				parsePointObject(jsonFeature);
			}
		}
	}
	private void parsePointObject(JSONObject point)
	{
		final JSONObject jsonProperties = point.getAsObject(PROPERTIES);
		final JSONObject jsonGeometry = point.getAsObject(GEOMETRY);
		final JSONArray jsonCoordinates = jsonGeometry.getAsArray(COORDINATES);
    
		final Angle latitude = Angle.fromDegrees(jsonCoordinates.getAsNumber(1).doubleValue());
		final Angle longitude = Angle.fromDegrees(jsonCoordinates.getAsNumber(0).doubleValue());
    
		final JSONBaseObject denominaci = jsonProperties.get(DENOMINATION);
		final JSONBaseObject clase = jsonProperties.get(CLASE);
    
		Mark mark;
    
		if (denominaci != null && clase != null)
		{
    
			IStringBuilder name = IStringBuilder.newStringBuilder();
			name.addString(IStringUtils.instance().capitalize(clase.asString().value()));
			name.addString(" ");
			name.addString(denominaci.asString().value());
    
			URL url;
			if (jsonProperties.getAsString(URLWEB)!= null || jsonProperties.getAsString(URLWEB).value().length() == 0)
			{
				url = new URL(jsonProperties.getAsString(URLWEB).value(),false);
			}
			else
			{
				url = new URL("http://sig.caceres.es/SerWeb/fichatoponimo.asp?mslink=0",false);
			}
    
			if (_icon.length() > 0)
			{
				mark = new Mark(name.getString(), new URL(_icon,false), new Geodetic3D(latitude, longitude, 0), url, _minDistance, null);
			}
			else
			{
				mark = new Mark(name.getString(), new URL("http://glob3m.glob3mobile.com/icons/markers/g3m.png",false), new Geodetic3D(latitude, longitude, 0), url, _minDistance, null);
			}
		}
		else
		{
			mark = new Mark("Unknown POI", new URL("http://glob3m.glob3mobile.com/icons/markers/g3m.png",false), new Geodetic3D(latitude, longitude, 0), null, _minDistance, null);
		}
		_marksRenderer.addMark(mark);
	}

}