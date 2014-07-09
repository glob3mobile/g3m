//
//  SingleBilElevationDataProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/21/13.
//
//

#include "SingleBilElevationDataProvider.hpp"

#include "Context.hpp"
#include "IDownloader.hpp"
#include "IBufferDownloadListener.hpp"
#include "TimeInterval.hpp"
#include "BilParser.hpp"
#include "InterpolatedSubviewElevationData.hpp"
#include "ShortBufferElevationData.hpp"

#include "IFactory.hpp"
#include "IThreadUtils.hpp"

SingleBilElevationDataProvider::SingleBilElevationDataProvider(const URL& bilUrl,
                                                                 const Sector& sector,
                                                                 const Vector2I& extent,
                                                                 double deltaHeight) :
_bilUrl(bilUrl),
_sector(sector),
_extentWidth(extent._x),
_extentHeight(extent._y),
_deltaHeight(deltaHeight),
_elevationData(NULL),
_elevationDataResolved(false),
_currentRequestID(0),
_downloader(NULL),
_requestToDownloaderID(-1),
_listener(NULL),
_threadUtils(NULL)
{

}


class SingleBilElevationDataProvider_BufferDownloadListener : public IBufferDownloadListener {
private:
  SingleBilElevationDataProvider* _singleBilElevationDataProvider;
  const Sector _sector;
  const int _resolutionWidth;
  const int _resolutionHeight;

  const double _deltaHeight;

public:
  SingleBilElevationDataProvider_BufferDownloadListener(SingleBilElevationDataProvider* singleBilElevationDataProvider,
                                                         const Sector& sector,
                                                         int resolutionWidth,
                                                         int resolutionHeight,
                                                         double deltaHeight) :
  _singleBilElevationDataProvider(singleBilElevationDataProvider),
  _sector(sector),
  _resolutionWidth(resolutionWidth),
  _resolutionHeight(resolutionHeight),
  _deltaHeight(deltaHeight)
  {

  }

  void notifyProviderHasBeenDeleted() {
    _singleBilElevationDataProvider = NULL;
  }

  void onDownload(const URL& url,
                  IByteBuffer* buffer,
                  bool expired) {
    if (_singleBilElevationDataProvider != NULL) {
      ShortBufferElevationData* elevationData = BilParser::parseBil16(_sector,
                                                                      Vector2I(_resolutionWidth, _resolutionHeight),
                                                                      buffer,
                                                                      _deltaHeight);

      _singleBilElevationDataProvider->onElevationData(elevationData);
    }
    delete buffer;
  }

  void onError(const URL& url) {
    if (_singleBilElevationDataProvider != NULL) {
      _singleBilElevationDataProvider->onElevationData(NULL);
    }
  }

  void onCancel(const URL& url) {
    ILogger::instance()->logInfo("SingleBilElevationDataProvider download petition was canceled.");
    if (_singleBilElevationDataProvider != NULL) {
      _singleBilElevationDataProvider->onElevationData(NULL);
    }
  }

  void onCanceledDownload(const URL& url,
                          IByteBuffer* data,
                          bool expired) {
  }
};


SingleBilElevationDataProvider::~SingleBilElevationDataProvider() {
  delete _elevationData;

  if (_downloader != NULL && _requestToDownloaderID > -1) {
    _downloader->cancelRequest(_requestToDownloaderID);
  }

  if (_listener != NULL) {
    _listener->notifyProviderHasBeenDeleted();
    _listener = NULL;
  }
}

void SingleBilElevationDataProvider::onElevationData(ElevationData* elevationData) {
  _elevationData = elevationData;
  _elevationDataResolved = true;
  if (_elevationData == NULL) {
    ILogger::instance()->logError("Can't download Elevation-Data from %s",
                                  _bilUrl._path.c_str());
  }

  drainQueue();

  _listener = NULL; //The listener will be autodeleted
}

void SingleBilElevationDataProvider::initialize(const G3MContext* context) {
  if (!_elevationDataResolved || _listener != NULL) {
    _downloader = context->getDownloader();
    _threadUtils = context->getThreadUtils();

    _listener = new SingleBilElevationDataProvider_BufferDownloadListener(this,
                                                                           _sector,
                                                                           _extentWidth,
                                                                           _extentHeight,
                                                                           _deltaHeight);

    _requestToDownloaderID = _downloader->requestBuffer(_bilUrl,
                                                        2000000000,
                                                        TimeInterval::fromDays(30),
                                                        true,
                                                        _listener,
                                                        true);
  }
}

