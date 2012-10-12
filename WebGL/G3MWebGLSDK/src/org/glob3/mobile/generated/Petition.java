package org.glob3.mobile.generated; 
//
//  Petition.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 20/08/12.
//
//

//
//  Petition.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 20/08/12.
//
//




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Tile;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Rectangle;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Sector;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IFactory;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class MutableVector2D;

public class Petition
{
  private final Sector _sector;
  final private URL _url; //Conversor creates class "Url"
  private IImage _image;

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  Petition(Petition that);

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void operator =(Petition that);


  public Petition(Sector sector, URL url)
  {
	  _sector = new Sector(sector);
	  _url = url;
	  _image = null;
  }

  public void dispose()
  {
	releaseImage();
  }

  public final void releaseImage()
  {
	if (_image != null)
	{
	  _image = null;
	  _image = null;
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean hasImage() const
  public final boolean hasImage()
  {
	return (_image != null);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const URL getURL() const
  public final URL getURL()
  {
	return _url;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Sector getSector() const
  public final Sector getSector()
  {
	return _sector;
  }

  public final void setImage(IImage image)
  {
	releaseImage();
	_image = image;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const IImage* getImage() const
  public final IImage getImage()
  {
	return _image;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String description() const
  public final String description()
  {
	IStringBuilder isb = IStringBuilder.newStringBuilder();
	isb.addString("Petition(url=");
	isb.addString(_url.description());
	isb.addString(", sector=");
	isb.addString(_sector.description());
	isb.addString(", buffer=");
	if (_image == null)
	{
	  isb.addString("NULL");
	}
	else
	{
	  isb.addString(_image.description());
	}
  
	final String s = isb.getString();
	if (isb != null)
		isb.dispose();
	return s;
  }

}