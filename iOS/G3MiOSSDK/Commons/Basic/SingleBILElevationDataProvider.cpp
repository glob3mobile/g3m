//
//  SingleBILElevationDataProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/21/13.
//
//

#include "SingleBILElevationDataProvider.hpp"

#include "G3MContext.hpp"
#include "IDownloader.hpp"
#include "IBufferDownloadListener.hpp"
#include "TimeInterval.hpp"
#include "BILParser.hpp"
#include "InterpolatedSubviewElevationData.hpp"
#include "ShortBufferElevationData.hpp"


SingleBILElevationDataProvider::SingleBILElevationDataProvider(const URL& bilUrl,
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
_listener(NULL)
{

}


class SingleBILElevationDataProvider_BufferDownloadListener : public IBufferDownloadListener {
private:
  SingleBILElevationDataProvider* _singleBILElevationDataProvider;
  const Sector _sector;
  const int _resolutionWidth;
  const int _resolutionHeight;

  const double _deltaHeight;

public:
  SingleBILElevationDataProvider_BufferDownloadListener(SingleBILElevationDataProvider* singleBILElevationDataProvider,
                                                        const Sector& sector,
                                                        int resolutionWidth,
                                                        int resolutionHeight,
                                                        double deltaHeight) :
  _singleBILElevationDataProvider(singleBILElevationDataProvider),
  _sector(sector),
  _resolutionWidth(resolutionWidth),
  _resolutionHeight(resolutionHeight),
  _deltaHeight(deltaHeight)
  {

  }

  void notifyProviderHasBeenDeleted() {
    _singleBILElevationDataProvider = NULL;
  }

  void onDownload(const URL& url,
                  IByteBuffer* buffer,
                  bool expired) {
    if (_singleBILElevationDataProvider != NULL) {
      ShortBufferElevationData* elevationData = BILParser::oldParseBIL16(_sector,
                                                                         Vector2I(_resolutionWidth, _resolutionHeight),
                                                                         buffer,
                                                                         _deltaHeight);

      _singleBILElevationDataProvider->onElevationData(elevationData);
    }
    delete buffer;
  }

  void onError(const URL& url) {
    if (_singleBILElevationDataProvider != NULL) {
      _singleBILElevationDataProvider->onElevationData(NULL);
    }
  }

  void onCancel(const URL& url) {
    ILogger::instance()->logInfo("SingleBILElevationDataProvider download petition was canceled.");
    if (_singleBILElevationDataProvider != NULL) {
      _singleBILElevationDataProvider->onElevationData(NULL);
    }
  }

  void onCanceledDownload(const URL& url,
                          IByteBuffer* data,
                          bool expired) {
  }
};


SingleBILElevationDataProvider::~SingleBILElevationDataProvider() {
  delete _elevationData;

  if (_downloader != NULL && _requestToDownloaderID > -1) {
    _downloader->cancelRequest(_requestToDownloaderID);
  }

  if (_listener != NULL) {
    _listener->notifyProviderHasBeenDeleted();
    _listener = NULL;
  }
}

void SingleBILElevationDataProvider::onElevationData(ElevationData* elevationData) {
  _elevationData = elevationData;
  _elevationDataResolved = true;
  if (_elevationData == NULL) {
    ILogger::instance()->logError("Can't download Elevation-Data from %s",
                                  _bilUrl._path.c_str());
  }

  drainQueue();

  _listener = NULL; //The listener will be autodeleted
}

void SingleBILElevationDataProvider::initialize(const G3MContext* context) {
  if (!_elevationDataResolved || _listener != NULL) {
    _downloader = context->getDownloader();

    _listener = new SingleBILElevationDataProvider_BufferDownloadListener(this,
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

const long long SingleBILElevationDataProvider::requestElevationData(const Sector& sector,
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

void SingleBILElevationDataProvider::cancelRequest(const long long requestID) {
  if (requestID >= 0) {
    removeQueueRequest(requestID);
  }
}

void SingleBILElevationDataProvider::drainQueue() {
  if (!_elevationDataResolved) {
    ILogger::instance()->logError("Trying to drain queue of requests without data.");
    return;
  }

#ifdef C_CODE
  std::map<long long, SingleBILElevationDataProvider_Request*>::iterator it = _requestsQueue.begin();
  for (; it != _requestsQueue.end(); it++) {
    SingleBILElevationDataProvider_Request* r = it->second;
    requestElevationData(r->_sector, r->_extent, r->_listener, r->_autodeleteListener);
    delete r;
  }
#endif
#ifdef JAVA_CODE
  for (final SingleBILElevationDataProvider_Request r : _requestsQueue.values()) {
    requestElevationData(r._sector, r._extent, r._listener, r._autodeleteListener);
    if (r != null) {
      r.dispose();
    }
  }
#endif
  _requestsQueue.clear();
}

const long long SingleBILElevationDataProvider::queueRequest(const Sector& sector,
                                                             const Vector2I& extent,
                                                             IElevationDataListener* listener,
                                                             bool autodeleteListener) {
  _currentRequestID++;
  _requestsQueue[_currentRequestID] = new SingleBILElevationDataProvider_Request(sector, extent, listener, autodeleteListener);
  return _currentRequestID;
}

void SingleBILElevationDataProvider::removeQueueRequest(const long long requestID) {
#ifdef C_CODE
  std::map<long long, SingleBILElevationDataProvider_Request*>::iterator it = _requestsQueue.find(requestID);
  if (it != _requestsQueue.end()) {
    delete it->second;
    _requestsQueue.erase(it);
  }
#endif
#ifdef JAVA_CODE
  _requestsQueue.remove(requestID);
#endif
}
