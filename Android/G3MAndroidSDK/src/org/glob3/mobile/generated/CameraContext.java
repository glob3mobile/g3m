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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Gesture getCurrentGesture() const
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