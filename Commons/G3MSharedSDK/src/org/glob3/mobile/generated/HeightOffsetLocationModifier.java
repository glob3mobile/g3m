package org.glob3.mobile.generated;//
//  DeviceAttitudeCameraHandler.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 1/9/15.
//
//

//
//  DeviceAttitudeCameraHandler.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 1/9/15.
//
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class MeshRenderer;


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
