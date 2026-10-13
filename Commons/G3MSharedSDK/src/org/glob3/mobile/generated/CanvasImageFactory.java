package org.glob3.mobile.generated;
//
//  CanvasImageFactory.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/10/14.
//
//

//
//  CanvasImageFactory.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/10/14.
//
//


//class ICanvas;

public abstract class CanvasImageFactory extends AbstractImageFactory
{
  private final int _width;
  private final int _height;
  private final boolean _retina;


  protected CanvasImageFactory(int width, int height, boolean retina)
  {
     _width = width;
     _height = height;
     _retina = retina;
  }

  protected final String getResolutionID(G3MContext context)
  {
    final IStringUtils su = context.getStringUtils();
  
    return (su.toString(_width) + "x" + su.toString(_height) + (_retina ? "@2x" : ""));
  }

  public void dispose()
  {
    super.dispose();
  }

  protected abstract void buildOnCanvas(G3MContext context, ICanvas canvas);

  protected abstract String getImageName(G3MContext context);

  public final void create(G3MContext context, IImageFactoryListener listener, boolean deleteListener)
  {
    ICanvas canvas = context.getFactory().createCanvas(_retina);
    canvas.initialize(_width, _height);
  
    buildOnCanvas(context, canvas);
  
    canvas.createImage(new CanvasOwnerImageListenerWrapper(canvas, new CanvasImageFactory_ImageListener(getImageName(context), listener, deleteListener), true), true);
  }

}