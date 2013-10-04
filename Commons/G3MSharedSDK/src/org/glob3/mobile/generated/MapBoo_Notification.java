package org.glob3.mobile.generated; 
public class MapBoo_Notification
{
  public final Geodetic2D _position ;
  public final String _message;
  public final MapBoo_CameraPosition _cameraPosition;

  public MapBoo_Notification(Geodetic2D position, String message, MapBoo_CameraPosition cameraPosition)
  {
     _position = new Geodetic2D(position);
     _message = message;
     _cameraPosition = cameraPosition;
  }

  public void dispose()
  {
    if (_cameraPosition != null)
       _cameraPosition.dispose();
  }
}