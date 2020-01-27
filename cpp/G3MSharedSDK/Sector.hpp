//
//  Sector.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/06/12.
//

#ifndef G3MiOSSDK_Sector
#define G3MiOSSDK_Sector

#include "Geodetic2D.hpp"

class Vector3D;
class Vector2D;
class Vector2F;
class G3MRenderContext;
class Planet;
class ICanvas;
class GEORasterProjection;
class GEORasterSymbol;
class Color;


class Sector {
private:
  // this lazy value represent the half diagonal of the sector, measured in radians
  // it's stored in double instead of Angle class to optimize performance in android
  // this value is only used in the method Sector::isBackOriented
  mutable double _deltaRadiusInRadians;

  mutable Vector3D* _normalizedCartesianCenter;

public:

  //Special instances
  static const Sector FULL_SPHERE;
  static const Sector NAN_SECTOR;


  const Geodetic2D _lower;
  const Geodetic2D _upper;

  const Geodetic2D _center;

  const Angle _deltaLatitude;
  const Angle _deltaLongitude;


  ~Sector();

  Sector(const Geodetic2D& lower,
         const Geodetic2D& upper);

  Sector(const Sector& sector);

  bool isNan() const;

  bool hasNoArea() const {
    return _deltaLatitude._radians == 0 || _deltaLongitude._radians == 0;
  }

  static Sector fromDegrees(double lowerLatitudeDegrees, double lowerLongitudeDegrees,
                            double upperLatitudeDegrees, double upperLongitudeDegrees) {
    return Sector(Geodetic2D::fromDegrees(lowerLatitudeDegrees, lowerLongitudeDegrees),
                  Geodetic2D::fromDegrees(upperLatitudeDegrees, upperLongitudeDegrees));
  }

  static Sector* newFromDegrees(double lowerLatitudeDegrees, double lowerLongitudeDegrees,
                                double upperLatitudeDegrees, double upperLongitudeDegrees) {
    return new Sector(Geodetic2D::fromDegrees(lowerLatitudeDegrees, lowerLongitudeDegrees),
                      Geodetic2D::fromDegrees(upperLatitudeDegrees, upperLongitudeDegrees));
  }

  static Sector fromRadians(double lowerLatitudeRadians, double lowerLongitudeRadians,
                            double upperLatitudeRadians, double upperLongitudeRadians) {
    return Sector(Geodetic2D::fromRadians(lowerLatitudeRadians, lowerLongitudeRadians),
                  Geodetic2D::fromRadians(upperLatitudeRadians, upperLongitudeRadians));
  }

  static Sector* newFromRadians(double lowerLatitudeRadians, double lowerLongitudeRadians,
                                double upperLatitudeRadians, double upperLongitudeRadians) {
    return new Sector(Geodetic2D::fromRadians(lowerLatitudeRadians, lowerLongitudeRadians),
                      Geodetic2D::fromRadians(upperLatitudeRadians, upperLongitudeRadians));
  }

  const Vector2D div(const Sector& that) const;

  bool fullContains(const Sector& that) const;

  Sector intersection(const Sector& that) const;

  Sector mergedWith(const Sector& that) const;

  static Sector fullSphere();

  bool contains(const Angle& latitude,
                const Angle& longitude) const;

  bool contains(const Geodetic2D& position) const {
    return contains(position._latitude,
                    position._longitude);
  }

  bool touchesWith(const Sector& that) const;

  const Geodetic2D getSW() const {
    return _lower;
  }

  const Geodetic2D getNE() const {
    return _upper;
  }

  const Geodetic2D getNW() const {
    return Geodetic2D(_upper._latitude,
                      _lower._longitude);
  }

  const Geodetic2D getSE() const {
    return Geodetic2D(_lower._latitude,
                      _upper._longitude);
  }

  const Geodetic2D getCenter() const {
    return _center;
  }

  // (u,v) are similar to texture coordinates inside the Sector
  // (u,v)=(0,0) in NW point, and (1,1) in SE point
  const Geodetic2D getInnerPoint(double u, double v) const;

