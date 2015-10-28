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
                                                         ILocationModifier* locationModifier):
_updateLocation(updateLocation),
_locationModifier(locationModifier)
{
  
}

DeviceAttitudeCameraHandler::~DeviceAttitudeCameraHandler(){
  IDeviceAttitude::instance()->stopTrackingDeviceOrientation();
  IDeviceLocation::instance()->stopTrackingLocation();

  delete _locationModifier;
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
  
  
  //Updating location
  if (_updateLocation){
    
    IDeviceLocation* loc = IDeviceLocation::instance();
    if (!loc->isTrackingLocation()){
      loc->startTrackingLocation();
    }
    
    Geodetic3D g = loc->getLocation();
    if (!g.isNan()){
      //Changing current location
      double lat = g._latitude._degrees;
      double lon = g._longitude._degrees;
      double height = g._height;
      if (_locationModifier != NULL){
        Geodetic3D g2 = _locationModifier->modify(g);
        lat = g2._latitude._degrees;
        lon = g2._latitude._degrees;
        height = g2._height;
      }
      
      Geodetic3D modG = Geodetic3D::fromDegrees(lat, lon, height);
      
      if (nextCamera->hasValidViewDirection()){
        nextCamera->setGeodeticPosition(Geodetic3D::fromDegrees(modG._latitude._degrees,
                                                                modG._longitude._degrees,
                                                                modG._height));
      } else{
        ILogger::instance()->logWarning("Trying to set position of unvalid camera. ViewDirection: %s",
                                        nextCamera->getViewDirection().description().c_str());
      }
    }
  }
  
}
