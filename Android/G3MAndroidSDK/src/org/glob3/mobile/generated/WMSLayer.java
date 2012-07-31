package org.glob3.mobile.generated; 
//
//  WMSLayer.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 18/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  WMSLayer.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 18/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



public class WMSLayer extends Layer
{

  private final String _name;
  private final String _format;
  private final String _style;
  private final String _srs;
  private Sector _bbox ;


  private final String _serverURL;
  private final String _serverVersion;


  public WMSLayer(String name, String serverURL, String serverVer, String format, Sector bbox, String srs, String style)
  {
	  _name = name;
	  _format = format;
	  _style = style;
	  _bbox = new Sector(bbox);
	  _srs = srs;
	  _serverURL = serverURL;
	  _serverVersion = serverVer;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean fullContains(const Sector& s) const
  public final boolean fullContains(Sector s)
  {
	return _bbox.fullContains(s);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: java.util.ArrayList<Petition*> getTilePetitions(const Tile& tile, int width, int height) const
  public final java.util.ArrayList<Petition> getTilePetitions(Tile tile, int width, int height)
  {
	java.util.ArrayList<Petition> vPetitions = new java.util.ArrayList<Petition>();
  
	if (!_bbox.fullContains(tile.getSector()))
	{
	  return vPetitions;
	}
  
	  //Server name
	String req = _serverURL;
	  if (req.charAt(req.length()-1) != '?')
	  {
		  req += '?';
	  }
  
	//If the server refer to itself as localhost...
	int pos = req.indexOf("localhost");
	if (pos != -1)
	{
	  req = req.substring(pos+9);
  
	  int pos2 = this._serverURL.indexOf("/", 8);
	  String newHost = this._serverURL.substring(0, pos2);
  
	  req = newHost + req;
	}
  
	  //Petition
	if (!_serverVersion.equals(""))
	  req += "REQUEST=GetMap&SERVICE=WMS&VERSION=" + _serverVersion;
	else
	  req += "REQUEST=GetMap&SERVICE=WMS&VERSION=1.1.1";
  
	  //Layer
	req += "&LAYERS=" + _name;
  
	  //Format
	  req += "&FORMAT=" + this._format;
  
	  //Ref. system
	if (!_srs.equals(""))
	  req += "&SRS=" + _srs;
	  else
	  req += "&SRS=EPSG:4326";
  
	//Style
	if (!_style.equals(""))
	  req += "&STYLES=" + _style;
	  else
	  req += "&STYLES=";
  
	//ASKING TRANSPARENCY
	req += "&TRANSPARENT=TRUE";
  
	Sector sector = tile.getSector();
  
	  //Texture Size and BBOX
	std.ostringstream oss = new std.ostringstream();
	oss << "&WIDTH=" << width << "&HEIGHT=" << height;
	oss << "&BBOX=" << sector.lower().longitude().degrees() << "," << sector.lower().latitude().degrees();
	oss << "," << sector.upper().longitude().degrees() << "," << sector.upper().latitude().degrees();
	String sizeAndBBox = oss.str();
	req += oss.str();
  
	if (_serverVersion.equals("1.3.0"))
	{
	  req += "&CRS=EPSG:4326";
	}
  
	Petition pet = new Petition(sector, req);
	vPetitions.add(pet);
  
	  return vPetitions;
  }

}