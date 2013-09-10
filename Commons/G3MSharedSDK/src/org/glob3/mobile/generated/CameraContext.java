package org.glob3.mobile.generated; 
public class CameraContext
{
  private Gesture _currentGesture;
  private Camera _nextCamera;

  public CameraContext(Gesture gesture, Camera nextCamera)
  {
     _currentGesture = gesture;
     _nextCamera = nextCamera;
  }

  public void dispose()
  {

  }

  public final Gesture getCurrentGesture()
  {
     return _currentGesture;
  }
  public final void setCurrentGesture(Gesture gesture)
  {
     _currentGesture = gesture;
  }
  public final Camera getNextCamera()
  {
     return _nextCamera;
  }
}