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
private:
  const std::vector<const LayerTilesRenderParameters*> _parametersVector;

protected:
  int _selectedLayerTilesRenderParametersIndex;

  ProceduralLayer(const std::vector<const LayerTilesRenderParameters*> parametersVector,
                  const float                                          transparency,
                  const LayerCondition*                                condition,
                  const std::string&                                   disclaimerInfo) :
  Layer(transparency,
        condition,
        disclaimerInfo),
  _parametersVector(parametersVector),
  _selectedLayerTilesRenderParametersIndex(-1)
  {
  }

public:
  const std::vector<const LayerTilesRenderParameters*> getLayerTilesRenderParametersVector() const {
    return _parametersVector;
  }

  void selectLayerTilesRenderParameters(int index) {
    _selectedLayerTilesRenderParametersIndex = index;
  }

};

#endif
