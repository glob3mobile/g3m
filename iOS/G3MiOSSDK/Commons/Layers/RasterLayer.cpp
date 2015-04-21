//
//  RasterLayer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/22/14.
//
//

#include "RasterLayer.hpp"
#include "TimeInterval.hpp"
#include "RasterLayerTileImageProvider.hpp"
#include "LayerCondition.hpp"
#include "Context.hpp"
#include "IDownloader.hpp"
#include "URL.hpp"
#include "LayerTilesRenderParameters.hpp"
#include "Tile.hpp"
#include "ErrorHandling.hpp"

RasterLayer::~RasterLayer() {
  delete _parameters;
  if (_tileImageProvider != NULL) {
    _tileImageProvider->layerDeleted(this);
    _tileImageProvider->_release();
  }
#ifdef JAVA_CODE
  super.dispose();
#endif
}

RasterLayer::RasterLayer(const TimeInterval&                    timeToCache,
                         const bool                             readExpired,
                         const LayerTilesRenderParameters*      parameters,
                         const float                            transparency,
                         const LayerCondition*                  condition,
                         std::vector<const Info*>*              layerInfo) :
Layer(transparency, condition, layerInfo),
_timeToCache(timeToCache),
_readExpired(readExpired),
_parameters(parameters),
_tileImageProvider(NULL)
{
}

bool RasterLayer::isEquals(const Layer* that) const {
  if (this == that) {
    return true;
  }

  if (that == NULL) {
    return false;
  }

  if (!Layer::isEquals(that)) {
    return false;
  }

  RasterLayer* rasterThat = (RasterLayer*) that;

  return ((_timeToCache.milliseconds() == rasterThat->_timeToCache.milliseconds()) &&
          (_readExpired                == rasterThat->_readExpired));
}

TileImageProvider* RasterLayer::createTileImageProvider(const G3MRenderContext* rc,
                                                        const LayerTilesRenderParameters* layerTilesRenderParameters) const {
  if (_tileImageProvider == NULL) {
    _tileImageProvider = new RasterLayerTileImageProvider(this, rc->getDownloader());;
  }
  _tileImageProvider->_retain();
  return _tileImageProvider;
}

const TileImageContribution* RasterLayer::contribution(const Tile* tile) const {
  if ((_condition == NULL) || _condition->isAvailable(tile)) {
    return rawContribution(tile);
  }
  return NULL;
}

long long RasterLayer::requestImage(const Tile* tile,
                                    IDownloader* downloader,
                                    long long tileDownloadPriority,
                                    bool logDownloadActivity,
                                    IImageDownloadListener* listener,
                                    bool deleteListener) const {
  const Tile* suitableTile = getParentTileOfSuitableLevel(tile);
    
  const URL url = createURL(suitableTile);
  if (logDownloadActivity) {
    ILogger::instance()->logInfo("Downloading %s", url._path.c_str());
  }
  return downloader->requestImage(url,
                                  tileDownloadPriority,
                                  _timeToCache,
                                  _readExpired,
                                  listener,
                                  deleteListener);
}

const std::vector<const LayerTilesRenderParameters*> RasterLayer::getLayerTilesRenderParametersVector() const {
  std::vector<const LayerTilesRenderParameters*> parametersVector;
  if (_parameters != NULL) {
    parametersVector.push_back(_parameters);
  }
  return parametersVector;
}

void RasterLayer::setParameters(const LayerTilesRenderParameters* parameters) {
  if (_parameters != parameters) {
    delete _parameters;
    _parameters = parameters;
    notifyChanges();
  }
}

const Tile* RasterLayer::getParentTileOfSuitableLevel(const Tile* tile) const {
  const int maxLevel = _parameters->_maxLevel;
#ifdef C_CODE
  const Tile* result = tile;
#endif
#ifdef JAVA_CODE
  Tile result = tile;
#endif
  while ((result != NULL) && (result->_level > maxLevel)) {
    result = result->getParent();
  }
  return result;
}

void RasterLayer::selectLayerTilesRenderParameters(int index) {
  THROW_EXCEPTION("Logic error");
}

const std::vector<URL*> RasterLayer::getDownloadURLs(const Tile* tile) const {
  std::vector<URL*> result;
  result.push_back( new URL(createURL(tile)) );
  return result;
}
