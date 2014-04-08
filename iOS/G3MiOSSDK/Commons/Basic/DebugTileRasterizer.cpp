//
//  DebugTileRasterizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/8/13.
//
//

#include "DebugTileRasterizer.hpp"

#include "IFactory.hpp"
#include "ICanvas.hpp"
#include "IImage.hpp"
#include "IImageListener.hpp"
#include "Tile.hpp"
#include "IStringBuilder.hpp"

#include "TextCanvasElement.hpp"
#include "ColumnCanvasElement.hpp"

void DebugTileRasterizer::initialize(const G3MContext* context) {
    _threadUtils = context->getThreadUtils(); //init for background rasterize
    
}

DebugTileRasterizer::DebugTileRasterizer(const GFont& font,
                                         const Color& color,
                                         bool showLabels,
                                         bool showTileBounds) :
_font(font),
_color(color),
_showLabels(showLabels),
_showTileBounds(showTileBounds)
{

}

DebugTileRasterizer::DebugTileRasterizer() :
_font(GFont::monospaced(15)),
_color(Color::yellow()),
_showLabels(true),
_showTileBounds(true)
{
}


DebugTileRasterizer::~DebugTileRasterizer() {
#ifdef JAVA_CODE
  super.dispose();
#endif
}


std::string DebugTileRasterizer::getTileKeyLabel(const Tile* tile) const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("L:");
  isb->addInt( tile->_level );

  isb->addString(", C:");
  isb->addInt( tile->_column );

  isb->addString(", R:");
  isb->addInt( tile->_row );

  const std::string s = isb->getString();
  delete isb;
  return s;
}

std::string DebugTileRasterizer::getSectorLabel1(const Sector& sector) const {
  return "Lower lat: " + sector._lower._latitude.description();
}

std::string DebugTileRasterizer::getSectorLabel2(const Sector& sector) const {
  return "Lower lon: " + sector._lower._longitude.description();
}

std::string DebugTileRasterizer::getSectorLabel3(const Sector& sector) const {
  return "Upper lat: " + sector._upper._latitude.description();
}

std::string DebugTileRasterizer::getSectorLabel4(const Sector& sector) const {
  return "Upper lon: " + sector._upper._longitude.description();
}

ICanvas* DebugTileRasterizer::buildCanvas(const IImage* image,
                     const TileRasterizerContext* trc,
                     IImageListener* listener,
                     bool autodelete) const {
    
    const Tile*   tile  = trc->_tile;
    
    const int width  = image->getWidth();
    const int height = image->getHeight();
    
    ICanvas* canvas = getCanvas(width, height);
    
    canvas->removeShadow();
    
    canvas->drawImage(image, 0, 0);
    
    if (_showTileBounds) {
        canvas->setLineColor(_color);
        canvas->setLineWidth(1);
        canvas->strokeRectangle(0, 0, width, height);
    }
    
    if (_showLabels) {
        canvas->setShadow(Color::black(), 2, 1, -1);
        ColumnCanvasElement col;
        col.add( new TextCanvasElement(getTileKeyLabel(tile), _font, _color) );
        
        const Sector sectorTile = tile->_sector;
        col.add( new TextCanvasElement(getSectorLabel1(sectorTile), _font, _color) );
        col.add( new TextCanvasElement(getSectorLabel2(sectorTile), _font, _color) );
        col.add( new TextCanvasElement(getSectorLabel3(sectorTile), _font, _color) );
        col.add( new TextCanvasElement(getSectorLabel4(sectorTile), _font, _color) );
        
        const Vector2F colExtent = col.getExtent(canvas);
        col.drawAt((width  - colExtent._x) / 2,
                   (height - colExtent._y) / 2,
                   canvas);
    }
    
    return canvas;
    
}

