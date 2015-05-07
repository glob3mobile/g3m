package org.glob3.mobile.generated; 
public abstract class IDeviceAttitude
{
  private static IDeviceAttitude _instance;

  protected CoordinateSystem _camCSPortrait = new CoordinateSystem();

  protected CoordinateSystem _camCSPortraitUD = new CoordinateSystem();

  protected CoordinateSystem _camCSLL = new CoordinateSystem();

  protected CoordinateSystem _camCSLR = new CoordinateSystem();

  public IDeviceAttitude()
  {
     _camCSPortrait = new CoordinateSystem(new Vector3D(1,0,0), new Vector3D(0,0,-1), new Vector3D(0,1,0), Vector3D.zero);
     _camCSPortraitUD = new CoordinateSystem(new Vector3D(1,0,0), new Vector3D(0,0,-1), new Vector3D(0,-1,0), Vector3D.zero);
     _camCSLL = new CoordinateSystem(new Vector3D(0,1,0), new Vector3D(0,0,-1), new Vector3D(-1,0,0), Vector3D.zero);
     _camCSLR = new CoordinateSystem(new Vector3D(0,1,0), new Vector3D(0,0,-1), new Vector3D(1,0,0), Vector3D.zero);
  
  }

  public static void setInstance(IDeviceAttitude deviceAttitude)
  {
    if (_instance != null)
    {
      ILogger.instance().logWarning("ILooger instance already set!");
      if (_instance != null)
         _instance.dispose();
    }
    _instance = deviceAttitude;
  }

  //Singleton
  public static IDeviceAttitude instance()
  {
    return _instance;
  }

  public void dispose()
  {
  }

  /**
   Must be called before any other operation
   **/

  public abstract void startTrackingDeviceOrientation();

  /**
   Must be called to stop operations
   **/

  public abstract void stopTrackingDeviceOrientation();

  public abstract boolean isTracking();

  public abstract void copyValueOfRotationMatrix(MutableMatrix44D rotationMatrix);

  public abstract InterfaceOrientation getCurrentInterfaceOrientation();

  public final CoordinateSystem getCameraCoordinateSystemForInterfaceOrientation(InterfaceOrientation orientation)
  {
  
    switch (orientation)
    {
      case PORTRAIT:
      {
        return _camCSPortrait;
      }
  
      case PORTRAIT_UPSIDEDOWN:
      {
        return _camCSPortraitUD;
      }
  
      case LANDSCAPE_LEFT:
      {
        return _camCSLL;
      }
  
      case LANDSCAPE_RIGHT:
      {
        return _camCSLR;
      }
  
      default:
      {
        //Landscape right
        return _camCSLR;
      }
    }
  }

}