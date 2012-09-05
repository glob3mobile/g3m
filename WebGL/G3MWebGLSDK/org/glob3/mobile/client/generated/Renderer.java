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

  public abstract void initialize(InitializationContext ic);

  public abstract boolean isReadyToRender(RenderContext rc);

  public abstract void render(RenderContext rc);

  public abstract boolean onTouchEvent(EventContext ec, TouchEvent touchEvent);

  public abstract void onResizeViewportEvent(EventContext ec, int width, int height);

  public abstract void start();

  public abstract void stop();

  public void dispose()
  {
  }
}