package org.glob3.mobile.generated;import java.util.*;

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

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  protected final java.util.ArrayList<const LayerTilesRenderParameters> _parametersVector = new java.util.ArrayList<const LayerTilesRenderParameters>();
//#else
  protected final java.util.ArrayList<LayerTilesRenderParameters> _parametersVector = new java.util.ArrayList<LayerTilesRenderParameters>();
//#endif
  protected int _selectedLayerTilesRenderParametersIndex;

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  protected VectorLayer(java.util.ArrayList<const LayerTilesRenderParameters> parametersVector, float transparency, LayerCondition condition, java.util.ArrayList<Info> layerInfo)
  {
	  super(transparency, condition, layerInfo);
	  _parametersVector = parametersVector;
	  _selectedLayerTilesRenderParametersIndex = 0;
  }
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  protected VectorLayer(final java.util.ArrayList<LayerTilesRenderParameters> parametersVector, final float transparency, final LayerCondition condition, final java.util.ArrayList<Info> layerInfo)
  {
	super(transparency, condition, layerInfo);
	_parametersVector.addAll(parametersVector);
	_selectedLayerTilesRenderParametersIndex = 0;
  }
//#endif

  public void dispose()
  {
	final int parametersVectorSize = _parametersVector.size();
	for (int i = 0; i < parametersVectorSize; i++)
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
