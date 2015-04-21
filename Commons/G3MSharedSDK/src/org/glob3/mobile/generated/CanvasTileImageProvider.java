package org.glob3.mobile.generated; 
//
//  CanvasTileImageProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/18/14.
//
//

//
//  CanvasTileImageProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/18/14.
//
//


//class Color;
//class ICanvas;

public abstract class CanvasTileImageProvider extends TileImageProvider
{
  private ICanvas _canvas;
  private int _canvasWidth;
  private int _canvasHeight;

  private final Color _transparent;

  protected CanvasTileImageProvider()
  {
     _canvas = null;
     _transparent = Color.newFromRGBA(0, 0, 0, 0);
  }

  public void dispose()
  {
    if (_canvas != null)
       _canvas.dispose();
    if (_transparent != null)
       _transparent.dispose();
  
    super.dispose();
  }

  protected final ICanvas getCanvas(int width, int height)
  {
    if ((_canvas == null) || (_canvasWidth != width) || (_canvasHeight != height))
    {
      if (_canvas != null)
         _canvas.dispose();
  
      _canvas = IFactory.instance().createCanvas();
      _canvas.initialize(width, height);
  
      _canvasWidth = width;
      _canvasHeight = height;
    }
    else
    {
      _canvas.clearRect(0, 0, width, height);
      _canvas.setFillColor(_transparent);
      _canvas.fillRectangle(0, 0, width, height);
    }
    return _canvas;
  }

}