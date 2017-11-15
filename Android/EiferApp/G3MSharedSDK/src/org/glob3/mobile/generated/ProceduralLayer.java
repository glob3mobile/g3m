package org.glob3.mobile.generated; 
//
//  ProceduralLayer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/15/14.
//
//

//
//  ProceduralLayer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/15/14.
//
//



public abstract class ProceduralLayer extends Layer
{
  private final java.util.ArrayList<LayerTilesRenderParameters> _parametersVector = new java.util.ArrayList<LayerTilesRenderParameters>();

  protected int _selectedLayerTilesRenderParametersIndex;

  protected ProceduralLayer(final java.util.ArrayList<LayerTilesRenderParameters> parametersVector,
                            final float transparency,
                            final LayerCondition condition,
                            final java.util.ArrayList<Info> layerInfo) {
    super(transparency, condition, layerInfo);
    _parametersVector.addAll(parametersVector);
    _selectedLayerTilesRenderParametersIndex = 0;
  }

  public void dispose()
  {
    for (int i = 0; i < _parametersVector.size(); i++)
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