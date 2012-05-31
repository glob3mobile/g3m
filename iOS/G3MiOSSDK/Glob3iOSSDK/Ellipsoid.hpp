//
//  Ellipsoid.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_Ellipsoid_hpp
#define G3MiOSSDK_Ellipsoid_hpp

#include "Vector3D.hpp"
#include "Geodetic3D.hpp"

#include <vector>
#include <list>


class Ellipsoid {
private:
  const Vector3D _radii;
  const Vector3D _radiiSquared;
  const Vector3D _radiiToTheFourth;
  const Vector3D _oneOverRadiiSquared;
  
  
public:
  static Ellipsoid WGS84() {
    return Ellipsoid(Vector3D(6378137.0, 6378137.0, 6356752.314245));
  }
  
  static Ellipsoid SPHERE_6365000() {
    return Ellipsoid(Vector3D(6365000.0, 6365000.0, 6365000.0));
  }
  
  Ellipsoid(const Vector3D& radii);
  
  Vector3D getRadii() {
    return _radii;
  }
  
  Vector3D centricSurfaceNormal(const Vector3D& positionOnEllipsoid) const {
    return positionOnEllipsoid.normalized();
  }
  
  Vector3D geodeticSurfaceNormal(const Vector3D& positionOnEllipsoid) const {
    return positionOnEllipsoid.times(_oneOverRadiiSquared).normalized();
  }
  
  Vector3D geodeticSurfaceNormal(const Geodetic3D& geodetic) const;
  
  std::vector<double> intersections(const Vector3D& origin,
                                    const Vector3D& direction) const;
  
  Vector3D toVector3D(const Geodetic3D& geodetic) const;
  
  Vector3D toVector3D(const Geodetic2D& geodetic) const {
    return toVector3D(Geodetic3D(geodetic, 0.0));
  }
  
  Geodetic2D toGeodetic2D(const Vector3D& positionOnEllipsoid) const;
  
  Geodetic3D toGeodetic3D(const Vector3D& position) const;
  
  Vector3D scaleToGeodeticSurface(const Vector3D& position) const;
  
  Vector3D scaleToGeocentricSurface(const Vector3D& position) const;
  
  std::list<Vector3D> computeCurve(const Vector3D& start,
                                   const Vector3D& stop,
                                   double granularity) const;
  
  double computePreciseLatLonDistance(const Geodetic2D& g1,
                                      const Geodetic2D& g2) const;
  
  double computeFastLatLonDistance(const Geodetic2D& g1,
                                   const Geodetic2D& g2) const;
  
};

#endif
