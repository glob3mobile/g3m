package org.glob3.mobile.generated; 
import java.util.*;

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



///#include "GPUProgramState.hpp"


//class Mark;
//class Camera;
//class MarkTouchListener;
//class IFloatBuffer;

public class MarksRenderer extends LeafRenderer
{
  private final boolean _readyWhenMarksReady;
  private java.util.ArrayList<Mark> _marks = new java.util.ArrayList<Mark>();

  private G3MContext _context;
  private Camera     _lastCamera;

  private MarkTouchListener _markTouchListener;
  private boolean _autoDeleteMarkTouchListener;

  private long _downloadPriority;

  private IFloatBuffer _billboardTexCoord;

  private GLState _glState = new GLState();

  private void updateGLState(G3MRenderContext rc)
  {
    final Camera cam = rc.getCurrentCamera();
    if (_projection == null)
    {
      _projection = new ProjectionGLFeature(cam);
      _glState.addGLFeature(_projection, true);
    }
    else
    {
      _projection.setMatrix(cam.getProjectionMatrix44D());
    }
  
    if (_model == null)
    {
      _model = new ModelGLFeature(cam);
      _glState.addGLFeature(_model, true);
    }
    else
    {
      _model.setMatrix(cam.getModelMatrix44D());
    }
  
    _glState.clearGLFeatureGroup(GLFeatureGroupName.NO_GROUP);
    _glState.addGLFeature(new ViewportExtentGLFeature(cam.getWidth(), cam.getHeight()), false);
  }

  private ProjectionGLFeature _projection;
  private ModelGLFeature _model;


  public MarksRenderer(boolean readyWhenMarksReady)
  {
     _readyWhenMarksReady = readyWhenMarksReady;
     _context = null;
     _lastCamera = null;
     _markTouchListener = null;
     _autoDeleteMarkTouchListener = false;
     _downloadPriority = DownloadPriority.MEDIUM;
     _model = null;
     _projection = null;
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
  
    if (_billboardTexCoord != null)
    {
      if (_billboardTexCoord != null)
         _billboardTexCoord.dispose();
    }
  }

  public void initialize(G3MContext context)
  {
    _context = context;
  
    int marksSize = _marks.size();
    for (int i = 0; i < marksSize; i++)
    {
      Mark mark = _marks.get(i);
      mark.initialize(context, _downloadPriority);
    }
  }

  public void render(G3MRenderContext rc)
  {
    // Saving camera for use in onTouchEvent
    _lastCamera = rc.getCurrentCamera();
  
    final Camera camera = rc.getCurrentCamera();
    final Vector3D cameraPosition = camera.getCartesianPosition();
  
    updateGLState(rc);
  
    final Planet planet = rc.getPlanet();
    GL gl = rc.getGL();
  
    final int marksSize = _marks.size();
    for (int i = 0; i < marksSize; i++)
    {
      Mark mark = _marks.get(i);
      if (mark.isReady())
      {
        mark.render(rc, cameraPosition, _glState, planet, gl);
      }
    }
  }

  public final void addMark(Mark mark)
  {
    _marks.add(mark);
    if (_context != null)
    {
      mark.initialize(_context, _downloadPriority);
    }
  }

  public final void removeMark(Mark mark)
  {
    int pos = -1;
    final int marksSize = _marks.size();
    for (int i = 0; i < marksSize; i++)
    {
      if (_marks.get(i) == mark)
      {
        pos = i;
        break;
      }
    }
    if (pos != -1)
    {
      _marks.remove(pos);
    }
  }

  public final void removeAllMarks()
  {
    final int marksSize = _marks.size();
    for (int i = 0; i < marksSize; i++)
    {
      if (_marks.get(i) != null)
         _marks.get(i).dispose();
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
        final Vector2I touchedPixel = touchEvent.getTouch(0).getPos();
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
  
          final Vector3D cartesianMarkPosition = mark.getCartesianPosition(planet);
          final Vector2F markPixel = _lastCamera.point2Pixel(cartesianMarkPosition);
  
          final RectangleF markPixelBounds = new RectangleF(markPixel._x - (textureWidth / 2), markPixel._y - (textureHeight / 2), textureWidth, textureHeight);
  
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
          handled = nearestMark.touched();
          if (!handled)
          {
            if (_markTouchListener != null)
            {
              handled = _markTouchListener.touchedMark(nearestMark);
            }
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

  public final void start(G3MRenderContext rc)
  {
  }

  public final void stop(G3MRenderContext rc)
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

  public final void onTouchEventRecived(G3MEventContext ec, TouchEvent touchEvent)
  {
  
    if (touchEvent.getType() == TouchEventType.DownUp)
    {
  
      if (_lastCamera != null)
      {
        final Vector2I touchedPixel = touchEvent.getTouch(0).getPos();
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
  
          final Vector3D cartesianMarkPosition = mark.getCartesianPosition(planet);
          final Vector2F markPixelF = _lastCamera.point2Pixel(cartesianMarkPosition);
          final Vector2I markPixel = new Vector2I((int)markPixelF._x, (int)markPixelF._y);
  
          final RectangleF markPixelBounds = new RectangleF(markPixel._x - (textureWidth / 2), markPixel._y - (textureHeight / 2), textureWidth, textureHeight);
  
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
          if (!nearestMark.touched())
          {
            if (_markTouchListener != null)
            {
              _markTouchListener.touchedMark(nearestMark);
            }
          }
        }
      }
    }
  }
}