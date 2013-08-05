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



//class Mark;
//class Camera;
//class MarkTouchListener;

public class MarksRenderer extends LeafRenderer
{
  private final boolean _readyWhenMarksReady;
  private java.util.ArrayList<Mark> _marks = new java.util.ArrayList<Mark>();

  private G3MContext _context;
  private Camera     _lastCamera;

  private MarkTouchListener _markTouchListener;
  private boolean _autoDeleteMarkTouchListener;

  private long _downloadPriority;


  public MarksRenderer(boolean readyWhenMarksReady)
  {
     _readyWhenMarksReady = readyWhenMarksReady;
     _context = null;
     _lastCamera = null;
     _markTouchListener = null;
     _autoDeleteMarkTouchListener = false;
     _downloadPriority = DownloadPriority.MEDIUM;
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
      mark.initialize(context, _downloadPriority);
    }
  }

  public void render(G3MRenderContext rc, GLState parentState)
  {
    //  rc.getLogger()->logInfo("MarksRenderer::render()");
  
    // Saving camera for use in onTouchEvent
    _lastCamera = rc.getCurrentCamera();
  
    GL gl = rc.getGL();
  
    GLState state = new GLState(parentState);
    state.disableDepthTest();
    state.enableBlend();
    state.enableTextures();
    state.enableTexture2D();
    state.enableVerticesPosition();
    gl.setState(state);
  
    Vector2D textureTranslation = new Vector2D(0.0, 0.0);
    Vector2D textureScale = new Vector2D(1.0, 1.0);
    gl.transformTexCoords(textureScale, textureTranslation);
  
    gl.setBlendFuncSrcAlpha();
  
    final Camera camera = rc.getCurrentCamera();
    final Vector3D cameraPosition = camera.getCartesianPosition();
  
    gl.startBillBoardDrawing(camera.getWidth(), camera.getHeight());
  
    final int marksSize = _marks.size();
    for (int i = 0; i < marksSize; i++)
    {
      Mark mark = _marks.get(i);
      //rc->getLogger()->logInfo("Rendering Mark: \"%s\"", mark->getName().c_str());
  
      if (mark.isReady())
      {
        mark.render(rc, cameraPosition);
      }
    }
  
    gl.stopBillBoardDrawing();
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
  
  //  if ( (touchEvent->getType() == Down) && (touchEvent->getTouchCount() == 1) ) {
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

}