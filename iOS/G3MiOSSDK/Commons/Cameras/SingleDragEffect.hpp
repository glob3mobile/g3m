//
//  SingleDragEffect.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 10/07/13.
//
//

#ifndef G3MiOSSDK_SingleDragEffect_h
#define G3MiOSSDK_SingleDragEffect_h


class SingleDragEffect : public EffectWithForce {
public:
  
  SingleDragEffect(const Vector3D& axis,
                   const Angle& angle):
  EffectWithForce(angle._degrees, 0.975),
  _axis(axis)
  {
  }
  
  virtual void start(const G3MRenderContext *rc,
                     const TimeInterval& when) {
  }
  
  virtual void doStep(const G3MRenderContext *rc,
                      const TimeInterval& when) {
    EffectWithForce::doStep(rc, when);
    rc->getNextCamera()->rotateWithAxis(_axis, Angle::fromDegrees(getForce()));
  }
  
  virtual void stop(const G3MRenderContext *rc,
                    const TimeInterval& when) {
    rc->getNextCamera()->rotateWithAxis(_axis, Angle::fromDegrees(getForce()));
  }
  
  virtual void cancel(const TimeInterval& when) {
    // do nothing, just leave the effect in the intermediate state
  }
  
private:
  Vector3D _axis;
};


#endif
