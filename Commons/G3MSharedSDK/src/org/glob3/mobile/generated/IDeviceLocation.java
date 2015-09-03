package org.glob3.mobile.generated; 
//
//  IDeviceLocation.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 25/6/15.
//
//

//
//  IDeviceLocation.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 25/6/15.
//
//



public abstract class IDeviceLocation
{

  private static IDeviceLocation _instance = null;


  public static IDeviceLocation instance()
  {
    return _instance;
  }

  public static void setInstance(IDeviceLocation loc)
  {
    if (_instance != null)
    {
      ILogger.instance().logWarning("IDeviceLocation instance already set!");
      if (_instance != null)
         _instance.dispose();
    }
    _instance = loc;
  }

  public void dispose()
  {
  }

  public abstract boolean startTrackingLocation();
  public abstract void stopTrackingLocation();
  public abstract boolean isTrackingLocation();

  public abstract Geodetic3D getLocation();

}