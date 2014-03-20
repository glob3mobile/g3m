package org.glob3.mobile.generated; 
//
//  GLErrorRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 20/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  GLErrorRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 20/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//



public class GLErrorRenderer extends DefaultRenderer
{

  public final void render(G3MRenderContext rc, GLState glState)
  {
    GL gl = rc.getGL();
    final ILogger logger = rc.getLogger();
  
    int error = gl.getError();
    while (error != GLError.noError())
    {
      logger.logError("GL Error: %d", error);
      error = gl.getError();
    }
  
  }

  public void dispose()
  {
    super.dispose();
  }
  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {

  }



}