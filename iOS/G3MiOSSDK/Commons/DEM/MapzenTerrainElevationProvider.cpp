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
#include "MercatorUtils.hpp"
#include "MeshRenderer.hpp"
#include "EllipsoidalPlanet.hpp"


int MapzenTerrainElevationProvider::_idCounter = 0;


class MapzenTerrainElevationProvider_ParserListener : public MapzenTerrariumParser::Listener {
private:
  MapzenTerrainElevationProvider* _provider;
  const int _z;
  const int _x;
  const int _y;

public:

  MapzenTerrainElevationProvider_ParserListener(MapzenTerrainElevationProvider* provider,
                                                int z, int x, int y) :
  _provider(provider),
  _z(z), _x(x), _y(y)
  {
    _provider->_retain();
  }

  virtual ~MapzenTerrainElevationProvider_ParserListener() {
    _provider->_release();
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  void onGrid(FloatBufferTerrainElevationGrid* grid) {
    _provider->onGrid(_z, _x, _y,
                      grid);
  }

};


class MapzenTerrainElevationProvider_ImageDownloadListener : public IImageDownloadListener {
  const G3MContext*               _context;
  MapzenTerrainElevationProvider* _provider;
  const int _z;
  const int _x;
  const int _y;
  const Sector _sector;
  const double _deltaHeight;

public:
  MapzenTerrainElevationProvider_ImageDownloadListener(const G3MContext* context,
                                                       MapzenTerrainElevationProvider* provider,
                                                       int z, int x, int y,
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

  virtual ~MapzenTerrainElevationProvider_ImageDownloadListener() {
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
                                 new MapzenTerrainElevationProvider_ParserListener(_provider,
                                                                                   _z, _x, _y),
                                 true);

//    FloatBufferTerrainElevationGrid* grid = MapzenTerrariumParser::parse(image, _sector, _deltaHeight);
//    _provider->onGrid(_z, _x, _y,
//                      grid);
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
                                                               bool                readExpired,
                                                               MeshRenderer*       meshRenderer) :
_apiKey(apiKey),
_downloadPriority(downloadPriority),
_timeToCache(timeToCache),
_readExpired(readExpired),
_meshRenderer(meshRenderer),
_context(NULL),
_instanceID("MapzenTerrainElevationProvider_" + IStringUtils::instance()->toString(++_idCounter)),
_rootGrid(NULL),
_errorDownloadingRootGrid(false)
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
  if (_errorDownloadingRootGrid) {
    return RenderState::error("Error downloading Mapzen root grid");
  }
  return (_rootGrid == NULL) ? RenderState::busy() : RenderState::ready();
}

void MapzenTerrainElevationProvider::requestTile(int z, int x, int y,
                                                 const Sector& sector,
                                                 double deltaHeight) {
  IDownloader* downloader = _context->getDownloader();

  const IStringUtils* su = IStringUtils::instance();
  const std::string path = "https://tile.mapzen.com/mapzen/terrain/v1/terrarium/" + su->toString(z) + "/" + su->toString(x) + "/" + su->toString(y) + ".png?api_key=" + _apiKey;

  downloader->requestImage(URL(path),
                           _downloadPriority,
                           _timeToCache,
                           _readExpired,
                           new MapzenTerrainElevationProvider_ImageDownloadListener(_context,
                                                                                    this,
                                                                                    z, x, y,
                                                                                    sector,
                                                                                    deltaHeight),
                           true);
}

void MapzenTerrainElevationProvider::initialize(const G3MContext* context) {
  _context = context;

  // request root grid
  requestTile(0, // z
              0, // x
              0, // y
              Sector::FULL_SPHERE,
              0 /* deltaHeight */);

  /*
   Touched on (Tile level=9, row=331, column=271, sector=(Sector (lat=46.558860303117171497d, lon=10.546875d) - (lat=47.040182144806649944d, lon=11.25d)))
   Touched on position (lat=46.64863034601081182d, lon=10.850429115221331244d, height=0)
   Touched on pixels (V2I 110, 208)
   Camera position=(lat=46.668763371822997499d, lon=10.800910848094183336d, height=135933.14638548778021) heading=3.472574 pitch=-90.000000
   
   
   
   const int numRows = (int) (_parameters->_topSectorSplitsByLatitude * _mu->pow(2.0, level));
   const int row     = numRows - tile->_row - 1;
   */

//  const int z = 10;
//  const int x = 154;
//  const int y = 304;
  const int z = 9;
  const int x = 271;
  const int y = 180;
  const double deltaHeight = 0;

  const Sector sector = MercatorUtils::getSector(z, x, y);
  ILogger::instance()->logInfo( sector.description() );
  requestTile(z, x, y,
              sector,
              deltaHeight);
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
    _meshRenderer->addMesh(  grid->createDebugMesh(EllipsoidalPlanet::createEarth(),
                                                   1, // verticalExaggeration,
                                                   Geodetic3D::zero(),
                                                   4 // pointSize
                                                   )  );

    grid->_release();
    //THROW_EXCEPTION("Not yet done");
  }
}

void MapzenTerrainElevationProvider::onDownloadError(int z, int x, int y) {
  ILogger::instance()->logError("Error downloading Mapzen terrarium at %i/%i/%i", z, x, y);
  if ((z == 0) && (x == 0) && (y == 0)) {
    _errorDownloadingRootGrid = true;
  }
}
