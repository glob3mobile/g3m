package org.glob3.mobile.generated;import java.util.*;

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

  private GLState _glState;

  public SGTextureNode(String id, String sId)
  {
	  super(id, sId);
	  _glState = null;

  }

  public void dispose()
  {
	final int layersCount = _layers.size();
	for (int i = 0; i < layersCount; i++)
	{
	  SGLayerNode layer = _layers.get(i);
	  if (layer != null)
		  layer.dispose();
	}
  
	if (_glState != null)
	{
	  _glState._release();
	}
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

  public final void addLayer(SGLayerNode layer)
  {
	_layers.add(layer);
  
	if (_context != null)
	{
	  layer.initialize(_context, _shape);
	}
  
	if (_glState != null)
	{
	  _glState._release();
	}
	_glState = null;
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

  public final GLState createState(G3MRenderContext rc, GLState parentState)
  {
	if (_glState == null)
	{
	  _glState = new GLState();
  
	  final int layersCount = _layers.size();
	  for (int i = 0; i < layersCount; i++)
	  {
		SGLayerNode layer = _layers.get(i);
		if (!layer.modifyGLState(rc, _glState))
		{
		  _glState._release();
		  _glState = null;
		  return parentState;
		}
	  }
  
	}
	if (_glState != null)
	{
	  _glState.setParent(parentState);
	}
	return _glState;
  }

  public final String description()
  {
	return "SGTextureNode";
  }

}
