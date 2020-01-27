//
//  SphericalPlanet.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//

#ifndef G3MiOSSDK_SphericalPlanet
#define G3MiOSSDK_SphericalPlanet

#include "Planet.hpp"

#include <list>
#include "Sphere.hpp"
#include "MutableVector3D.hpp"


class SphericalPlanet : public Planet {
private:
#ifdef C_CODE
  const Sphere _sphere;
#endif
#ifdef JAVA_CODE
  private Sphere _sphere;
#endif
  const Vector3D _radii;

  mutable MutableVector3D _origin;
  mutable MutableVector3D _initialPoint;
  mutable MutableVector3D _centerPoint;
  mutable MutableVector3D _centerRay;
  mutable MutableVector3D _initialPoint0;
  mutable MutableVector3D _initialPoint1;
  mutable MutableVector3D _lastDragAxis;
  mutable double          _lastDragRadians;
  mutable double          _lastDragRadiansStep;
  mutable double          _angleBetweenInitialRays;
  mutable double          _angleBetweenInitialPoints;
  mutable bool            _validSingleDrag;


public:
  static const Planet* createEarth();

  SphericalPlanet(const Sphere& sphere);

  ~SphericalPlanet() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  Vector3D getRadii() const {
    return _radii;
  }

  Vector3D centricSurfaceNormal(const Vector3D& position) const {
    return position.normalized();
  }

  Vector3D geodeticSurfaceNormal(const Vector3D& position) const {
    return position.normalized();
  }

  Vector3D geodeticSurfaceNormal(const MutableVector3D& position) const {
    return position.normalized().asVector3D();
  }

  Vector3D geodeticSurfaceNormal(const Angle& latitude,
                                 const Angle& longitude) const;

  Vector3D geodeticSurfaceNormal(const Geodetic3D& geodetic) const;

  Vector3D geodeticSurfaceNormal(const Geodetic2D& geodetic) const;

  void geodeticSurfaceNormal(const Angle& latitude,
                             const Angle& longitude,
                             MutableVector3D& result) const;

  std::vector<double> intersectionsDistances(double originX,
                                             double originY,
                                             double originZ,
                                             double directionX,
                                             double directionY,
                                             double directionZ) const;

  Vector3D toCartesian(const Angle& latitude,
                       const Angle& longitude,
                       const double height) const;

  Vector3D toCartesian(const Geodetic3D& geodetic) const;

  Vector3D toCartesian(const Geodetic2D& geodetic) const;

  Vector3D toCartesian(const Geodetic2D& geodetic,
                       const double height) const;

  void toCartesian(const Angle& latitude,
                   const Angle& longitude,
                   const double height,
                   MutableVector3D& result) const;

  void toCartesian(const Geodetic3D& geodetic,
                   MutableVector3D& result) const;
  void toCartesian(const Geodetic2D& geodetic,
                   MutableVector3D& result) const;
  void toCartesian(const Geodetic2D& geodetic,
                   const double height,
                   MutableVector3D& result) const;

  Geodetic2D toGeodetic2D(const Vector3D& position) const;

  Geodetic3D toGeodetic3D(const Vector3D& position) const;

  Vector3D scaleToGeodeticSurface(const Vector3D& position) const;

  Vector3D scaleToGeocentricSurface(const Vector3D& position) const;

  std::list<Vector3D> computeCurve(const Vector3D& start,
                                   const Vector3D& stop,
                                   double granularity) const;

  Geodetic2D getMidPoint (const Geodetic2D& P0, const Geodetic2D& P1) const;


  double computePreciseLatLonDistance(const Geodetic2D& g1,
                                      const Geodetic2D& g2) const;

  double computeFastLatLonDistance(const Geodetic2D& g1,
                                   const Geodetic2D& g2) const;

  Vector3D closestPointToSphere(const Vector3D& pos, const Vector3D& ray) const;

  MutableMatrix44D createGeodeticTransformMatrix(const Angle& latitude,
                                                 const Angle& longitude,
                                                 const double height) const;

  bool isFlat() const { return false; }

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
    return Vector3D::UP_Z;
  }

  void applyCameraConstrains(const Camera* previousCamera,
                             Camera* nextCamera) const;

  Geodetic3D getDefaultCameraPosition(const Sector& rendereSector) const;
  
  const std::string getType() const {
    return "Spherical";
  }
  
};

#endif
