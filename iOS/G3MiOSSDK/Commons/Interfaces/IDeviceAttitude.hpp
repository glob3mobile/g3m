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
#include "CoordinateSystem.hpp"

enum InterfaceOrientation{
  PORTRAIT,
  PORTRAIT_UPSIDEDOWN,
  LANDSCAPE_RIGHT,
  LANDSCAPE_LEFT
};

class IDeviceAttitude{
private:
  static IDeviceAttitude* _instance;
public:
  
  IDeviceAttitude();
  
  static void setInstance(IDeviceAttitude* deviceAttitude);
  
  //Singleton
  static IDeviceAttitude* instance();
  
  virtual ~IDeviceAttitude(){}
  
  /**
   Must be called before any other operation
   **/
  
  virtual void startTrackingDeviceOrientation() const = 0;
  
  /**
   Must be called to stop operations
   **/
  
  virtual void stopTrackingDeviceOrientation() const = 0;
  
  virtual bool isTracking() const = 0;

  virtual void copyValueOfRotationMatrix(MutableMatrix44D& rotationMatrix) const = 0;
  
  virtual InterfaceOrientation getCurrentInterfaceOrientation() const = 0;
  
  virtual CoordinateSystem getCameraCoordinateSystemForInterfaceOrientation(InterfaceOrientation orientation) const = 0;
  
};


#endif /* defined(__G3MiOSDemo__IDeviceAttitude__) */
