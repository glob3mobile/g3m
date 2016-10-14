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

MapzenTerrainElevationProvider* MapzenTerrainElevationProvider::createDefault(long long           downloadPriority,
                                                                              const TimeInterval& timeToCache,
                                                                              bool                readExpired) {
  return new MapzenTerrainElevationProvider(downloadPriority, timeToCache, readExpired);
}

MapzenTerrainElevationProvider::MapzenTerrainElevationProvider(long long           downloadPriority,
                                                               const TimeInterval& timeToCache,
                                                               bool                readExpired) :
_downloadPriority(downloadPriority),
_timeToCache(timeToCache),
_readExpired(readExpired)
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
  IDownloader* downloader = context->getDownloader();

////#error Diego at work!
//  downloader->requestImage(URL("http://terrain-preview.mapzen.com/terrarium/0/0/0.png"),
//                           _downloadPriority,
//                           _timeToCache,
//                           _readExpired,
//                           <#IImageDownloadListener *listener#>,
//                           <#bool deleteListener#>);
}


void MapzenTerrainElevationProvider::cancel() {
//#error man at work!
}
