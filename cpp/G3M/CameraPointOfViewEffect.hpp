//
//  CameraPointOfViewEffect.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 9/30/26.
//

#ifndef G3M_CameraPointOfViewEffect
#define G3M_CameraPointOfViewEffect

#include "Effects.hpp"

#include "Geodetic3D.hpp"
#include "Angle.hpp"

class CameraFlightArc;
class Planet;


// Moves the camera around a target that stays at the center of the viewport (see Camera::setPointOfView)
class CameraPointOfViewEffect : public EffectWithDuration {
private:
  const Geodetic3D _fromTarget;
  const Geodetic3D _toTarget;

  const double _fromDistance;
  const double _toDistance;

  const Angle _fromAzimuth;
  const Angle _toAzimuth;

  const Angle _fromAltitude;
  const Angle _toAltitude;

  const bool       _linearDistance;
  CameraFlightArc* _arc;

  Geodetic3D targetAt(const Planet* planet,
                      const double alpha) const;
  double     distanceAt(const double alpha) const;

public:

  CameraPointOfViewEffect(const TimeInterval& duration,
                          const Geodetic3D& fromTarget,
                          const Geodetic3D& toTarget,
                          const double fromDistance,
                          const double toDistance,
                          const Angle& fromAzimuth,
                          const Angle& toAzimuth,
                          const Angle& fromAltitude,
                          const Angle& toAltitude,
                          const bool linearTiming,
                          const bool linearDistance) :
  EffectWithDuration(duration, linearTiming),
  _fromTarget(fromTarget),
  _toTarget(toTarget),
  _fromDistance(fromDistance),
  _toDistance(toDistance),
  _fromAzimuth(fromAzimuth),
  _toAzimuth(toAzimuth),
  _fromAltitude(fromAltitude),
  _toAltitude(toAltitude),
  _linearDistance(linearDistance),
  _arc(NULL)
  {
  }

  ~CameraPointOfViewEffect();

  void start(const G3MRenderContext* rc,
             const TimeInterval& when);

  void doStep(const G3MRenderContext* rc,
              const TimeInterval& when);

  void stop(const G3MRenderContext* rc,
            const TimeInterval& when);

  void cancel(const TimeInterval& when) {
    // do nothing, just leave the effect in the intermediate state
  }

};

#endif
