//
//  DeviceAttitudeCameraConstrainer.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 7/5/15.
//
//

#include "DeviceAttitudeCameraConstrainer.hpp"

#include "IDeviceAttitude.hpp"

bool DeviceAttitudeCameraConstrainer::onCameraChange(const Planet* planet,
                    const Camera* previousCamera,
                    Camera* nextCamera) const{
  
  IDeviceAttitude* devAtt = IDeviceAttitude::instance();
  
  if (devAtt == NULL){
    ILogger::instance()->logError("IDeviceAttitude not initilized");
    return false;
  }
  
  if (!devAtt->isTracking()){
    devAtt->startTrackingDeviceOrientation();
  }

  Geodetic3D camPosition = nextCamera->getGeodeticPosition();
  
  //Getting interface orientation
  InterfaceOrientation ori = IDeviceAttitude::instance()->getCurrentInterfaceOrientation();
  
  //Getting Attitude Matrix
  CoordinateSystem camCS = IDeviceAttitude::instance()->getCameraCoordinateSystemForInterfaceOrientation(ori);
  
  //Getting Global Rotation
  MutableMatrix44D attitudeMatrix;
  IDeviceAttitude::instance()->copyValueOfRotationMatrix(attitudeMatrix);
  
  //Transforming global rotation to local rotation
  CoordinateSystem local = planet->getCoordinateSystemAt(camPosition);
  MutableMatrix44D localRM = local.getRotationMatrix();
  MutableMatrix44D reorientation = MutableMatrix44D::createGeneralRotationMatrix(Angle::fromDegrees(90), local._z, local._origin);
  MutableMatrix44D finalAttitude = reorientation.multiply(localRM).multiply(attitudeMatrix);
  
  //Applying to Camera CS
  CoordinateSystem finalCS = camCS.applyRotation(finalAttitude);
  nextCamera->setCameraCoordinateSystem(finalCS);
  
  return true;
}
