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
  private URL _url = new URL(); //Conversor creates class "Url"
  private ByteArrayWrapper _buffer;
  private final Sector _sector;


  public Petition(Sector sector, URL url)
  {
	  _sector = new Sector(sector);
	  _url = url;
	  _buffer = null;
  }

  public void dispose()
  {
	releaseBuffer();
  }

  public final void releaseBuffer()
  {
	if (_buffer != null)
	{
	  _buffer = null;
	  _buffer = null;
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean hasByteArrayWrapper() const
  public final boolean hasByteArrayWrapper()
  {
	return _buffer != null;
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

  public final void setByteArrayWrapper(ByteArrayWrapper buffer)
  {
	releaseBuffer();
	_buffer = buffer;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const ByteArrayWrapper* getByteArrayWrapper() const
  public final ByteArrayWrapper getByteArrayWrapper()
  {
	return _buffer;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String description() const
  public final String description()
  {
  
	IStringBuilder isb = IStringBuilder.newStringBuilder();
	isb.add("Petition(url=").add(_url.description()).add(", sector=").add(_sector.description()).add(", buffer=");
	if (_buffer == null)
	{
	  isb.add("NULL");
	}
	else
	{
	  isb.add(_buffer.description());
	}
	String s = isb.getString();
  
	if (isb != null)
		isb.dispose();
	return s;
  }

}