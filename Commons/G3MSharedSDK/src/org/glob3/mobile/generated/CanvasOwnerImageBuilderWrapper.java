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
  private final boolean _autodelete;

  public CanvasOwnerImageBuilderWrapper(ICanvas canvas, IImageBuilder imageBuilder, boolean autodelete)
  {
     _canvas = canvas;
     _imageBuilder = imageBuilder;
     _autodelete = autodelete;
  
  }

  public void dispose()
  {
    if (_canvas != null)
       _canvas.dispose();
    if (_autodelete)
    {
      if (_imageBuilder != null)
         _imageBuilder.dispose();
    }
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