//
//  AbstractImageBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/3/14.
//
//

#include "AbstractImageBuilder.hpp"

#include "ChangedListener.hpp"
#include "ILogger.hpp"
#include "ErrorHandling.hpp"

void AbstractImageBuilder::changed() {
  if (_changeListener != NULL) {
    _changeListener->changed();
  }
}

void AbstractImageBuilder::setChangeListener(ChangedListener* changeListener) {
  if (_changeListener != NULL) {
    THROW_EXCEPTION("changeListener already set!");
  }
  _changeListener = changeListener;
}
