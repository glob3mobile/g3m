package org.glob3.mobile.generated; 
//
//  IRenderer.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TouchEvent;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class InitializationContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class RenderContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class EventContext;

public abstract class Renderer
{
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean isEnable() const = 0;
  public abstract boolean isEnable();

  public abstract void setEnable(boolean enable);

  public abstract void onResume(InitializationContext ic);

  public abstract void onPause(InitializationContext ic);

  public abstract void initialize(InitializationContext ic);

  public abstract boolean isReadyToRender(RenderContext rc);

  public abstract void render(RenderContext rc);

  /*
   Gives to Renderer the opportunity to process touch, events.
   
   The Renderer answer true if the event was processed.
   */
  public abstract boolean onTouchEvent(EventContext ec, TouchEvent touchEvent);

  public abstract void onResizeViewportEvent(EventContext ec, int width, int height);

  public abstract void start();

  public abstract void stop();

  public void dispose()
  {
  }
}