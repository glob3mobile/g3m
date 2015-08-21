package org.glob3.mobile.generated; 
public class MapBooOLD_CameraPosition
{
  private final Geodetic3D _position ;
  private final Angle _heading ;
  private final Angle _pitch ;
  private final boolean _animated;

  public MapBooOLD_CameraPosition(Geodetic3D position, Angle heading, Angle pitch, boolean animated)
  {
     _position = new Geodetic3D(position);
     _heading = new Angle(heading);
     _pitch = new Angle(pitch);
     _animated = animated;
  }

  public void dispose()
  {

  }

  public final Geodetic3D getPosition()
  {
    return _position;
  }

  public final Angle getHeading()
  {
    return _heading;
  }

  public final Angle getPitch()
  {
    return _pitch;
  }

  public final boolean isAnimated()
  {
    return _animated;
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
  
    isb.addString("[CameraPosition position=");
    isb.addString(_position.description());
  
    isb.addString(", heading=");
    isb.addString(_heading.description());
  
    isb.addString(", pitch=");
    isb.addString(_pitch.description());
  
    isb.addString(", animated=");
    isb.addBool(_animated);
  
    isb.addString("]");
  
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }
  @Override
  public String toString() {
    return description();
  }

}