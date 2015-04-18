//
//  IDownloader.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_IDownloader
#define G3MiOSSDK_IDownloader

class URL;
class G3MContext;
class IBufferDownloadListener;
class IImageDownloadListener;
class TimeInterval;
class FrameTasksExecutor;

#include <string>

class IDownloader {
public:
  virtual ~IDownloader() {
  }

  virtual void initialize(const G3MContext* context,
                          FrameTasksExecutor* frameTasksExecutor) = 0;

  virtual void onResume(const G3MContext* context) = 0;

  virtual void onPause(const G3MContext* context) = 0;

  virtual void onDestroy(const G3MContext* context) = 0;

  virtual void start() = 0;

  virtual void stop() = 0;

  virtual long long requestBuffer(const URL& url,
                                  long long priority,
                                  const TimeInterval& timeToCache,
                                  bool readExpired,
                                  IBufferDownloadListener* listener,
                                  bool deleteListener) = 0;

  virtual long long requestImage(const URL& url,
                                 long long priority,
                                 const TimeInterval& timeToCache,
                                 bool readExpired,
                                 IImageDownloadListener* listener,
                                 bool deleteListener) = 0;

  virtual void cancelRequest(long long requestId) = 0;

  virtual const std::string statistics() = 0;

};

#endif
