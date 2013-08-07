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

DebugTileRasterizer::DebugTileRasterizer() :
_font(GFont::monospaced(15)),
_color(Color::white())
{
}


DebugTileRasterizer::~DebugTileRasterizer() {

}

std::string DebugTileRasterizer::getTileKeyLabel(const Tile* tile) const {
  IStringBuilder *isb = IStringBuilder::newStringBuilder();
  isb->addString("L:");
  isb->addInt( tile->getLevel() );

  isb->addString(", C:");
  isb->addInt( tile->getColumn() );

  isb->addString(", R:");
  isb->addInt( tile->getRow() );

  const std::string s = isb->getString();
  delete isb;
  return s;
}

std::string DebugTileRasterizer::getSectorLabel1(const Sector& sector) const {
  return "Lower lat: " + sector.lower().latitude().description();
}

std::string DebugTileRasterizer::getSectorLabel2(const Sector& sector) const {
  return "Lower lon: " + sector.lower().longitude().description();
}

std::string DebugTileRasterizer::getSectorLabel3(const Sector& sector) const {
  return "Upper lat: " + sector.upper().latitude().description();
}

std::string DebugTileRasterizer::getSectorLabel4(const Sector& sector) const {
  return "Upper lon: " + sector.upper().longitude().description();
}

//ICanvas* DebugTileRasterizer::getCanvas(int width, int height) const {
//  if ((_canvas == NULL) ||
//      (_canvasWidth  != width) ||
//      (_canvasHeight != height)) {
//    delete _canvas;
//
//    _canvas = IFactory::instance()->createCanvas();
//    _canvas->initialize(width, height);
//
//    _canvasWidth  = width;
//    _canvasHeight = height;
//  }
//  else {
//    _canvas->setFillColor(Color::transparent());
//    _canvas->fillRectangle(0, 0, width, height);
//  }
//  return _canvas;
//}

void DebugTileRasterizer::rasterize(IImage* image,
                                    const Tile* tile,
                                    bool mercator,
                                    IImageListener* listener,
                                    bool autodelete) const {

  const int width  = image->getWidth();
  const int height = image->getHeight();

  ICanvas* canvas = getCanvas(width, height);

  canvas->removeShadow();

  canvas->drawImage(image, 0, 0);

  canvas->setLineColor(_color);
  canvas->setLineWidth(1);
  canvas->strokeRectangle(0, 0, width, height);


  canvas->setShadow(Color::black(), 2, 1, -1);
  ColumnCanvasElement col;
  col.add( new TextCanvasElement(getTileKeyLabel(tile), _font, _color) );

  const Sector sectorTile = tile->getSector();
  col.add( new TextCanvasElement(getSectorLabel1(sectorTile), _font, _color) );
  col.add( new TextCanvasElement(getSectorLabel2(sectorTile), _font, _color) );
  col.add( new TextCanvasElement(getSectorLabel3(sectorTile), _font, _color) );
  col.add( new TextCanvasElement(getSectorLabel4(sectorTile), _font, _color) );

  const Vector2F colExtent = col.getExtent(canvas);
  col.drawAt((width  - colExtent._x) / 2,
             (height - colExtent._y) / 2,
             canvas);


  canvas->createImage(listener, autodelete);
  
  delete image;
}
