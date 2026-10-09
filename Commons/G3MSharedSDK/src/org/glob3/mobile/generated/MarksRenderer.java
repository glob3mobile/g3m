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
//class Planet;
//class TimeInterval;
//class IImageFactory;
//class IImage;
//class MarksRenderer_HintListener;
//class MutableVector3D;
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

  private boolean _declutter;
  private boolean _horizonBand;
  private float _declutterMargin;
  private long _transitionMS;
  private MarkTransitionMode _transitionMode;

  // the default hint: one image, shared by every mark without a hint of its own
  private IImageFactory _hintImageFactory;
  private MarksRenderer_HintListener _hintListener;
  private IImage              _hintImage;
  private String _hintImageName;

  private void startHintImage()
  {
    if ((_context == null) || (_hintImageFactory == null))
    {
      return;
    }
    IImageFactory hintImageFactory = _hintImageFactory;
    _hintImageFactory = null; // ownership moved to the listener
    _hintListener = new MarksRenderer_HintListener(this, hintImageFactory);
    hintImageFactory.create(_context, _hintListener, true);
  }

  // every mark gets its own copy of the image; the same name shares the texture
  private void attachHint(Mark mark)
  {
    if ((_hintImage == null) || mark.hasHint())
    {
      return;
    }
    mark.addHint(new MarkOutfit(new StaticImageFactory(_hintImage.shallowCopy(), _hintImageName), new FixedMarkAnchor(0.5f, 0.5f)));
    if (mark.isInitialized())
    {
      mark.createPendingOutfitImages(_context);
    }
  }
  // reused between frames: the marks in placing order and the screen rectangles taken
  private java.util.ArrayList<Mark> _declutterCandidates = new java.util.ArrayList<Mark>();
  private java.util.ArrayList<Mark> _declutterOrder = new java.util.ArrayList<Mark>();
  private java.util.ArrayList<Float> _takenLeft = new java.util.ArrayList<Float>();
  private java.util.ArrayList<Float> _takenTop = new java.util.ArrayList<Float>();
  private java.util.ArrayList<Float> _takenRight = new java.util.ArrayList<Float>();
  private java.util.ArrayList<Float> _takenBottom = new java.util.ArrayList<Float>();

  private void declutter(Camera camera, Planet planet, MutableVector3D cameraPosition, double cameraHeight)
  {
    // the order the marks end up on top: the last drawn is the first one
    java.util.ArrayList<Mark> candidates = _declutterCandidates;
    candidates.clear();
    final int marksSize = _marks.size();
    for (int i = 0; i < marksSize; i++)
    {
      final int ii = _renderInReverse ? i : (marksSize-1-i);
      Mark mark = _marks.get(ii);
      if (mark.isReady() && mark.isVisibleFrom(planet, cameraPosition, cameraHeight))
      {
        candidates.add(mark);
      }
    }
  
    // explicit priorities first, the highest first; a stable insertion keeps the drawing order for the rest
    java.util.ArrayList<Mark> ordered = _declutterOrder;
    ordered.clear();
    for (int i = 0; i < candidates.size(); i++)
    {
      Mark mark = candidates.get(i);
      if (mark.hasPriority())
      {
        int position = 0;
        while ((position < ordered.size()) && (ordered.get(position).getPriority() >= mark.getPriority()))
        {
          position++;
        }
        ordered.add(position, mark);
      }
    }
    for (int i = 0; i < candidates.size(); i++)
    {
      Mark mark = candidates.get(i);
      if (!mark.hasPriority())
      {
        ordered.add(mark);
      }
    }
  
    _takenLeft.clear();
    _takenTop.clear();
    _takenRight.clear();
    _takenBottom.clear();
  
    for (int i = 0; i < ordered.size(); i++)
    {
      Mark mark = ordered.get(i);
      final Vector2F markPixel = camera.point2Pixel(mark.getCartesianPosition(planet));
  
      boolean placed = false;
      final int outfitsCount = mark.getOutfitsCount();
      for (int outfitIndex = 0; (outfitIndex < outfitsCount) && !placed; outfitIndex++)
      {
        final Vector2F size = mark.getOutfitScreenSize(outfitIndex);
        if ((size._x <= 0) || (size._y <= 0))
        {
          continue; // its image does not exist yet
        }
        final Vector2F anchor = mark.getOutfitAnchor(outfitIndex);
        final float left = markPixel._x - (size._x * anchor._x);
        final float top = markPixel._y - (size._y * anchor._y);
  
        final int target = mark.getDeclutterTarget();
        final boolean grows = (target < 0) || ((int) outfitIndex < target);
        final float margin = grows ? _declutterMargin : 0;
  
        if (isFree(left - margin, top - margin, left + size._x + margin, top + size._y + margin))
        {
          mark.setDeclutterTarget((int) outfitIndex);
          _takenLeft.add(left);
          _takenTop.add(top);
          _takenRight.add(left + size._x);
          _takenBottom.add(top + size._y);
          placed = true;
        }
      }
  
      if (!placed)
      {
        mark.setDeclutterTarget(-1);
      }
    }
  }

  private boolean isFree(float left, float top, float right, float bottom)
  {
    final int takenSize = _takenLeft.size();
    for (int i = 0; i < takenSize; i++)
    {
      if ((left < _takenRight.get(i)) && (right > _takenLeft.get(i)) && (top < _takenBottom.get(i)) && (bottom > _takenTop.get(i)))
      {
        return false;
      }
    }
    return true;
  }


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
     _declutter = false;
     _horizonBand = true;
     _declutterMargin = 2F;
     _transitionMS = 500;
     _transitionMode = MarkTransitionMode.SCALE_AND_ALPHA;
     _hintImageFactory = null;
     _hintListener = null;
     _hintImage = null;
     _hintImageName = "";
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

  /**
   * Each frame, every visible mark takes its largest outfit that does not
   * overlap the marks placed before it, or hides when none fits. Marks with
   * priority go first, the highest first; the rest in the order they are drawn
   * on top.
   */
  public final void setDeclutter(boolean declutter)
  {
    _declutter = declutter;
    if (!_declutter)
    {
      for (int i = 0; i < _marks.size(); i++)
      {
        _marks.get(i).resetDeclutter();
      }
    }
  }

  public final boolean getDeclutter()
  {
    return _declutter;
  }

  /** the free space a mark needs around it to grow or come back; it keeps its place with no margin, so it does not blink */
  public final void setDeclutterMargin(float marginInPixels)
  {
    _declutterMargin = marginInPixels;
  }

  /** the hint of the marks without their own: drawn centred on the position when nothing else fits; the renderer owns the factory; NULL: no hint for the marks added from now on */
  public final void setHint(IImageFactory hintImageFactory)
  {
    if (_hintListener != null)
    {
      _hintListener.forgetRenderer();
      _hintListener = null;
    }
    if (_hintImageFactory != null)
       _hintImageFactory.dispose();
    _hintImage = null;
    _hintImage = null;
    _hintImageName = "";
  
    _hintImageFactory = hintImageFactory;
    startHintImage();
  }

  public final void onHintImageCreated(IImage image, String imageName)
  {
    _hintListener = null;
    _hintImage = image;
    _hintImageName = imageName;
  
    for (int i = 0; i < _marks.size(); i++)
    {
      attachHint(_marks.get(i));
    }
  }


  // the factory deletes the listener right after reporting the error
  public final void onHintImageCreationError()
  {
    _hintListener = null;
  }

  /** the marks shrink while they sink behind the horizon, over their own apparent height, instead of vanishing at once; on by default */
  public final void setHorizonBand(boolean horizonBand)
  {
    _horizonBand = horizonBand;
  }

  public final boolean getHorizonBand()
  {
    return _horizonBand;
  }

  /** how long an outfit takes to come in, and the one it replaces to go away, both at once; 500ms by default */
  public final void setDeclutterTransitionDuration(TimeInterval duration)
  {
    _transitionMS = duration.milliseconds();
  }

  /** how outfits come in and go away: SCALE_AND_ALPHA by default */
  public final void setDeclutterTransitionMode(MarkTransitionMode mode)
  {
    _transitionMode = mode;
  }

  public final MarkTransitionMode getDeclutterTransitionMode()
  {
    return _transitionMode;
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
  
    if (_hintListener != null)
    {
      _hintListener.forgetRenderer();
    }
    if (_hintImageFactory != null)
       _hintImageFactory.dispose(); // only while it waits for a context
    _hintImage = null;
  
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
    startHintImage();
  
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
  
      if (_declutter)
      {
        declutter(camera, planet, cameraPosition, cameraHeight);
  
        final long nowMS = rc.getFrameStartTimer().nowInMilliseconds();
        for (int i = 0; i < marksSize; i++)
        {
          _marks.get(i).stepDeclutterTransition(nowMS, _transitionMS, _transitionMode);
        }
      }
  
      final double horizonBandRadiansPerPixel = _horizonBand ? (camera.getVerticalFOV()._radians / camera.getViewPortHeight()) : 0;
  
      for (int i = 0; i < marksSize; i++)
      {
        final int ii = _renderInReverse ? (marksSize-1-i) : i;
        Mark mark = _marks.get(ii);
        if (mark.isReady())
        {
          mark.render(rc, this, cameraPosition, cameraHeight, _glState, planet, gl, billboardTexCoord, horizonBandRadiansPerPixel);
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
    attachHint(mark);
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
          if (!mark.isRendered() || mark.isShowingHint())
          {
            continue;
          }
  
          final float markWidth = mark.getScreenWidth();
          if (markWidth <= 0)
          {
            continue;
          }
  
          final float markHeight = mark.getScreenHeight();
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
      java.util.ArrayList<Mark> survivingMarks = new java.util.ArrayList<Mark>();
      for (int i = 0; i < marksSize; i++)
      {
        Mark mark = _marks.get(i);
        if (filter.test(mark))
        {
          removed++;
          final boolean visible = isEnable() && mark.isRendered();
          if (visible || mark.isDisappearing())
          {
            mark.animatedRemove(deleteMarks);
            survivingMarks.add(mark); // the zoom-out effect removes it when done
          }
          else
          {
            // nobody sees it, and the zoom-out only starts on render, which may never come
            mark.cancelEffects();
            if (deleteMarks)
            {
              if (mark != null)
                 mark.dispose();
            }
          }
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