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

  public abstract void onResume(InitializationContext ic);

  public abstract void onPause(InitializationContext ic);

  public abstract void initialize(InitializationContext ic);

  public abstract boolean isReadyToRender(RenderContext rc);

  public abstract void render(RenderContext rc);

  public abstract boolean onTouchEvent(EventContext ec, TouchEvent touchEvent);

  public abstract void onResizeViewportEvent(EventContext ec, int width, int height);

  public abstract void start();

  public abstract void stop();

}