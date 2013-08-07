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
