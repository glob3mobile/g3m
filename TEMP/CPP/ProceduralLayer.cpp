//
//  ProceduralLayer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/15/14.
//
//

#include "ProceduralLayer.hpp"

#include "LayerTilesRenderParameters.hpp"


ProceduralLayer::~ProceduralLayer() {
  for (size_t i = 0; i < _parametersVector.size(); i++) {
    const LayerTilesRenderParameters* parameters = _parametersVector[i];
    delete parameters;
  }
#ifdef JAVA_CODE
  super.dispose();
#endif
}
