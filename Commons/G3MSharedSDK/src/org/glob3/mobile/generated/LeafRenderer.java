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



public abstract class LeafRenderer extends Renderer
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

  public void dispose()
  {

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isEnable() const
  public final boolean isEnable()
  {
	return _enable;
  }

  public void setEnable(final boolean enable) {
	_enable = enable;
  }

  public abstract void onResume(G3MContext context);

  public abstract void onPause(G3MContext context);

  public abstract void initialize(G3MContext context);

  public abstract boolean isReadyToRender(G3MRenderContext rc);

  public abstract void render(G3MRenderContext rc, GLState parentState);

  public abstract boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent);

  public abstract void onResizeViewportEvent(G3MEventContext ec, int width, int height);

  public abstract void start();

  public abstract void stop();

}