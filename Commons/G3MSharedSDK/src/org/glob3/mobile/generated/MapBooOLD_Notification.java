package org.glob3.mobile.generated; 
public class MapBooOLD_Notification
{
  private final Geodetic2D _position ;
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

  public final Geodetic2D getPosition()
  {
    return _position;
  }

  public final MapBooOLD_CameraPosition getCameraPosition()
  {
    return _cameraPosition;
  }

  public final String getMessage()
  {
    return _message;
  }

  public final URL getIconURL()
  {
    return _iconURL;
  }

}