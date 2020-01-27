package org.glob3.mobile.generated;
//
//  GLErrorRenderer.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 20/06/12.
//

//
//  GLErrorRenderer.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 20/06/12.
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
