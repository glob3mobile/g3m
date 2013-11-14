//
//  ShapePositionEffect.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/1/13.
//
//

#ifndef __G3MiOSSDK__ShapePositionEffect__
#define __G3MiOSSDK__ShapePositionEffect__

#include "Effects.hpp"
#include "Geodetic3D.hpp"

class Shape;

class ShapePositionEffect : public EffectWithDuration {
private:
  Shape* _shape;

  const Geodetic3D _fromPosition;
  const Geodetic3D _toPosition;

public:
  ShapePositionEffect(const TimeInterval& duration,
                      Shape* shape,
                      const Geodetic3D& fromPosition,
                      const Geodetic3D& toPosition,
                      const bool linearTiming=false) :
  EffectWithDuration(duration, linearTiming),
  _shape(shape),
  _fromPosition(fromPosition),
  _toPosition(toPosition)
  {

  }

  void doStep(const G3MRenderContext* rc,
              const TimeInterval& when);

  void cancel(const TimeInterval& when);

  void stop(const G3MRenderContext* rc,
            const TimeInterval& when);
  
};

#endif
