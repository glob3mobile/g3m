package org.glob3.mobile.generated; 
//
//  TilePetitions.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 17/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  TilePetitions.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 17/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
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
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TileImagesTileTexturizer;

public class Petition
{
  private final String _url;
  private final Sector _sector;
  private ByteBuffer _bb;
  private int _downloadID;
  private final boolean _transparentImage;


  public Petition(Sector s, String url, boolean transparent)
  {
	  _url = url;
	  _sector = new Sector(s);
	  _bb = null;
	  _downloadID = -1;
	  _transparentImage = transparent;
  }

  public void dispose()
  {
	releaseData();
  }

  public final void releaseData()
  {
	if (_bb != null)
		_bb = null;
	_bb = null;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getDownloadID() const
  public final int getDownloadID()
  {
	return _downloadID;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isArrived() const
  public final boolean isArrived()
  {
	return _bb != null;
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
//ORIGINAL LINE: String getURL() const
  public final String getURL()
  {
	return _url;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Sector getSector() const
  public final Sector getSector()
  {
	return _sector;
  }

  public final void setByteBuffer(ByteBuffer bb)
  {
	if (_bb != null)
		_bb = null;
	_bb = bb;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const ByteBuffer* getByteBuffer() const
  public final ByteBuffer getByteBuffer()
  {
	return _bb;
  }
}