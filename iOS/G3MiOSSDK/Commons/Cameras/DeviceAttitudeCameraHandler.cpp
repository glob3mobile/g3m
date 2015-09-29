//
//  DeviceAttitudeCameraHandler.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 1/9/15.
//
//

#include "DeviceAttitudeCameraHandler.hpp"

#include "IDeviceAttitude.hpp"
#include "IDeviceLocation.hpp"
#include "IFactory.hpp"

DeviceAttitudeCameraHandler::DeviceAttitudeCameraHandler(bool updateLocation,
                                                         double heightOffset):
_updateLocation(updateLocation),
_heightOffset(heightOffset),
_lastLocationUpdateTimeInMS(0),
_timer(NULL)
{
  
}

DeviceAttitudeCameraHandler::~DeviceAttitudeCameraHandler(){
  IDeviceAttitude::instance()->stopTrackingDeviceOrientation();
  IDeviceLocation::instance()->stopTrackingLocation();
  
  delete _timer;
}


void DeviceAttitudeCameraHandler::render(const G3MRenderContext* rc, CameraContext *cameraContext){
  
  
  
  IDeviceAttitude* devAtt = IDeviceAttitude::instance();
  
  Camera* nextCamera = rc->getNextCamera();
  
  if (devAtt == NULL){
    ILogger::instance()->logError("IDeviceAttitude not initilized");
    return;
  }
  
  if (!devAtt->isTracking()){
    devAtt->startTrackingDeviceOrientation();
  }
  
  //Getting Global Rotation
  IDeviceAttitude::instance()->copyValueOfRotationMatrix(_attitudeMatrix);
  if (!_attitudeMatrix.isValid()){
    return;
  }
  
  Geodetic3D camPosition = nextCamera->getGeodeticPosition();
  
  //Getting interface orientation
  InterfaceOrientation ori = IDeviceAttitude::instance()->getCurrentInterfaceOrientation();
  
  //Getting Attitude Matrix
  CoordinateSystem camCS = IDeviceAttitude::instance()->getCameraCoordinateSystemForInterfaceOrientation(ori);
  
  //Transforming global rotation to local rotation
  CoordinateSystem local = rc->getPlanet()->getCoordinateSystemAt(camPosition);
  local.copyValueOfRotationMatrix(_localRM);
  _camRM.copyValueOfMultiplication(_localRM, _attitudeMatrix);
  
  //Applying to Camera CS
  CoordinateSystem finalCS = camCS.applyRotation(_camRM);
  nextCamera->setCameraCoordinateSystem(finalCS);
  
  if (_updateLocation){
    
    if (_timer == NULL){
      _timer = IFactory::instance()->createTimer();
    }
    
    long long t = _timer->nowInMilliseconds();
    
    if ((t - _lastLocationUpdateTimeInMS > 5000) || (_lastLocationUpdateTimeInMS == 0)){
      
      IDeviceLocation* loc = IDeviceLocation::instance();
      if (!loc->isTrackingLocation()){
        loc->startTrackingLocation();
      }
      
      Geodetic3D g = loc->getLocation();
      if (!g.isNan()){
        _lastLocationUpdateTimeInMS = t;
        
        if (nextCamera->hasValidViewDirection()){
          nextCamera->setGeodeticPosition(Geodetic3D::fromDegrees(g._latitude._degrees,
                                                                  g._longitude._degrees,
                                                                  g._height + _heightOffset));
        } else{
          ILogger::instance()->logWarning("Trying to set position of unvalid camera. ViewDirection: %s",
                                          nextCamera->getViewDirection().description().c_str());
        }
      }
    }
    
  }
}
