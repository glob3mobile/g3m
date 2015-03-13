package org.glob3.mobile.client;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.Camera;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.ICameraConstrainer;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.Planet;


public class MyCameraConstrainer implements ICameraConstrainer
{
	float _maxPitch;
	
	public MyCameraConstrainer(float maxPitch)
	{
		_maxPitch = maxPitch;
	}
	
	public void dispose()
	{
	}

	public boolean onCameraChange(Planet planet, Camera previousCamera, Camera nextCamera)
	{
		final double radii = planet.getRadii().maxAxis();
		final double maxHeight = radii *9;
		final double minHeight = 10;

		final Geodetic3D cameraPosition = nextCamera.getGeodeticPosition();
		final double cameraHeight = cameraPosition._height;
		final double cameraPitch = nextCamera.getPitch()._degrees;
		
		ILogger.instance().logInfo("camera pos = %f %f %f", cameraPosition._latitude._degrees,
				cameraPosition._longitude._degrees, cameraPosition._height);
		ILogger.instance().logInfo("heading=" + nextCamera.getHeading()._degrees +
				"   pitch=" + cameraPitch + "<" + _maxPitch);

		if (cameraHeight > maxHeight)
		{
			nextCamera.copyFrom(previousCamera);
			/*nextCamera->setGeodeticPosition(cameraPosition._latitude,
	                                      cameraPosition._longitude,
	                                      maxHeight);*/
		}
		if (cameraHeight < minHeight)
		{
			nextCamera.copyFrom(previousCamera);
			/*nextCamera->setGeodeticPosition(cameraPosition._latitude,
	                                      cameraPosition._longitude,
	                                      minHeight);*/
		}
		if (cameraPitch > _maxPitch)
		{
			//nextCamera.copyFrom(previousCamera);
			double prevHeight = previousCamera.getGeodeticPosition()._height;
			Angle prevPitch = previousCamera.getPitch();
			nextCamera.setGeodeticPosition(cameraPosition.asGeodetic2D(), prevHeight);
			nextCamera.setPitch(prevPitch);
		}

		return true;
	}

}