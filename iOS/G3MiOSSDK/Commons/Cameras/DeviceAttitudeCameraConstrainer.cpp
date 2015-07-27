//
//  DeviceAttitudeCameraConstrainer.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 7/5/15.
//
//

#include "DeviceAttitudeCameraConstrainer.hpp"

#include "IDeviceAttitude.hpp"

DeviceAttitudeCameraConstrainer::DeviceAttitudeCameraConstrainer(){
  
}

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
  
  //Getting Global Rotation
  IDeviceAttitude::instance()->copyValueOfRotationMatrix(_attitudeMatrix);
  if (!_attitudeMatrix.isValid()){
    return false;
  }

  Geodetic3D camPosition = nextCamera->getGeodeticPosition();
   
  //Getting interface orientation
  InterfaceOrientation ori = IDeviceAttitude::instance()->getCurrentInterfaceOrientation();
  
  //Getting Attitude Matrix
  CoordinateSystem camCS = IDeviceAttitude::instance()->getCameraCoordinateSystemForInterfaceOrientation(ori);
  
  //Transforming global rotation to local rotation
  CoordinateSystem local = planet->getCoordinateSystemAt(camPosition);
  local.copyValueOfRotationMatrix(_localRM);
  _camRM.copyValueOfMultiplication(_localRM, _attitudeMatrix);
  
  //Applying to Camera CS
  CoordinateSystem finalCS = camCS.applyRotation(_camRM);
  nextCamera->setCameraCoordinateSystem(finalCS);
  
  return true;
}
