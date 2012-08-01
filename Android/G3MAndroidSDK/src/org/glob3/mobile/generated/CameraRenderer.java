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




public class CameraRenderer extends CameraHandler
{

  public final boolean onTouchEvent(TouchEvent touchEvent)
  {
	  return false;
  }
  public final int render(RenderContext rc)
  {
	_camera = rc.getCamera(); //Saving camera reference
	_planet = rc.getPlanet();
	gl = rc.getGL();
  
	_camera.render(rc);
  
	return Renderer.maxTimeToRender;
  }

  /*
  #include "Renderer.hpp"
  #include "Camera.hpp"
  
  class ILogger;
  class Planet;
  class IGL;
  
  
  class CameraRenderer: public Renderer
  {
  private:
    
	const ILogger * _logger;
    
	const Planet* _planet;
	IGL *gl;
      
	Camera _camera0; //Initial Camera saved on Down event
	Camera* _camera; // Camera used at current frame
    
	MutableVector3D _initialPoint; //Initial point at dragging
	MutableVector3D _initialPixel; //Initial pixel at start of gesture
      
	double _initialFingerSeparation;
	double _initialFingerInclination;
      
    
    
  public:
    
	CameraHandler();
    
	void initialize(const InitializationContext* ic);  
    
	virtual int render(const RenderContext* rc) = 0;
    
	virtual bool onTouchEvent(const TouchEvent* touchEvent) = 0;
    
	void onResizeViewportEvent(int width, int height);
    
	bool isReadyToRender(const RenderContext* rc) {
	  return true;
	}
    
    
  };
  */
  
  
  
  
  
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

}