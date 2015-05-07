//
//  DeviceAttitudeCameraConstrainer.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 7/5/15.
//
//

#include "DeviceAttitudeCameraConstrainer.hpp"

#include "IDeviceAttitude.hpp"


DeviceAttitudeCameraConstrainer::~DeviceAttitudeCameraConstrainer(){
  IDeviceAttitude::instance()->stopTrackingDeviceOrientation();
}

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
  IDeviceAttitude::instance()->copyValueOfRotationMatrix(_attitudeMatrix);
  
  //Transforming global rotation to local rotation
  CoordinateSystem local = planet->getCoordinateSystemAt(camPosition);
  local.copyValueOfRotationMatrix(_localRM);
  
  MutableMatrix44D reorientation = MutableMatrix44D::createGeneralRotationMatrix(Angle::halfPi, local._z, local._origin);
  
  reorientation.copyValueOfMultiplication(reorientation, _localRM);
  reorientation.copyValueOfMultiplication(reorientation, _attitudeMatrix);
  
  //Applying to Camera CS
  CoordinateSystem finalCS = camCS.applyRotation(reorientation);
  nextCamera->setCameraCoordinateSystem(finalCS);
  
  return true;
}
