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
    Camera* nextCamera = rc->getNextCamera();
    
    
    Geodetic3D* position;
    
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
//                    setPositionOnNextCamera(nextCamera, g);
                    position = new Geodetic3D(g);
                } else{
                    Geodetic3D g2 = _locationModifier->modify(g);
                    if (!g2.isNan()){
//                        setPositionOnNextCamera(nextCamera, g2);
                        position = new Geodetic3D(g2);
                    }
                }
            }
        }
    } else{
        position = new Geodetic3D(nextCamera->getGeodeticPosition());
    }
    
    
    
    if (devAtt == NULL) {
        THROW_EXCEPTION("IDeviceAttitude not initilized");
    }
    
    if (!devAtt->isTracking()) {
        devAtt->startTrackingDeviceOrientation();
    }
    
    //Getting Global Rotation
    IDeviceAttitude::instance()->copyValueOfRotationMatrix(_attitudeMatrix);
    if (!_attitudeMatrix.isValid()) {
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
    
    
//    Vector3D l = finalCS._x;
//    Vector3D f = finalCS._y;
//    Vector3D up = finalCS._z;
//    printf("FORWARD: %.2f, %.2f, %.2f\n", f._x, f._y, f._z);
//    printf("UP: %.2f, %.2f, %.2f\n", up._x, up._y, up._z);
//    printf("LEFT: %.2f, %.2f, %.2f\n", l._x, l._y, l._z);
    
//    if (f._z < 0){
//        nextCamera->setCameraCoordinateSystem(finalCS);
//    } else{
//#warning TODO: QUICK FIX - RESEARCH NEED FOR THIS HACK
//        CoordinateSystem finalCS2(finalCS._x.times(-1),
//                                  finalCS._y.times(-1),
//                                  finalCS._z,
//                                  finalCS._origin);
//
//        nextCamera->setCameraCoordinateSystem(finalCS2);
//    }
    if (position != NULL){
        nextCamera->setCameraCoordinateSystem(finalCS.changeOrigin(rc->getPlanet()->toCartesian(*position)));
        delete position;
    }
    
}
