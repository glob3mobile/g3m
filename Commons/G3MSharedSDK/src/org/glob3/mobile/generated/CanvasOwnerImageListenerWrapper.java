package org.glob3.mobile.generated;
//
//  CanvasOwnerImageListenerWrapper.cpp
//  G3MiOSSDK
//
//  Created by Diego on 1/30/20.
//

//
//  CanvasOwnerImageListenerWrapper.hpp
//  G3MiOSSDK
//
//  Created by Diego on 1/30/20.
//




public class CanvasOwnerImageListenerWrapper extends CanvasOwnerImageListener
{

  private IImageListener _listener;
  private final boolean _autodelete;

  public CanvasOwnerImageListenerWrapper(ICanvas canvas, IImageListener listener, boolean autodelete)
  {
     super(canvas);
     _listener = listener;
     _autodelete = autodelete;
  
  }

  public void dispose()
  {
    if (_autodelete)
    {
      if (_listener != null)
         _listener.dispose();
    }
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
    super.dispose();
//#endif
  }

  public final void imageCreated(IImage image)
  {
    _listener.imageCreated(image);
  }

}
