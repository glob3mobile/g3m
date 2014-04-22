//
//  RasterLayer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/22/14.
//
//

#ifndef __G3MiOSSDK__RasterLayer__
#define __G3MiOSSDK__RasterLayer__

#include "Layer.hpp"

class RasterLayer : public Layer {
protected:
  RasterLayer(LayerCondition* condition,
              const TimeInterval& timeToCache,
              bool readExpired,
              const LayerTilesRenderParameters* parameters,
              float transparency) :
  Layer(condition,
        timeToCache,
        readExpired,
        parameters,
        transparency)
  {
  }

};

#endif
