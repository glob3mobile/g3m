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

void DebugTileImageProvider::ImageListener::imageCreated(const IImage* image) {
  _listener->imageCreated(_tile,
                          image,
                          _tile->_sector,
                          RectangleF(0, 0, image->getWidth(), image->getHeight()),
                          1 /* alpha */);
  if (_deleteListener) {
    delete _listener;
  }
}

TileImageContribution DebugTileImageProvider::contribution(Tile* tile) {
  return FULL_COVERAGE_TRANSPARENT;
}

void DebugTileImageProvider::create(Tile* tile,
                                    const Vector2I& resolution,
                                    TileImageListener* listener,
                                    bool deleteListener) {
  const int width  = resolution._x;
  const int height = resolution._y;

  ICanvas* canvas = getCanvas(width, height);

  canvas->setLineColor(Color::green());
  canvas->setLineWidth(1);
  canvas->strokeRectangle(0, 0, width, height);

  canvas->createImage(new DebugTileImageProvider::ImageListener(tile,
                                                                listener,
                                                                deleteListener),
                      true);
}

void DebugTileImageProvider::cancel(Tile* tile) {
  // do nothing, can't cancel
}
