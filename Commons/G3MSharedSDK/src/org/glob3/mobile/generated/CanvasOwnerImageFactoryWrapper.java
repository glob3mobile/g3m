package org.glob3.mobile.generated;
//
//  CanvasOwnerImageFactoryWrapper.cpp
//  G3MiOSSDK
//
//  Created by Diego on 1/30/20.
//

//
//  CanvasOwnerImageFactoryWrapper.hpp
//  G3MiOSSDK
//
//  Created by Diego on 1/30/20.
//



//class ICanvas;

public class CanvasOwnerImageFactoryWrapper implements IImageFactory
{
  private ICanvas _canvas;
  private IImageFactory _imageFactory;
  private final boolean _autodelete;

  public CanvasOwnerImageFactoryWrapper(ICanvas canvas, IImageFactory imageFactory, boolean autodelete)
  {
     _canvas = canvas;
     _imageFactory = imageFactory;
     _autodelete = autodelete;
  
  }

  public void dispose()
  {
    if (_canvas != null)
       _canvas.dispose();
    if (_autodelete)
    {
      if (_imageFactory != null)
         _imageFactory.dispose();
    }
  }

  public final boolean isMutable()
  {
    return _imageFactory.isMutable();
  }

  public final void create(G3MContext context, IImageFactoryListener listener, boolean deleteListener)
  {
    _imageFactory.create(context, listener, deleteListener);
  }

  public final void setChangeListener(ChangedListener listener)
  {
    _imageFactory.setChangeListener(listener);
  }

}