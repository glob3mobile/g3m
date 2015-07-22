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
             std::vector<const Info*>* layerInfo) :
_transparency(transparency),
_condition(condition),
_layerInfo(layerInfo),
_layerSet(NULL),
_enable(true),
_title("")
{
}

void Layer::setTransparency(float transparency) {
  if (_transparency != transparency) {
    _transparency = transparency;
    notifyChanges();
  }
}


Layer::~Layer() {
  delete _condition;
  
  const size_t numInfos = _layerInfo->size();
  for (size_t i = 0; i < numInfos; i++) {
    const Info* inf = _layerInfo->at(i);
    delete inf;
  }
  _layerInfo->clear();
#ifdef C_CODE
  delete _layerInfo;
#endif
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
    _layerSet->changedInfo(*_layerInfo);
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

  const int infoSize = _layerInfo->size();
  const int thatInfoSize = that->_layerInfo->size();
  if (infoSize != thatInfoSize) {
    return false;
  }

  for (int i = 0; i < infoSize; i++) {
    if (_layerInfo[i] != that->_layerInfo[i]) {
      return false;
    }
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

void Layer::setInfo(const std::vector<const Info*>& info) const {
  const size_t numInfos = _layerInfo->size();
  for (size_t i = 0; i < numInfos; i++) {
    const Info* inf = _layerInfo->at(i);
    delete inf;
  }
  _layerInfo->clear();
#ifdef C_CODE
  _layerInfo->insert(_layerInfo->end(),
               info.begin(),
               info.end());
#endif
#ifdef JAVA_CODE
  _layerInfo.addAll(info);
#endif

}

void Layer::addInfo(const std::vector<const Info*>& info){
#ifdef C_CODE
  _layerInfo->insert(_layerInfo->end(),
               info.begin(),
               info.end());
#endif
#ifdef JAVA_CODE
  _layerInfo.addAll(info);
#endif
}

void Layer::addInfo(const Info* info){
#ifdef C_CODE
  _layerInfo->insert(_layerInfo->end(), info);
#endif
#ifdef JAVA_CODE
  _layerInfo.add(info);
#endif
}


const std::vector<const Info*>& Layer::getInfo() const {
  return *_layerInfo;
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
