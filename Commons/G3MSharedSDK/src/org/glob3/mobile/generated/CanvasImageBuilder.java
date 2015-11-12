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
  private ICanvas _canvas;
  private int _canvasWidth;
  private int _canvasHeight;

  private ICanvas getCanvas(G3MContext context)
  {
    if ((_canvas == null) || (_canvasWidth != _width) || (_canvasHeight != _height))
    {
      if (_canvas != null)
         _canvas.dispose();
  
      final IFactory factory = context.getFactory();
  
      _canvas = factory.createCanvas();
      _canvas.initialize(_width, _height);
      _canvasWidth = _width;
      _canvasHeight = _height;
    }
    else
    {
      _canvas.setFillColor(Color.transparent());
      _canvas.fillRectangle(0, 0, _width, _height);
    }
  
    return _canvas;
  }

//  static long long _counter;

  protected final int _width;
  protected final int _height;

  protected CanvasImageBuilder(int width, int height)
  {
     _width = width;
     _height = height;
     _canvas = null;
     _canvasWidth = 0;
     _canvasHeight = 0;
  }


  ///#include "IStringUtils.hpp"
  
  public void dispose()
  {
    if (_canvas != null)
       _canvas.dispose();
  
    super.dispose();
  }

  protected abstract void buildOnCanvas(G3MContext context, ICanvas canvas);

  protected abstract String getImageName(G3MContext context);


  //long long CanvasImageBuilder::_counter = 0;
  
  //std::string CanvasImageBuilder::getImageName(const G3MContext* context) const {
  //  const IStringUtils* su = context->getStringUtils();
  //
  //  return "_CanvasImageBuilder_" + su->toString(_counter++);
  //}
  
  public final void build(G3MContext context, IImageBuilderListener listener, boolean deleteListener)
  {
    ICanvas canvas = getCanvas(context);
  
    buildOnCanvas(context, canvas);
  
    canvas.createImage(new CanvasImageBuilder_ImageListener(getImageName(context), listener, deleteListener), true);
  }

}