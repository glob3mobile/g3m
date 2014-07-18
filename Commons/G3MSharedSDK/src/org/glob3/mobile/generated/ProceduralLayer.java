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

  protected ProceduralLayer(java.util.ArrayList<LayerTilesRenderParameters> parametersVector, float transparency, LayerCondition condition, String disclaimerInfo)
  {
     super(transparency, condition, disclaimerInfo);
     _parametersVector = parametersVector;
     _selectedLayerTilesRenderParametersIndex = -1;
  }

  protected final java.util.ArrayList<LayerTilesRenderParameters> getLayerTilesRenderParametersVector()
  {
    return _parametersVector;
  }

  protected final void selectLayerTilesRenderParameters(int index)
  {
    _selectedLayerTilesRenderParametersIndex = index;
  }

}