package org.glob3.mobile.generated;import java.util.*;

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
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final java.util.ArrayList<const LayerTilesRenderParameters> _parametersVector = new java.util.ArrayList<const LayerTilesRenderParameters>();
//#else
  private final java.util.ArrayList<LayerTilesRenderParameters> _parametersVector = new java.util.ArrayList<LayerTilesRenderParameters>();
//#endif

  protected int _selectedLayerTilesRenderParametersIndex;

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  protected ProceduralLayer(java.util.ArrayList<const LayerTilesRenderParameters> parametersVector, float transparency, LayerCondition condition, java.util.ArrayList<Info> layerInfo)
  {
	  super(transparency, condition, layerInfo);
	  _parametersVector = parametersVector;
	  _selectedLayerTilesRenderParametersIndex = 0;
  }
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  protected ProceduralLayer(final java.util.ArrayList<LayerTilesRenderParameters> parametersVector, final float transparency, final LayerCondition condition, final java.util.ArrayList<Info> layerInfo)
  {
	super(transparency, condition, layerInfo);
	_parametersVector.addAll(parametersVector);
	_selectedLayerTilesRenderParametersIndex = 0;
  }
//#endif

  public void dispose()
  {
	for (int i = 0; i < _parametersVector.size(); i++)
	{
	  final LayerTilesRenderParameters parameters = _parametersVector.get(i);
	  if (parameters != null)
		  parameters.dispose();
	}
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const java.util.ArrayList<const LayerTilesRenderParameters*> getLayerTilesRenderParametersVector() const
  public final java.util.ArrayList<LayerTilesRenderParameters> getLayerTilesRenderParametersVector()
  {
	return _parametersVector;
  }

  public final void selectLayerTilesRenderParameters(int index)
  {
	_selectedLayerTilesRenderParametersIndex = index;
  }

}