class SubviewEDTask: public GAsyncTask {
public:
  
  ElevationData* _ed;
  ElevationData* _subview;
  const Sector _sector;
  const Vector2I& _extent;
  IElevationDataListener* _listener;
  bool _autodelete;
  
  SubviewEDTask(ElevationData* ed, const Sector& sector, const Vector2I& extent,
                IElevationDataListener* listener, bool autodelete):
  _ed(ed),
  _sector(sector),
  _extent(extent),
  _subview(NULL),
  _listener(listener),
  _autodelete(autodelete)
  {
    
  }
  
  virtual void runInBackground(const G3MContext* context){
    
    _subview = new InterpolatedSubviewElevationData(_ed,
                                                                        _sector,
                                                                        _extent);
    
  }
  
  virtual void onPostExecute(const G3MContext* context){
    
    _listener->onData(_sector,
                     _extent,
                     _subview);
    
    if (_autodelete) {
      delete _listener;
    }
    
  }
  
};

const long long SingleBilElevationDataProvider::requestElevationData(const Sector& sector,
                                                                      const Vector2I& extent,
                                                                      IElevationDataListener* listener,
                                                                      bool autodeleteListener) {
  if (!_elevationDataResolved) {
    return queueRequest(sector,
                        extent,
                        listener,
                        autodeleteListener);
  }

  if (_elevationData == NULL) {
    listener->onError(sector, extent);
  }
  else {
    
#warning THIS LINE DOES NOT WORK COZ TILES DOES NOT SUPPORT ASYNC EDP
//    _threadUtils->invokeAsyncTask(new SubviewEDTask(_elevationData, sector, extent, listener, autodeleteListener), true);
    
    
    //int _DGD_working_on_terrain;
    ElevationData *elevationData = new InterpolatedSubviewElevationData(_elevationData,
                                                                        sector,
                                                                        extent);
    listener->onData(sector,
                     extent,
                     elevationData);
  }

  if (autodeleteListener) {
    delete listener;
  }

  return -1;
}

void SingleBilElevationDataProvider::cancelRequest(const long long requestId) {
  if (requestId >= 0) {
    removeQueueRequest(requestId);
  }
}

void SingleBilElevationDataProvider::drainQueue() {
  if (!_elevationDataResolved) {
    ILogger::instance()->logError("Trying to drain queue of requests without data.");
    return;
  }

#ifdef C_CODE
  std::map<long long, SingleBilElevationDataProvider_Request*>::iterator it = _requestsQueue.begin();
  for (; it != _requestsQueue.end(); it++) {
    SingleBilElevationDataProvider_Request* r = it->second;
    requestElevationData(r->_sector, r->_extent, r->_listener, r->_autodeleteListener);
    delete r;
  }
#endif
#ifdef JAVA_CODE
  for (final Long key : _requestsQueue.keySet()) {
    final SingleBilElevationDataProvider_Request r = _requestsQueue.get(key);
    requestElevationData(r._sector, r._extent, r._listener, r._autodeleteListener);
    if (r != null) {
      r.dispose();
    }
  }
#endif
  _requestsQueue.clear();
}

const long long SingleBilElevationDataProvider::queueRequest(const Sector& sector,
                                                              const Vector2I& extent,
                                                              IElevationDataListener* listener,
                                                              bool autodeleteListener) {
  _currentRequestID++;
  _requestsQueue[_currentRequestID] = new SingleBilElevationDataProvider_Request(sector, extent, listener, autodeleteListener);
  return _currentRequestID;
}

void SingleBilElevationDataProvider::removeQueueRequest(const long long requestId) {
#ifdef C_CODE
  std::map<long long, SingleBilElevationDataProvider_Request*>::iterator it = _requestsQueue.find(requestId);
  if (it != _requestsQueue.end()) {
    delete it->second;
    _requestsQueue.erase(it);
  }
#endif
#ifdef JAVA_CODE
  _requestsQueue.remove(requestId);
#endif
}
