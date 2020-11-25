package org.glob3.mobile.generated;
//
//  MutableAngle.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/4/17.
//
//

//
//  MutableAngle.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/4/17.
//
//



//class Angle;


public class MutableAngle
{
  private double _degrees;
  private double _radians;

  private MutableAngle(double degrees, double radians)
  {
     _degrees = degrees;
     _radians = radians;
  }

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  MutableAngle operator =(MutableAngle that);

  public MutableAngle(MutableAngle angle)
  {
     _degrees = angle._degrees;
     _radians = angle._radians;
  }

  public static MutableAngle fromDegrees(double degrees)
  {
    return new MutableAngle(degrees, ((degrees) / 180.0 * 3.14159265358979323846264338327950288));
  }

  public static MutableAngle fromRadians(double radians)
  {
    return new MutableAngle(((radians) * (180.0 / 3.14159265358979323846264338327950288)), radians);
  }

  public final void setDegrees(double degrees)
  {
    _degrees = degrees;
    _radians = ((degrees) / 180.0 * 3.14159265358979323846264338327950288);
  }

  public final void setRadians(double radians)
  {
    _degrees = ((radians) * (180.0 / 3.14159265358979323846264338327950288));
    _radians = radians;
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addDouble(_degrees);
    isb.addString("d");
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

  @Override
  public String toString() {
    return description();
  }

  public final Angle asAngle()
  {
    return Angle.fromRadians(_radians);
  }

}