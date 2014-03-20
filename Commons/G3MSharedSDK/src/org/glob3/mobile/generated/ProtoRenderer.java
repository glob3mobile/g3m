package org.glob3.mobile.generated; 
//
//  ProtoRenderer.hpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 18/03/14.
//
//


//class G3MContext;
//class G3MRenderContext;
//class GLState;
//class G3MEventContext;

public abstract class ProtoRenderer
{
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void dispose();

  public abstract void initialize(G3MContext context);


  public abstract void render(G3MRenderContext rc, GLState glState);

  public abstract void onResizeViewportEvent(G3MEventContext ec, int width, int height);

  public abstract void start(G3MRenderContext rc);

  public abstract void stop(G3MRenderContext rc);

  // Android activity lifecyle
  public abstract void onResume(G3MContext context);

  public abstract void onPause(G3MContext context);

  public abstract void onDestroy(G3MContext context);

}