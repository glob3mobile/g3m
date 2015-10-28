package org.glob3.mobile.generated; 
//
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



public abstract class ILocationModifier
{
  public void dispose()
  {
  }

  /** Modifies the sensors position every frame **/
  public abstract Geodetic3D modify(Geodetic3D location);

}