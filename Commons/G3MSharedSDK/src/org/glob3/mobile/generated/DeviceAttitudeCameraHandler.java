package org.glob3.mobile.generated; 
public class DeviceAttitudeCameraHandler extends CameraEventHandler
{

  private MutableMatrix44D _localRM = new MutableMatrix44D();
  private MutableMatrix44D _attitudeMatrix = new MutableMatrix44D();
  private MutableMatrix44D _camRM = new MutableMatrix44D();

  private boolean _updateLocation;

  private ILocationModifier _locationModifier;

  private void setPositionOnNextCamera(Camera nextCamera, Geodetic3D pos)
  {
    if (nextCamera.hasValidViewDirection())
    {
      nextCamera.setGeodeticPosition(pos);
    }
    else
    {
      ILogger.instance().logWarning("Trying to set position of unvalid camera. ViewDirection: %s", nextCamera.getViewDirection().description());
    }
  }


  public DeviceAttitudeCameraHandler(boolean updateLocation)
  {
     this(updateLocation, null);
  }
  public DeviceAttitudeCameraHandler(boolean updateLocation, ILocationModifier locationModifier)
  {
     _updateLocation = updateLocation;
     _locationModifier = locationModifier;
  
  }

  public void dispose()
  {
    IDeviceAttitude.instance().stopTrackingDeviceOrientation();
    IDeviceLocation.instance().stopTrackingLocation();
  
    _locationModifier = null;
    super.dispose();
  }

  public final void render(G3MRenderContext rc, CameraContext cameraContext)
  {
    Camera nextCamera = rc.getNextCamera();
  
    //Updating location
    if (_updateLocation)
    {
      IDeviceLocation deviceLocation = IDeviceLocation.instance();
  
      boolean isTracking = deviceLocation.isTrackingLocation();
      if (!isTracking)
      {
        isTracking = deviceLocation.startTrackingLocation();
      }
  
      if (isTracking)
      {
        Geodetic3D g = deviceLocation.getLocation();
        if (!g.isNan())
        {
          //Changing current location
          if (_locationModifier == null)
          {
            setPositionOnNextCamera(nextCamera, g);
          }
          else
          {
            Geodetic3D g2 = _locationModifier.modify(g);
            setPositionOnNextCamera(nextCamera, g2);
          }
        }
      }
    }
  
    IDeviceAttitude deviceAttitude = IDeviceAttitude.instance();
    if (deviceAttitude == null)
    {
      //THROW_EXCEPTION("IDeviceAttitude not initilized");
      ILogger.instance().logError("IDeviceAttitude not initilized");
      return;
    }
  
    if (!deviceAttitude.isTracking())
    {
      deviceAttitude.startTrackingDeviceOrientation();
    }
  
    //Getting Global Rotation
    deviceAttitude.copyValueOfRotationMatrix(_attitudeMatrix);
    if (!_attitudeMatrix.isValid())
    {
      return;
    }
  
    Geodetic3D camPosition = nextCamera.getGeodeticPosition();
  
    //Getting interface orientation
    InterfaceOrientation ori = deviceAttitude.getCurrentInterfaceOrientation();
  
    //Getting Attitude Matrix
    CoordinateSystem camCS = deviceAttitude.getCameraCoordinateSystemForInterfaceOrientation(ori);
  
    //Transforming global rotation to local rotation
    CoordinateSystem local = rc.getPlanet().getCoordinateSystemAt(camPosition);
    local.copyValueOfRotationMatrix(_localRM);
    _camRM.copyValueOfMultiplication(_localRM, _attitudeMatrix);
  
    //Applying to Camera CS
    CoordinateSystem finalCS = camCS.applyRotation(_camRM);
    nextCamera.setCameraCoordinateSystem(finalCS);
  
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