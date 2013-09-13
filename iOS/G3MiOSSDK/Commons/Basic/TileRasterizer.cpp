//
//  TileRasterizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/8/13.
//
//

#include "TileRasterizer.hpp"

#include "ILogger.hpp"
#include "ChangedListener.hpp"
#include "IImageListener.hpp"

void TileRasterizer::setChangeListener(ChangedListener* listener) {
  if (_listener != NULL) {
    ILogger::instance()->logError("Listener already set");
  }
  _listener = listener;
}

void TileRasterizer::notifyChanges() const {
  if (_listener != NULL) {
    _listener->changed();
  }
}

void TileRasterizer::rasterize(const IImage* image,
                               const TileRasterizerContext& trc,
                               IImageListener* listener,
                               bool autodelete) const {
  if (_enable) {
    rawRasterize(image,
                 trc,
                 listener,
                 autodelete);
  }
  else {
    listener->imageCreated(image);
    if (autodelete) {
      delete listener;
    }
  }
}

void TileRasterizer::setEnable(bool enable) {
  if (enable != _enable) {
    _enable = enable;
    notifyChanges();
  }
}
