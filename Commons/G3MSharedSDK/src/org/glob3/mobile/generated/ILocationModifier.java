package org.glob3.mobile.generated; 
//
//  ILocationModifier.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 28/10/15.
//
//


/** Class used as modifier of GPS data from DeviceAttitudeCameraHandler**/


public abstract class ILocationModifier
{
  public void dispose()
  {
  }

  /** Modifies the sensors position every frame **/
  public abstract Geodetic3D modify(Geodetic3D location);

}