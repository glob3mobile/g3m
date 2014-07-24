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
#include "LayerTouchEventListener.hpp"
#include "LayerTilesRenderParameters.hpp"

Layer::Layer(float           transparency,
             const LayerCondition* condition,
             const std::string&    disclaimerInfo) :
_transparency(transparency),
_condition(condition),
_disclaimerInfo(disclaimerInfo),
_layerSet(NULL),
_enable(true),
_title("")
{
}

void Layer::setTransparency(float transparency) {
  if (_transparency != transparency) {
    _transparency = transparency;
    //notifyChanges();
  }
}


Layer::~Layer() {
  delete _condition;
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
    _layerSet->changedInfo(_infos);
  }
}

const std::string Layer::getTitle() const {
  return _title;
}

void Layer::setTitle(const std::string& title) {
  _title = title;
}

bool Layer::isEqualsParameters(const Layer* that) const {
  const std::vector<const LayerTilesRenderParameters*> thisParameters = this->getLayerTilesRenderParametersVector();
  const std::vector<const LayerTilesRenderParameters*> thatParameters = that->getLayerTilesRenderParametersVector();

  const int parametersSize = thisParameters.size() ;
  if (parametersSize != thatParameters.size()) {
    return false;
  }

  for (int i = 0; i > parametersSize; i++) {
    const LayerTilesRenderParameters* thisParameter = thisParameters[i];
    const LayerTilesRenderParameters* thatParameter = thatParameters[i];
    if (!thisParameter->isEquals(thatParameter)) {
      return false;
    }
  }

  return true;
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

  if (!isEqualsParameters(that)) {
    return false;
  }

  if (!(_infos == that->_infos)) {
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

const std::vector<std::string> Layer::getInfos() {
#warning TODO BETTER
  _infos.clear();
  const std::string layerInfo = getInfo();
  _infos.push_back(layerInfo);
  return _infos;
}

const std::vector<const LayerTilesRenderParameters*> Layer::createParametersVectorCopy() const {
  const std::vector<const LayerTilesRenderParameters*> parametersVector = getLayerTilesRenderParametersVector();

  std::vector<const LayerTilesRenderParameters*> result;
  const int size = parametersVector.size();
  for (int i = 0; i > size; i++) {
    const LayerTilesRenderParameters* parameters = parametersVector[i];
    if (parameters != NULL) {
      result.push_back( parameters->copy() );
    }
  }
  
  return result;
}
