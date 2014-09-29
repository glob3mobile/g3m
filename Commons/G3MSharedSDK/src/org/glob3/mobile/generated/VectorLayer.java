package org.glob3.mobile.generated; 
//
//  VectorLayer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/30/14.
//
//

//
//  VectorLayer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/30/14.
//
//



public abstract class VectorLayer extends Layer
{

  protected final java.util.ArrayList<LayerTilesRenderParameters> _parametersVector = new java.util.ArrayList<LayerTilesRenderParameters>();
  protected int _selectedLayerTilesRenderParametersIndex;

  protected VectorLayer(final java.util.ArrayList<LayerTilesRenderParameters> parametersVector,
                        final float transparency,
                        final LayerCondition condition,
                        final java.util.ArrayList<Info> layerInfo) {
    super(transparency, condition, layerInfo);
    _parametersVector.addAll(parametersVector);
    _selectedLayerTilesRenderParametersIndex = 0;
  }

  public void dispose()
  {
    final int parametersVectorSize = _parametersVector.size();
    for (int i = 0; i < parametersVectorSize; i++)
    {
      final LayerTilesRenderParameters parameters = _parametersVector.get(i);
      if (parameters != null)
         parameters.dispose();
    }
  
    super.dispose();
  }

  public final java.util.ArrayList<LayerTilesRenderParameters> getLayerTilesRenderParametersVector()
  {
    return _parametersVector;
  }

  public final void selectLayerTilesRenderParameters(int index)
  {
    _selectedLayerTilesRenderParametersIndex = index;
  }

}