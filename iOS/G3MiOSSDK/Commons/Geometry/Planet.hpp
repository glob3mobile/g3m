//
//  Planet.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_Planet_hpp
#define G3MiOSSDK_Planet_hpp

#include <string>
#include <list>
#include <vector>
#include "Vector3D.hpp"

class Planet : public Disposable {
public:
  
  static const Planet* createEarth();
  static const Planet* createSphericalEarth();
  
  virtual ~Planet() {
#ifdef JAVA_CODE
  super.dispose();
#endif

  }
  
  virtual Vector3D getRadii() const = 0;
  virtual Vector3D centricSurfaceNormal(const Vector3D& positionOnEllipsoid) const = 0;
  virtual Vector3D geodeticSurfaceNormal(const Vector3D& positionOnEllipsoid) const = 0;
  
  virtual Vector3D geodeticSurfaceNormal(const MutableVector3D& positionOnEllipsoid) const = 0;
  
  
  virtual Vector3D geodeticSurfaceNormal(const Angle& latitude,
                                         const Angle& longitude) const = 0;
  
  virtual Vector3D geodeticSurfaceNormal(const Geodetic3D& geodetic) const = 0;
  virtual Vector3D geodeticSurfaceNormal(const Geodetic2D& geodetic) const = 0;
  virtual std::vector<double> intersectionsDistances(const Vector3D& origin,
                                                     const Vector3D& direction) const = 0;
  
  virtual Vector3D toCartesian(const Angle& latitude,
                               const Angle& longitude,
                               const double height) const = 0;
  
  virtual Vector3D toCartesian(const Geodetic3D& geodetic) const = 0;
  virtual Vector3D toCartesian(const Geodetic2D& geodetic) const = 0;
  virtual Vector3D toCartesian(const Geodetic2D& geodetic,
                               const double height) const = 0;
  virtual Geodetic2D toGeodetic2D(const Vector3D& positionOnEllipsoid) const = 0;
  
  virtual Geodetic3D toGeodetic3D(const Vector3D& position) const = 0;
  
  virtual Vector3D scaleToGeodeticSurface(const Vector3D& position) const = 0;
  
  virtual Vector3D scaleToGeocentricSurface(const Vector3D& position) const = 0;
  
  virtual std::list<Vector3D> computeCurve(const Vector3D& start,
                                           const Vector3D& stop,
                                           double granularity) const = 0;
  
  virtual Geodetic2D getMidPoint (const Geodetic2D& P0, const Geodetic2D& P1) const = 0;
  
  
  virtual double computePreciseLatLonDistance(const Geodetic2D& g1,
                                              const Geodetic2D& g2) const = 0;
  
  virtual double computeFastLatLonDistance(const Geodetic2D& g1,
                                           const Geodetic2D& g2) const = 0;
  
  virtual Vector3D closestPointToSphere(const Vector3D& pos, const Vector3D& ray) const = 0;
  
  virtual Vector3D closestIntersection(const Vector3D& pos, const Vector3D& ray) const = 0;
  
  
  virtual MutableMatrix44D createGeodeticTransformMatrix(const Geodetic3D& position) const = 0;
  
};

#endif
