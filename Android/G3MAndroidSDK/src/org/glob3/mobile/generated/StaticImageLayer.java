package org.glob3.mobile.generated; 
//
//  StaticImageLayer.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 26/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  StaticImageLayer.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 26/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IStorage;

public class StaticImageLayer extends Layer
{
  private Sector _sector ;
  private final IImage _image;
  private final String _layerID;
  private final IStorage _storage;

  private final boolean _saveInBackground;


  public StaticImageLayer(String layerID, IImage image, Sector sector, IStorage storage, LayerCondition condition, boolean saveInBackground)
  {
	  super(condition);
	  _image = image;
	  _sector = new Sector(sector);
	  _layerID = layerID;
	  _storage = storage;
	  _saveInBackground = saveInBackground;

  }

  public void dispose()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: java.util.ArrayList<Petition*> getMapPetitions(const RenderContext* rc, const Tile* tile, int width, int height) const
  public final java.util.ArrayList<Petition> getMapPetitions(RenderContext rc, Tile tile, int width, int height)
  {
	java.util.ArrayList<Petition> res = new java.util.ArrayList<Petition>();
  
	Sector tileSector = tile.getSector();
  
	if (!_sector.fullContains(tileSector))
	{
	  return res;
	}
  
	//CREATING ID FOR PETITION
	IStringBuilder isb = IStringBuilder.newStringBuilder();
	isb.addString(_layerID);
	isb.addString("_");
	isb.addDouble(tileSector.lower().latitude().degrees());
	isb.addString("_");
	isb.addDouble(tileSector.lower().longitude().degrees());
	isb.addString("_");
	isb.addDouble(tileSector.upper().latitude().degrees());
	isb.addString("_");
	isb.addDouble(tileSector.upper().longitude().degrees());
  
	final URL id = new URL(isb.getString());
  
	if (isb != null)
		isb.dispose();
  
	Petition pet = new Petition(tileSector, id);
  
	if (_storage != null)
	{
	  if (_storage.containsImage(id))
	  {
		IImage image = _storage.readImage(id);
		pet.setImage(image); //FILLING DATA
		res.add(pet);
		return res;
	  }
	}
  
	final double widthUV = tileSector.getDeltaLongitude().degrees() / _sector.getDeltaLongitude().degrees();
	final double heightUV = tileSector.getDeltaLatitude().degrees() / _sector.getDeltaLatitude().degrees();
  
	final Vector2D p = _sector.getUVCoordinates(tileSector.lower());
	final Vector2D pos = new Vector2D(p._x, p._y - heightUV);
  
	Rectangle r = new Rectangle(pos._x * _image.getWidth(), pos._y * _image.getHeight(), widthUV * _image.getWidth(), heightUV * _image.getHeight());
  
	final IImage subImage = _image.subImage(r);
  
	pet.setImage(subImage);
  
	res.add(pet);
  
	if (_storage != null)
	{
	  _storage.saveImage(id, subImage, _saveInBackground);
	}
  
	return res;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isTransparent() const
  public final boolean isTransparent()
  {
	return true;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: URL getFeatureInfoURL(const Geodetic2D& g, const IFactory* factory, const Sector& sector, int width, int height) const
  public final URL getFeatureInfoURL(Geodetic2D g, IFactory factory, Sector sector, int width, int height)
  {
	return URL.nullURL();
  }


}