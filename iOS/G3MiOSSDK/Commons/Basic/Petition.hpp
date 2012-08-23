//
//  Petition.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 20/08/12.
//
//

#ifndef __G3MiOSSDK__Petition__
#define __G3MiOSSDK__Petition__

#include <string>
#include <vector>

#include "Sector.hpp"
#include "IDownloadListener.hpp"
#include "IDownloader.hpp"
#include "GLTextureID.hpp"

class Tile;
class Rectangle;
class Sector;
class IFactory;
class MutableVector2D;
class TileImagesTileTexturizer;

class Petition {
private:
  const URL         _url;
  const Sector*     _sector;
  const ByteBuffer* _buffer;
  
public:
  
  Petition(const Sector& sector,
           const URL& url):
  _sector(new Sector(sector)),
  _url(url),
  _buffer(NULL)
  {
  }
  
  ~Petition(){
    delete _sector;
    releaseBuffer();
  }
  
  void releaseBuffer() {
    if (_buffer != NULL) {
      delete _buffer;
      _buffer = NULL;
    }
  }
  
  bool hasByteBuffer() const {
    return _buffer != NULL;
  }
  
  const URL getURL() const {
    return _url;
  }
  
  Sector getSector() const {
    return *_sector;
  }
  
  void setByteBuffer(const ByteBuffer* buffer) {
    releaseBuffer();
    _buffer = buffer;
  }
  
  const ByteBuffer* getByteBuffer() const {
    return _buffer;
  }
  
  const std::string description() const;
  
};

#endif
