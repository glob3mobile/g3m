package org.glob3.mobile.generated; 
public abstract class ICanvas
{
  protected int _canvasWidth;
  protected int _canvasHeight;
  protected GFont _currentFont;

  protected final boolean isInitialized()
  {
    return (_canvasWidth > 0) && (_canvasHeight > 0);
  }

  protected final void checkInitialized()
  {
    if (!isInitialized())
    {
      ILogger.instance().logError("Canvas is not initialized");
    }
  }
  protected final void checkCurrentFont()
  {
    if (_currentFont == null)
    {
      ILogger.instance().logError("Current font no set");
    }
  }

  protected abstract void _initialize(int width, int height);

  protected abstract void _setFillColor(Color color);

  protected abstract void _setLineColor(Color color);

  protected abstract void _setLineWidth(float width);

  protected abstract void _setLineCap(StrokeCap cap);

  protected abstract void _setLineJoin(StrokeJoin join);

  protected abstract void _setLineMiterLimit(float limit);

  protected abstract void _setLineDash(float[] lengths, int count, float phase);

  protected abstract void _fillRectangle(float left, float top, float width, float height);

  protected abstract void _strokeRectangle(float left, float top, float width, float height);

  protected abstract void _fillAndStrokeRectangle(float left, float top, float width, float height);


  protected abstract void _fillRoundedRectangle(float left, float top, float width, float height, float radius);

  protected abstract void _strokeRoundedRectangle(float left, float top, float width, float height, float radius);
  protected abstract void _fillAndStrokeRoundedRectangle(float left, float top, float width, float height, float radius);

  protected abstract void _setShadow(Color color, float blur, float offsetX, float offsetY);

  protected abstract void _removeShadow();

  protected abstract void _clearRect(float left, float top, float width, float height);

  protected abstract void _createImage(IImageListener listener, boolean autodelete);

  protected abstract void _setFont(GFont font);

  protected abstract Vector2F _textExtent(String text);

  protected abstract void _fillText(String text, float left, float top);

  protected abstract void _drawImage(IImage image, float destLeft, float destTop);

  protected abstract void _drawImage(IImage image, float destLeft, float destTop, float transparency);

  protected abstract void _drawImage(IImage image, float destLeft, float destTop, float destWidth, float destHeight);

  protected abstract void _drawImage(IImage image, float destLeft, float destTop, float destWidth, float destHeight, float transparency);

  protected abstract void _drawImage(IImage image, float srcLeft, float srcTop, float srcWidth, float srcHeight, float destLeft, float destTop, float destWidth, float destHeight);

  protected abstract void _drawImage(IImage image, float srcLeft, float srcTop, float srcWidth, float srcHeight, float destLeft, float destTop, float destWidth, float destHeight, float transparency);


  protected abstract void _beginPath();

  protected abstract void _closePath();

  protected abstract void _stroke();

  protected abstract void _fill();

  protected abstract void _fillAndStroke();

  protected abstract void _moveTo(float x, float y);

  protected abstract void _lineTo(float x, float y);

  protected abstract void _fillEllipse(float left, float top, float width, float height);

  protected abstract void _strokeEllipse(float left, float top, float width, float height);

  protected abstract void _fillAndStrokeEllipse(float left, float top, float width, float height);


  public ICanvas()
  {
     _canvasWidth = -1;
     _canvasHeight = -1;
     _currentFont = null;
  }

  public void dispose()
  {
    if (_currentFont != null)
       _currentFont.dispose();
  }

  /**
   Initialize the Canvas, must be called just one time before calling most methods.
   */
  public final void initialize(int width, int height)
  {
    if ((width <= 0) || (height <= 0))
    {
      ILogger.instance().logError("Invalid extent");
      return;
    }
  
    if (isInitialized())
    {
      ILogger.instance().logError("Canvas already initialized");
      return;
    }
  
    _canvasWidth = width;
    _canvasHeight = height;
    _initialize(width, height);
  }


  /**
   Returns the size of the text if it were to be rendered with the actual font on a single line.

   NOTE: The current font has to be set before calling this method.
   NOTE: No need to initialize the canvas before calling this method.
   */
  public final Vector2F textExtent(String text)
  {
    checkCurrentFont();
    return _textExtent(text);
  }

  /**
   Set the actual font.

   NOTE: No need to initialize the canvas before calling this method.
   */
  public final void setFont(GFont font)
  {
    if (_currentFont != null)
       _currentFont.dispose();
    _currentFont = new GFont(font);
    _setFont(font);
  }

  public final void setFillColor(Color color)
  {
    checkInitialized();
    _setFillColor(color);
  }

  public final void setLineColor(Color color)
  {
    checkInitialized();
    _setLineColor(color);
  }

  public final void setLineWidth(float width)
  {
    checkInitialized();
    _setLineWidth(width);
  }

  public final void setLineCap(StrokeCap cap)
  {
    checkInitialized();
    _setLineCap(cap);
  }

  public final void setLineJoin(StrokeJoin join)
  {
    checkInitialized();
    _setLineJoin(join);
  }

  public final void setLineMiterLimit(float limit)
  {
    checkInitialized();
    _setLineMiterLimit(limit);
  }

  public final void setLineDash(float[] lengths, int count, int phase)
  {
    checkInitialized();
    _setLineDash(lengths, count, phase);
  }

  void setLineDash(float[] lengths,
                   int phase) {
    setLineDash(lengths, lengths.length, phase);
  }

