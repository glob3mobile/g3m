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

DebugTileRasterizer::DebugTileRasterizer(const GFont& font,
                                         const Color& color,
                                         bool showIDLabel,
                                         bool showSectorLabels,
                                         bool showTileBounds) :
_font(font),
_color(color),
_showIDLabel(showIDLabel),
_showSectorLabels(showSectorLabels),
_showTileBounds(showTileBounds)
{

}

DebugTileRasterizer::DebugTileRasterizer() :
_font(GFont::monospaced(15)),
_color(Color::yellow()),
_showIDLabel(true),
_showSectorLabels(true),
_showTileBounds(true)
{
}


DebugTileRasterizer::~DebugTileRasterizer() {
#ifdef JAVA_CODE
  super.dispose();
#endif
}

std::string DebugTileRasterizer::getIDLabel(const Tile* tile) const {
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

void DebugTileRasterizer::rawRasterize(const IImage* image,
                                       const TileRasterizerContext& trc,
                                       IImageListener* listener,
                                       bool autodelete) const {

  const Tile*   tile  = trc._tile;

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

  if (_showIDLabel || _showSectorLabels) {
    canvas->setShadow(Color::black(), 2, 1, -1);
    ColumnCanvasElement col;
    if (_showIDLabel) {
      col.add( new TextCanvasElement(getIDLabel(tile), _font, _color) );
    }

    if (_showSectorLabels) {
      const Sector sectorTile = tile->_sector;
      col.add( new TextCanvasElement(getSectorLabel1(sectorTile), _font, _color) );
      col.add( new TextCanvasElement(getSectorLabel2(sectorTile), _font, _color) );
      col.add( new TextCanvasElement(getSectorLabel3(sectorTile), _font, _color) );
      col.add( new TextCanvasElement(getSectorLabel4(sectorTile), _font, _color) );
    }

    const Vector2F colExtent = col.getExtent(canvas);
    col.drawAt((width  - colExtent._x) / 2,
               (height - colExtent._y) / 2,
               canvas);
  }

  canvas->createImage(listener, autodelete);
  
  delete image;
}
