//
//  VectorLayer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/30/14.
//
//

#ifndef __G3MiOSSDK__VectorLayer__
#define __G3MiOSSDK__VectorLayer__

#include "Layer.hpp"

class VectorLayer : public Layer {
protected:
  VectorLayer(const LayerTilesRenderParameters* parameters,
              const float                       transparency,
              const LayerCondition*             condition) :
  Layer(parameters, transparency, condition)
  {
  }

};

#endif
