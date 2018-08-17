//
//  ShapeScaleEffect.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/23/12.
//
//

#ifndef __G3MiOSSDK__ShapeScaleEffect__
#define __G3MiOSSDK__ShapeScaleEffect__

#include "Effects.hpp"

class Shape;

class ShapeScaleEffect : public EffectWithDuration {
private:
  Shape* _shape;

  double _fromScaleX;
  double _fromScaleY;
  double _fromScaleZ;

  double _toScaleX;
  double _toScaleY;
  double _toScaleZ;

public:
  ShapeScaleEffect(const TimeInterval& duration,
                   Shape* shape,
                   double fromScaleX, double fromScaleY, double fromScaleZ,
                   double toScaleX, double toScaleY, double toScaleZ,
                   const bool linearTiming=false) :
  EffectWithDuration(duration, linearTiming),
  _shape(shape),
  _fromScaleX(fromScaleX),
  _fromScaleY(fromScaleY),
  _fromScaleZ(fromScaleZ),
  _toScaleX(toScaleX),
  _toScaleY(toScaleY),
  _toScaleZ(toScaleZ)
  {

  }


  void doStep(const G3MRenderContext* rc,
              const TimeInterval& when);

  void cancel(const TimeInterval& when);

  void stop(const G3MRenderContext* rc,
            const TimeInterval& when);


};

#endif
