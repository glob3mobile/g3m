//
//  MapzenTerrainElevationProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/13/16.
//
//

#include "MapzenTerrainElevationProvider.hpp"

#include "RenderState.hpp"
#include "G3MContext.hpp"
#include "IDownloader.hpp"
#include "URL.hpp"
#include "IImageDownloadListener.hpp"


int MapzenTerrainElevationProvider::_idCounter = 0;


class MapzenTerrainElevationProvider_ImageDownloadListener : public IImageDownloadListener {
  MapzenTerrainElevationProvider* _mapzenTerrainElevationProvider;

public:
  MapzenTerrainElevationProvider_ImageDownloadListener(MapzenTerrainElevationProvider* mapzenTerrainElevationProvider) :
  _mapzenTerrainElevationProvider(mapzenTerrainElevationProvider)
  {
    _mapzenTerrainElevationProvider->_retain();
  }

  virtual ~MapzenTerrainElevationProvider_ImageDownloadListener() {
    _mapzenTerrainElevationProvider->_release();
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  void onDownload(const URL& url,
                  IImage* image,
                  bool expired) {
//#error Diego at work!
  }

  void onError(const URL& url) {
//#error Diego at work!
  }

  void onCancel(const URL& url) {
    // do nothing
  }

  void onCanceledDownload(const URL& url,
                          IImage* image,
                          bool expired) {
    // do nothing
  }

};



MapzenTerrainElevationProvider::MapzenTerrainElevationProvider(long long           downloadPriority,
                                                               const TimeInterval& timeToCache,
                                                               bool                readExpired) :
_downloadPriority(downloadPriority),
_timeToCache(timeToCache),
_readExpired(readExpired),
_context(NULL),
_instanceID("MapzenTerrainElevationProvider_" + (++_idCounter))
{

}

MapzenTerrainElevationProvider::~MapzenTerrainElevationProvider() {
#ifdef JAVA_CODE
  super.dispose();
#endif
}

RenderState MapzenTerrainElevationProvider::getRenderState() {
  return RenderState::error("MapzenTerrainElevationProvider: under construction");
}

void MapzenTerrainElevationProvider::initialize(const G3MContext* context) {
  _context = context;
  IDownloader* downloader = context->getDownloader();

  downloader->requestImage(URL("http://terrain-preview.mapzen.com/terrarium/0/0/0.png"),
                           _downloadPriority,
                           _timeToCache,
                           _readExpired,
                           new MapzenTerrainElevationProvider_ImageDownloadListener(this),
                           true);
}

void MapzenTerrainElevationProvider::cancel() {
  _context->getDownloader()->cancelRequestsTagged(_instanceID);
}
