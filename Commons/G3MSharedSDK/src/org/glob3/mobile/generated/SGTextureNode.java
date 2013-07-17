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
  }

  public final void addLayer(SGLayerNode layer)
  {
    _layers.add(layer);
  
    if (_context != null)
    {
      layer.initialize(_context, _shape);
    }
  
    if (_glState != null)
       _glState.dispose();
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

//  void prepareRender(const G3MRenderContext* rc);
//
//  void cleanUpRender(const G3MRenderContext* rc);

  public final GLState createState(G3MRenderContext rc, GLState parentState)
  {
    if (_glState == null)
    {
      _glState = new GLState();
  
      final int layersCount = _layers.size();
      for (int i = 0; i < layersCount; i++)
      {
        SGLayerNode layer = _layers.get(i);
  //      layer->createGLState(rc, _glState);
        if (!layer.modifyGLState(rc, _glState))
        {
          if (_glState != null)
             _glState.dispose();
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

//  void render(const G3MRenderContext* rc,
//              GLState* parentState,
//              bool renderNotReadyShapes);

  public final String description()
  {
    return "SGTextureNode";
  }

}
//void SGTextureNode::render(const G3MRenderContext* rc,
//                           GLState* parentState,
//                           bool renderNotReadyShapes) {
//
//  const GLState* glState = createState(rc, *parentState);
//
//  const int childrenCount = _children.size();
//  for (int j = 0; j < childrenCount; j++) {
//    SGNode* child = _children[j];
//    child->render(rc, glState, renderNotReadyShapes);
//  }
//
///*
//
////  const GLState* myState = createState(rc, parentState);
//  GLState* state2 = parentState;
////  if (myState == NULL) {
////    state2 = parentState;
////  }
////  else {
////    state2 = myState;
////  }
//
////  prepareRender(rc);
//
//  //  rawRender(rc, *state);
//
//  const int layersCount = _layers.size();
//  for (int i = 0; i < layersCount; i++) {
//    SGLayerNode* layer = _layers[i];
//
//    GLState* layerState = layer->createGLState(rc, state2); //TODO: This is getGLState
//    GLState* state;
//    if (layerState == NULL) {
//      state = state2;
//    }
//    else {
//      state = layerState;
//    }
//
//    layer->rawRender(rc, state);
//
//    const int childrenCount = _children.size();
//    for (int j = 0; j < childrenCount; j++) {
//      SGNode* child = _children[j];
//      child->render(rc, state, renderNotReadyShapes);
//    }
//
////    delete layerState;
//  }
//  
////  cleanUpRender(rc);
//
////  delete myState;
// 
// */
//}
