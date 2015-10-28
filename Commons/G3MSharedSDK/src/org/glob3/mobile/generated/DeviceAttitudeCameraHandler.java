package org.glob3.mobile.generated; 
public class DeviceAttitudeCameraHandler extends CameraEventHandler
{

  private MutableMatrix44D _localRM = new MutableMatrix44D();
  private MutableMatrix44D _attitudeMatrix = new MutableMatrix44D();
  private MutableMatrix44D _camRM = new MutableMatrix44D();

  private boolean _updateLocation;

  private ILocationModifier _locationModifier;


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
  
  
    //Updating location
    if (_updateLocation)
    {
  
      IDeviceLocation loc = IDeviceLocation.instance();
      if (!loc.isTrackingLocation())
      {
        loc.startTrackingLocation();
      }
  
      Geodetic3D g = loc.getLocation();
      if (!g.isNan())
      {
        //Changing current location
        double lat = g._latitude._degrees;
        double lon = g._longitude._degrees;
        double height = g._height;
        if (_locationModifier != null)
        {
          Geodetic3D g2 = _locationModifier.modify(g);
          lat = g2._latitude._degrees;
          lon = g2._latitude._degrees;
          height = g2._height;
        }
  
        Geodetic3D modG = Geodetic3D.fromDegrees(lat, lon, height);
  
        if (nextCamera.hasValidViewDirection())
        {
          nextCamera.setGeodeticPosition(Geodetic3D.fromDegrees(modG._latitude._degrees, modG._longitude._degrees, modG._height));
        }
        else
        {
          ILogger.instance().logWarning("Trying to set position of unvalid camera. ViewDirection: %s", nextCamera.getViewDirection().description());
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