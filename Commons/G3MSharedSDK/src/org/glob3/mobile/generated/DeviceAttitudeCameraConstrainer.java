package org.glob3.mobile.generated; 
//
//  DeviceAttitudeCameraConstrainer.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 7/5/15.
//
//

//
//  DeviceAttitudeCameraConstrainer.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 7/5/15.
//
//



//class ITimer;


/**
 Class that applies the Rotation obtained with IDeviceAttitude and IInterfaceOrientation to the camera.
 
 It translate to the Global Coordinate System to the Local CS on the camera geodetic location.
 
 **/

public class DeviceAttitudeCameraConstrainer implements ICameraConstrainer
{

  private MutableMatrix44D _localRM = new MutableMatrix44D();
  private MutableMatrix44D _attitudeMatrix = new MutableMatrix44D();
  private MutableMatrix44D _camRM = new MutableMatrix44D();

  private boolean _updateLocation;
  private double _heightOffset;
  private long _lastLocationUpdateTimeInMS;
  private ITimer _timer;


  public DeviceAttitudeCameraConstrainer(boolean updateLocation)
  {
     this(updateLocation, 1000);
  }
  public DeviceAttitudeCameraConstrainer(boolean updateLocation, double heightOffset)
  {
     _updateLocation = updateLocation;
     _heightOffset = heightOffset;
     _lastLocationUpdateTimeInMS = 0;
     _timer = null;
  
  }

  public void dispose()
  {
    IDeviceAttitude.instance().stopTrackingDeviceOrientation();
    IDeviceLocation.instance().stopTrackingLocation();
  
    if (_timer != null)
       _timer.dispose();
  }

  public final boolean onCameraChange(Planet planet, Camera previousCamera, Camera nextCamera)
  {
  
  
    IDeviceAttitude devAtt = IDeviceAttitude.instance();
  
    if (devAtt == null)
    {
      ILogger.instance().logError("IDeviceAttitude not initilized");
      return false;
    }
  
    if (!devAtt.isTracking())
    {
      devAtt.startTrackingDeviceOrientation();
    }
  
    //Getting Global Rotation
    IDeviceAttitude.instance().copyValueOfRotationMatrix(_attitudeMatrix);
    if (!_attitudeMatrix.isValid())
    {
      return false;
    }
  
    Geodetic3D camPosition = nextCamera.getGeodeticPosition();
  
    //Getting interface orientation
    InterfaceOrientation ori = IDeviceAttitude.instance().getCurrentInterfaceOrientation();
  
    //Getting Attitude Matrix
    CoordinateSystem camCS = IDeviceAttitude.instance().getCameraCoordinateSystemForInterfaceOrientation(ori);
  
    //Transforming global rotation to local rotation
    CoordinateSystem local = planet.getCoordinateSystemAt(camPosition);
    local.copyValueOfRotationMatrix(_localRM);
    _camRM.copyValueOfMultiplication(_localRM, _attitudeMatrix);
  
    //Applying to Camera CS
    CoordinateSystem finalCS = camCS.applyRotation(_camRM);
    nextCamera.setCameraCoordinateSystem(finalCS);
  
    if (_updateLocation)
    {
  
      if (_timer == null)
      {
        _timer = IFactory.instance().createTimer();
      }
  
      long t = _timer.nowInMilliseconds();
  
      if ((t - _lastLocationUpdateTimeInMS > 5000) || (_lastLocationUpdateTimeInMS == 0))
      {
  
  
        IDeviceLocation loc = IDeviceLocation.instance();
        if (!loc.isTrackingLocation())
        {
          loc.startTrackingLocation();
        }
  
        Geodetic3D g = loc.getLocation();
        if (!g.isNan())
        {
          _lastLocationUpdateTimeInMS = t;
  
          nextCamera.setGeodeticPosition(Geodetic3D.fromDegrees(g._latitude._degrees, g._longitude._degrees, g._height + _heightOffset));
        }
      }
  
    }
  
    return true;
  }
}