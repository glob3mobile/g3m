//
//  TilePetitions.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 17/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_TilePetitions_hpp
#define G3MiOSSDK_TilePetitions_hpp

#include "IDownloadListener.hpp"
#include "MutableVector2D.hpp"
#include "TileRenderer.hpp"
#include "TileImagesTileTexturizer.hpp"


#include <string>
#include <vector>

#include "Tile.hpp"
#include "IFactory.hpp"

class Petition{
  
  double _minLat, _minLon, _maxLat, _maxLon; //Degrees
  std::string _url;
  ByteBuffer* _bb;
  
public:
  
  Petition(Sector s, std::string url): _url(url), 
  _minLat(s.lower().latitude().degrees()),
  _minLon(s.lower().longitude().degrees()),
  _maxLat(s.upper().latitude().degrees()),
  _maxLon(s.upper().longitude().degrees()),
  _bb(NULL)
  {}
  
  ~Petition(){ if (_bb != NULL) delete _bb;}
  
  std::string getURL() const { return _url;}
  
  bool isArrived() const{ return _bb != NULL;}
  void setByteBuffer(ByteBuffer* bb) { _bb = bb;}
  const ByteBuffer* getByteBuffer() const { return _bb;}
};


class TilePetitions: public IDownloadListener{
  
  const int    _level;
  const int    _row;
  const int    _column;
  
  TileImagesTileTexturizer* const _texturizer;
  std::vector<Petition> _petitions;
  
  int _nDownloads, _nErrors;
  
public:
  
  //TilePetitions(): _tileKey(""), _texturizer(NULL){}
  
  
  
  TilePetitions(  const int    l,
                const int    r,
                const int    c, TileImagesTileTexturizer* const tt):
  _level(l), _row(r), _column(c), _texturizer(tt), _nDownloads(0), _nErrors(0){}
  
  int getLevel() const {
    return _level;
  }
  
  int getRow() const {
    return _row;
  }
  
  int getColumn() const {
    return _column;
  }
  
  
  void add(const std::string& url, const Sector& s){
    Petition p(s, url);
    _petitions.push_back(p);
  }

  Petition& getPetition(int i) { return _petitions[i];}
  int getNumPetitions() { return _petitions.size();}
  
  bool allFinished() const;
  
  void onDownload(const Response &response); 
  void onError(const Response& e);
  
  void onCancel(const std::string& url);
  
};

#endif