void DebugTileRasterizer::rawRasterize(const IImage* image,
                                       const TileRasterizerContext& trc,
                                       IImageListener* listener,
                                       bool autodelete) const {

//  const Tile*   tile  = trc._tile;
//
//  const int width  = image->getWidth();
//  const int height = image->getHeight();
//
//  ICanvas* canvas = getCanvas(width, height);
//
//  canvas->removeShadow();
//
//  canvas->drawImage(image, 0, 0);
//
//  if (_showTileBounds) {
//    canvas->setLineColor(_color);
//    canvas->setLineWidth(1);
//    canvas->strokeRectangle(0, 0, width, height);
//  }
//
//  if (_showLabels) {
//    canvas->setShadow(Color::black(), 2, 1, -1);
//    ColumnCanvasElement col;
//    col.add( new TextCanvasElement(getTileKeyLabel(tile), _font, _color) );
//
//    const Sector sectorTile = tile->_sector;
//    col.add( new TextCanvasElement(getSectorLabel1(sectorTile), _font, _color) );
//    col.add( new TextCanvasElement(getSectorLabel2(sectorTile), _font, _color) );
//    col.add( new TextCanvasElement(getSectorLabel3(sectorTile), _font, _color) );
//    col.add( new TextCanvasElement(getSectorLabel4(sectorTile), _font, _color) );
//
//    const Vector2F colExtent = col.getExtent(canvas);
//    col.drawAt((width  - colExtent._x) / 2,
//               (height - colExtent._y) / 2,
//               canvas);
//  }
    
  ICanvas* canvas = buildCanvas(image, &trc, listener, autodelete);

  canvas->createImage(listener, autodelete);
  
  delete image;
}



class DebugTileRasterizer_AsyncTask : public TileRasterizer_AsyncTask {
private:
    const DebugTileRasterizer*     _debugTileRasterizer;
#ifdef C_CODE
    mutable ICanvas* _canvas;
#endif
#ifdef JAVA_CODE
    private ICanvas _canvas;
#endif
    
    
public:
    DebugTileRasterizer_AsyncTask(const IImage* image,
                                  const TileRasterizerContext* trc,
                                  IImageListener* listener,
                                  bool autodelete,
                                  const DebugTileRasterizer* tileRasterizer):
    TileRasterizer_AsyncTask(image,
                             trc,
                             listener,
                             autodelete),
    _debugTileRasterizer(tileRasterizer),
    _canvas(NULL)
    {
        //_trc = new TileRasterizerContext(trc->_tile, trc->_mercator);
        ILogger::instance()->logInfo("_builder->_retain from DebugTileRasterizer_AsyncTask: %p, [%d,%d]", _builder, _trc->_tile->_row, _trc->_tile->_column);
    }
    
    
    ~DebugTileRasterizer_AsyncTask() {
        ILogger::instance()->logInfo("terminando la tarea DebugTileRasterizer: [%d,%d]", _trc->_tile->_row, _trc->_tile->_column);
        //TileRasterizer_AsyncTask::~TileRasterizer_AsyncTask();
    }
    
    void runInBackground(const G3MContext* context) {
        ILogger::instance()->logInfo("runInBackground DebugTileRasterizer: [%d,%d]", _trc->_tile->_row, _trc->_tile->_column);
        _canvas = _debugTileRasterizer->buildCanvas(_image, _trc, _listener, _autodelete);
        _canvas->createImage(_listener, _autodelete);
        delete _image;
    }
    
    void onPostExecute(const G3MContext* context) {
        
//        _canvas = _debugTileRasterizer->buildCanvas(_image, _trc, _listener, _autodelete);
//        _canvas->createImage(_listener, _autodelete);
//        delete _image;
    }
};


TileRasterizer_AsyncTask* DebugTileRasterizer::getRawRasterizeTask(const IImage* image,
                                                                   const TileRasterizerContext& trc,
                                                                   IImageListener* listener,
                                                                   bool autodelete) const {

    //return NULL;
    
    return new DebugTileRasterizer_AsyncTask(image,
                                             &trc,
                                             listener,
                                             autodelete,
                                             this);
    
}
