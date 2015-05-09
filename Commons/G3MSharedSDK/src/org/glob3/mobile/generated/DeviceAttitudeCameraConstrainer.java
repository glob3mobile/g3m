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




/**
 Class that applies the Rotation obtained with IDeviceAttitude and IInterfaceOrientation to the camera.
 
 It translate to the Global Coordinate System to the Local CS on the camera geodetic location.
 
 **/

public class DeviceAttitudeCameraConstrainer implements ICameraConstrainer
{

  private MutableMatrix44D _localRM = new MutableMatrix44D();
  private MutableMatrix44D _attitudeMatrix = new MutableMatrix44D();


  public DeviceAttitudeCameraConstrainer()
  {
  }

  public void dispose()
  {
    IDeviceAttitude.instance().stopTrackingDeviceOrientation();
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
  
    MutableMatrix44D reorientation = MutableMatrix44D.createGeneralRotationMatrix(Angle.halfPi, local._z, local._origin);
  
    reorientation.copyValueOfMultiplication(reorientation, _localRM);
    reorientation.copyValueOfMultiplication(reorientation, _attitudeMatrix);
  
    //Applying to Camera CS
    CoordinateSystem finalCS = camCS.applyRotation(reorientation);
    nextCamera.setCameraCoordinateSystem(finalCS);
  
    return true;
  }
}