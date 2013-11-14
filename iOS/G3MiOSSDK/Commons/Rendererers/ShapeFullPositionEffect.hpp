//
//  ShapeFullPositionEffect.hpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso Pérez on 03/05/13.
//
//

#ifndef __G3MiOSSDK__ShapeFullPositionEffect__
#define __G3MiOSSDK__ShapeFullPositionEffect__

#include "Effects.hpp"
#include "Geodetic3D.hpp"

class Shape;

class ShapeFullPositionEffect : public EffectWithDuration {
private:
  Shape* _shape;
  
  const Geodetic3D _fromPosition;
  const Geodetic3D _toPosition;

  const Angle _fromPitch;
  const Angle _toPitch;

  const Angle _fromHeading;
  const Angle _toHeading;

  const Angle _fromRoll;
  const Angle _toRoll;


public:
  ShapeFullPositionEffect(const TimeInterval& duration,
                          Shape* shape,
                          const Geodetic3D& fromPosition,
                          const Geodetic3D& toPosition,
                          const Angle& fromPitch,
                          const Angle& toPitch,
                          const Angle& fromHeading,
                          const Angle& toHeading,
                          const Angle& fromRoll,
                          const Angle& toRoll,
                          const bool linearTiming=false) :
  EffectWithDuration(duration, linearTiming),
  _shape(shape),
  _fromPosition(fromPosition),
  _toPosition(toPosition),
  _fromPitch(fromPitch),
  _toPitch(toPitch),
  _fromHeading(fromHeading),
  _toHeading(toHeading),
  _fromRoll(fromRoll),
  _toRoll(toRoll)
  {
    
  }
  
  void doStep(const G3MRenderContext* rc,
              const TimeInterval& when);
  
  void cancel(const TimeInterval& when);
  void stop(const G3MRenderContext* rc,
            const TimeInterval& when);
  
};

#endif
