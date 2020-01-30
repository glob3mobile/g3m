package org.glob3.mobile.generated;
//
//  CanvasImageBuilder.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/10/14.
//
//

//
//  CanvasImageBuilder.hpp
//  G3M
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


  protected CanvasImageBuilder(int width, int height, boolean retina)
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

  public final void build(G3MContext context, IImageBuilderListener listener, boolean deleteListener)
  {
    ICanvas canvas = context.getFactory().createCanvas(_retina);
    canvas.initialize(_width, _height);
  
    buildOnCanvas(context, canvas);
  
    canvas.createImage(new CanvasImageBuilder_ImageListener(canvas, getImageName(context), listener, deleteListener), true); // transfer canvas to be deleted AFTER the image creation
  }

}
