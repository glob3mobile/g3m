//
//  DebugTileImageProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/18/14.
//
//

#include "DebugTileImageProvider.hpp"

#include "Vector2I.hpp"
#include "ICanvas.hpp"
#include "Color.hpp"
#include "TileImageListener.hpp"
#include "Tile.hpp"
#include "IImage.hpp"
#include "RectangleF.hpp"
#include "IStringBuilder.hpp"
#include "TileImageContribution.hpp"

#include "IStringBuilder.hpp"
#include "TextCanvasElement.hpp"
#include "ColumnCanvasElement.hpp"
#include "IFactory.hpp"

DebugTileImageProvider::ImageListener::ImageListener(const std::string&           tileId,
                                                     const TileImageContribution* contribution,
                                                     TileImageListener*           listener,
                                                     bool                         deleteListener) :
_tileId(tileId),
_contribution(contribution),
_listener(listener),
_deleteListener(deleteListener)
{
  TileImageContribution::retainContribution(_contribution);
}

DebugTileImageProvider::ImageListener::~ImageListener() {
  TileImageContribution::releaseContribution(_contribution);
#ifdef JAVA_CODE
  super.dispose();
#endif
}

const std::string DebugTileImageProvider::ImageListener::getImageId(const std::string& tileId) {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("DebugTileImageProvider/");
  isb->addString(tileId);
  const std::string s = isb->getString();
  delete isb;
  return s;
}

void DebugTileImageProvider::ImageListener::imageCreated(const IImage* image) {
  const std::string imageId = getImageId(_tileId);
  _listener->imageCreated(_tileId,
                          image,
                          imageId,
                          _contribution);
  if (_deleteListener) {
    delete _listener;
  }
}

const TileImageContribution* DebugTileImageProvider::contribution(const Tile* tile) {
  return TileImageContribution::fullCoverageTransparent(1);
}



DebugTileImageProvider::DebugTileImageProvider(const GFont& font,
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

DebugTileImageProvider::DebugTileImageProvider() :
_font(GFont::monospaced(15)),
_color(Color::yellow()),
_showIDLabel(true),
_showSectorLabels(true),
_showTileBounds(true)
{
}


std::string DebugTileImageProvider::getIDLabel(const Tile* tile) const {
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

std::string DebugTileImageProvider::getSectorLabel1(const Sector& sector) const {
  return "Lower lat: " + sector._lower._latitude.description();
}

std::string DebugTileImageProvider::getSectorLabel2(const Sector& sector) const {
  return "Lower lon: " + sector._lower._longitude.description();
}

std::string DebugTileImageProvider::getSectorLabel3(const Sector& sector) const {
  return "Upper lat: " + sector._upper._latitude.description();
}

std::string DebugTileImageProvider::getSectorLabel4(const Sector& sector) const {
  return "Upper lon: " + sector._upper._longitude.description();
}

void DebugTileImageProvider::create(const Tile* tile,
                                    const TileImageContribution* contribution,
                                    const Vector2I& resolution,
                                    long long tileDownloadPriority,
                                    bool logDownloadActivity,
                                    TileImageListener* listener,
                                    bool deleteListener,
                                    FrameTasksExecutor* frameTasksExecutor) {
  const int width  = resolution._x;
  const int height = resolution._y;
  
  ICanvas* canvas = getCanvas(width, height);
  
  //canvas->removeShadow();
  
  //canvas->clearRect(0, 0, width, height);

  
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
  
  //ILogger::instance()->logInfo(getIDLabel(tile));
  
  canvas->createImage(new DebugTileImageProvider::ImageListener(tile->_id,
                                                                contribution,
                                                                listener,
                                                                deleteListener),
                      true);  
}

void DebugTileImageProvider::cancel(const std::string& tileId) {
  // do nothing, can't cancel
}
