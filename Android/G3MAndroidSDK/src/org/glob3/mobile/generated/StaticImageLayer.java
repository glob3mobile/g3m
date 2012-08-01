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
	if (_image != null)
		_image.dispose();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean fullContains(const Sector& s) const
  public final boolean fullContains(Sector s)
  {
	return _bbox.fullContains(s);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: java.util.ArrayList<Petition*> getTilePetitions(const IFactory& factory, const Tile& tile, int width, int height) const
  public final java.util.ArrayList<Petition> getTilePetitions(IFactory factory, Tile tile, int width, int height)
  {
	java.util.ArrayList<Petition> res = new java.util.ArrayList<Petition>();
  
	if (!_bbox.fullContains(tile.getSector()))
	{
	  return res;
	}
  
	Sector imageSector = tile.getSector();
  
	//CREATING ID FOR PETITION
	String id = factory.stringFormat("%s_%f_%f_%f_%f", _layerID, imageSector.lower().latitude().degrees(), imageSector.lower().longitude().degrees(), imageSector.upper().latitude().degrees(), imageSector.upper().longitude().degrees());
  
	Petition pet = new Petition(tile.getSector(), id);
  
	if (_storage != null)
	{
	  if (_storage.contains(id))
	  {
		ByteBuffer bb = _storage.read(id);
		pet.setByteBuffer(bb); //FILLING DATA
		res.add(pet);
		return res;
	  }
	}
  
	double widthUV = imageSector.getDeltaLongitude().degrees() / _bbox.getDeltaLongitude().degrees();
	double heightUV = imageSector.getDeltaLatitude().degrees() / _bbox.getDeltaLatitude().degrees();
  
	Vector2D p = _bbox.getUVCoordinates(imageSector.lower().latitude(), imageSector.lower().longitude());
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

}