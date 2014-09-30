package org.glob3.mobile.generated; 
//
//  CanvasTileRasterizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/11/13.
//
//

//
//  CanvasTileRasterizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/11/13.
//
//



//class ICanvas;
//class Color;

public abstract class CanvasTileRasterizer extends TileRasterizer
{
  private ICanvas _canvas;
  private int _canvasWidth;
  private int _canvasHeight;

  private Color _transparent;


  protected CanvasTileRasterizer()
  {
     _canvas = null;
     _canvasWidth = -1;
     _canvasHeight = -1;
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

  protected ICanvas getCanvas(int width, int height)
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
      _canvas.setFillColor(_transparent);
      _canvas.fillRectangle(0, 0, width, height);
    }
    return _canvas;
  }

}