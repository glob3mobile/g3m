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

  IImage* getCachedImage(const URL& url);

#ifdef C_CODE
  const IImage* _lastImage;
#endif
#ifdef JAVA_CODE
  private IImage _lastImage;
#endif

  URL*          _lastImageURL;

#ifdef C_CODE
  const G3MContext* _context;
#endif
#ifdef JAVA_CODE
  private G3MContext _context;
#endif

  FrameTasksExecutor* _frameTasksExecutor;

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
  _lastImage(NULL),
  _lastImageURL(NULL),
  _context(NULL),
  _frameTasksExecutor(NULL)
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
                          IBufferDownloadListener* listener,
                          bool deleteListener);

  long long requestImage(const URL& url,
                         long long priority,
                         const TimeInterval& timeToCache,
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
