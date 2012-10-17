package org.glob3.mobile.generated; 
//
//  LeafRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/16/12.
//
//

//
//  LeafRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/16/12.
//
//



public class LeafRenderer extends Renderer
{
  private boolean _enable;

  public LeafRenderer()
  {
	  _enable = true;

  }

  public LeafRenderer(boolean enable)
  {
	  _enable = enable;

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isEnable() const
  public final boolean isEnable()
  {
	return _enable;
  }

  public final void setEnable(boolean enable)
  {
	_enable = enable;
  }

}