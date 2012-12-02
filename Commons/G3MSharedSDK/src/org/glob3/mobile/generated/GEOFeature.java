package org.glob3.mobile.generated; 
//
//  GEOFeature.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/29/12.
//
//

//
//  GEOFeature.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/29/12.
//
//




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEOGeometry;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class JSONBaseObject;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class JSONObject;

public class GEOFeature extends GEOObject
{
  private final JSONBaseObject _id;
  private GEOGeometry _geometry;
  private final JSONObject _properties;


  public GEOFeature(JSONBaseObject id, GEOGeometry geometry, JSONObject properties)
  {
	  _id = id;
	  _geometry = geometry;
	  _properties = properties;

  }

  public void dispose()
  {
	if (_id != null)
		_id.dispose();
	if (_geometry != null)
		_geometry.dispose();
	if (_properties != null)
		_properties.dispose();
  }

  public final void render(G3MRenderContext rc)
  {
	_geometry.render(rc);
  }

}