//
//  Layer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 23/08/12.
//
//

#include "Layer.hpp"

#include "LayerCondition.hpp"
#include "LayerSet.hpp"
#include "LayerTilesRenderParameters.hpp"
#include "LayerTouchEventListener.hpp"

Layer::Layer(const LayerTilesRenderParameters* parameters,
             const float                       transparency,
             const LayerCondition*             condition,
             const std::string&                disclaimerInfo) :
_parameters(parameters),
_transparency(transparency),
_condition(condition),
_disclaimerInfo(disclaimerInfo),
_layerSet(NULL),
_enable(true),
_title("")
{
}

Layer::~Layer() {
  delete _condition;
  delete _parameters;
}

bool Layer::isAvailable(const Tile* tile) const {
  if (!isEnable()) {
    return false;
  }
  if (_condition == NULL) {
    return true;
  }
  return _condition->isAvailable(tile);
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
    _layerSet->changedInfo(_info);
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


  if (!(_info == that->_info)) {
    return false;
  }

  if (!(_disclaimerInfo == that->_disclaimerInfo)) {
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

void Layer::setInfo(const std::string& disclaimerInfo) {
  if (_disclaimerInfo != disclaimerInfo) {
    _disclaimerInfo = disclaimerInfo;
    if (_layerSet != NULL) {
      _layerSet->changedInfo(getInfos());
    }
  }
}
