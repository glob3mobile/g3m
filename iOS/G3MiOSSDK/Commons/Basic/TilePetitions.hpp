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

#include <string>
#include <vector>

#include "Tile.hpp"
#include "IFactory.hpp"

class TileImagesTileTexturizer;

class Petition {
  
  double _minLat, _minLon, _maxLat, _maxLon; //Degrees
  std::string _url;
  ByteBuffer* _bb;

public:
  
  Petition(const Sector s, std::string url) :
  _url(url), 
  _minLat(s.lower().latitude().degrees()),
  _minLon(s.lower().longitude().degrees()),
  _maxLat(s.upper().latitude().degrees()),
  _maxLon(s.upper().longitude().degrees()),
  _bb(NULL)
  {}
  
  ~Petition() { if (_bb != NULL) delete _bb; }
  
  std::string getURL() const { return _url; }
  Sector getSector() const { return Sector::fromDegrees(_minLat, _minLon, _maxLat, _maxLon); }
  
  bool isArrived() const{ return _bb != NULL; }
  void setByteBuffer(ByteBuffer* bb) { _bb = bb; }
  const ByteBuffer* getByteBuffer() const { return _bb; }
};


class TilePetitions: public IDownloadListener {
  
  const int    _level;
  const int    _row;
  const int    _column;
  
  TileImagesTileTexturizer* _texturizer;
  std::vector<Petition> _petitions;
  
  int _downloadsCounter;
  int _errorsCounter;
  
  TilePetitions(const TilePetitions& that);
  
public:
  
  TilePetitions(const int level,
                const int row,
                const int column,
                TileImagesTileTexturizer* const texturizer):
  _level(level),
  _row(row),
  _column(column),
  _texturizer(texturizer),
  _downloadsCounter(0),
  _errorsCounter(0)
  {
  }
  
  void notify(TileImagesTileTexturizer* texturizer)
  {
    _texturizer = texturizer;
  }
  
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
  
  void add(const std::vector<Petition>& pet){
    for (int i = 0; i < pet.size(); i++) {
      add(pet[i].getURL(), pet[i].getSector());
    }
  }
  
  std::string getPetitionsID() const;

  Petition& getPetition(int i) { return _petitions[i];}
  int getNumPetitions() { return _petitions.size();}
  
  bool allFinished() const;
  
  void onDownload(const Response &response); 
  void onError(const Response& e);
  
  void onCancel(const std::string& url);
  
};

#endif
