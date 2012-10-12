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



public class GLErrorRenderer extends Renderer
{

  public final void initialize(InitializationContext ic)
  {
  
  }

  public final void render(RenderContext rc)
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

  public final boolean onTouchEvent(EventContext ec, TouchEvent touchEvent)
  {
	return false;
  }

  public void dispose()
  {
  }

  public final void onResizeViewportEvent(EventContext ec, int width, int height)
  {
  }

  public final boolean isReadyToRender(RenderContext rc)
  {
	return true;
  }

  public final void start()
  {

  }

  public final void stop()
  {

  }

  public final void onResume(InitializationContext ic)
  {

  }

  public final void onPause(InitializationContext ic)
  {

  }

}