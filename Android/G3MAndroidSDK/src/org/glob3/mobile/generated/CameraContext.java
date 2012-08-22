package org.glob3.mobile.generated; 
public class CameraContext implements EffectTarget
{
  private Gesture _currentGesture;
  private Camera _camera;

  public CameraContext(Gesture gesture, Camera camera)
  {
	  _currentGesture = gesture;
	  _camera = camera;
  }

  public void dispose()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Gesture getCurrentGesture() const
  public final Gesture getCurrentGesture()
  {
	  return _currentGesture;
  }
  public final void setCurrentGesture(Gesture gesture)
  {
	  _currentGesture = gesture;
  }
  public final Camera getCamera()
  {
	  return _camera;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isEffectable() const
  public final boolean isEffectable()
  {
	return true;
  }
}