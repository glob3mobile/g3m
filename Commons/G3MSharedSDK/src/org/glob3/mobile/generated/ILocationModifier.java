package org.glob3.mobile.generated;
//
//  ILocationModifier.h
//  G3M
//
//  Created by Jose Miguel SN on 28/10/15.
//
//


//class Geodetic3D;

/** Class used as modifier of GPS data from DeviceAttitudeCameraHandler**/

public interface ILocationModifier
{

  /** 
   Modifies the sensors position every frame 
   **/
  Geodetic3D modify(Geodetic3D location);

}