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

class Petition {
private:
#ifdef C_CODE
  const URL             _url;
  const ByteBuffer* _buffer;
#endif
#ifdef JAVA_CODE
  private URL _url = new URL(); //Conversor creates class "Url"
  private ByteBuffer _buffer;
#endif
  const Sector*       _sector;
  long                     _downloadID;
  const bool            _transparentImage;
  
public:
  
  Petition(Sector sector,
           URL url,
           bool transparent):
  _sector(new Sector(sector)),
  _url(url),
  _transparentImage(transparent),
  _buffer(NULL),
  _downloadID(-1)
  {
  }
  
  ~Petition(){
#ifdef C_CODE
    delete _sector;
    releaseData();
#endif
  }
  
  void releaseData(){
    if (_buffer != NULL) {
      delete _buffer;
      _buffer = NULL;
    }
  }
  
  long getDownloadID() const {
    return _downloadID;
  }
  
  bool hasByteBuffer() const{
    return _buffer != NULL;
  }
  
  bool isTransparent() const{
    return _transparentImage;
  }
  
  void setDownloadID(long id){
    _downloadID = id;
  }
  
  const URL getURL() const {
    return _url;
  }
  
  Sector getSector() const {
    return *_sector;
  }
  
  void setByteBuffer(const ByteBuffer* buffer) {
    if (_buffer != NULL) {
      delete _buffer;
    }
    _buffer = buffer;
  }
  
  const ByteBuffer* getByteBuffer() const {
    return _buffer;
  }
  
  const std::string description() const;
  
};

#endif
