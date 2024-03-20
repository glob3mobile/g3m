package org.glob3.mobile.generated;
//
//  MarksRenderer.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 05/06/12.
//

//
//  MarksRenderer.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 05/06/12.
//



//class Mark;
//class Camera;
//class MarkTouchListener;
//class IFloatBuffer;
//class ITimer;
//class MarkFilter;


public class MarksRenderer extends DefaultRenderer
{
  private final boolean _readyWhenMarksReady;
  private java.util.ArrayList<Mark> _marks = new java.util.ArrayList<Mark>();

  private Camera     _lastCamera;

  private MarkTouchListener _markTouchListener;
  private boolean _autoDeleteMarkTouchListener;

  private long _downloadPriority;

  private GLState _glState;

  private void updateGLState(G3MRenderContext rc)
  {
    final Camera camera = rc.getCurrentCamera();
  
    ModelViewGLFeature f = (ModelViewGLFeature) _glState.getGLFeature(GLFeatureID.GLF_MODEL_VIEW);
    if (f == null)
    {
      _glState.addGLFeature(new ModelViewGLFeature(camera), true);
    }
    else
    {
      f.setMatrix(camera.getModelViewMatrix44D());
    }
  
    if (_glState.getGLFeature(GLFeatureID.GLF_VIEWPORT_EXTENT) == null)
    {
      _glState.clearGLFeatureGroup(GLFeatureGroupName.NO_GROUP);
      _glState.addGLFeature(new ViewportExtentGLFeature(camera, rc.getViewMode()), false);
    }
  }
  private IFloatBuffer _billboardTexCoords;
  private IFloatBuffer getBillboardTexCoords()
  {
    if (_billboardTexCoords == null)
    {
      FloatBufferBuilderFromCartesian2D texCoor = new FloatBufferBuilderFromCartesian2D();
      texCoor.add(1, 1);
      texCoor.add(1, 0);
      texCoor.add(0, 1);
      texCoor.add(0, 0);
      _billboardTexCoords = texCoor.create();
    }
    return _billboardTexCoords;
  }

  private boolean _renderInReverse;
  private boolean _progressiveInitialization;
  private ITimer _initializationTimer;


  public MarksRenderer(boolean readyWhenMarksReady, boolean renderInReverse)
  {
     this(readyWhenMarksReady, renderInReverse, true);
  }
  public MarksRenderer(boolean readyWhenMarksReady)
  {
     this(readyWhenMarksReady, false, true);
  }
  public MarksRenderer(boolean readyWhenMarksReady, boolean renderInReverse, boolean progressiveInitialization)
  {
     _readyWhenMarksReady = readyWhenMarksReady;
     _renderInReverse = renderInReverse;
     _progressiveInitialization = progressiveInitialization;
     _lastCamera = null;
     _markTouchListener = null;
     _autoDeleteMarkTouchListener = false;
     _downloadPriority = DownloadPriority.MEDIUM;
     _glState = new GLState();
     _billboardTexCoords = null;
     _initializationTimer = null;
    _context = null;
  }

  public final void setRenderInReverse(boolean renderInReverse)
  {
    _renderInReverse = renderInReverse;
  }

  public final boolean getRenderInReverse()
  {
    return _renderInReverse;
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
    if (_initializationTimer != null)
       _initializationTimer.dispose();
  
    final int marksSize = _marks.size();
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
  
    _glState._release();
  
    if (_billboardTexCoords != null)
       _billboardTexCoords.dispose();
  
    super.dispose();
  }

  public void onChangedContext()
  {
    final int marksSize = _marks.size();
    for (int i = 0; i < marksSize; i++)
    {
      Mark mark = _marks.get(i);
      mark.initialize(_context, _downloadPriority);
    }
  }

  public void render(G3MRenderContext rc, GLState glState)
  {
    final int marksSize = _marks.size();
  
    if (marksSize > 0)
    {
      final Camera camera = rc.getCurrentCamera();
  
      _lastCamera = camera; // Saving camera for use in onTouchEvent
  
      MutableVector3D cameraPosition = new MutableVector3D();
      camera.getCartesianPositionMutable(cameraPosition);
      final double cameraHeight = camera.getGeodeticHeight();
  
      updateGLState(rc);
  
      final Planet planet = rc.getPlanet();
      GL gl = rc.getGL();
  
      IFloatBuffer billboardTexCoord = getBillboardTexCoords();
  
      if (_progressiveInitialization)
      {
        if (_initializationTimer == null)
        {
          _initializationTimer = rc.getFactory().createTimer();
        }
        else
        {
          _initializationTimer.start();
        }
  
        for (int i = 0; i < marksSize; i++)
        {
          if (_initializationTimer.elapsedTimeInMilliseconds() > 5)
          {
            break;
          }
  
          final int ii = _renderInReverse ? i : (marksSize-1-i);
          Mark mark = _marks.get(ii);
          if (!mark.isInitialized())
          {
            mark.initialize(_context, _downloadPriority);
          }
        }
      }
  
      for (int i = 0; i < marksSize; i++)
      {
        final int ii = _renderInReverse ? (marksSize-1-i) : i;
        Mark mark = _marks.get(ii);
        if (mark.isReady())
        {
          mark.render(rc, this, cameraPosition, cameraHeight, _glState, planet, gl, billboardTexCoord);
        }
      }
    }
  }

