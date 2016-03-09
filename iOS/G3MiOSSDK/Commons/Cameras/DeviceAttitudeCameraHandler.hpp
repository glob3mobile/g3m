//
//  DeviceAttitudeCameraHandler.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 1/9/15.
//
//

#ifndef __G3MiOSSDK__DeviceAttitudeCameraHandler__
#define __G3MiOSSDK__DeviceAttitudeCameraHandler__

#include "CameraEventHandler.hpp"
#include "MutableMatrix44D.hpp"
#include "ILocationModifier.hpp"
#include "MeshRenderer.hpp"



class HeightOffsetLocationModifier: public ILocationModifier{
  double _offsetInMeters;

public:
  
  HeightOffsetLocationModifier(double offsetInMeters):_offsetInMeters(offsetInMeters){}
  
  Geodetic3D modify(const Geodetic3D& location){
    return Geodetic3D::fromDegrees(location._latitude._degrees,
                                   location._longitude._degrees,
                                   location._height + _offsetInMeters);
  }
  
};

class DeviceAttitudeCameraHandler: public CameraEventHandler{
  
private:
  mutable MutableMatrix44D _localRM;
  mutable MutableMatrix44D _attitudeMatrix;
  mutable MutableMatrix44D _camRM;
  
  bool _updateLocation;
  
  ILocationModifier* _locationModifier;
  
  void setPositionOnNextCamera(Camera* nextCamera, Geodetic3D& pos) const;
  
public:
  
  DeviceAttitudeCameraHandler(bool updateLocation,
                              ILocationModifier* locationModifier = NULL);
  
  ~DeviceAttitudeCameraHandler();
  
  void render(const G3MRenderContext* rc, CameraContext *cameraContext);
  
  virtual bool onTouchEvent(const G3MEventContext *eventContext,
                            const TouchEvent* touchEvent,
                            CameraContext *cameraContext){ return false;}
  
  
  virtual void onDown(const G3MEventContext *eventContext,
                      const TouchEvent& touchEvent,
                      CameraContext *cameraContext){}
  
  virtual void onMove(const G3MEventContext *eventContext,
                      const TouchEvent& touchEvent,
                      CameraContext *cameraContext){}
  
  virtual void onUp(const G3MEventContext *eventContext,
                    const TouchEvent& touchEvent,
                    CameraContext *cameraContext){}
  
  void setDebugMeshRenderer(MeshRenderer* meshRenderer) {}
  
  void onMouseWheel(const G3MEventContext *eventContext,
                    const TouchEvent& touchEvent,
                    CameraContext *cameraContext){}
  
};

#endif /* defined(__G3MiOSSDK__DeviceAttitudeCameraHandler__) */
