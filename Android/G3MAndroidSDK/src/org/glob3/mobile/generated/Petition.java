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
  private Url _url = new URL();
  private final Sector _sector;
  private final ByteBuffer _buffer;
  private int _downloadID;
  private final boolean _transparentImage;


  public Petition(Sector sector, URL url, boolean transparent)
  {
	  _sector = new Sector(sector);
	  _url = new URL(url);
	  _transparentImage = transparent;
	  _buffer = null;
	  _downloadID = -1;
  }

  public void dispose()
  {
	if (_sector != null)
		_sector.dispose();
	releaseData();
  }

  public final void releaseData()
  {
	if (_buffer != null)
	{
	  if (_buffer != null)
		  _buffer.dispose();
	  _buffer = null;
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getDownloadID() const
  public final int getDownloadID()
  {
	return _downloadID;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean hasByteBuffer() const
  public final boolean hasByteBuffer()
  {
	return _buffer != null;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isTransparent() const
  public final boolean isTransparent()
  {
	return _transparentImage;
  }

  public final void setDownloadID(int id)
  {
	_downloadID = id;
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

  public final void setByteBuffer(ByteBuffer buffer)
  {
	if (_buffer != null)
	{
	  if (_buffer != null)
		  _buffer.dispose();
	}
	_buffer = buffer;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const ByteBuffer* getByteBuffer() const
  public final ByteBuffer getByteBuffer()
  {
	return _buffer;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String description() const
  public final String description()
  {
	std.ostringstream buffer = new std.ostringstream();
	buffer << "Petition(url=";
	buffer << _url.description();
	buffer << ", sector=";
	buffer << _sector.description();
	buffer << ", buffer=";
	if (_buffer == null)
	{
	  buffer << "NULL";
	}
	else
	{
	  buffer << _buffer.description();
	}
	buffer << ", downloadID=";
	buffer << _downloadID;
	buffer << ", transparentImage=";
	buffer << _transparentImage;
	buffer << ")";
	return buffer.str();
  }

}