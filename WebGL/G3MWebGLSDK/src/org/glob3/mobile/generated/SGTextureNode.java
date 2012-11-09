package org.glob3.mobile.generated; 
//
//  SGTextureNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

//
//  SGTextureNode.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class SGLayerNode;

public class SGTextureNode extends SGNode
{
  private java.util.ArrayList<SGLayerNode> _layers = new java.util.ArrayList<SGLayerNode>();


  protected final void rawRender(RenderContext rc)
  {
	final int layersCount = _layers.size();
	for (int i = 0; i < layersCount; i++)
	{
	  SGLayerNode layer = _layers.get(i);
	  layer.render(rc);
	}
  }

  public final void addLayer(SGLayerNode layer)
  {
	_layers.add(layer);
  
	if (_initializationContext != null)
	{
	  layer.initialize(_initializationContext);
	}
  }

  public final boolean isReadyToRender(RenderContext rc)
  {
	final int layersCount = _layers.size();
	for (int i = 0; i < layersCount; i++)
	{
	  SGLayerNode layer = _layers.get(i);
	  if (!layer.isReadyToRender(rc))
	  {
		return false;
	  }
	}
  
	return super.isReadyToRender(rc);
  }

}