  public final boolean hasMarks()
  {
    return !_marks.isEmpty();
  }

  public final void addMark(Mark mark)
  {
    _marks.add(mark);
    if ((_context != null) && !_progressiveInitialization)
    {
      mark.initialize(_context, _downloadPriority);
    }
  }

  public final void removeMark(Mark mark)
  {
    final int marksSize = _marks.size();
    for (int i = 0; i < marksSize; i++)
    {
      if (_marks.get(i) == mark)
      {
        _marks.remove(i);
        break;
      }
    }
  }

  public final void removeAllMarks()
  {
     removeAllMarks(true);
  }
  public final void removeAllMarks(boolean deleteMarks)
  {
    if (deleteMarks)
    {
      final int marksSize = _marks.size();
      for (int i = 0; i < marksSize; i++)
      {
        if (_marks.get(i) != null)
           _marks.get(i).dispose();
      }
    }
    _marks.clear();
  }

  public final boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent)
  {
  
    boolean handled = false;
    if (touchEvent.getType() == TouchEventType.DownUp)
    {
      if (_lastCamera != null)
      {
        final Vector2F touchedPixel = touchEvent.getTouch(0).getPos();
  
        final Planet planet = ec.getPlanet();
  
        double minSqDistance = IMathUtils.instance().maxDouble();
        Mark nearestMark = null;
  
        final int marksSize = _marks.size();
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
  
          final float markWidth = mark.getTextureWidth();
          if (markWidth <= 0)
          {
            continue;
          }
  
          final float markHeight = mark.getTextureHeight();
          if (markHeight <= 0)
          {
            continue;
          }
  
          final Vector3D cartesianMarkPosition = mark.getCartesianPosition(planet);
          final Vector2F markPixel = _lastCamera.point2Pixel(cartesianMarkPosition);
  
          final RectangleF markPixelBounds = new RectangleF(markPixel._x - (markWidth * mark.getMarkAnchorU()), markPixel._y - (markHeight * mark.getMarkAnchorV()), markWidth, markHeight);
  
          if (markPixelBounds.contains(touchedPixel._x, touchedPixel._y))
          {
            final double sqDistance = markPixel.squaredDistanceTo(touchedPixel);
            if (sqDistance < minSqDistance)
            {
              nearestMark = mark;
              minSqDistance = sqDistance;
            }
          }
        }
  
        if (nearestMark != null)
        {
          handled = nearestMark.touched(touchEvent);
          if (!handled)
          {
            if (_markTouchListener != null)
            {
              handled = _markTouchListener.touchedMark(nearestMark, touchEvent);
            }
          }
        }
      }
    }
  
    return handled;
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {
    _glState.clearGLFeatureGroup(GLFeatureGroupName.NO_GROUP);
  
    int logicWidth = width;
    if (ec.getViewMode() == ViewMode.STEREO)
    {
      logicWidth /= 2;
    }
  
    _glState.addGLFeature(new ViewportExtentGLFeature(logicWidth, height), false);
  }

  public final RenderState getRenderState(G3MRenderContext rc)
  {
    if (_readyWhenMarksReady)
    {
      final int marksSize = _marks.size();
      for (int i = 0; i < marksSize; i++)
      {
        if (!_marks.get(i).isReady())
        {
          return RenderState.busy();
        }
      }
    }
  
    return RenderState.ready();
  }

  //TODO: WHY? VTP
  public final void onResume(G3MContext context)
  {
    _context = context;
  }

  /**
   Change the download-priority used by Marks (for downloading textures).

   Default value is 1000000
   */
  public final void setDownloadPriority(long downloadPriority)
  {
    _downloadPriority = downloadPriority;
  }

  public final long getDownloadPriority()
  {
    return _downloadPriority;
  }

  public final boolean isVisible(G3MRenderContext rc)
  {
    return true;
  }

  public final void modifiyGLState(GLState state)
  {

  }

  public final int removeAllMarks(MarkFilter filter, boolean animated, boolean deleteMarks)
  {
    int removed = 0;
    final int marksSize = _marks.size();
  
    if (animated)
    {
      for (int i = 0; i < marksSize; i++)
      {
        Mark mark = _marks.get(i);
        if (filter.test(mark))
        {
          mark.animatedRemove(deleteMarks);
        }
      }
    }
    else
    {
      java.util.ArrayList<Mark> survivingMarks = new java.util.ArrayList<Mark>();
      for (int i = 0; i < marksSize; i++)
      {
        Mark mark = _marks.get(i);
        if (filter.test(mark))
        {
          if (deleteMarks)
          {
            if (mark != null)
               mark.dispose();
          }
          removed++;
        }
        else
        {
          survivingMarks.add(mark);
        }
      }
  
      if (removed > 0)
      {
        _marks = survivingMarks;
      }
    }
    return removed;
  }

  public final java.util.ArrayList<Mark> getAllMarks(MarkFilter filter)
  {
    java.util.ArrayList<Mark> result = new java.util.ArrayList<Mark>();
  
    final int marksSize = _marks.size();
    for (int i = 0; i < marksSize; i++)
    {
      Mark mark = _marks.get(i);
      if (filter.test(mark))
      {
        result.add(mark);
      }
    }
  
    return result;
  }

}