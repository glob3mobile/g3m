package org.glob3.mobile.generated;//
//  ILocationModifier.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 28/10/15.
//
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Geodetic3D;

/** Class used as modifier of GPS data from DeviceAttitudeCameraHandler**/

public abstract class ILocationModifier
{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  public void dispose()
  {
  }
//#endif

  /** 
   Modifies the sensors position every frame 
   **/
  public abstract Geodetic3D modify(Geodetic3D location);

}
