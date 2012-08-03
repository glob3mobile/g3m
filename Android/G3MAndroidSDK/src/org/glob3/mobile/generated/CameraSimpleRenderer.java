package org.glob3.mobile.generated; 
//
//  CameraSimpleRenderer.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 04/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  CameraSimpleRenderer.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 04/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//




public class CameraSimpleRenderer extends CameraRenderer
{

  public final boolean onTouchEvent(TouchEvent touchEvent)
  {
	  return false;
  }
  public final int render(RenderContext rc)
  {
	_camera = rc.getCamera(); //Saving camera reference
  
	_camera.render(rc);
  
	  return Renderer.maxTimeToRender;
  }
  public final void initialize(InitializationContext ic)
  {
	_logger = ic.getLogger();
  }
  public final void onResizeViewportEvent(int width, int height)
  {
	if (_camera != null)
	{
	  _camera.resizeViewport(width, height);
	}
  }


  private ILogger _logger;
  private Camera _camera; // Camera used at current frame


}