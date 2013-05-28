//
//  Sphere.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_Sphere_hpp
#define G3MiOSSDK_Sphere_hpp

#include "MutableVector3D.hpp"
#include "Geodetic3D.hpp"
#include "Planet.hpp"


class Sphere: public Planet {
private:
  const double _radii;
  const double _radiiSquared;
  const double _oneOverRadiiSquared;


public:

  Sphere(double radii);

  ~Sphere() {

  }

  Vector3D getRadii() const{
    return Vector3D(_radii, _radii, _radii);
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
  
  Vector3D geodeticSurfaceNormal(const Geodetic3D& geodetic) const {
    return geodeticSurfaceNormal(geodetic.latitude(), geodetic.longitude());
  }

  Vector3D geodeticSurfaceNormal(const Geodetic2D& geodetic) const {
    return geodeticSurfaceNormal(geodetic.latitude(), geodetic.longitude());
  }

  std::vector<double> intersectionsDistances(const Vector3D& origin,
                                             const Vector3D& direction) const;

  Vector3D toCartesian(const Angle& latitude,
                       const Angle& longitude,
                       const double height) const;

  Vector3D toCartesian(const Geodetic3D& geodetic) const {
    return toCartesian(geodetic.latitude(),
                       geodetic.longitude(),
                       geodetic.height());
  }

  Vector3D toCartesian(const Geodetic2D& geodetic) const {
    return toCartesian(geodetic.latitude(),
                       geodetic.longitude(),
                       0.0);
  }

  Vector3D toCartesian(const Geodetic2D& geodetic,
                       const double height) const {
    return toCartesian(geodetic.latitude(),
                       geodetic.longitude(),
                       height);
  }

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

  Vector3D closestIntersection(const Vector3D& pos, const Vector3D& ray) const;


  MutableMatrix44D createGeodeticTransformMatrix(const Geodetic3D& position) const;
  
};

#endif
