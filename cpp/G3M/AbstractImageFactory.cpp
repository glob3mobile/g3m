//
//  AbstractImageFactory.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/3/14.
//
//

#include "AbstractImageFactory.hpp"

#include "ChangedListener.hpp"
#include "ILogger.hpp"
#include "ErrorHandling.hpp"

void AbstractImageFactory::changed() {
  if (_changeListener != NULL) {
    _changeListener->changed();
  }
}

void AbstractImageFactory::setChangeListener(ChangedListener* changeListener) {
  if (_changeListener != NULL) {
    THROW_EXCEPTION("changeListener already set!");
  }
  _changeListener = changeListener;
}
