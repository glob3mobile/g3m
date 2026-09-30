//
//  CameraGoToPositionEffect.hpp
//  G3M
//
//  Created by José Miguel S N on 24/10/12.
//

#ifndef G3M_CameraGoToPositionEffect
#define G3M_CameraGoToPositionEffect

#include "Effects.hpp"

#include "Geodetic3D.hpp"

class Planet;
class CameraFlightArc;


class CameraGoToPositionEffect : public EffectWithDuration {
private:
  const Geodetic3D _fromPosition;
  const Geodetic3D _toPosition;

  const Angle _fromHeading;
  const Angle _toHeading;

  const Angle _fromPitch;
  const Angle _toPitch;

  const bool       _linearHeight;
  CameraFlightArc* _arc;

  double           _planetRadius;
  double           _fromAngleBelowHorizonRadians;
  double           _toAngleBelowHorizonRadians;

  double horizonDepressionRadians(const double height) const;

  double angleBelowHorizonRadians(const Angle& pitch,
                                  const double height) const;

public:

  CameraGoToPositionEffect(const TimeInterval& duration,
                           const Geodetic3D& fromPosition,
                           const Geodetic3D& toPosition,
                           const Angle& fromHeading,
                           const Angle& toHeading,
                           const Angle& fromPitch,
                           const Angle& toPitch,
                           const bool linearTiming,
                           const bool linearHeight):
  EffectWithDuration(duration, linearTiming),
  _fromPosition(fromPosition),
  _toPosition(toPosition),
  _fromHeading(fromHeading),
  _toHeading(toHeading),
  _fromPitch(fromPitch),
  _toPitch(toPitch),
  _linearHeight(linearHeight),
  _arc(NULL),
  _planetRadius(0),
  _fromAngleBelowHorizonRadians(0),
  _toAngleBelowHorizonRadians(0)
  {
  }

  ~CameraGoToPositionEffect();

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
