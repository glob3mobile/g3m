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



public class DeviceAttitudeCameraHandler extends CameraEventHandler
{

  private MutableMatrix44D _localRM = new MutableMatrix44D();
  private MutableMatrix44D _attitudeMatrix = new MutableMatrix44D();
  private MutableMatrix44D _camRM = new MutableMatrix44D();

  private boolean _updateLocation;
  private double _heightOffset;
  private long _lastLocationUpdateTimeInMS;
  private ITimer _timer;


  public DeviceAttitudeCameraHandler(boolean updateLocation)
  {
     this(updateLocation, 1000);
  }
  public DeviceAttitudeCameraHandler(boolean updateLocation, double heightOffset)
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

  public final void render(G3MRenderContext rc, CameraContext cameraContext)
  {
  
  
  
    IDeviceAttitude devAtt = IDeviceAttitude.instance();
  
    Camera nextCamera = rc.getNextCamera();
  
    if (devAtt == null)
    {
      ILogger.instance().logError("IDeviceAttitude not initilized");
      return;
    }
  
    if (!devAtt.isTracking())
    {
      devAtt.startTrackingDeviceOrientation();
    }
  
    //Getting Global Rotation
    IDeviceAttitude.instance().copyValueOfRotationMatrix(_attitudeMatrix);
    if (!_attitudeMatrix.isValid())
    {
      return;
    }
  
    Geodetic3D camPosition = nextCamera.getGeodeticPosition();
  
    //Getting interface orientation
    InterfaceOrientation ori = IDeviceAttitude.instance().getCurrentInterfaceOrientation();
  
    //Getting Attitude Matrix
    CoordinateSystem camCS = IDeviceAttitude.instance().getCameraCoordinateSystemForInterfaceOrientation(ori);
  
    //Transforming global rotation to local rotation
    CoordinateSystem local = rc.getPlanet().getCoordinateSystemAt(camPosition);
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
  
          if (nextCamera.hasValidViewDirection())
          {
            nextCamera.setGeodeticPosition(Geodetic3D.fromDegrees(g._latitude._degrees, g._longitude._degrees, g._height + _heightOffset));
          }
          else
          {
            ILogger.instance().logWarning("Trying to set position of unvalid camera. ViewDirection: %s", nextCamera.getViewDirection().description());
          }
        }
      }
  
    }
  }

  public boolean onTouchEvent(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
     return false;
  }


  public void onDown(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
  }

  public void onMove(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
  }

  public void onUp(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
  }

  public final void setDebugMeshRenderer(MeshRenderer meshRenderer)
  {
  }

  public final void onMouseWheel(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
  }

}