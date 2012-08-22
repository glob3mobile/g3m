package org.glob3.mobile.generated; 
public class SimpleCameraConstrainer implements ICameraConstrainer
{
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean acceptsCamera(const Camera* camera, const Planet *planet) const
  public final boolean acceptsCamera(Camera camera, Planet planet)
  {
	final double distance = camera.getPosition().length();
	final double radii = planet.getRadii().maxAxis();
	if (distance > radii *10)
	{
	  // printf ("--- camera constraint!\n");
	  return false;
	}

	return true;
  }
}