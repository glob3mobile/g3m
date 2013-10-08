package org.glob3.mobile.generated; 
//
//  IDeviceInfo.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/8/13.
//
//

//
//  IDeviceInfo.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/8/13.
//
//


public abstract class IDeviceInfo
{

  public void dispose()
  {

  }

  public final float getPixelsInMM(float millimeters)
  {
    return getDPI() / 25.4f * millimeters;
  }

  public abstract float getDPI();

}