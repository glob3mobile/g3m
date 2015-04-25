//
//  FlatPlanet.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 10/07/13.
//
//

#ifndef G3MiOSSDK_FlatPlanet
#define G3MiOSSDK_FlatPlanet

#include "MutableVector3D.hpp"
#include "Geodetic3D.hpp"
#include "Planet.hpp"
#include "Vector2D.hpp"
#include "Sector.hpp"


class FlatPlanet: public Planet {
private:
  const Vector2D _size;
  const Vector3D _radii;

  mutable MutableVector3D _origin;
  mutable MutableVector3D _initialPoint;
  mutable MutableVector3D _lastFinalPoint;
  mutable bool            _validSingleDrag;
  mutable MutableVector3D _lastDirection;

  mutable MutableVector3D _centerRay;
  mutable MutableVector3D _initialPoint0;
  mutable MutableVector3D _initialPoint1;
  mutable double          _distanceBetweenInitialPoints;
  mutable MutableVector3D _centerPoint;
  //  mutable double          _angleBetweenInitialRays;



public:

  FlatPlanet(const Vector2D& size);

  ~FlatPlanet() {

  }

  Vector3D getRadii() const {
    return _radii;
  }

  Vector3D centricSurfaceNormal(const Vector3D& position) const {
    return Vector3D(0, 0, 1);
  }

  Vector3D geodeticSurfaceNormal(const Vector3D& position) const {
    return Vector3D(0, 0, 1);
  }

  Vector3D geodeticSurfaceNormal(const MutableVector3D& position) const {
    return Vector3D(0, 0, 1);
  }

  void geodeticSurfaceNormal(const Angle& latitude,
                             const Angle& longitude,
                             MutableVector3D& result) const {
    result.set(0, 0, 1);
  }

  Vector3D geodeticSurfaceNormal(const Angle& latitude,
                                 const Angle& longitude) const;

  Vector3D geodeticSurfaceNormal(const Geodetic3D& geodetic) const {
    return Vector3D(0, 0, 1);
  }

  Vector3D geodeticSurfaceNormal(const Geodetic2D& geodetic) const {
    return Vector3D(0, 0, 1);
  }

  std::vector<double> intersectionsDistances(double originX,
                                             double originY,
                                             double originZ,
                                             double directionX,
                                             double directionY,
                                             double directionZ) const;

  Vector3D toCartesian(const Angle& latitude,
                       const Angle& longitude,
                       const double height) const {
    const double x = longitude._degrees * _size._x / 360.0;
    const double y = latitude._degrees  * _size._y / 180.0;
    return Vector3D(x, y, height);
  }

  Vector3D toCartesian(const Geodetic3D& geodetic) const {
    return toCartesian(geodetic._latitude,
                       geodetic._longitude,
                       geodetic._height);
  }

  Vector3D toCartesian(const Geodetic2D& geodetic) const {
    return toCartesian(geodetic._latitude,
                       geodetic._longitude,
                       0.0);
  }

  Vector3D toCartesian(const Geodetic2D& geodetic,
                       const double height) const {
    return toCartesian(geodetic._latitude,
                       geodetic._longitude,
                       height);
  }

  void toCartesian(const Angle& latitude,
                   const Angle& longitude,
                   const double height,
                   MutableVector3D& result) const {
    const double x = longitude._degrees * _size._x / 360.0;
    const double y = latitude._degrees  * _size._y / 180.0;
    result.set(x, y, height);
  }

  void toCartesian(const Geodetic3D& geodetic,
                   MutableVector3D& result) const {
    toCartesian(geodetic._latitude,
                geodetic._longitude,
                geodetic._height,
                result);
  }
  void toCartesian(const Geodetic2D& geodetic,
                   MutableVector3D& result) const {
    toCartesian(geodetic._latitude,
                geodetic._longitude,
                0.0,
                result);
  }

  void toCartesian(const Geodetic2D& geodetic,
                   const double height,
                   MutableVector3D& result) const {
    toCartesian(geodetic._latitude,
                geodetic._longitude,
                height,
                result);
  }

  Geodetic2D toGeodetic2D(const Vector3D& position) const;

  Geodetic3D toGeodetic3D(const Vector3D& position) const;

  Vector3D scaleToGeodeticSurface(const Vector3D& position) const;

  Vector3D scaleToGeocentricSurface(const Vector3D& position) const;

  Geodetic2D getMidPoint (const Geodetic2D& P0, const Geodetic2D& P1) const;


  double computePreciseLatLonDistance(const Geodetic2D& g1,
                                      const Geodetic2D& g2) const;

  double computeFastLatLonDistance(const Geodetic2D& g1,
                                   const Geodetic2D& g2) const;

  MutableMatrix44D createGeodeticTransformMatrix(const Geodetic3D& position) const;

  bool isFlat() const { return true; }

  void beginSingleDrag(const Vector3D& origin, const Vector3D& initialRay) const;

  MutableMatrix44D singleDrag(const Vector3D& finalRay) const;

  Effect* createEffectFromLastSingleDrag() const;

  void beginDoubleDrag(const Vector3D& origin,
                       const Vector3D& centerRay,
                       const Vector3D& initialRay0,
                       const Vector3D& initialRay1) const;

  MutableMatrix44D doubleDrag(const Vector3D& finalRay0,
                              const Vector3D& finalRay1) const;

  Effect* createDoubleTapEffect(const Vector3D& origin,
                                const Vector3D& centerRay,
                                const Vector3D& tapRay) const;

  double distanceToHorizon(const Vector3D& position) const;

  MutableMatrix44D drag(const Geodetic3D& origin, const Geodetic3D& destination) const;

  Vector3D getNorth() const {
    return Vector3D::upY();
  }

  void applyCameraConstrainers(const Camera* previousCamera,
                               Camera* nextCamera) const;

  Geodetic3D getDefaultCameraPosition(const Sector& rendereSector) const {
    const Vector3D asw = toCartesian(rendereSector.getSW());
    const Vector3D ane = toCartesian(rendereSector.getNE());
    const double height = asw.sub(ane).length() * 1.9;

    return Geodetic3D(rendereSector._center,
                      height);
  }

  const std::string getType() const {
    return "Flat";
  }
  
};


#endif
