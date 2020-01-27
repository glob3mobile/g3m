//
//  MapzenDEMProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/13/16.
//
//

#include "MapzenDEMProvider.hpp"

#include "RenderState.hpp"
#include "G3MContext.hpp"
#include "IDownloader.hpp"
#include "URL.hpp"
#include "IImageDownloadListener.hpp"
#include "MapzenTerrariumParser.hpp"
#include "Sector.hpp"
#include "ErrorHandling.hpp"
#include "FloatBufferDEMGrid.hpp"
#include "ILogger.hpp"


int MapzenDEMProvider::_instanceCounter = 0;


class MapzenDEMProvider_ParserListener : public MapzenTerrariumParser::Listener {
private:
  MapzenDEMProvider* _provider;
  const int _z;
  const int _x;
  const int _y;

public:
  MapzenDEMProvider_ParserListener(MapzenDEMProvider* provider,
                                   int z,
                                   int x,
                                   int y) :
  _provider(provider),
  _z(z),
  _x(x),
  _y(y)
  {
    _provider->_retain();
  }

  virtual ~MapzenDEMProvider_ParserListener() {
    _provider->_release();
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  void onGrid(FloatBufferDEMGrid* grid) {
    _provider->onGrid(_z, _x, _y,
                      grid);
  }
};


class MapzenDEMProvider_ImageDownloadListener : public IImageDownloadListener {
  const G3MContext*               _context;
  MapzenDEMProvider* _provider;
  const int _z;
  const int _x;
  const int _y;
  const Sector _sector;
  const double _deltaHeight;

public:
  MapzenDEMProvider_ImageDownloadListener(const G3MContext* context,
                                          MapzenDEMProvider* provider,
                                          int z,
                                          int x,
                                          int y,
                                          const Sector& sector,
                                          double deltaHeight) :
  _context(context),
  _provider(provider),
  _z(z), _x(x), _y(y),
  _sector(sector),
  _deltaHeight(deltaHeight)
  {
    _provider->_retain();
  }

  virtual ~MapzenDEMProvider_ImageDownloadListener() {
    _provider->_release();
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  void onDownload(const URL& url,
                  IImage* image,
                  bool expired) {
    MapzenTerrariumParser::parse(_context,
                                 image,
                                 _sector,
                                 _deltaHeight,
                                 new MapzenDEMProvider_ParserListener(_provider,
                                                                      _z, _x, _y),
                                 true);

    // synchronous
    // FloatBufferDEMGrid* grid = MapzenTerrariumParser::parse(image, _sector, _deltaHeight);
    // _provider->onGrid(_z, _x, _y,
    //                   grid);
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


MapzenDEMProvider::MapzenDEMProvider(const std::string&  apiKey,
                                     long long           downloadPriority,
                                     const TimeInterval& timeToCache,
                                     bool                readExpired,
                                     const double        deltaHeight) :
MercatorPyramidDEMProvider(deltaHeight,
                           Vector2S((short)256, (short)256)),
_apiKey(apiKey),
_downloadPriority(downloadPriority),
_timeToCache(timeToCache),
_readExpired(readExpired),
_context(NULL),
_instanceID("MapzenDEMProvider_" + IStringUtils::instance()->toString(++_instanceCounter)),
_rootGridDownloaded(false),
_errorDownloadingRootGrid(false)
{
}

MapzenDEMProvider::~MapzenDEMProvider() {
#ifdef JAVA_CODE
  super.dispose();
#endif
}

RenderState MapzenDEMProvider::getRenderState() {
  if (_errorDownloadingRootGrid) {
    return RenderState::error("Error downloading Mapzen root grid");
  }
  return _rootGridDownloaded ? RenderState::ready() : RenderState::busy();
}

void MapzenDEMProvider::requestTile(int z,
                                    int x,
                                    int y,
                                    const Sector& sector) {
  IDownloader* downloader = _context->getDownloader();

  const IStringUtils* su = IStringUtils::instance();
  const std::string path = "https://tile.mapzen.com/mapzen/terrain/v1/terrarium/" + su->toString(z) + "/" + su->toString(x) + "/" + su->toString(y) + ".png?api_key=" + _apiKey;

  downloader->requestImage(URL(path),
                           _downloadPriority,
                           _timeToCache,
                           _readExpired,
                           new MapzenDEMProvider_ImageDownloadListener(_context,
                                                                       this,
                                                                       z, x, y,
                                                                       sector,
                                                                       _deltaHeight),
                           true,
                           _instanceID);
}

void MapzenDEMProvider::initialize(const G3MContext* context) {
  _context = context;

  // request root grid
  requestTile(0, // z
              0, // x
              0, // y
              Sector::FULL_SPHERE);
}

void MapzenDEMProvider::cancel() {
  _context->getDownloader()->cancelRequestsTagged(_instanceID);
}

void MapzenDEMProvider::onGrid(int z,
                               int x,
                               int y,
                               FloatBufferDEMGrid* grid) {
  bool stickyGrid = false;

  if ((z == 0) && (x == 0) && (y == 0)) {
    _rootGridDownloaded = true;
    stickyGrid = true;
  }

  insertGrid(z, x, y,
             grid, stickyGrid);
}

void MapzenDEMProvider::onDownloadError(int z,
                                        int x,
                                        int y) {
  ILogger::instance()->logError("Error downloading Mapzen terrarium at %d/%d/%d", z, x, y);
  if ((z == 0) && (x == 0) && (y == 0)) {
    _errorDownloadingRootGrid = true;
  }
}
