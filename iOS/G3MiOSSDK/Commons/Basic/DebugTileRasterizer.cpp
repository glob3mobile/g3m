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
#include "Color.hpp"
#include "GFont.hpp"
#include "Tile.hpp"
#include "IStringBuilder.hpp"

#include "TextCanvasElement.hpp"
#include "ColumnCanvasElement.hpp"

std::string DebugTileRasterizer::getTileLabel1(const Tile* tile) const {
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

std::string DebugTileRasterizer::getTileLabel2(const Tile* tile) const {
  return "Lower lat: " + tile->getSector().lower().latitude().description();
}

std::string DebugTileRasterizer::getTileLabel3(const Tile* tile) const {
  return "Lower lon: " + tile->getSector().lower().longitude().description();
}

std::string DebugTileRasterizer::getTileLabel4(const Tile* tile) const {
  return "Upper lat: " + tile->getSector().upper().latitude().description();
}

std::string DebugTileRasterizer::getTileLabel5(const Tile* tile) const {
  return "Upper lon: " + tile->getSector().upper().longitude().description();
}

void DebugTileRasterizer::rasterize(IImage* image,
                                    const Tile* tile,
                                    IImageListener* listener,
                                    bool autodelete) const {

  const int width  = image->getWidth();
  const int height = image->getHeight();

  ICanvas* canvas = IFactory::instance()->createCanvas();
  canvas->initialize(width, height);

  canvas->drawImage(image, 0, 0);

  canvas->setStrokeColor(Color::yellow());
  canvas->setStrokeWidth(2);
  canvas->strokeRectangle(0, 0, width, height);


  ColumnCanvasElement col;
  col.add( new TextCanvasElement(getTileLabel1(tile), GFont::serif(), Color::yellow()) );

  const GFont sectorFont = GFont::monospaced(14);
  const Color sectorColor = Color::yellow();
  col.add( new TextCanvasElement(getTileLabel2(tile), sectorFont, sectorColor) );
  col.add( new TextCanvasElement(getTileLabel3(tile), sectorFont, sectorColor) );
  col.add( new TextCanvasElement(getTileLabel4(tile), sectorFont, sectorColor) );
  col.add( new TextCanvasElement(getTileLabel5(tile), sectorFont, sectorColor) );

  const Vector2F colExtent = col.getExtent(canvas);
  col.drawAt((width  - colExtent._x) / 2,
             (height - colExtent._y) / 2,
             canvas);

  canvas->createImage(listener, autodelete);

  delete canvas;
  
  delete image;
}
