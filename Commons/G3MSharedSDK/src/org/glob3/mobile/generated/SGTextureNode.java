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



//class SGLayerNode;

public class SGTextureNode extends SGNode
{
  private java.util.ArrayList<SGLayerNode> _layers = new java.util.ArrayList<SGLayerNode>();

  public SGTextureNode(String id, String sId)
  {
     super(id, sId);

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

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void prepareRender(G3MRenderContext rc);

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void cleanUpRender(G3MRenderContext rc);

  public final GLState createState(G3MRenderContext rc, GLState parentState)
  {
    return null;
  }

  public final void render(G3MRenderContext rc, GLState parentState, boolean renderNotReadyShapes)
  {
  //  const GLState* myState = createState(rc, parentState);
    GLState state2 = parentState;
  //  if (myState == NULL) {
  //    state2 = parentState;
  //  }
  //  else {
  //    state2 = myState;
  //  }
  
    prepareRender(rc);
  
    //  rawRender(rc, *state);
  
    final int layersCount = _layers.size();
    for (int i = 0; i < layersCount; i++)
    {
      SGLayerNode layer = _layers.get(i);
  
      GLState layerState = layer.createGLState(rc, state2);
      GLState state;
      if (layerState == null)
      {
        state = state2;
      }
      else
      {
        state = layerState;
      }
  
      layer.rawRender(rc, state);
  
      final int childrenCount = _children.size();
      for (int j = 0; j < childrenCount; j++)
      {
        SGNode child = _children.get(j);
        child.render(rc, state, renderNotReadyShapes);
      }
  
      if (layerState != null)
         layerState.dispose();
    }
  
    cleanUpRender(rc);
  
  //  delete myState;
  }

}