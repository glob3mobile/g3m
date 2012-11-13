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


  public SGShape(SGNode node)
  {
	  super(null);
	  _node = node;

  }

  public final void initialize(InitializationContext ic)
  {
	_node.initialize(ic);
  }

  public final boolean isReadyToRender(RenderContext rc)
  {
	return _node.isReadyToRender(rc);
  }

  public final void rawRender(RenderContext rc)
  {
	_node.render(rc);
  }

}