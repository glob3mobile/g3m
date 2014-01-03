//
//  IImageBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/2/14.
//
//

#include "IImageBuilder.hpp"

#include "ChangedListener.hpp"
#include "ILogger.hpp"

void IImageBuilder::changed() {
  if (_listener != NULL) {
    _listener->changed();
  }
}

void IImageBuilder::setChangeListener(ChangedListener* listener) {
  if (_listener != NULL) {
    ILogger::instance()->logError("listener already set!");
  }
  _listener = listener;
}
