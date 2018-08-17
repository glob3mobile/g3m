package org.glob3.mobile.generated;import java.util.*;

public class MapBooOLD_CameraPosition
{
  private final Geodetic3D _position = new Geodetic3D();
  private final Angle _heading = new Angle();
  private final Angle _pitch = new Angle();
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Geodetic3D getPosition() const
  public final Geodetic3D getPosition()
  {
	return _position;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Angle getHeading() const
  public final Angle getHeading()
  {
	return _heading;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Angle getPitch() const
  public final Angle getPitch()
  {
	return _pitch;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isAnimated() const
  public final boolean isAnimated()
  {
	return _animated;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String description() const
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
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public final Override public String toString()
  {
	return description();
  }
//#endif

}
