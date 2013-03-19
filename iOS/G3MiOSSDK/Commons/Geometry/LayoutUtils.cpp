//
//  LayoutUtils.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 18/03/13.
//
//

#include "LayoutUtils.hpp"
#include "Geodetic3D.hpp"
#include "Vector3D.hpp"
#include "Ellipsoid.hpp"
#include "IMathUtils.hpp"

std::vector<Geodetic3D*> LayoutUtils::splitOverCircle(const Ellipsoid* ellipsoid,
                                                      const Geodetic3D& center,
                                                      double radiusInMeters,
                                                      int splits,
                                                      const Angle& startAngle) {
  std::vector<Geodetic3D*> result;
  
  const double startAngleInRadians = startAngle._radians;
  const double deltaInRadians      = (IMathUtils::instance()->pi() * 2.0) / splits;
  const Vector3D cartesianCenter   = ellipsoid->toCartesian(center);
  const Vector3D normal            = ellipsoid->geodeticSurfaceNormal(center);
  const Vector3D northInPlane      = Vector3D::upZ().projectionInPlane(normal).normalized().times(radiusInMeters);
  
  for (int i = 0; i < splits; i++) {
    const double angleInRadians = startAngleInRadians + (deltaInRadians * i);
    
    const Vector3D finalVector = northInPlane.rotateAroundAxis(normal,
                                                               Angle::fromRadians(angleInRadians));
    const Vector3D cartesianPosition = cartesianCenter.add(finalVector);
    
    result.push_back( new Geodetic3D( ellipsoid->toGeodetic3D(cartesianPosition) ) );
  }
  
  return result;
}


std::vector<Geodetic2D*> LayoutUtils::splitOverCircle(const Ellipsoid* ellipsoid,
                                                      const Geodetic2D& center,
                                                      double radiusInMeters,
                                                      int splits,
                                                      const Angle& startAngle) {
  std::vector<Geodetic2D*> result2D;
  std::vector<Geodetic3D*> result3D = splitOverCircle(ellipsoid, Geodetic3D(center, 0), radiusInMeters, splits, startAngle);
  
  for (int i = 0; i < splits; i++) {    
    result2D.push_back( new Geodetic2D(result3D[i]->asGeodetic2D()));
    delete result3D[i];
  }
  
  return result2D;
}
