//
//  CachedDownloader.h
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 15/08/12.
//
//

#ifndef __G3MiOSSDK__CachedDownloader__
#define __G3MiOSSDK__CachedDownloader__


#include "IDownloader.hpp"
#include "IStorage.hpp"
#include "URL.hpp"

class CachedDownloader : public IDownloader {
private:
  IDownloader* _downloader;
    
  long _requestsCounter;
  long _cacheHitsCounter;
  long _savesCounter;
  
  const bool _saveInBackground;
  
public:
  CachedDownloader(IDownloader* downloader,
                   bool         saveInBackground) :
    _downloader(downloader),
  _requestsCounter(0),
  _cacheHitsCounter(0),
  _savesCounter(0),
  _saveInBackground(saveInBackground)
  {
    
  }
  
  bool saveInBackground() const {
    return _saveInBackground;
  }
  
  void start();
  
  void stop();
  
  long long requestBuffer(const URL& url,
                          long long priority,
                          IBufferDownloadListener* listener,
                          bool deleteListener);
  
  long long requestImage(const URL& url,
                         long long priority,
                         IImageDownloadListener* listener,
                         bool deleteListener);
  
  void cancelRequest(long long requestId);
  
  virtual ~CachedDownloader() {
#ifdef C_CODE
    delete _downloader;
#endif
  }
  
  const std::string statistics();
  
  void countSave() {
    _savesCounter++;
  }
  
  void onResume(const InitializationContext* ic);
  
  void onPause(const InitializationContext* ic);

};

#endif