  const Angle getInnerPointLongitude(double u) const;
  const Angle getInnerPointLatitude(double v) const;


  const Vector2D getUVCoordinates(const Geodetic2D& point) const;

  Vector2D getUVCoordinates(const Angle& latitude,
                            const Angle& longitude) const;

  Vector2F getUVCoordinatesF(const Angle& latitude,
                             const Angle& longitude) const;

  double getUCoordinate(const Angle& longitude) const {
    return (longitude._radians - _lower._longitude._radians) / _deltaLongitude._radians;
  }

  double getVCoordinate(const Angle& latitude) const {
    return (_upper._latitude._radians - latitude._radians)   / _deltaLatitude._radians;
  }

  bool isBackOriented(const G3MRenderContext* rc,
                      double minHeight,
                      const Planet* planet,
                      const Vector3D& cameraNormalizedPosition,
                      double cameraAngle2HorizonInRadians) const;

  const Geodetic2D clamp(const Geodetic2D& pos) const;

  const Geodetic2D clamp(const Angle& latitude,
                         const Angle& longitude) const;

  const Angle clampLatitude(const Angle& latitude) const;
  const Angle clampLongitude(const Angle& longitude) const;

  const std::string id() const;
  const std::string description() const;
#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

  Sector* shrinkedByPercentP(float percent) const {
    const Angle deltaLatitude  = _deltaLatitude.times(percent).div(2);
    const Angle deltaLongitude = _deltaLongitude.times(percent).div(2);

    const Geodetic2D delta(deltaLatitude, deltaLongitude);

    return new Sector(_lower.add( delta ),
                      _upper.sub( delta ) );
  }

  Sector shrinkedByPercent(float percent) const {
    const Angle deltaLatitude  = _deltaLatitude.times(percent).div(2);
    const Angle deltaLongitude = _deltaLongitude.times(percent).div(2);

    const Geodetic2D delta(deltaLatitude, deltaLongitude);

    return Sector(_lower.add( delta ),
                  _upper.sub( delta ) );
  }

  bool isEquals(const Sector& that) const {
    return _lower.isEquals(that._lower) && _upper.isEquals(that._upper);
  }

  bool touchesNorthPole() const {
    return (_upper._latitude._degrees >= 89.9);
  }

  bool touchesSouthPole() const {
    return (_lower._latitude._degrees <= -89.9);
  }

  Angle getNorth() const {
    return _upper._latitude;
  }

  Angle getSouth() const {
    return _lower._latitude;
  }

  Angle getEast() const {
    return _upper._longitude;
  }

  Angle getWest() const {
    return _lower._longitude;
  }

  void rasterize(ICanvas*                   canvas,
                 const GEORasterProjection* projection) const;

  bool touchesPoles() const {
    return ((_upper._latitude._degrees >=  89.9) ||
            (_lower._latitude._degrees <= -89.9));
  }

  double getDeltaRadiusInRadians() const;

  const Vector3D getNormalizedCartesianCenter(const Planet* planet) const;

  const double getAngularAreaInSquaredDegrees() const {
    return _deltaLatitude._degrees * _deltaLongitude._degrees;
  }

  const GEORasterSymbol* createGEOSymbol(const Color& c) const;

  Geodetic2D getClosesInnerPoint(const Geodetic2D& g) const;

#ifdef JAVA_CODE
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((_lower == null) ? 0 : _lower.hashCode());
    result = (prime * result) + ((_upper == null) ? 0 : _upper.hashCode());
    return result;
  }


  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Sector other = (Sector) obj;
    if (_lower == null) {
      if (other._lower != null) {
        return false;
      }
    }
    else if (!_lower.equals(other._lower)) {
      return false;
    }
    if (_upper == null) {
      if (other._upper != null) {
        return false;
      }
    }
    else if (!_upper.equals(other._upper)) {
      return false;
    }
    return true;
  }
#endif
  
};

#endif
