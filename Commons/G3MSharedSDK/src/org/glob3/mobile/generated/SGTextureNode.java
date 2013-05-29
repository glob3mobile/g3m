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


  //void SGTextureNode::rawRender(const G3MRenderContext* rc,
  //                              const GLGlobalState& parentState) {
  //  const int layersCount = _layers.size();
  //  for (int i = 0; i < layersCount; i++) {
  //    SGLayerNode* layer = _layers[i];
  //
  //    const GLGlobalState* layerState = layer->createState(rc, parentState);
  //    const GLGlobalState* state;
  //    if (layerState == NULL) {
  //      state = &parentState;
  //    }
  //    else {
  //      state = layerState;
  //    }
  //
  //    layer->rawRender(rc, *state);
  //
  //    delete layerState;
  //  }
  //}
  
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

//  void rawRender(const G3MRenderContext* rc,
//                 const GLGlobalState& parentState);

  public final GLGlobalState createState(G3MRenderContext rc, GLGlobalState parentState)
  {
    return null;
  }

//  void render(const G3MRenderContext* rc);


  /*
  void SGTextureNode::render(const G3MRenderContext* rc) {
  //  GLGlobalState* myState = createState(rc, parentState);
  //  GLGlobalState* state2;
  //  if (myState == NULL) {
  //    state2 = new GLGlobalState(parentState);
  //  }
  //  else {
  //    state2 = myState;
  //  }
  
    //  rawRender(rc, *state);
  
    const int layersCount = _layers.size();
    for (int i = 0; i < layersCount; i++) {
      SGLayerNode* layer = _layers[i];
  
      const GLGlobalState* layerState = layer->createState(rc, *state2);
      const GLGlobalState* state;
      if (layerState == NULL) {
        state = state2;
      }
      else {
        state = layerState;
      }
  
      //layer->rawRender(rc, *state);
  
      const int childrenCount = _children.size();
      for (int j = 0; j < childrenCount; j++) {
        SGNode* child = _children[j];
        child->render(rc);
      }
  
      delete layerState;
    }
    
    delete myState;
  }
  */
  
  public final void modifyGLGlobalState(GLGlobalState GLGlobalState)
  {
    final int layersCount = _layers.size();
    for (int i = 0; i < layersCount; i++)
    {
      SGLayerNode layer = _layers.get(i);
      layer.modifyGLGlobalState(GLGlobalState);
    }
  }
  public final void modifyGPUProgramState(GPUProgramState progState)
  {
    final int layersCount = _layers.size();
    for (int i = 0; i < layersCount; i++)
    {
      SGLayerNode layer = _layers.get(i);
      layer.modifyGPUProgramState(progState);
    }
  }


}