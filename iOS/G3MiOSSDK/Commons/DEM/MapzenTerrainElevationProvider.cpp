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


MapzenTerrainElevationProvider* MapzenTerrainElevationProvider::createDefault() {
  return new MapzenTerrainElevationProvider();
}

MapzenTerrainElevationProvider::MapzenTerrainElevationProvider() {

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

//#error Diego at work!
//  downloader->requestBuffer(<#const URL &url#>,
//                            <#long long priority#>,
//                            <#const TimeInterval &timeToCache#>,
//                            <#bool readExpired#>,
//                            <#IBufferDownloadListener *listener#>,
//                            <#bool deleteListener#>);
}
