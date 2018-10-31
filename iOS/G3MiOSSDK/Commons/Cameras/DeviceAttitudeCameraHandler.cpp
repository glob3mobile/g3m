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
}

void DeviceAttitudeCameraHandler::setPositionOnNextCamera(Camera* nextCamera, Geodetic3D& pos) const{
    if (nextCamera->hasValidViewDirection()) {
        nextCamera->setGeodeticPosition(pos);
    } else{
        ILogger::instance()->logWarning("Trying to set position of unvalid camera. ViewDirection: %s",
                                        nextCamera->getViewDirection().description().c_str());
    }
}


void DeviceAttitudeCameraHandler::render(const G3MRenderContext* rc, CameraContext *cameraContext) {
    
    IDeviceAttitude* devAtt = IDeviceAttitude::instance();
    if (devAtt == NULL) {
        THROW_EXCEPTION("IDeviceAttitude not initilized");
    }
    
    if (!devAtt->isTracking()) {
        devAtt->startTrackingDeviceOrientation();
    }
    
    Camera* nextCamera = rc->getNextCamera();

    Geodetic3D* position = NULL;
    
    //Updating location
    if (_updateLocation) {
        
        IDeviceLocation* loc = IDeviceLocation::instance();
        
        bool isTracking = loc->isTrackingLocation();
        if (!isTracking) {
            isTracking = loc->startTrackingLocation();
        }
        
        if (isTracking) {
            Geodetic3D g = loc->getLocation();
            if (!g.isNan()) {
                //Changing current location
                if (_locationModifier == NULL) {
                    position = new Geodetic3D(g);
                } else{
                    Geodetic3D g2 = _locationModifier->modify(g);
                    if (!g2.isNan()){
                        position = new Geodetic3D(g2);
                    }
                }
            }
        }
    } else{
        position = new Geodetic3D(nextCamera->getGeodeticPosition());
    }
    
    //Getting Global Rotation
    IDeviceAttitude::instance()->copyValueOfRotationMatrix(_attitudeMatrix);
    if (!_attitudeMatrix.isValid() || position == NULL) {
        delete position;
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

    nextCamera->setCameraCoordinateSystem(finalCS.changeOrigin(rc->getPlanet()->toCartesian(*position)));
    delete position;
}
