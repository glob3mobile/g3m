package org.glob3.mobile.generated;//
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


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Color;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
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
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: ICanvas* getCanvas(int width, int height) const
  protected final ICanvas getCanvas(int width, int height)
  {
	if ((_canvas == null) || (_canvasWidth != width) || (_canvasHeight != height))
	{
	  if (_canvas != null)
		  _canvas.dispose();
  
	  _canvas = IFactory.instance().createCanvas(false);
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
