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

public:
  CachedDownloader(IDownloader* downloader,
                   IStorage*    cacheStorage) :
  _downloader(downloader),
  _cacheStorage(cacheStorage)
  {
    
  }
  
  void start();
  
  long request(const URL& url,
               long priority,
               IDownloadListener* listener,
               bool deleteListener);
  
  void cancelRequest(long requestId);
  
  virtual ~CachedDownloader() {
    delete _downloader;
  }
  
  
};

#endif
