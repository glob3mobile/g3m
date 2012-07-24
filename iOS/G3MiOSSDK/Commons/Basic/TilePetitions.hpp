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
  const std::string _url;
  const Sector      _sector;
  ByteBuffer*       _bb;

  Petition& operator=(const Petition& that);
  Petition(const Petition& that);
  
public:
  
  Petition(const Sector& sector,
           std::string url):
  _sector(sector),
  _url(url), 
  _bb(NULL)
  {
  }
  
  ~Petition(){ 
    if (_bb != NULL) delete _bb;
  }
  
  std::string getURL() const {
    return _url;
  }
  
  Sector getSector() const {
    return _sector;
  }
  
  bool isArrived() const {
    return _bb != NULL;
  }
  
  void setByteBuffer(ByteBuffer* bb) {
    if (_bb != NULL) delete _bb;
    _bb = bb;
  }

  const ByteBuffer* getByteBuffer() const {
    return _bb;
  }
};


class TilePetitions: public IDownloadListener {
  
  const int    _level;
  const int    _row;
  const int    _column;
  const Sector _tileSector;
  
  TileImagesTileTexturizer* _texturizer;
  std::vector<Petition*> _petitions;
  
  int _downloadsCounter;
  int _errorsCounter;
  
  TilePetitions(const TilePetitions& that);
  
  void tryToDeleteMyself()
  {
    if (_downloadsCounter + _errorsCounter == _petitions.size()){
      delete this;
    }
  }
  
public:
  
  TilePetitions(const int level,
                const int row,
                const int column,
                const Sector sector,
                const std::vector<Petition*>& petitions,
                TileImagesTileTexturizer* const texturizer):
  _level(level),
  _row(row),
  _column(column),
  _texturizer(texturizer),
  _tileSector(sector),
  _downloadsCounter(0),
  _errorsCounter(0),
  _petitions(petitions)
  {
  }
  
  ~TilePetitions()
  {
    for (int i = 0; i < _petitions.size(); i++) {
      delete _petitions[i];
    }
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
  
  Sector getSector() const{ 
    return _tileSector;
  }

  std::string getPetitionsID() const;

  Petition* getPetition(int i) { return _petitions[i];}
  int getNumPetitions() { return _petitions.size();}
  
  bool allFinished() const;
  
  void onDownload(const Response &response); 
  void onError(const Response& e);
  
  void onCancel(const std::string& url);
  
};

#endif
