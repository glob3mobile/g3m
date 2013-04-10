package org.glob3.mobile.generated; 
//
//  ICanvas.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/9/13.
//
//

//
//  ICanvas.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/9/13.
//
//


//class Color;
//class IImageListener;
//class GFont;


public abstract class ICanvas
{
  private int _width;
  private int _height;
  private GFont _currentFont;

  private boolean isInitialized()
  {
    return (_width > 0) && (_height > 0);
  }

  private void checkInitialized()
  {
    if (!isInitialized())
    {
      throw G3MError("Canvas is not initialized");
    }
  }
  private void checkCurrentFont()
  {
    if (_currentFont == null)
    {
      throw G3MError("Current font no set");
    }
  }


  protected abstract void _initialize(int width, int height);


  protected abstract void _setFillColor(Color color);

  protected abstract void _setStrokeColor(Color color);

  protected abstract void _setStrokeWidth(float width);


  protected abstract void _fillRectangle(float x, float y, float width, float height);

  protected abstract void _strokeRectangle(float x, float y, float width, float height);

  protected abstract void _fillAndStrokeRectangle(float x, float y, float width, float height);


  protected abstract void _fillRoundedRectangle(float x, float y, float width, float height, float radius);

  protected abstract void _strokeRoundedRectangle(float x, float y, float width, float height, float radius);
  protected abstract void _fillAndStrokeRoundedRectangle(float x, float y, float width, float height, float radius);

  protected abstract void _setShadow(Color color, float blur, float offsetX, float offsetY);

  protected abstract void _removeShadow();


  protected abstract void _createImage(IImageListener listener, boolean autodelete);

  protected abstract void _setFont(GFont font);

  protected abstract Vector2F _textExtent(String text);

  protected abstract void _fillText(String text, float x, float y);

  public ICanvas()
  {
     _width = -1;
     _height = -1;
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
      throw G3MError("Invalid extent");
    }
  
    if (isInitialized())
    {
      throw G3MError("Canvas already initialized");
    }
  
    _width = width;
    _height = height;
    _initialize(width, height);
  }

  /**
   Returns the size of the text if it were to be rendered with the specified font on a single line.

   NOTE: No need to initialize the canvas before calling this method.
   */
  public abstract Vector2F textExtent(String text, GFont font);

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

  public final void setStrokeColor(Color color)
  {
    checkInitialized();
    _setStrokeColor(color);
  }

  public final void setStrokeWidth(float width)
  {
    checkInitialized();
    _setStrokeWidth(width);
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


  public final void fillRectangle(float x, float y, float width, float height)
  {
    checkInitialized();
    _fillRectangle(x, y, width, height);
  }

  public final void strokeRectangle(float x, float y, float width, float height)
  {
    checkInitialized();
    _strokeRectangle(x, y, width, height);
  }

  public final void fillAndStrokeRectangle(float x, float y, float width, float height)
  {
    checkInitialized();
    _fillAndStrokeRectangle(x, y, width, height);
  }

  public final void fillRoundedRectangle(float x, float y, float width, float height, float radius)
  {
    checkInitialized();
    _fillRoundedRectangle(x, y, width, height, radius);
  }

  public final void strokeRoundedRectangle(float x, float y, float width, float height, float radius)
  {
    checkInitialized();
    _strokeRoundedRectangle(x, y, width, height, radius);
  }

  public final void fillAndStrokeRoundedRectangle(float x, float y, float width, float height, float radius)
  {
    checkInitialized();
    _fillAndStrokeRoundedRectangle(x, y, width, height, radius);
  }

  public final void createImage(IImageListener listener, boolean autodelete)
  {
    checkInitialized();
    _createImage(listener, autodelete);
  }

  public final void fillText(String text, float x, float y)
  {
    checkInitialized();
    checkCurrentFont();
    _fillText(text, x, y);
  }

}