//
//  CameraEffects.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 17/07/13.
//
//

#ifndef G3MiOSSDK_CameraEffects_h
#define G3MiOSSDK_CameraEffects_h


#include "Effects.hpp"
#include "Vector3D.hpp"

class Angle;



class RotateWithAxisEffect : public EffectWithForce {
public:
  
  RotateWithAxisEffect(const Vector3D& axis, const Angle& angle);
  
  virtual ~RotateWithAxisEffect() {};
  
  virtual void start(const G3MRenderContext *rc, const TimeInterval& when) {
  }
  
  virtual void doStep(const G3MRenderContext *rc, const TimeInterval& when);
  
  virtual void stop(const G3MRenderContext *rc, const TimeInterval& when);
  
  virtual void cancel(const TimeInterval& when) {
    // do nothing, just leave the effect in the intermediate state
  }
  
private:
  Vector3D _axis;
};



#endif
