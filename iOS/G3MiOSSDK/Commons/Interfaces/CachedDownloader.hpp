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
  IStorage*    _storage;

  long _requestsCounter;
  long _cacheHitsCounter;
  long _savesCounter;

  const bool _saveInBackground;

  IImageResult getCachedImageResult(const URL& url,
                                    bool readExpired);

  IImageResult* _lastImageResult;
  URL*          _lastImageURL;

public:
  CachedDownloader(IDownloader* downloader,
                   IStorage*    storage,
                   bool         saveInBackground) :
  _downloader(downloader),
  _storage(storage),
  _requestsCounter(0),
  _cacheHitsCounter(0),
  _savesCounter(0),
  _saveInBackground(saveInBackground),
  _lastImageResult(NULL),
  _lastImageURL(NULL)
  {

  }

  bool saveInBackground() const {
    return _saveInBackground;
  }

  void start();

  void stop();

  long long requestBuffer(const URL& url,
                          long long priority,
                          const TimeInterval& timeToCache,
                          bool readExpired,
                          IBufferDownloadListener* listener,
                          bool deleteListener);

  long long requestImage(const URL& url,
                         long long priority,
                         const TimeInterval& timeToCache,
                         bool readExpired,
                         IImageDownloadListener* listener,
                         bool deleteListener);

  void cancelRequest(long long requestId);

  virtual ~CachedDownloader();

  const std::string statistics();

  void countSave() {
    _savesCounter++;
  }

  void onResume(const G3MContext* context);

  void onPause(const G3MContext* context);

  void onDestroy(const G3MContext* context);

  void initialize(const G3MContext* context,
                  FrameTasksExecutor* frameTasksExecutor);
  
};

#endif
