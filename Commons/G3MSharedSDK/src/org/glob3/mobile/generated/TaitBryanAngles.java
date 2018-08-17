package org.glob3.mobile.generated;import java.util.*;

//
//  TaitBryanAngles.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 06/02/14.
//
//

//
//  TaitBryanAngles.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 06/02/14.
//
//



public class TaitBryanAngles
{
  public final Angle _heading = new Angle();
  public final Angle _pitch = new Angle();
  public final Angle _roll = new Angle();

  public TaitBryanAngles(Angle heading, Angle pitch, Angle roll)
  {
	  _heading = new Angle(heading);
	  _pitch = new Angle(pitch);
	  _roll = new Angle(roll);
  }

  public static TaitBryanAngles fromRadians(double heading, double pitch, double roll)
  {
	return new TaitBryanAngles(Angle.fromRadians(heading), Angle.fromRadians(pitch), Angle.fromRadians(roll));
  }

  public static TaitBryanAngles fromDegrees(double heading, double pitch, double roll)
  {
	return new TaitBryanAngles(Angle.fromDegrees(heading), Angle.fromDegrees(pitch), Angle.fromDegrees(roll));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: String description() const
  public final String description()
  {
  
	IStringBuilder isb = IStringBuilder.newStringBuilder();
	isb.addString("(TaitBryanAngles Heading= ");
	isb.addDouble(_heading._degrees);
	isb.addString(", Pitch= ");
	isb.addDouble(_pitch._degrees);
	isb.addString(", Roll= ");
	isb.addDouble(_roll._degrees);
	isb.addString(")");
	final String s = isb.getString();
	if (isb != null)
		isb.dispose();
	return s;
  
  }
}
