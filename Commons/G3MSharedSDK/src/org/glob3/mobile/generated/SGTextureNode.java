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


  protected final void rawRender(G3MRenderContext rc)
  {
	final int layersCount = _layers.size();
	for (int i = 0; i < layersCount; i++)
	{
	  SGLayerNode layer = _layers.get(i);
	  layer.rawRender(rc);
	}
  }

  protected final void prepareRender(G3MRenderContext rc)
  {
	final int layersCount = _layers.size();
	for (int i = 0; i < layersCount; i++)
	{
	  SGLayerNode layer = _layers.get(i);
	  layer.prepareRender(rc);
	}
  }

  protected final void cleanUpRender(G3MRenderContext rc)
  {
	final int layersCount = _layers.size();
	for (int i = 0; i < layersCount; i++)
	{
	  SGLayerNode layer = _layers.get(i);
	  layer.cleanUpRender(rc);
	}
  }

  public SGTextureNode(String id, String sId)
  {
	  super(id, sId);

  }

  public final void addLayer(SGLayerNode layer)
  {
	_layers.add(layer);
  
	if (_context != null)
	{
	  layer.initialize(_context, _shape);
	}
  }

  public final boolean isReadyToRender(G3MRenderContext rc)
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

  public final void initialize(G3MContext context, SGShape shape)
  {
	super.initialize(context, shape);
  
	final int layersCount = _layers.size();
	for (int i = 0; i < layersCount; i++)
	{
	  SGLayerNode child = _layers.get(i);
	  child.initialize(context, shape);
	}
  }

}