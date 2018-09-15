package org.glob3.mobile.generated;
//
//  CanvasImageBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/10/14.
//
//

//
//  CanvasImageBuilder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/10/14.
//
//


//class ICanvas;

public abstract class CanvasImageBuilder extends AbstractImageBuilder
{
  private final int _width;
  private final int _height;
  private final boolean _retina;

  private ICanvas _canvas;
  private int _canvasWidth;
  private int _canvasHeight;
  private boolean _canvasRetina;

  private ICanvas getCanvas(G3MContext context)
  {
    if ((_canvas == null) || (_canvasWidth != _width) || (_canvasHeight != _height) || (_canvasRetina != _retina))
    {
      if (_canvas != null)
         _canvas.dispose();
  
      final IFactory factory = context.getFactory();
  
      _canvas = factory.createCanvas(_retina);
      _canvas.initialize(_width, _height);
      _canvasWidth = _width;
      _canvasHeight = _height;
      _canvasRetina = _retina;
    }
    else
    {
      _canvas.setFillColor(Color.TRANSPARENT);
      _canvas.fillRectangle(0, 0, _width, _height);
    }
  
    return _canvas;
  }


  protected CanvasImageBuilder(int width, int height, boolean retina)
  {
     _width = width;
     _height = height;
     _retina = retina;
     _canvas = null;
     _canvasWidth = 0;
     _canvasHeight = 0;
     _canvasRetina = false;
  }

  protected final String getResolutionID(G3MContext context)
  {
    final IStringUtils su = context.getStringUtils();
  
    return (su.toString(_width) + "x" + su.toString(_height) + (_retina ? "@2x" : ""));
  }

  public void dispose()
  {
    if (_canvas != null)
       _canvas.dispose();
  
    super.dispose();
  }

  protected abstract void buildOnCanvas(G3MContext context, ICanvas canvas);

  protected abstract String getImageName(G3MContext context);

  public final void build(G3MContext context, IImageBuilderListener listener, boolean deleteListener)
  {
    ICanvas canvas = getCanvas(context);
  
    buildOnCanvas(context, canvas);
  
    canvas.createImage(new CanvasImageBuilder_ImageListener(getImageName(context), listener, deleteListener), true);
  }

}
