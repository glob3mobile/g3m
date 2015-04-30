//
//  IDeviceAttitude.h
//  G3MiOSDemo
//
//  Created by Jose Miguel SN on 30/4/15.
//
//

#ifndef __G3MiOSDemo__IDeviceAttitude__
#define __G3MiOSDemo__IDeviceAttitude__

#include <stdio.h>

#include "MutableMatrix44D.hpp"
#include "ICameraConstrainer.hpp"
#include "G3MWidget.hpp"

enum InterfaceOrientation{
  PORTRAIT,
  UPSIDEDOWN_PORTRAIT,
  LANDSCAPE_RIGHT,
  LANDSCAPE_LEFT
};

/**
 Class that represents the Rotations of the UI on the device.
 **/

class IInterfaceOrientation{
private:
  IInterfaceOrientation* _instance;
public:
  
  //Singleton
  static IInterfaceOrientation* instance();
  
  InterfaceOrientation getCurrentInterfaceOrientation() const;
  
  /**
  Corrects the camera given by IDeviceAttitude::createRotationMatrix() taking into account the interface orientation:
  On iOS:
  UIInterfaceOrientation orientation = [[UIApplication sharedApplication] statusBarOrientation]
  On Android:
  Display.getRotation()
  On JS:
   $wnd.screen.mozOrientation //Mozilla
   $wnd.screen.orientation //Chrome
  **/
  MutableMatrix44D transformDeviceAttitudeToCameraAttitude(const MutableMatrix44D& m);
  
};

class IDeviceAttitude{
private:
  IDeviceAttitude* _instance;
public:
  
  //Singleton
  static IDeviceAttitude* instance();
  
  /**
   Must be called before any other operation
   **/
  
  virtual void startTrackingDeviceOrientation() const = 0;
  
  /**
   Must be called to stop operations
   **/
  
  virtual void stopTrackingDeviceOrientation() const = 0;
  
  
  /**
   Returns a matrix that represents the rotation of the device relative to the Global X,Y,Z coordinate system.
   
   For a smartphone-like device with the backside oriented to the north and the front-camera on top of the screen
   ("natural" position) should return the identity matrix.
   
   if startTrackingDeviceOrientation() has not been called it will return an invalid matrix.
   
   It has into account the interface orientation:
   On iOS:
   _mm.deviceMotion.attitude.rotationMatrix
   On Android:
   SensorManager.getRotationMatrix()
   On JS:
   
   **/
  
  virtual MutableMatrix44D createRotationMatrix() const = 0;
  
};

/**
 Class that applies the Rotation obtained with IDeviceAttitude and IInterfaceOrientation to the camera.
 
 It translate to the Global Coordinate System to the Local CS on the camera geodetic location.
 
 **/

class DeviceOrientationCameraConstrainer: public ICameraConstrainer{
  G3MWidget* _widget;
public:
  
  DeviceOrientationCameraConstrainer(G3MWidget* widget):_widget(widget){
    IDeviceAttitude::instance()->startTrackingDeviceOrientation();
  };
  
  bool onCameraChange(const Planet* planet,
                      const Camera* previousCamera,
                      Camera* nextCamera) const{
    
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
    
    return true;
  }
};

#endif /* defined(__G3MiOSDemo__IDeviceAttitude__) */
