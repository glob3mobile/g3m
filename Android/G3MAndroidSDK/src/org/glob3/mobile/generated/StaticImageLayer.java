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
  private Sector _bbox ;
  private final IImage _image;
  private final String _layerID;
  private final IStorage _storage;

  public StaticImageLayer(String layerID, IImage image, Sector sector, IStorage storage)
  {
	  _image = image;
	  _bbox = new Sector(sector);
	  _layerID = layerID;
	  _storage = storage;

  }

  public void dispose()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean fullContains(const Sector& s) const
  public final boolean fullContains(Sector s)
  {
	return _bbox.fullContains(s);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: java.util.ArrayList<Petition*> getTilePetitions(const RenderContext* rc, const Tile* tile, int width, int height) const
  public final java.util.ArrayList<Petition> getTilePetitions(RenderContext rc, Tile tile, int width, int height)
  {
	java.util.ArrayList<Petition> res = new java.util.ArrayList<Petition>();
  
	Sector tileSector = tile.getSector();
  
	if (!_bbox.fullContains(tileSector))
	{
	  return res;
	}
  
	//CREATING ID FOR PETITION
  
	IStringBuilder isb = IStringBuilder.newStringBuilder();
	isb.add(_layerID).add("_").add(tileSector.lower().latitude().degrees());
	isb.add("_").add(tileSector.lower().longitude().degrees());
	isb.add("_").add(tileSector.upper().latitude().degrees());
	isb.add("_").add(tileSector.upper().longitude().degrees());
  
  
	final URL id = new URL(isb.getString());
  
	Petition pet = new Petition(tileSector, id, true);
  
	if (_storage != null)
	{
	  if (_storage.contains(id))
	  {
		final ByteBuffer bb = _storage.read(id);
		pet.setByteBuffer(bb); //FILLING DATA
		res.add(pet);
		return res;
	  }
	}
  
	double widthUV = tileSector.getDeltaLongitude().degrees() / _bbox.getDeltaLongitude().degrees();
	double heightUV = tileSector.getDeltaLatitude().degrees() / _bbox.getDeltaLatitude().degrees();
  
	Vector2D p = _bbox.getUVCoordinates(tileSector.lower().latitude(), tileSector.lower().longitude());
	Vector2D pos = new Vector2D(p.x(), p.y() - heightUV);
  
	Rectangle r = new Rectangle(pos.x() * _image.getWidth(), pos.y() * _image.getHeight(), widthUV * _image.getWidth(), heightUV * _image.getHeight());
  
	IImage subImage = _image.subImage(r);
  
	ByteBuffer bb = subImage.getEncodedImage(); //Image Encoding PNG
	pet.setByteBuffer(bb); //FILLING DATA
	if (subImage != null)
		subImage.dispose();
  
	res.add(pet);
  
	if (_storage != null)
	{
	  _storage.save(id, bb);
	}
  
	return res;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isAvailable(const RenderContext* rc, const Tile* tile)const
  public final boolean isAvailable(RenderContext rc, Tile tile)
  {
	return true;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isTransparent() const
  public final boolean isTransparent()
  {
	return true;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: URL getFeatureURL(const Geodetic2D& g, const IFactory* factory, const Sector& sector, int width, int height) const
  public final URL getFeatureURL(Geodetic2D g, IFactory factory, Sector sector, int width, int height)
  {
	return URL.nullURL();
  }

}