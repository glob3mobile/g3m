package org.glob3.mobile.generated;
//
//  TaitBryanAngles.cpp
//  G3M
//
//  Created by Jose Miguel SN on 06/02/14.
//
//

//
//  TaitBryanAngles.hpp
//  G3M
//
//  Created by Jose Miguel SN on 06/02/14.
//
//



public class TaitBryanAngles
{
  public final Angle _heading ;
  public final Angle _pitch ;
  public final Angle _roll ;

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

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("(heading= ");
    isb.addString(_heading.description());
    isb.addString(", pitch= ");
    isb.addString(_pitch.description());
    isb.addString(", roll= ");
    isb.addString(_roll.description());
    isb.addString(")");
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

}
