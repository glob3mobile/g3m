//
//  ProceduralLayer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/15/14.
//
//

#ifndef __G3MiOSSDK__ProceduralLayer__
#define __G3MiOSSDK__ProceduralLayer__

#include "Layer.hpp"

class ProceduralLayer : public Layer {
protected:
  ProceduralLayer(const LayerTilesRenderParameters* parameters,
                  const float                       transparency,
                  const LayerCondition*             condition,
                  const std::string&                disclaimerInfo) :
  Layer(parameters,
        transparency,
        condition,
        disclaimerInfo)
  {
  }

};

#endif
