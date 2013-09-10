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

  private final boolean _isTransparent;


  public SGShape(SGNode node, String uriPrefix, boolean isTransparent)
  {
     super(null, AltitudeMode.ABSOLUTE);
     _node = node;
     _uriPrefix = uriPrefix;
     _isTransparent = isTransparent;

  }

  public final SGNode getNode()
  {
    return _node;
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

  public final void rawRender(G3MRenderContext rc, GLState parentState, boolean renderNotReadyShapes)
  {
    _node.render(rc, parentState, renderNotReadyShapes);
  }

  public final boolean isTransparent(G3MRenderContext rc)
  {
    return _isTransparent;
  }

}