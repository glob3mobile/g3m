package org.glob3.mobile.generated;
//
//  CameraContext.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/14/17.
//
//

//
//  CameraContext.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/14/17.
//
//



//class Camera;


public class CameraContext
{
  private CameraEventGesture _currentGesture;
  private Camera _nextCamera;

  public CameraContext(CameraEventGesture gesture, Camera nextCamera)
  {
     _currentGesture = gesture;
     _nextCamera = nextCamera;
  }

  public void dispose()
  {
  }

  public final CameraEventGesture getCurrentGesture()
  {
    return _currentGesture;
  }

  public final void setCurrentGesture(CameraEventGesture gesture)
  {
    _currentGesture = gesture;
  }

  public final Camera getNextCamera()
  {
    return _nextCamera;
  }
}
