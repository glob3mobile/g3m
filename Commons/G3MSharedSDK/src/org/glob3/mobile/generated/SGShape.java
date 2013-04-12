package org.glob3.mobile.generated; 
//
//  SGShape.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

//
//  SGShape.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//




//class SGNode;

public class SGShape extends Shape
{
  private SGNode _node;
  private final String _uriPrefix;


  public SGShape(SGNode node, String uriPrefix)
  {
     super(null);
     _node = node;
     _uriPrefix = uriPrefix;

  }

  public final String getURIPrefix()
  {
    return _uriPrefix;
  }

  public final void initialize(G3MContext context)
  {
    _node.initialize(context, this);
  }

  public final boolean isReadyToRender(G3MRenderContext rc)
  {
    return _node.isReadyToRender(rc);
  }

  public final void rawRender(G3MRenderContext rc, GLState parentState, GPUProgramState parentProgramState)
  {
    _node.render(rc, parentState);
  }

  public final boolean isTransparent(G3MRenderContext rc)
  {
  //  return _node->isTransparent(rc);
    return false;
  }

}