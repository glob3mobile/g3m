package org.glob3.mobile.generated; 
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
	if (_sector != null)
		_sector.dispose();
	releaseData();
  }

  public final void releaseData()
  {
	if (_bb != null)
		if (_bb != null)
			_bb.dispose();
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
		if (_bb != null)
			_bb.dispose();
	_bb = bb;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const ByteBuffer* getByteBuffer() const
  public final ByteBuffer getByteBuffer()
  {
	return _bb;
  }
}
//class TilePetitions: public IDownloadListener {
//  
//  const int    _level;
//  const int    _row;
//  const int    _column;
//  const Sector _tileSector;
//
//  std::vector<Petition*> _petitions;
//  
//  std::string _petitionsID;
//  
//  int _texId;      //TEXTURE ID ONCE IS FINISHED
//  
//  int _downloadsCounter;
//  int _errorsCounter;
//  
//  TilePetitions(const TilePetitions& that);
//  
//  Rectangle* getImageRectangleInTexture(const Sector& wholeSector, 
//                                       const Sector& imageSector,
//                                       int texWidth, int texHeight) const;
//  
//  Petition* getPetition(int i) { return _petitions[i];}
//  
//  int getNumPetitions() { return _petitions.size();}
//  
//  std::string createPetitionsID(const IFactory* fac) const;
//  
//public:
//  
//  TilePetitions(const int level,
//                const int row,
//                const int column,
//                const Sector sector,
//                const std::vector<Petition*>& petitions,
//                const IFactory* fac):
//  _level(level),
//  _row(row),
//  _column(column),
//  _tileSector(sector),
//  _downloadsCounter(0),
//  _errorsCounter(0),
//  _petitions(petitions),
//  _texId(-1)
//  {
//    removeUnnecesaryPetitions();
//    _petitionsID = createPetitionsID(fac);
//  }
//  
//  ~TilePetitions()
//  {
//    for (int i = 0; i < _petitions.size(); i++) {
//      delete _petitions[i];
//    }
//  }
//  
//  void requestToNet(Downloader& downloader, int priority);
//  void requestToCache(Downloader& downloader);
//  void cancelPetitions(Downloader& downloader);
//  
//  int getLevel() const {
//    return _level;
//  }
//  
//  int getRow() const {
//    return _row;
//  }
//  
//  int getColumn() const {
//    return _column;
//  }
//  
//  Sector getSector() const{ 
//    return _tileSector;
//  }
//  
//  void createTexture(TexturesHandler* texHandler, const IFactory* factory, int width, int height);
//  
//  int getTexID() const{ return _texId;}
//
//  std::string getPetitionsID() const{
//    return _petitionsID;
//  }
//  
//  bool allFinished() const;
//  
//  void onDownload(const Response &response); 
//  void onError(const Response& e);
//  void onCancel(const URL& url);
//  
//  void removeUnnecesaryPetitions();
//  
//};

