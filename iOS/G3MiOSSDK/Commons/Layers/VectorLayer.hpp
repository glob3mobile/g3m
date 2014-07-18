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
#ifdef C_CODE
  const std::vector<const LayerTilesRenderParameters*> _parametersVector;
#endif
#ifdef JAVA_CODE
  private final java.util.ArrayList<LayerTilesRenderParameters> _parametersVector;
#endif
  int _selectedLayerTilesRenderParametersIndex;

  VectorLayer(const std::vector<const LayerTilesRenderParameters*>& parametersVector,
              const float                                           transparency,
              const LayerCondition*                                 condition,
              const std::string&                                    disclaimerInfo) :
  Layer(transparency, condition, disclaimerInfo),
  _parametersVector(parametersVector),
  _selectedLayerTilesRenderParametersIndex(-1)
  {
  }

  ~VectorLayer();

public:
  const std::vector<const LayerTilesRenderParameters*> getLayerTilesRenderParametersVector() const {
    return _parametersVector;
  }

  void selectLayerTilesRenderParameters(int index) {
    _selectedLayerTilesRenderParametersIndex = index;
  }

};

#endif
