package org.glob3.mobile.generated; 
public class MarkWidget
{
  private GLState _glState;
  private Geometry2DGLFeature _geo2Dfeature;
  private ViewportExtentGLFeature _viewportExtent;
  private IImage _image;
  private String _imageName;
  private IImageBuilder _imageBuilder;
  private TexturesHandler _texHandler;

  private float _halfWidth;
  private float _halfHeight;

  private float _x; //Screen position
  private float _y;

  private MarkWidgetTouchListener _touchListener;

  private static class WidgetImageListener implements IImageBuilderListener
  {
    private MarkWidget _widget;
    public WidgetImageListener(MarkWidget widget)
    {
       this(widget, null);
    }
    public WidgetImageListener(MarkWidget widget, MarkWidgetTouchListener touchListener)
    {
       _widget = widget;
    }

    public void dispose()
    {
    }

    public void imageCreated(IImage image, String imageName)
    {
      _widget.prepareWidget(image, imageName);
    }

    public void onError(String error)
    {
      ILogger.instance().logError(error);
    }

  }

  private void prepareWidget(IImage image, String imageName)
  {
    _image = image;
    _imageName = imageName;
  
    _halfWidth = image.getWidth() / 2;
    _halfHeight = image.getHeight() / 2;
  
    FloatBufferBuilderFromCartesian2D pos2D = new FloatBufferBuilderFromCartesian2D();
    pos2D.add(-_halfWidth, -_halfHeight); //vertex 1
    pos2D.add(-_halfWidth, _halfHeight); //vertex 2
    pos2D.add(_halfWidth, -_halfHeight); //vertex 3
    pos2D.add(_halfWidth, _halfHeight); //vertex 4
  
    _geo2Dfeature = new Geometry2DGLFeature(pos2D.create(), 2, 0, true, 0, 1.0f, true, 10.0f, new Vector2F(_x, _y));
  
    _glState.addGLFeature(_geo2Dfeature, false);
  
    FloatBufferBuilderFromCartesian2D texCoords = new FloatBufferBuilderFromCartesian2D();
    texCoords.add(0.0f, 1.0f); //vertex 1
    texCoords.add(0.0f, 0.0f); //vertex 2
    texCoords.add(1.0f, 1.0f); //vertex 3
    texCoords.add(1.0f, 0.0f); //vertex 4
  
    final TextureIDReference textureID = _texHandler.getTextureIDReference(_image, GLFormat.rgba(), _imageName, false);
  
    SimpleTextureMapping textureMapping = new SimpleTextureMapping(textureID, texCoords.create(), true, true);
  
  
  
    textureMapping.modifyGLState(_glState);
  }

  public MarkWidget(IImageBuilder imageBuilder)
  {
     _image = null;
     _imageBuilder = imageBuilder;
     _viewportExtent = null;
     _geo2Dfeature = null;
     _glState = null;
     _x = java.lang.Float.NaN;
     _y = java.lang.Float.NaN;
     _halfHeight = 0F;
     _halfWidth = 0F;
     _touchListener = null;
  }

  public void dispose()
  {
    _image = null;
    if (_imageBuilder != null)
       _imageBuilder.dispose();
    if (_touchListener != null)
       _touchListener.dispose();
  
    _glState._release();
  }

  public final void init(G3MRenderContext rc, int viewportWidth, int viewportHeight)
  {
    if (_glState == null)
    {
      _glState = new GLState();
      _viewportExtent = new ViewportExtentGLFeature(viewportWidth, viewportHeight);
  
      _texHandler = rc.getTexturesHandler();
      _imageBuilder.build(rc, new WidgetImageListener(this), true);
  
      _glState.addGLFeature(_viewportExtent, false);
    }
  }

  public final void render(G3MRenderContext rc, GLState glState)
  {
    rc.getGL().drawArrays(GLPrimitive.triangleStrip(), 0, 4, _glState, rc.getGPUProgramManager());
  }

  public final void setScreenPos(float x, float y)
  {
    if (_geo2Dfeature != null)
    {
      _geo2Dfeature.setTranslation(x, y);
    }
    _x = x;
    _y = y;
  }
  public final Vector2F getScreenPos()
  {
     return new Vector2F(_x, _y);
  }
  public final void resetPosition()
  {
    if (_geo2Dfeature != null)
    {
      _geo2Dfeature.setTranslation(0, 0);
    }
    _x = java.lang.Float.NaN;
    _y = java.lang.Float.NaN;
  }

  public final float getHalfWidth()
  {
     return _halfWidth;
  }
  public final float getHalfHeight()
  {
     return _halfHeight;
  }

  public final void onResizeViewportEvent(int width, int height)
  {
    if (_viewportExtent != null)
    {
      _viewportExtent.changeExtent(width, height);
    }
  }

  public final boolean isReady()
  {
    return _image != null;
  }

  public final void clampPositionInsideScreen(int viewportWidth, int viewportHeight, int margin)
  {
    final IMathUtils mu = IMathUtils.instance();
    float x = mu.clamp(_x, _halfWidth + margin, viewportWidth - _halfWidth - margin);
    float y = mu.clamp(_y, _halfHeight + margin, viewportHeight - _halfHeight - margin);
  
    setScreenPos(x, y);
  }

  public final boolean onTouchEvent(float x, float y)
  {
    final IMathUtils mu = IMathUtils.instance();
    if (mu.isBetween(x, _x - _halfWidth, _x + _halfWidth) && mu.isBetween(y, _y - _halfHeight, _y + _halfHeight))
    {
      if (_touchListener != null)
      {
        _touchListener.touchedMark(this, x, y);
      }
      return true;
    }
    return false;
  }

  public final void setTouchListener(MarkWidgetTouchListener tl)
  {
    if (_touchListener != null && _touchListener != tl)
    {
      if (_touchListener != null)
         _touchListener.dispose();
    }
    _touchListener = tl;
  }

}