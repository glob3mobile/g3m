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
#include "GLTextureId.hpp"

class Tile;
class Rectangle;
class Sector;
class IFactory;
class MutableVector2D;

class Petition {
private:
#ifdef C_CODE
  const URL             _url;
  const ByteArrayWrapper* _buffer;
#endif
#ifdef JAVA_CODE
  private URL _url = new URL(); //Conversor creates class "Url"
  private ByteArrayWrapper _buffer;
#endif
  const Sector*       _sector;
  
public:
  
  Petition(const Sector& sector,
           const URL& url):
  _sector(new Sector(sector)),
  _url(url),
  _buffer(NULL)
  {
  }
  
  ~Petition(){
#ifdef C_CODE
    delete _sector;
#endif
    releaseBuffer();
  }
  
  void releaseBuffer() {
    if (_buffer != NULL) {
      delete _buffer;
      _buffer = NULL;
    }
  }
  
  bool hasByteArrayWrapper() const {
    return _buffer != NULL;
  }
  
  const URL getURL() const {
    return _url;
  }
  
  Sector getSector() const {
    return *_sector;
  }
  
  void setByteArrayWrapper(const ByteArrayWrapper* buffer) {
    releaseBuffer();
    _buffer = buffer;
  }
  
  const ByteArrayWrapper* getByteArrayWrapper() const {
    return _buffer;
  }
  
  const std::string description() const;
  
};

#endif
