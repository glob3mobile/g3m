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

DeviceAttitudeCameraHandler::~DeviceAttitudeCameraHandler() {
  IDeviceAttitude::instance()->stopTrackingDeviceOrientation();
  IDeviceLocation::instance()->stopTrackingLocation();

  delete _locationModifier;
#ifdef JAVA_CODE
  super.dispose();
#endif
}

void DeviceAttitudeCameraHandler::setPositionOnNextCamera(Camera* nextCamera, Geodetic3D& pos) const{
  if (nextCamera->hasValidViewDirection()) {
    nextCamera->setGeodeticPosition(pos);
  }
  else {
    ILogger::instance()->logWarning("Trying to set position of unvalid camera. ViewDirection: %s",
                                    nextCamera->getViewDirection().description().c_str());
  }
}


void DeviceAttitudeCameraHandler::render(const G3MRenderContext* rc,
                                         CameraContext *cameraContext) {
  Camera* nextCamera = rc->getNextCamera();

  //Updating location
  if (_updateLocation) {
    IDeviceLocation* deviceLocation = IDeviceLocation::instance();

    bool isTracking = deviceLocation->isTrackingLocation();
    if (!isTracking) {
      isTracking = deviceLocation->startTrackingLocation();
    }

    if (isTracking) {
      Geodetic3D g = deviceLocation->getLocation();
      if (!g.isNan()) {
        //Changing current location
        if (_locationModifier == NULL) {
          setPositionOnNextCamera(nextCamera, g);
        }
        else {
          Geodetic3D g2 = _locationModifier->modify(g);
          setPositionOnNextCamera(nextCamera, g2);
        }
      }
    }
  }

  IDeviceAttitude* deviceAttitude = IDeviceAttitude::instance();
  if (deviceAttitude == NULL) {
    //THROW_EXCEPTION("IDeviceAttitude not initilized");
    ILogger::instance()->logError("IDeviceAttitude not initilized");
    return;
  }

  if (!deviceAttitude->isTracking()) {
    deviceAttitude->startTrackingDeviceOrientation();
  }

  //Getting Global Rotation
  deviceAttitude->copyValueOfRotationMatrix(_attitudeMatrix);
  if (!_attitudeMatrix.isValid()) {
    return;
  }

  Geodetic3D camPosition = nextCamera->getGeodeticPosition();

  //Getting interface orientation
  InterfaceOrientation ori = deviceAttitude->getCurrentInterfaceOrientation();

  //Getting Attitude Matrix
  CoordinateSystem camCS = deviceAttitude->getCameraCoordinateSystemForInterfaceOrientation(ori);

  //Transforming global rotation to local rotation
  CoordinateSystem local = rc->getPlanet()->getCoordinateSystemAt(camPosition);
  local.copyValueOfRotationMatrix(_localRM);
  _camRM.copyValueOfMultiplication(_localRM, _attitudeMatrix);

  //Applying to Camera CS
  CoordinateSystem finalCS = camCS.applyRotation(_camRM);
  nextCamera->setCameraCoordinateSystem(finalCS);
  
}
