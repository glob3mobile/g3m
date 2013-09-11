//
//  SingleBillElevationDataProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/21/13.
//
//

#include "SingleBillElevationDataProvider.hpp"

#include "Context.hpp"
#include "IDownloader.hpp"
#include "IBufferDownloadListener.hpp"
#include "TimeInterval.hpp"
#include "BilParser.hpp"
#include "InterpolatedSubviewElevationData.hpp"
#include "ShortBufferElevationData.hpp"


SingleBillElevationDataProvider::SingleBillElevationDataProvider(const URL& bilUrl,
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
_currentRequestID(0)
{

}

class SingleBillElevationDataProvider_BufferDownloadListener : public IBufferDownloadListener {
private:
  SingleBillElevationDataProvider* _singleBillElevationDataProvider;
  const Sector _sector;
  const int _resolutionWidth;
  const int _resolutionHeight;

  const double _deltaHeight;

public:
  SingleBillElevationDataProvider_BufferDownloadListener(SingleBillElevationDataProvider* singleBillElevationDataProvider,
                                                         const Sector& sector,
                                                         int resolutionWidth,
                                                         int resolutionHeight,
                                                         double deltaHeight) :
  _singleBillElevationDataProvider(singleBillElevationDataProvider),
  _sector(sector),
  _resolutionWidth(resolutionWidth),
  _resolutionHeight(resolutionHeight),
  _deltaHeight(deltaHeight)
  {

  }

  void onDownload(const URL& url,
                  IByteBuffer* buffer,
                  bool expired) {
    const Vector2I resolution(_resolutionWidth, _resolutionHeight);

    ShortBufferElevationData* elevationData = BilParser::parseBil16(_sector, resolution, buffer, _deltaHeight);

    delete buffer;

    _singleBillElevationDataProvider->onElevationData(elevationData);
  }

  void onError(const URL& url) {
    _singleBillElevationDataProvider->onElevationData(NULL);
  }

  void onCancel(const URL& url) {

  }

  void onCanceledDownload(const URL& url,
                          IByteBuffer* data,
                          bool expired) {
  }
};

void SingleBillElevationDataProvider::onElevationData(ElevationData* elevationData) {
  _elevationData = elevationData;
  _elevationDataResolved = true;
  if (_elevationData == NULL) {
    ILogger::instance()->logError("Can't download Elevation-Data from %s",
                                  _bilUrl.getPath().c_str());
  }

  drainQueue();
}

void SingleBillElevationDataProvider::initialize(const G3MContext* context) {
  if (!_elevationDataResolved) {
    IDownloader* downloader = context->getDownloader();
    downloader->requestBuffer(_bilUrl,
                              2000000000,
                              TimeInterval::fromDays(30),
                              true,
                              new SingleBillElevationDataProvider_BufferDownloadListener(this,
                                                                                         _sector,
                                                                                         _extentWidth,
                                                                                         _extentHeight,
                                                                                         _deltaHeight),
                              true);
  }
}

const long long SingleBillElevationDataProvider::requestElevationData(const Sector& sector,
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

void SingleBillElevationDataProvider::cancelRequest(const long long requestId) {
  if (requestId >= 0) {
    removeQueueRequest(requestId);
  }
}

void SingleBillElevationDataProvider::drainQueue() {
  if (!_elevationDataResolved) {
    ILogger::instance()->logError("Trying to drain queue of requests without data.");
    return;
  }

#ifdef C_CODE
  std::map<long long, SingleBillElevationDataProvider_Request*>::iterator it = _requestsQueue.begin();
  for (; it != _requestsQueue.end(); it++) {
    SingleBillElevationDataProvider_Request* r = it->second;
    requestElevationData(r->_sector, r->_extent, r->_listener, r->_autodeleteListener);
    delete r;
  }
#endif
#ifdef JAVA_CODE
  for (final Long key : _requestsQueue.keySet()) {
    final SingleBillElevationDataProvider_Request r = _requestsQueue.get(key);
    requestElevationData(r._sector, r._extent, r._listener, r._autodeleteListener);
    if (r != null) {
      r.dispose();
    }
  }
#endif
  _requestsQueue.clear();
}

const long long SingleBillElevationDataProvider::queueRequest(const Sector& sector,
                                                              const Vector2I& extent,
                                                              IElevationDataListener* listener,
                                                              bool autodeleteListener) {
  _currentRequestID++;
  _requestsQueue[_currentRequestID] = new SingleBillElevationDataProvider_Request(sector, extent, listener, autodeleteListener);
  return _currentRequestID;
}

void SingleBillElevationDataProvider::removeQueueRequest(const long long requestId) {
#ifdef C_CODE
  std::map<long long, SingleBillElevationDataProvider_Request*>::iterator it = _requestsQueue.find(requestId);
  if (it != _requestsQueue.end()) {
    delete it->second;
    _requestsQueue.erase(it);
  }
#endif
#ifdef JAVA_CODE
  _requestsQueue.remove(requestId);
#endif
}
