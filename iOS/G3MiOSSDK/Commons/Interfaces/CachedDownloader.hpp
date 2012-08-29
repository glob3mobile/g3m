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
  IStorage*    _cacheStorage;
  
  //  const URL getCacheFileName(const URL& url) const;
  
  std::string removeInvalidChars(const std::string& path) const;
  
  long _requestsCounter;
  long _cacheHitsCounter;
  long _savesCounter;
  
public:
  CachedDownloader(IDownloader* downloader,
                   IStorage*    cacheStorage) :
  _downloader(downloader),
  _cacheStorage(cacheStorage),
  _requestsCounter(0),
  _cacheHitsCounter(0),
  _savesCounter(0)
  {
    
  }
  
  void start();
  
  void stop();
  
  long long request(const URL& url,
                    long long priority,
                    IDownloadListener* listener,
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
  
};

#endif
