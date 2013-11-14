//
//  GEOTileRasterizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/10/13.
//
//

#include "GEOTileRasterizer.hpp"

#include "GEORasterSymbol.hpp"
#include "Tile.hpp"
#include "IImageListener.hpp"
#include "IImage.hpp"
#include "ICanvas.hpp"
#include "Color.hpp"
#include "GFont.hpp"
#include "IStringBuilder.hpp"
#include "GEORasterProjection.hpp"


void GEOTileRasterizer::initialize(const G3MContext* context) {
}

void GEOTileRasterizer::addSymbol(const GEORasterSymbol* symbol) {
  const Sector* sector = symbol->getSector();

  if (sector == NULL) {
    //    ILogger::instance()->logError("Symbol %s has not sector, can't symbolize",
    //                                  symbol->description().c_str());
    delete symbol;
  }
  else {
    const bool added = _quadTree.add(*sector, symbol);
    if (added) {
      notifyChanges();
    }
    else {
      delete symbol;
    }
  }
}

class GEOTileRasterizer_QuadTreeVisitor : public QuadTreeVisitor {
private:
  ICanvas*                   _canvas;
  const GEORasterProjection* _projection;
  const int                  _tileLevel;

public:
  GEOTileRasterizer_QuadTreeVisitor(ICanvas* canvas,
                                    const GEORasterProjection* projection,
                                    int tileLevel) :
  _canvas(canvas),
  _projection(projection),
  _tileLevel(tileLevel)
  {
  }

  bool visitElement(const Sector& sector,
                    const void*   element) const {
    GEORasterSymbol* symbol = (GEORasterSymbol*) element;

    symbol->rasterize(_canvas, _projection, _tileLevel);

    return false;
  }

  void endVisit(bool aborted) const {
  }

};


//class GEOTileRasterizer_ASync : public GAsyncTask {
//private:
//  const IImage*   _image;
//  const Tile*     _tile;
//  const bool      _mercator;
//  IImageListener* _listener;
//  bool            _autodelete;
//  const QuadTree* _quadTree;
//  
//public:
//  GEOTileRasterizer_ASync(const IImage*   image,
//                          const Tile*     tile,
//                          const bool      mercator,
//                          IImageListener* listener,
//                          bool            autodelete,
//                          const QuadTree* quadTree) :
//  _image(image),
//  _tile(tile),
//  _mercator(mercator),
//  _listener(listener),
//  _quadTree(quadTree)
//  {
//
//  }
//
//  void runInBackground(const G3MContext* context) {
//    const int width  = _image->getWidth();
//    const int height = _image->getHeight();
//
//    GEORasterProjection* projection = new GEORasterProjection(_tile->getSector(), _mercator,
//                                                              width, height);
//
//    ICanvas* canvas = IFactory::instance()->createCanvas();
//    canvas->initialize(width, height);
//
//    canvas->drawImage(_image, 0, 0);
//
//    _quadTree->acceptVisitor(_tile->getSector(),
//                             GEOTileRasterizer_QuadTreeVisitor(canvas, projection, _tile->_level));
//
//    canvas->createImage(_listener, _autodelete);
//
//    delete _image;
//    _image = NULL;
//    
//    delete projection;
//
//    delete canvas;
//  }
//
//  void onPostExecute(const G3MContext* context) {
//
//  }
//
//};

void GEOTileRasterizer::rawRasterize(const IImage* image,
                                     const TileRasterizerContext& trc,
                                     IImageListener* listener,
                                     bool autodelete) const {

//  _context->getThreadUtils()->invokeAsyncTask(new GEOTileRasterizer_ASync(image,
//                                                                          trc._tile,
//                                                                          trc._mercator,
//                                                                          listener,
//                                                                          autodelete,
//                                                                          &_quadTree),
//                                              true);

  const Tile*   tile     = trc._tile;
  const bool    mercator = trc._mercator;

  const int width  = image->getWidth();
  const int height = image->getHeight();

  GEORasterProjection* projection = new GEORasterProjection(tile->_sector, mercator,
                                                            width, height);

  ICanvas* canvas = getCanvas(width, height);

  canvas->drawImage(image, 0, 0);

  _quadTree.acceptVisitor(tile->_sector,
                          GEOTileRasterizer_QuadTreeVisitor(canvas, projection, tile->_level));

  canvas->createImage(listener, autodelete);
  
  delete image;
  
  delete projection;
}
