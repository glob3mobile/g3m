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
#else
  std::vector<const LayerTilesRenderParameters*> _parametersVector;
#endif
  int _selectedLayerTilesRenderParametersIndex;

#ifdef C_CODE
  VectorLayer(const std::vector<const LayerTilesRenderParameters*>& parametersVector,
              const float                                           transparency,
              const LayerCondition*                                 condition,
              std::vector<const Info*>*                             layerInfo) :
  Layer(transparency, condition, layerInfo),
  _parametersVector(parametersVector),
  _selectedLayerTilesRenderParametersIndex(0)
  {
  }
#endif
#ifdef JAVA_CODE
  protected VectorLayer(final java.util.ArrayList<LayerTilesRenderParameters> parametersVector,
                        final float transparency,
                        final LayerCondition condition,
                        final java.util.ArrayList<Info> layerInfo) {
    super(transparency, condition, layerInfo);
    _parametersVector.addAll(parametersVector);
    _selectedLayerTilesRenderParametersIndex = 0;
  }
#endif

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
