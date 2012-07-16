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


#include <string>
#include <vector>

#include "Tile.hpp"
#include "IFactory.hpp"

class Petition{
  
  double _minLat, _minLon, _maxLat, _maxLon; //Degrees
  std::string _url;
  bool _arrived;
  ByteBuffer _bb;
  
public:
  
  Petition(Sector s, std::string url): _url(url), 
                                      _minLat(s.lower().latitude().degrees()),
  _minLon(s.lower().longitude().degrees()),
  _maxLat(s.upper().latitude().degrees()),
  _maxLon(s.upper().longitude().degrees()),
  _arrived(false)
  {}
  
  std::string getURL() const { return _url;}
  
  bool isArrived() const { return _arrived;}
  void setByteBuffer(const ByteBuffer& bb) { _bb = bb; _arrived = true;}
  const ByteBuffer& getByteBuffer() const { return _bb;}
  
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
  std::vector<Petition> getPetitions() const{ return _petitions;}
  
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
  
  TilePetitions getTilePetitions(const Tile* tile) const;
  
public:
  virtual Mesh* texturize(const RenderContext* rc,
                          const Tile* tile,
                          Mesh* mesh) const;
  
  void onDownload(const Response &response); 
  void onError(const Response& e);
};

#endif
