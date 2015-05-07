//
//  IDeviceAttitude.cpp
//  G3MiOSDemo
//
//  Created by Jose Miguel SN on 30/4/15.
//
//

#include "IDeviceAttitude.hpp"

IDeviceAttitude* IDeviceAttitude::_instance;


IDeviceAttitude::IDeviceAttitude():
_camCSPortrait(Vector3D(1,0,0), //X
               Vector3D(0,0,-1), //Y -> View Direction
               Vector3D(0,1,0), //Z -> Up
               Vector3D::zero),

_camCSPortraitUD(Vector3D(1,0,0), //X
                 Vector3D(0,0,-1), //Y -> View Direction
                 Vector3D(0,-1,0), //Z -> Up
                 Vector3D::zero),

_camCSLL(Vector3D(0,1,0), //X
         Vector3D(0,0,-1), //Y -> View Direction
         Vector3D(-1,0,0), //Z -> Up
         Vector3D::zero),

_camCSLR(Vector3D(0,1,0), //X
         Vector3D(0,0,-1), //Y -> View Direction
         Vector3D(1,0,0), //Z -> Up
         Vector3D::zero){
  
}

void IDeviceAttitude::setInstance(IDeviceAttitude* deviceAttitude) {
  if (_instance != NULL) {
    ILogger::instance()->logWarning("ILooger instance already set!");
    delete _instance;
  }
  _instance = deviceAttitude;
}

IDeviceAttitude* IDeviceAttitude::instance(){
  return _instance;
}


CoordinateSystem IDeviceAttitude::getCameraCoordinateSystemForInterfaceOrientation(InterfaceOrientation orientation) const{
  
  switch (orientation) {
    case PORTRAIT:{
      return _camCSPortrait;
    }
      
    case PORTRAIT_UPSIDEDOWN:{
      return _camCSPortraitUD;
    }
      
    case LANDSCAPE_LEFT:{
      return _camCSLL;
    }
      
    case LANDSCAPE_RIGHT:{
      return _camCSLR;
    }
      
    default:{
      //Landscape right
      return _camCSLR;
    }
  }
}
