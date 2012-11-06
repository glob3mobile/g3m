//
//  IDownloader.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_IDownloader_hpp
#define G3MiOSSDK_IDownloader_hpp

#include "URL.hpp"
#include "ILogger.hpp"

#include <string>

class IBufferDownloadListener;
class IImageDownloadListener;
class InitializationContext;

class IDownloader {
private:
    static IDownloader* _instance;
public:
    static void setInstance(IDownloader* downloader) {
        if (_instance != NULL) {
            ILogger::instance()->logWarning("Warning, IDownloader instance set twice\n");
        }
        _instance = downloader;
    }
    
    static IDownloader* instance() {
        return _instance;
    }
    
  virtual void onResume(const InitializationContext* ic) = 0;
  
  virtual void onPause(const InitializationContext* ic) = 0;
  
  virtual void start() = 0;
  
  virtual void stop() = 0;
  
  virtual long long requestBuffer(const URL& url,
                                  long long priority,
                                  IBufferDownloadListener* listener,
                                  bool deleteListener) = 0;
  
  virtual long long requestImage(const URL& url,
                                 long long priority,
                                 IImageDownloadListener* listener,
                                 bool deleteListener) = 0;
  
  virtual void cancelRequest(long long requestId) = 0;
  
  virtual ~IDownloader() {
  }
  
  virtual const std::string statistics() = 0;
  
};

#endif
