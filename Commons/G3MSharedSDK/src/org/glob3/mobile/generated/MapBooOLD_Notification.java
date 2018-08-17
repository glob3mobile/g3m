package org.glob3.mobile.generated;import java.util.*;

public class MapBooOLD_Notification
{
  private final Geodetic2D _position = new Geodetic2D();
  private final MapBooOLD_CameraPosition _cameraPosition;
  private final String _message;
  private final URL _iconURL;


  public MapBooOLD_Notification(Geodetic2D position, MapBooOLD_CameraPosition cameraPosition, String message, URL iconURL)
  {
	  _position = new Geodetic2D(position);
	  _cameraPosition = cameraPosition;
	  _message = message;
	  _iconURL = iconURL;
  }

  public void dispose()
  {
	if (_iconURL != null)
		_iconURL.dispose();
	if (_cameraPosition != null)
		_cameraPosition.dispose();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Geodetic2D getPosition() const
  public final Geodetic2D getPosition()
  {
	return _position;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const MapBooOLD_CameraPosition* getCameraPosition() const
  public final MapBooOLD_CameraPosition getCameraPosition()
  {
	return _cameraPosition;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String getMessage() const
  public final String getMessage()
  {
	return _message;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const URL* getIconURL() const
  public final URL getIconURL()
  {
	return _iconURL;
  }

}