  public final void setShadow(Color color, float blur, float offsetX, float offsetY)
  {
    checkInitialized();
    _setShadow(color, blur, offsetX, offsetY);
  }

  public final void removeShadow()
  {
    checkInitialized();
    _removeShadow();
  }

  public final void clearRect(float left, float top, float width, float height)
  {
    checkInitialized();
    _clearRect(left, top, width, height);
  }

  public final void fillRectangle(float left, float top, float width, float height)
  {
    checkInitialized();
    _fillRectangle(left, top, width, height);
  }

  public final void strokeRectangle(float left, float top, float width, float height)
  {
    checkInitialized();
    _strokeRectangle(left, top, width, height);
  }

  public final void fillAndStrokeRectangle(float left, float top, float width, float height)
  {
    checkInitialized();
    _fillAndStrokeRectangle(left, top, width, height);
  }

  public final void fillRoundedRectangle(float left, float top, float width, float height, float radius)
  {
    checkInitialized();
    _fillRoundedRectangle(left, top, width, height, radius);
  }

  public final void strokeRoundedRectangle(float left, float top, float width, float height, float radius)
  {
    checkInitialized();
    _strokeRoundedRectangle(left, top, width, height, radius);
  }

  public final void fillAndStrokeRoundedRectangle(float left, float top, float width, float height, float radius)
  {
    checkInitialized();
    _fillAndStrokeRoundedRectangle(left, top, width, height, radius);
  }

  public final void fillEllipse(float left, float top, float width, float height)
  {
    checkInitialized();
    _fillEllipse(left, top, width, height);
  }

  public final void strokeEllipse(float left, float top, float width, float height)
  {
    checkInitialized();
    _strokeEllipse(left, top, width, height);
  }

  public final void fillAndStrokeEllipse(float left, float top, float width, float height)
  {
    checkInitialized();
    _fillAndStrokeEllipse(left, top, width, height);
  }


  public final void createImage(IImageListener listener, boolean autodelete)
  {
    checkInitialized();
    _createImage(listener, autodelete);
  }

  public final void fillText(String text, float left, float top)
  {
    checkInitialized();
    checkCurrentFont();
    _fillText(text, left, top);
  }

  public final void drawImage(IImage image, float destLeft, float destTop)
  {
    checkInitialized();
    _drawImage(image, destLeft, destTop);
  }

  public final void drawImage(IImage image, float destLeft, float destTop, float transparency)
  {
    checkInitialized();
    _drawImage(image, destLeft, destTop, transparency);
  }

  public final void drawImage(IImage image, float destLeft, float destTop, float destWidth, float destHeight)
  {
    checkInitialized();
    _drawImage(image, destLeft, destTop, destWidth, destHeight);
  }

  public final void drawImage(IImage image, float destLeft, float destTop, float destWidth, float destHeight, float transparency)
  {
    checkInitialized();
    _drawImage(image, destLeft, destTop, destWidth, destHeight, transparency);
  }

  public final void drawImage(IImage image, float srcLeft, float srcTop, float srcWidth, float srcHeight, float destLeft, float destTop, float destWidth, float destHeight)
  {
    checkInitialized();
  
    if (!RectangleF.fullContains(0, 0, image.getWidth(), image.getHeight(), srcLeft, srcTop, srcWidth, srcHeight))
    {
      ILogger.instance().logError("Invalid source rectangle in drawImage");
    }
  
    _drawImage(image, srcLeft, srcTop, srcWidth, srcHeight, destLeft, destTop, destWidth, destHeight);
  }

  public final void drawImage(IImage image, float srcLeft, float srcTop, float srcWidth, float srcHeight, float destLeft, float destTop, float destWidth, float destHeight, float transparency)
  {
    checkInitialized();
  
    if (!RectangleF.fullContains(0, 0, image.getWidth(), image.getHeight(), srcLeft, srcTop, srcWidth, srcHeight))
    {
      ILogger.instance().logError("Invalid source rectangle in drawImage");
    }
    else
    {
      if (transparency <= 0.0)
      {
        return;
      }
  
      if (transparency >= 1.0)
      {
        _drawImage(image, srcLeft, srcTop, srcWidth, srcHeight, destLeft, destTop, destWidth, destHeight);
      }
      else
      {
        _drawImage(image, srcLeft, srcTop, srcWidth, srcHeight, destLeft, destTop, destWidth, destHeight, transparency);
      }
  
    }
  
  
  }

  public final int getWidth()
  {
    return _canvasWidth;
  }

  public final int getHeight()
  {
    return _canvasHeight;
  }

  public final void beginPath()
  {
    checkInitialized();
    _beginPath();
  }

  public final void closePath()
  {
    checkInitialized();
    _closePath();
  }

  public final void stroke()
  {
    checkInitialized();
    _stroke();
  }

  public final void fill()
  {
    checkInitialized();
    _fill();
  }

  public final void fillAndStroke()
  {
    checkInitialized();
    _fillAndStroke();
  }

  public final void moveTo(float x, float y)
  {
    checkInitialized();
    _moveTo(x, y);
  }

  public final void moveTo(Vector2F position)
  {
    moveTo(position._x, position._y);
  }

  public final void lineTo(float x, float y)
  {
    checkInitialized();
    _lineTo(x, y);
  }

  public final void lineTo(Vector2F position)
  {
    lineTo(position._x, position._y);
  }

}