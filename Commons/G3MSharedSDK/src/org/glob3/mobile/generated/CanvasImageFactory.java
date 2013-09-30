package org.glob3.mobile.generated; 
//
//  CanvasImageFactory.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/27/13.
//
//

//
//  CanvasImageFactory.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/27/13.
//
//



//class ICanvas;

public abstract class CanvasImageFactory extends HUDImageRenderer.ImageFactory
{

  protected abstract void drawOn(ICanvas canvas, int width, int height);

  public CanvasImageFactory()
  {

  }

  public final void create(G3MRenderContext rc, int width, int height, IImageListener listener, boolean deleteListener)
  {
  
    ICanvas canvas = rc.getFactory().createCanvas();
    canvas.initialize(width, height);
  
    drawOn(canvas, width, height);
  
    canvas.createImage(listener, deleteListener);
  
    if (canvas != null)
       canvas.dispose();
  }

}