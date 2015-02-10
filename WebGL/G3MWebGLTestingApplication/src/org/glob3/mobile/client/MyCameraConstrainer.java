package org.glob3.mobile.client;

import org.glob3.mobile.generated.Camera;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.ICameraConstrainer;
import org.glob3.mobile.generated.Planet;


public class MyCameraConstrainer implements ICameraConstrainer
{

	public void dispose()
	{
	}

	public boolean onCameraChange(Planet planet, Camera previousCamera, Camera nextCamera)
	{

		final double radii = planet.getRadii().maxAxis();
		final double maxHeight = radii *9;
		final double minHeight = 30000;

		final Geodetic3D cameraPosition = nextCamera.getGeodeticPosition();
		final double cameraHeight = cameraPosition._height;

		if (cameraHeight > maxHeight)
		{
			nextCamera.copyFrom(previousCamera);
			/*nextCamera->setGeodeticPosition(cameraPosition._latitude,
	                                      cameraPosition._longitude,
	                                      maxHeight);*/
		}
		else if (cameraHeight < minHeight)
		{
			nextCamera.copyFrom(previousCamera);
			/*nextCamera->setGeodeticPosition(cameraPosition._latitude,
	                                      cameraPosition._longitude,
	                                      minHeight);*/
		}

		return true;
	}

}