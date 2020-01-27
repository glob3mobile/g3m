//
//  VectorLayer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/30/14.
//
//

#include "VectorLayer.hpp"

#include "LayerTilesRenderParameters.hpp"

VectorLayer::~VectorLayer() {
  const size_t parametersVectorSize = _parametersVector.size();
  for (size_t i = 0; i < parametersVectorSize; i++) {
    const LayerTilesRenderParameters* parameters = _parametersVector[i];
    delete parameters;
  }

#ifdef JAVA_CODE
  super.dispose();
#endif
}
