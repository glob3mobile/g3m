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
#include "MapzenTerrariumParser.hpp"
#include "Sector.hpp"
#include "ErrorHandling.hpp"
#include "FloatBufferTerrainElevationGrid.hpp"


int MapzenTerrainElevationProvider::_idCounter = 0;


class MapzenTerrainElevationProvider_ImageDownloadListener : public IImageDownloadListener {
  MapzenTerrainElevationProvider* _provider;
  const int _z;
  const int _x;
  const int _y;
  const Sector _sector;
  const double _deltaHeight;

public:
  MapzenTerrainElevationProvider_ImageDownloadListener(MapzenTerrainElevationProvider* provider,
                                                       int z,
                                                       int x,
                                                       int y,
                                                       const Sector& sector,
                                                       double deltaHeight) :
  _provider(provider),
  _z(z),
  _x(x),
  _y(y),
  _sector(sector),
  _deltaHeight(deltaHeight)
  {
    _provider->_retain();
  }

  virtual ~MapzenTerrainElevationProvider_ImageDownloadListener() {
    _provider->_release();
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  void onDownload(const URL& url,
                  IImage* image,
                  bool expired) {
    FloatBufferTerrainElevationGrid* grid = MapzenTerrariumParser::parse(image, _sector, _deltaHeight);
    _provider->onGrid(_z, _x, _y, grid);
  }

  void onError(const URL& url) {
    _provider->onDownloadError(_z, _x, _y);
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



MapzenTerrainElevationProvider::MapzenTerrainElevationProvider(const std::string&  apiKey,
                                                               long long           downloadPriority,
                                                               const TimeInterval& timeToCache,
                                                               bool                readExpired) :
_apiKey(apiKey),
_downloadPriority(downloadPriority),
_timeToCache(timeToCache),
_readExpired(readExpired),
_context(NULL),
_instanceID("MapzenTerrainElevationProvider_" + IStringUtils::instance()->toString(++_idCounter)),
_rootGrid(NULL),
_errorDownloadingRoot(false)
{

}

MapzenTerrainElevationProvider::~MapzenTerrainElevationProvider() {
  if (_rootGrid != NULL) {
    _rootGrid->_release();
  }
#ifdef JAVA_CODE
  super.dispose();
#endif
}

RenderState MapzenTerrainElevationProvider::getRenderState() {
  if (_errorDownloadingRoot) {
    return RenderState::error("Error downloading Mapzen root grid");
  }
  return (_rootGrid == NULL) ? RenderState::busy() : RenderState::ready();
}

void MapzenTerrainElevationProvider::initialize(const G3MContext* context) {
  _context = context;
  IDownloader* downloader = context->getDownloader();

  // https://tile.mapzen.com/mapzen/terrain/v1/terrarium/{z}/{x}/{y}.png?api_key=mapzen-xxxxxxx

  // MapzenTerrariumParser

  downloader->requestImage(URL("https://tile.mapzen.com/mapzen/terrain/v1/terrarium/0/0/0.png?api_key=" + _apiKey),
                           _downloadPriority,
                           _timeToCache,
                           _readExpired,
                           new MapzenTerrainElevationProvider_ImageDownloadListener(this,
                                                                                    0, // z
                                                                                    0, // x
                                                                                    0, // y
                                                                                    Sector::FULL_SPHERE,
                                                                                    0 /* deltaHeight */),
                           true);
}

void MapzenTerrainElevationProvider::cancel() {
  _context->getDownloader()->cancelRequestsTagged(_instanceID);
}

void MapzenTerrainElevationProvider::onGrid(int z, int x, int y,
                                            FloatBufferTerrainElevationGrid* grid) {
  if ((z == 0) && (x == 0) && (y == 0)) {
    if (_rootGrid != NULL) {
      _rootGrid->_release();
    }
    _rootGrid = grid;
  }
  else {
    THROW_EXCEPTION("Not yet done");
  }
}

void MapzenTerrainElevationProvider::onDownloadError(int z, int x, int y) {
  ILogger::instance()->logError("Error downloading Mapzen terrarium at %i/%i/%i", z, x, y);
  if ((z == 0) && (x == 0) && (y == 0)) {
    _errorDownloadingRoot = true;
  }
}
