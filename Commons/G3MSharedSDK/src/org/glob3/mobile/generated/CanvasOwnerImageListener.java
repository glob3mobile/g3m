package org.glob3.mobile.generated;
//
//  CanvasOwnerImageListener.cpp
//  G3MiOSSDK
//
//  Created by Diego on 1/29/20.
//

//
//  CanvasOwnerImageListener.hpp
//  G3MiOSSDK
//
//  Created by Diego on 1/29/20.
//



//class ICanvas;


public class CanvasOwnerImageListener extends IImageListener
{
  private ICanvas _canvas;

  protected CanvasOwnerImageListener(ICanvas canvas)
  {
     _canvas = canvas;
  
  }

  public void dispose()
  {
    if (_canvas != null)
       _canvas.dispose();
  }

}
