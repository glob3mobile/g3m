//
//  Layer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 23/08/12.
//
//

#include "Layer.hpp"

#include "TimeInterval.hpp"
#include "LayerCondition.hpp"
#include "LayerSet.hpp"
#include "LayerTilesRenderParameters.hpp"
#include "LayerTouchEventListener.hpp"

Layer::Layer(LayerCondition* condition,
             const TimeInterval& timeToCache,
             bool readExpired,
             const LayerTilesRenderParameters* parameters,
             float transparency) :
_condition(condition),
_layerSet(NULL),
_timeToCacheMS(timeToCache._milliseconds),
_readExpired(readExpired),
_enable(true),
_parameters(parameters),
_title(""),
_transparency(transparency)
{
}

Layer::~Layer() {
  delete _condition;
  delete _parameters;
}

const TimeInterval Layer::getTimeToCache() const {
  return TimeInterval::fromMilliseconds(_timeToCacheMS);
}

bool Layer::isAvailable(const G3MRenderContext* rc,
                        const Tile* tile) const {
  if (!isEnable()) {
    return false;
  }
  if (_condition == NULL) {
    return true;
  }
  return _condition->isAvailable(rc, tile);
}

bool Layer::isAvailable(const G3MEventContext* ec,
                        const Tile* tile) const {
  if (!isEnable()) {
    return false;
  }
  if (_condition == NULL) {
    return true;
  }
  return _condition->isAvailable(ec, tile);
}

void Layer::setLayerSet(LayerSet* layerSet) {
  if (_layerSet != NULL) {
    ILogger::instance()->logError("LayerSet already set.");
  }
  _layerSet = layerSet;
}

void Layer::removeLayerSet(LayerSet* layerSet) {
  if (_layerSet != layerSet) {
    ILogger::instance()->logError("_layerSet doesn't match.");
  }
  _layerSet = NULL;
}

void Layer::notifyChanges() const {
  if (_layerSet != NULL) {
    _layerSet->layerChanged(this);
  }
}

const std::string Layer::getTitle() const {
  return _title;
}

void Layer::setTitle(const std::string& title) {
  _title = title;
}

void Layer::setParameters(const LayerTilesRenderParameters* parameters) {
  if (parameters != _parameters) {
    delete _parameters;
    _parameters = parameters;
    notifyChanges();
  }
}

bool Layer::isEquals(const Layer* that) const {
  if (this == that) {
    return true;
  }

  if (that == NULL) {
    return false;
  }

  if (getLayerType() != that->getLayerType()) {
    return false;
  }

  if (_condition != that->_condition) {
    return false;
  }

  const int thisListenersSize = _listeners.size();
  const int thatListenersSize = that->_listeners.size();
  if (thisListenersSize != thatListenersSize) {
    return false;
  }

  for (int i = 0; i < thisListenersSize; i++) {
    if (_listeners[i] != that->_listeners[i]) {
      return false;
    }
  }

  if (_enable != that->_enable) {
    return false;
  }

  if (!_parameters->isEquals(that->_parameters)) {
    return false;
  }

  if (_timeToCacheMS != that->_timeToCacheMS) {
    return false;
  }

  if (_readExpired != that->_readExpired) {
    return false;
  }

  return rawIsEquals(that);
}

bool Layer::onLayerTouchEventListener(const G3MEventContext* ec,
                                      const LayerTouchEvent& tte) const {
  const int listenersSize = _listeners.size();
  for (int i = 0; i < listenersSize; i++) {
    LayerTouchEventListener* listener = _listeners[i];
    if (listener != NULL) {
      if (listener->onTerrainTouch(ec, tte)) {
        return true;
      }
    }
  }
  return false;
}

