package org.glob3.mobile.generated; 
//
//  ImageBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/12/13.
//
//

//
//  ImageBuilder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/12/13.
//
//


//class CanvasElement;
//class IImageListener;
//class Color;

public class ImageBuilder
{
  private ImageBuilder()
  {

  }


  public static void build(CanvasElement element, IImageListener listener, boolean autodelete)
  {
    ICanvas canvas = IFactory.instance().createCanvas();
  
    final Vector2F extent = element.getExtent(canvas);
  
    final IMathUtils mu = IMathUtils.instance();
  
    canvas.initialize(mu.round(extent._x), mu.round(extent._y));
  
    element.drawAt(0, 0, canvas);
  
    canvas.createImage(listener, autodelete);
  
    if (element != null)
       element.dispose();
  
    if (canvas != null)
       canvas.dispose();
  }

}