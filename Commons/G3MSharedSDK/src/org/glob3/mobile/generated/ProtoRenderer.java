package org.glob3.mobile.generated; 
//
//  ProtoRenderer.cpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 23/04/14.
//
//

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

public interface ProtoRenderer
{
  void dispose();

  void initialize(G3MContext context);


  void render(G3MRenderContext rc, GLState glState);

  void onResizeViewportEvent(G3MEventContext ec, int width, int height);

  void start(G3MRenderContext rc);

  void stop(G3MRenderContext rc);

  // Android activity lifecyle
  void onResume(G3MContext context);

  void onPause(G3MContext context);

  void onDestroy(G3MContext context);

}