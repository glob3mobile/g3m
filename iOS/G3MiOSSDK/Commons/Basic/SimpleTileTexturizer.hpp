//
//  SimpleTileTexturizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_SimpleTileTexturizer_hpp
#define G3MiOSSDK_SimpleTileTexturizer_hpp

#include "TileTexturizer.hpp"

#include "IDownloadListener.hpp"
#include "MutableVector2D.hpp"


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
class TilePetitions{
  const Tile * _tile;
  std::vector<Petition> _petitions;
  
public:
  
  TilePetitions(): _tile(NULL){}
  TilePetitions(const Tile* t): _tile(t){}
  void add(const std::string& url, const Sector& s){
    Petition p(s, url);
    _petitions.push_back(p);
  }
  
  const Tile * getTile() const { return _tile;}
  Petition& getPetition(int i) { return _petitions[i];}
  int getNumPetitions() { return _petitions.size();}
  
  bool allFinished() const{ 
    for (int i = 0; i < _petitions.size(); i++) {
      if (!_petitions[i].isArrived()){
        return false;
      }
    }
    return true;
  }
  
};


class SimpleTileTexturizer : public TileTexturizer, IDownloadListener {
private:
  
  std::vector<TilePetitions> _tilePetitions;
  
  TilePetitions registerTilePetitions(const Tile* tile);
  
  std::vector<MutableVector2D> createTextureCoordinates() const;
  
  
  Mesh* getMesh(const RenderContext* rc,
                Tile* tile,
                Mesh* mesh);
  
  const int _resolution;
  
public:
  
  SimpleTileTexturizer(int resolution): _resolution(resolution){}
  
  virtual Mesh* texturize(const RenderContext* rc,
                          Tile* tile,
                          Mesh* mesh,
                          Mesh* previousMesh);
  
  void onDownload(const Response &response); 
  void onError(const Response& e);
};

#endif
