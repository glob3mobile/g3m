package org.glob3.mobile.generated;//
//  CameraEventHandler.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 07/08/12.
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TouchEvent;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class G3MRenderContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class CameraContext;

public abstract class CameraEventHandler
{

  public abstract boolean onTouchEvent(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext);

  public abstract void render(G3MRenderContext rc, CameraContext cameraContext);

  public void dispose()
  {

  }

  public abstract void onDown(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext);

  public abstract void onMove(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext);

  public abstract void onUp(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext);

}
