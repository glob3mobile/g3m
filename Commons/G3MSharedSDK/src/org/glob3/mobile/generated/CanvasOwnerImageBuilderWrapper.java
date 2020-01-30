package org.glob3.mobile.generated;
//
//  CanvasOwnerImageBuilderWrapper.cpp
//  G3MiOSSDK
//
//  Created by Diego on 1/30/20.
//

//
//  CanvasOwnerImageBuilderWrapper.hpp
//  G3MiOSSDK
//
//  Created by Diego on 1/30/20.
//



//class ICanvas;

public class CanvasOwnerImageBuilderWrapper implements IImageBuilder
{
  private ICanvas _canvas;
  private IImageBuilder _imageBuilder;

  public CanvasOwnerImageBuilderWrapper(ICanvas canvas, IImageBuilder imageBuilder)
  {
     _canvas = canvas;
     _imageBuilder = imageBuilder;
  
  }

  public void dispose()
  {
    if (_canvas != null)
       _canvas.dispose();
  }

  public final boolean isMutable()
  {
    return _imageBuilder.isMutable();
  }

  public final void build(G3MContext context, IImageBuilderListener listener, boolean deleteListener)
  {
    _imageBuilder.build(context, listener, deleteListener);
  }

  public final void setChangeListener(ChangedListener listener)
  {
    _imageBuilder.setChangeListener(listener);
  }

}
