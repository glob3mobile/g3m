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




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String getURIPrefix() const
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

  public final void rawRender(G3MRenderContext rc)
  {
	_node.render(rc);
  }

  public final boolean isTransparent(G3MRenderContext rc)
  {
	int ____DIEGO_AT_WORK;
  //  return _node->isTransparent(rc);
	return false;
  }

}