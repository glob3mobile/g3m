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

Layer::~Layer() {
  delete _condition;
  delete _parameters;

#ifdef JAVA_CODE
  super.dispose();
#endif

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

const std::string Layer::getName() {
  return _name;
}

void Layer::setParameters(const LayerTilesRenderParameters* parameters) {
  if (parameters != _parameters) {
    delete _parameters;
    _parameters = parameters;
    notifyChanges();
  }
}
