//
//  IImageDownloadListenerTileCache.h
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 04/12/13.
//
//

#ifndef __G3MiOSSDK__IImageDownloadListenerTileCache__
#define __G3MiOSSDK__IImageDownloadListenerTileCache__

#include <iostream>
#include "IImageDownloadListener.hpp"
#include "Tile.hpp"
#include "ILogger.hpp"


#endif /* defined(__G3MiOSSDK__IImageDownloadListenerTileCache__) */
class IImageDownloadListenerTileCache : public IImageDownloadListener {
  long long _counter;
  const std::string _layername;
  const Tile* _tile;
private:
  
public:
  virtual ~IImageDownloadListenerTileCache(){
    #ifdef JAVA_CODE
    super.dispose();
    #endif
  }
  
  IImageDownloadListenerTileCache(long long counter, const Tile* tile, const std::string layername):_counter(counter), _tile(tile), _layername(layername) {
  
  }
  
  
  
  /**
   Callback method invoked on a successful download.  The image has to be deleted in C++ / .disposed() in Java
   */
  void onDownload(const URL& url,
                  IImage* image,
                  bool expired){
    ILogger::instance()->logInfo("Downloaded petition %d of layer %s. Tile z: %d, x: %d, y: %d", _counter, _layername.c_str(), _tile->_level,_tile->_column,_tile->_row);
    if(image != NULL){
      delete image;
      #ifdef JAVA_CODE
      image.dispose();
      #endif
    }
  }
  
  /**
   Callback method invoke after an error trying to download url
   */
  void onError(const URL& url){
    ILogger::instance()->logError("Error Download petition %d of layer %s. Tile z: %d, x: %d, y: %d", _counter, _layername.c_str(), _tile->_level,_tile->_column,_tile->_row);
  }
  
  /**
   Callback method invoke after canceled request
   */
  void onCancel(const URL& url){
    ILogger::instance()->logError("Cancel Download petition %d of layer %s. Tile z: %d, x: %d, y: %d", _counter, _layername.c_str(), _tile->_level,_tile->_column,_tile->_row);
  }
  
  /**
   This method will be call, before onCancel, when the data arrived before the cancelation.
   
   The image WILL be deleted/disposed after the method finishs.  If you need to keep the image, use shallowCopy() to store a copy of the image.
   */
  void onCanceledDownload(const URL& url,
                          IImage* image,
                          bool expired){
    ILogger::instance()->logError("Canceled Download petition %d of layer %s. Tile z: %d, x: %d, y: %d", _counter, _layername.c_str(), _tile->_level,_tile->_column,_tile->_row);
    if(image != NULL){
      delete image;
      #ifdef JAVA_CODE
      image.dispose();
      #endif
    }
  }
};