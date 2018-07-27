//
//  CameraOrbitFixedPositionEffect.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel S N on 27/07/2018.
//

#ifndef CameraOrbitFixedPositionEffect_hpp
#define CameraOrbitFixedPositionEffect_hpp


#include "Effects.hpp"
#include "Angle.hpp"
#include "Geodetic3D.hpp"

class Shape;

class CameraOrbitFixedPositionEffect : public EffectWithDuration {
private:
    Geodetic3D _pivot;
    
    const double _fromDistance;
    const double _toDistance;
    
    const double _fromAzimuthInRadians;
    const double _toAzimuthInRadians;
    
    const double _fromAltitudeInRadians;
    const double _toAltitudeInRadians;
    
public:
    
    CameraOrbitFixedPositionEffect(const TimeInterval& duration,
                           const Geodetic3D& pivot,
                           double fromDistance,       double toDistance,
                           const Angle& fromAzimuth,  const Angle& toAzimuth,
                           const Angle& fromAltitude, const Angle& toAltitude,
                           const bool linearTiming=false) :
    EffectWithDuration(duration, linearTiming),
    _pivot(pivot),
    _fromDistance(fromDistance),
    _toDistance(toDistance),
    _fromAzimuthInRadians(fromAzimuth._radians),
    _toAzimuthInRadians(toAzimuth._radians),
    _fromAltitudeInRadians(fromAltitude._radians),
    _toAltitudeInRadians(toAltitude._radians)
    {
        
    }
    
    void doStep(const G3MRenderContext* rc,
                const TimeInterval& when);
    
    void cancel(const TimeInterval& when);
    
    void stop(const G3MRenderContext* rc,
              const TimeInterval& when);
    
};

#endif /* CameraOrbitFixedPositionEffect_hpp */
