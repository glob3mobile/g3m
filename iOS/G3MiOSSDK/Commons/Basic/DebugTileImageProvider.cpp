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

TileImageContribution DebugTileImageProvider::contribution(const Tile* tile) {
  //return FULL_COVERAGE_TRANSPARENT;
  return TileImageContribution::fullCoverageTransparent(1);
}

void DebugTileImageProvider::create(const Tile* tile,
                                    const TileImageContribution& contribution,
                                    const Vector2I& resolution,
                                    long long tileDownloadPriority,
                                    bool logDownloadActivity,
                                    TileImageListener* listener,
                                    bool deleteListener) {
  const int width  = resolution._x;
  const int height = resolution._y;

  ICanvas* canvas = getCanvas(width, height);

  canvas->setLineColor(Color::green());
  canvas->setLineWidth(1);
  canvas->strokeRectangle(0, 0, width, height);

  canvas->createImage(new DebugTileImageProvider::ImageListener(tile->_id,
                                                                contribution,
                                                                listener,
                                                                deleteListener),
                      true);
}

void DebugTileImageProvider::cancel(const Tile* tile) {
  // do nothing, can't cancel
}
