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



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Mark;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Camera;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class MarkTouchListener;

public class MarksRenderer extends LeafRenderer
{
  private final boolean _readyWhenMarksReady;
  private java.util.ArrayList<Mark> _marks = new java.util.ArrayList<Mark>();

  private G3MContext _context;
  private Camera     _lastCamera;

  private MarkTouchListener _markTouchListener;
  private boolean _autoDeleteMarkTouchListener;


  public MarksRenderer(boolean readyWhenMarksReady)
  {
	  _readyWhenMarksReady = readyWhenMarksReady;
	  _context = null;
	  _lastCamera = null;
	  _markTouchListener = null;
	  _autoDeleteMarkTouchListener = false;
  }

  public final void setMarkTouchListener(MarkTouchListener markTouchListener, boolean autoDelete)
  {
	  if (_autoDeleteMarkTouchListener)
	  {
		  if (_markTouchListener != null)
			  _markTouchListener.dispose();
	  }
  
	  _markTouchListener = markTouchListener;
	  _autoDeleteMarkTouchListener = autoDelete;
  }

  public void dispose()
  {
	int marksSize = _marks.size();
	for (int i = 0; i < marksSize; i++)
	{
	  if (_marks.get(i) != null)
		  _marks.get(i).dispose();
	}
  
	if (_autoDeleteMarkTouchListener)
	{
	  if (_markTouchListener != null)
		  _markTouchListener.dispose();
	}
	_markTouchListener = null;
  }

  public void initialize(G3MContext context)
  {
	_context = context;
  
	int marksSize = _marks.size();
	for (int i = 0; i < marksSize; i++)
	{
	  Mark mark = _marks.get(i);
	  mark.initialize(context);
	}
  }

  public void render(G3MRenderContext rc, GLState parentState)
  {
	//  rc.getLogger()->logInfo("MarksRenderer::render()");
  
	// Saving camera for use in onTouchEvent
	_lastCamera = rc.getCurrentCamera();
  
	GLState state = new GLState(parentState);
	state.disableDepthTest();
	state.enableBlend();
	state.enableTextures();
	state.enableTexture2D();
	state.enableVerticesPosition();
  
	GL gl = rc.getGL();
  
	gl.setState(state);
	gl.setBlendFuncSrcAlpha();
  
  //  const Vector3D radius = rc->getPlanet()->getRadii();
  //  const double minDistanceToCamera = (radius._x + radius._y + radius._z) / 3 * 0.75;
  
	int marksSize = _marks.size();
	for (int i = 0; i < marksSize; i++)
	{
	  Mark mark = _marks.get(i);
	  //rc->getLogger()->logInfo("Rendering Mark: \"%s\"", mark->getName().c_str());
  
	  if (mark.isReady())
	  {
  //      mark->render(rc, state, minDistanceToCamera);
		  mark.render(rc, state);
	  }
	}
  }

  public final void addMark(Mark mark)
  {
	_marks.add(mark);
	if (_context != null)
	{
	  mark.initialize(_context);
	}
  }

  public final void removeMark(Mark mark)
  {
	  int pos = -1;
	  for (int i = 0; i < _marks.size(); i++)
	  {
		  if (_marks.get(i) == mark)
		  {
			  pos = i;
		  }
		  break;
	  }
//C++ TO JAVA CONVERTER TODO TASK: There is no direct equivalent to the STL vector 'erase' method in Java:
	  _marks.erase(_marks.iterator()+pos);
  }

  public final void removeAllMarks()
  {
	  for (int i = 0; i < _marks.size(); i++)
	  {
		  if (_marks.get(i) != null)
			  _marks.get(i).dispose();
	  }
	  _marks.clear();
  }

  public final boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent)
  {
	if (_markTouchListener == null)
	{
	  return false;
	}
  
	boolean handled = false;
  
	if ((touchEvent.getType() == TouchEventType.Down) && (touchEvent.getTouchCount() == 1))
	{
  
	  if (_lastCamera != null)
	  {
		final Vector2I touchedPixel = touchEvent.getTouch(0).getPos();
		final Planet planet = ec.getPlanet();
  
		double minSqDistance = IMathUtils.instance().maxDouble();
		Mark nearestMark = null;
  
		int marksSize = _marks.size();
		for (int i = 0; i < marksSize; i++)
		{
		  Mark mark = _marks.get(i);
  
		  if (!mark.isReady())
		  {
			continue;
		  }
		  if (!mark.isRendered())
		  {
			continue;
		  }
  
		  final int textureWidth = mark.getTextureWidth();
		  if (textureWidth <= 0)
		  {
			continue;
		  }
  
		  final int textureHeight = mark.getTextureHeight();
		  if (textureHeight <= 0)
		  {
			continue;
		  }
  
		  final Vector3D cartesianMarkPosition = planet.toCartesian(mark.getPosition());
		  final Vector2I markPixel = _lastCamera.point2Pixel(cartesianMarkPosition);
  
		  final RectangleI markPixelBounds = new RectangleI(markPixel._x - (textureWidth / 2), markPixel._y - (textureHeight / 2), textureWidth, textureHeight);
  
		  if (markPixelBounds.contains(touchedPixel._x, touchedPixel._y))
		  {
			final double distance = markPixel.sub(touchedPixel).squaredLength();
			if (distance < minSqDistance)
			{
			  nearestMark = mark;
			  minSqDistance = distance;
			}
		  }
		}
  
		if (nearestMark != null)
		{
		  if (nearestMark.getListener() != null)
		  {
			  handled = nearestMark.getListener().touchedMark(nearestMark);
		  }
		  else
		  {
			  handled = _markTouchListener.touchedMark(nearestMark);
		  }
		}
  
	  }
  
	}
  
	return handled;
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {

  }

  public final boolean isReadyToRender(G3MRenderContext rc)
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

  public final void onResume(G3MContext context)
  {
	_context = context;
  }

  public final void onPause(G3MContext context)
  {

  }

  public final void onDestroy(G3MContext context)
  {

  }

}