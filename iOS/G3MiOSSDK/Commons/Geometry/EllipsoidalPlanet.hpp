//
//  EllipsoidalPlanet.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 15/05/13.
//
//

#ifndef G3MiOSSDK_EllipsoidalPlanet_hpp
#define G3MiOSSDK_EllipsoidalPlanet_hpp

#include <vector>
#include <list>

#include "Vector3D.hpp"
#include "Planet.hpp"

#include "MutableVector3D.hpp"

#include "Ellipsoid.hpp"

class EllipsoidalPlanet: public Planet {
private:
  
#ifdef C_CODE
  const Ellipsoid _ellipsoid;
#endif
#ifdef JAVA_CODE
  private final Ellipsoid _ellipsoid;
#endif


public:
  
  EllipsoidalPlanet(const Ellipsoid& ellipsoid);
  
  ~EllipsoidalPlanet() {
#ifdef JAVA_CODE
  super.dispose();
#endif

  }
  
  Vector3D getRadii() const{
    return _ellipsoid.getRadii();
  }
  
  Vector3D centricSurfaceNormal(const Vector3D& positionOnEllipsoidalPlanet) const {
    return positionOnEllipsoidalPlanet.normalized();
  }
  
  Vector3D geodeticSurfaceNormal(const Vector3D& positionOnEllipsoidalPlanet) const {
    return positionOnEllipsoidalPlanet.times(_ellipsoid.getOneOverRadiiSquared()).normalized();
  }
  
  Vector3D geodeticSurfaceNormal(const MutableVector3D& positionOnEllipsoidalPlanet) const {
    return positionOnEllipsoidalPlanet.times(_ellipsoid.getOneOverRadiiSquared()).normalized().asVector3D();
  }
  
  
  Vector3D geodeticSurfaceNormal(const Angle& latitude,
                                 const Angle& longitude) const;
  
  Vector3D geodeticSurfaceNormal(const Geodetic3D& geodetic) const {
    return geodeticSurfaceNormal(geodetic._latitude, geodetic._longitude);
  }
  
  Vector3D geodeticSurfaceNormal(const Geodetic2D& geodetic) const {
    return geodeticSurfaceNormal(geodetic._latitude, geodetic._longitude);
  }
  
  std::vector<double> intersectionsDistances(const Vector3D& origin,
                                             const Vector3D& direction) const;
  
  Vector3D toCartesian(const Angle& latitude,
                       const Angle& longitude,
                       const double height) const;
  
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
  
  Geodetic2D toGeodetic2D(const Vector3D& positionOnEllipsoidalPlanet) const;
  
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
  
  Vector3D closestIntersection(const Vector3D& pos, const Vector3D& ray) const;
  
  
  MutableMatrix44D createGeodeticTransformMatrix(const Geodetic3D& position) const;
  
};


#endif
