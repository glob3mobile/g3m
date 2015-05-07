//
//  DeviceOrientationCameraConstrainer.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 7/5/15.
//
//

#include "DeviceOrientationCameraConstrainer.hpp"

bool DeviceOrientationCameraConstrainer::onCameraChange(const Planet* planet,
                    const Camera* previousCamera,
                    Camera* nextCamera) const{
  /*
  MutableMatrix44D deviceMatrix = IDeviceAttitude::instance()->createRotationMatrix();
  MutableMatrix44D cameraMatrix = IInterfaceOrientation::instance()->transformDeviceAttitudeToCameraAttitude(deviceMatrix);
  
  Camera* camera = _widget->getNextCamera();
  Geodetic3D camPosition = camera->getGeodeticPosition();
  
  CoordinateSystem global = CoordinateSystem::global();
  CoordinateSystem local = planet->getCoordinateSystemAt(camPosition);
  MutableMatrix44D localRM = local.getRotationMatrix();
  
  CoordinateSystem unorientedFinal = global.applyRotation(localRM.multiply(cameraMatrix) ).changeOrigin(local._origin);
  
  Vector3D planetNormal = planet->geodeticSurfaceNormal(unorientedFinal._origin);
  
  CoordinateSystem final = unorientedFinal.applyRotation(MutableMatrix44D::createRotationMatrix(Angle::fromDegrees(90), planetNormal));
  
  if (!final.isConsistent()){
    ILogger::instance()->logError("Invalid device attitude, skipping.");
    return true;
  }
  
  CoordinateSystem camCS(final._z.times(-1), //ViewDirection
                         final._y,            //Up
                         final._origin);       //Origin
  
  _widget->getNextCamera()->setCameraCoordinateSystem(camCS);
  */
  return true;
}
