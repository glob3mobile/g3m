package org.glob3.mobile.generated; 
//
//  MarksRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  MarksRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//



public class MarksRenderer extends Renderer
{
  private boolean _readyWhenMarksReady;
  private java.util.ArrayList<Mark> _marks = new java.util.ArrayList<Mark>();

  private InitializationContext _initializationContext;


  public MarksRenderer(boolean readyWhenMarksReady)
  {
	  _readyWhenMarksReady = readyWhenMarksReady;
	  _initializationContext = null;
  }

  public void dispose()
  {
	int marksSize = _marks.size();
	for (int i = 0; i < marksSize; i++)
	{
	  if (_marks.get(i) != null)
		  _marks.get(i).dispose();
	}
  }

  public void initialize(InitializationContext ic)
  {
	_initializationContext = ic;
  
	int marksSize = _marks.size();
	for (int i = 0; i < marksSize; i++)
	{
	  Mark mark = _marks.get(i);
	  mark.initialize(_initializationContext);
	}
  }

  public void render(RenderContext rc)
  {
	//  rc.getLogger()->logInfo("MarksRenderer::render()");
  
	GL gl = rc.getGL();
  
	gl.enableVerticesPosition();
	gl.enableTextures();
  
	gl.disableDepthTest();
	gl.enableBlend();
  
	final Vector3D radius = rc.getPlanet().getRadii();
	final double minDistanceToCamera = (radius._x + radius._y + radius._z) * 2;
  
	int marksSize = _marks.size();
	for (int i = 0; i < marksSize; i++)
	{
	  Mark mark = _marks.get(i);
	  //rc->getLogger()->logInfo("Rendering Mark: \"%s\"", mark->getName().c_str());
  
	  if (mark.isReady())
	  {
		mark.render(rc, minDistanceToCamera);
	  }
	}
  
	gl.enableDepthTest();
	gl.disableBlend();
  
	gl.disableTextures();
	gl.disableVerticesPosition();
	gl.disableTexture2D();
  }

  public final void addMark(Mark mark)
  {
	_marks.add(mark);
	if (_initializationContext != null)
	{
	  mark.initialize(_initializationContext);
	}
  }

  public boolean onTouchEvent(EventContext ec, TouchEvent touchEvent)
  {
	return false;
  }

  public final void onResizeViewportEvent(EventContext ec, int width, int height)
  {

  }

  public final boolean isReadyToRender(RenderContext rc)
  {
	if (_readyWhenMarksReady)
	{
	  int marksSize = _marks.size();
	  for (int i = 0; i < marksSize; i++)
	  {
		if (!_marks.get(i).isReady())
		{
		  return false;
		}
	  }
	}
  
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