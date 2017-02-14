//
//  GEOVectorTileImageProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/17/14.
//
//

#include "GEOVectorTileImageProvider.hpp"

#include "GEOVectorLayer.hpp"
#include "FrameTasksExecutor.hpp"
#include "Tile.hpp"
#include "IFactory.hpp"
#include "ICanvas.hpp"
#include "TileImageListener.hpp"
#include "GEORasterProjection.hpp"
#include "TileImageContribution.hpp"
#include "ErrorHandling.hpp"
#include "IStringBuilder.hpp"

GEOVectorTileImageProvider::GEOVectorTileImageProvider(const GEOVectorLayer* layer) :
_layer(layer)
{
}

const TileImageContribution* GEOVectorTileImageProvider::contribution(const Tile* tile) {
  return (_layer == NULL) ? NULL : _layer->contribution(tile);
}


GEOVectorTileImageProvider::GEORasterizerFrameTask::~GEORasterizerFrameTask() {
  _geoVectorTileImageProvider->rasterizerDeleted(_tileID);
  _geoVectorTileImageProvider->_release();

  if (_deleteListener) {
    delete _listener;
  }

  if (_contribution != NULL) {
    _contribution->_release();
  }

#ifdef JAVA_CODE
  super.dispose();
#endif
}


void GEOVectorTileImageProvider::GEORasterizerFrameTask::cancel() {
  _isCanceled = true;
  _listener->imageCreationCanceled(_tileID);
}

bool GEOVectorTileImageProvider::GEORasterizerFrameTask::isCanceled(const G3MRenderContext* rc) {
  return _isCanceled;
}

void GEOVectorTileImageProvider::GEORasterizerFrameTask::execute(const G3MRenderContext* rc) {
  _geoVectorTileImageProvider->rasterize(_contribution,
                                         _tileID,
                                         _tileSector,
                                         _tileMercator,
                                         _tileLevel,
                                         _resolutionWidth,
                                         _resolutionHeight,
                                         _listener,
                                         _deleteListener);
  _listener     = NULL; // moves ownership to _geoVectorTileImageProvider
  _contribution = NULL; // moves ownership to _geoVectorTileImageProvider
}

bool GEOVectorTileImageProvider::GEORasterizerQuadTreeVisitor::visitElement(const Sector&           sector,
                                                                            const QuadTree_Content* content) const {
  GEORasterSymbol* symbol = (GEORasterSymbol*) content;
  symbol->rasterize(_canvas, _projection, _tileLevel);
  return false;
}

void GEOVectorTileImageProvider::GEORasterizerQuadTreeVisitor::endVisit(bool aborted) const {

}

const std::string GEOVectorTileImageProvider::GEORasterizerCanvasImageListener::getImageID(const std::string& tileID) const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("GEOVectorTileImageProvider/");
  isb->addString(tileID);
  const std::string s = isb->getString();
  delete isb;
  return s;
}

void GEOVectorTileImageProvider::GEORasterizerCanvasImageListener::imageCreated(const IImage* image) {
  _listener->imageCreated(_tileID,
                          image,
                          getImageID(_tileID),
                          _contribution);
  if (_deleteListener) {
    delete _listener;
  }
}

void GEOVectorTileImageProvider::rasterize(const TileImageContribution* contribution,
                                           const std::string& tileID,
                                           const Sector& tileSector,
                                           bool tileMercator,
                                           int tileLevel,
                                           int resolutionWidth,
                                           int resolutionHeight,
                                           TileImageListener* listener,
                                           bool deleteListener) {
  ICanvas* canvas = IFactory::instance()->createCanvas(false);
  canvas->initialize(resolutionWidth, resolutionHeight);

  GEORasterProjection* projection = new GEORasterProjection(tileSector,
                                                            tileMercator,
                                                            resolutionWidth, resolutionHeight);

  _layer->getQuadTree().acceptVisitor(tileSector,
                                      GEORasterizerQuadTreeVisitor(canvas, projection, tileLevel));

  delete projection;

  canvas->createImage(new GEORasterizerCanvasImageListener(contribution,
                                                           tileID,
                                                           listener,
                                                           deleteListener),
                      true /* autodelete */);

  delete canvas;
}

void GEOVectorTileImageProvider::create(const Tile* tile,
                                        const TileImageContribution* contribution,
                                        const Vector2S& resolution,
                                        long long tileTextureDownloadPriority,
                                        bool logDownloadActivity,
                                        TileImageListener* listener,
                                        bool deleteListener,
                                        FrameTasksExecutor* frameTasksExecutor) {

  const std::string tileID = tile->_id;
  GEORasterizerFrameTask* rasterizer = new GEORasterizerFrameTask(this,
                                                                  contribution,
                                                                  tileID,
                                                                  tile->_sector,
                                                                  tile->_mercator,
                                                                  tile->_level,
                                                                  resolution,
                                                                  listener,
                                                                  deleteListener);
  _rasterizers[tileID] = rasterizer;

  frameTasksExecutor->addPreRenderTask(rasterizer);
}

void GEOVectorTileImageProvider::cancel(const std::string& tileID) {
#ifdef C_CODE
  if (_rasterizers.find(tileID) != _rasterizers.end()) {
    GEORasterizerFrameTask* rasterizer = _rasterizers[tileID];
    rasterizer->cancel();
  }
#endif
#ifdef JAVA_CODE
  final GEORasterizerFrameTask rasterizer = _rasterizers.get(tileID);
  if (rasterizer != null) {
    rasterizer.cancel();
  }
#endif
}

void GEOVectorTileImageProvider::rasterizerDeleted(const std::string& tileID) {
#ifdef C_CODE
  if (_rasterizers.find(tileID) != _rasterizers.end()) {
    _rasterizers.erase(tileID);
  }
#endif
#ifdef JAVA_CODE
  _rasterizers.remove(tileID);
#endif
}

void GEOVectorTileImageProvider::layerDeleted(const GEOVectorLayer* layer) {
  if (layer != _layer) {
    THROW_EXCEPTION("Logic error");
  }
  _layer = NULL;
}
