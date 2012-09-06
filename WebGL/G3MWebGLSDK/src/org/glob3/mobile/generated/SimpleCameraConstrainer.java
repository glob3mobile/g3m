package org.glob3.mobile.generated; 
public class SimpleCameraConstrainer implements ICameraConstrainer
{
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void onCameraChange(const Planet *planet, const Camera* previousCamera, Camera* nextCamera) const
  public final void onCameraChange(Planet planet, Camera previousCamera, Camera nextCamera)
  {

	final double radii = planet.getRadii().maxAxis();

	final Geodetic3D cameraPosition3D = planet.toGeodetic3D(nextCamera.getCartesianPosition());
	final double cameraHeight = cameraPosition3D.height();

	if (cameraHeight > radii *9)
	{
	  nextCamera.resetPosition();
	  nextCamera.setPosition(planet.toGeodetic3D(previousCamera.getCartesianPosition()));
	}
  }


  //  bool acceptsCamera(const Camera* camera,
  //                     const Planet *planet) const {
  //    const double distance = camera->getPosition().length();
  //    const double radii    = planet->getRadii().maxAxis();
  //    if (distance > radii*10) {
  //      // printf ("--- camera constraint!\n");
  //      return false;
  //    }
  //
  //    return true;
  //  }
}