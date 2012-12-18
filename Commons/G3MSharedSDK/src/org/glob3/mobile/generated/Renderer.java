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
//class G3MContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class G3MRenderContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class G3MEventContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GLState;

public abstract class Renderer
{
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean isEnable() const = 0;
  public abstract boolean isEnable();

  public abstract void setEnable(boolean enable);


  public abstract void initialize(G3MContext context);

  public abstract boolean isReadyToRender(G3MRenderContext rc);

  public abstract void render(G3MRenderContext rc, GLState parentState);

  /*
   Gives to Renderer the opportunity to process touch, events.
   
   The Renderer answer true if the event was processed.
   */
  public abstract boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent);

  public abstract void onResizeViewportEvent(G3MEventContext ec, int width, int height);

  public abstract void start();

  public abstract void stop();

  public void dispose()
  {
  }

  // Android activity lifecyle
  public abstract void onResume(G3MContext context);

  public abstract void onPause(G3MContext context);

  public abstract void onDestroy(G3MContext context);

}