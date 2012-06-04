package org.glob3.mobile.generated; 
//
//  CameraRenderer.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 04/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  CameraRenderer.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 04/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



public class CameraRenderer extends Renderer
{

  private void initialize(InitializationContext ic)
  {
  }

  private int render(RenderContext rc)
  {
	rc.getCamera().Draw(rc);
  }

//C++ TO JAVA CONVERTER TODO TASK: There are no simple equivalents to events in Java:
//  boolean onTouchEvent(const TouchEvent& event);


}
//C++ TO JAVA CONVERTER TODO TASK: There are no simple equivalents to events in Java:
//boolean CameraRenderer::onTouchEvent(const TouchEvent& event)
//{
//}