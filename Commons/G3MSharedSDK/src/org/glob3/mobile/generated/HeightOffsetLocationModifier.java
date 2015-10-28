package org.glob3.mobile.generated; 
public class HeightOffsetLocationModifier implements ILocationModifier
{
  private double _offsetInMeters;


  public HeightOffsetLocationModifier(double offsetInMeters)
  {
     _offsetInMeters = offsetInMeters;
  }

  public final Geodetic3D modify(Geodetic3D location)
  {
    return Geodetic3D.fromDegrees(location._latitude._degrees, location._longitude._degrees, location._height + _offsetInMeters);
  }

}