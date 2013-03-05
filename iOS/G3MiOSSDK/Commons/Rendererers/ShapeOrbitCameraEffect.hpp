//
//  ShapeOrbitCameraEffect.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/31/13.
//
//

#ifndef __G3MiOSSDK__ShapeOrbitCameraEffect__
#define __G3MiOSSDK__ShapeOrbitCameraEffect__

#include "Effects.hpp"
#include "Angle.hpp"

class Shape;

class ShapeOrbitCameraEffect : public EffectWithDuration {
private:
  Shape* _shape;

  const double _fromDistance;
  const double _toDistance;

  const double _fromAzimuthInRadians;
  const double _toAzimuthInRadians;

  const double _fromAltitudeInRadians;
  const double _toAltitudeInRadians;

public:

  ShapeOrbitCameraEffect(const TimeInterval& duration,
                         Shape* shape,
                         double fromDistance,       double toDistance,
                         const Angle& fromAzimuth,  const Angle& toAzimuth,
                         const Angle& fromAltitude, const Angle& toAltitude,
                         const bool linearTiming=false) :
  EffectWithDuration(duration, linearTiming),
  _shape(shape),
  _fromDistance(fromDistance),
  _toDistance(toDistance),
  _fromAzimuthInRadians(fromAzimuth.radians()),
  _toAzimuthInRadians(toAzimuth.radians()),
  _fromAltitudeInRadians(fromAltitude.radians()),
  _toAltitudeInRadians(toAltitude.radians())
  {

  }

  void doStep(const G3MRenderContext *rc,
              const TimeInterval& when);

  void cancel(const TimeInterval& when);

  void stop(const G3MRenderContext *rc,
            const TimeInterval& when);

};

#endif
