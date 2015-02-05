//
//  LayoutUtils.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo on 18/03/13.
//
//

#include "LayoutUtils.hpp"
#include "Geodetic3D.hpp"
#include "Vector3D.hpp"
#include "EllipsoidalPlanet.hpp"
#include "IMathUtils.hpp"

std::vector<Geodetic3D*> LayoutUtils::splitOverCircle(const EllipsoidalPlanet* EllipsoidalPlanet,
                                                      const Geodetic3D& center,
                                                      double radiusInMeters,
                                                      int splits,
                                                      const Angle& startAngle) {
  std::vector<Geodetic3D*> result;
  
  const double startAngleInRadians = startAngle._radians;
  const double deltaInRadians      = (PI * 2.0) / splits;
  const Vector3D cartesianCenter   = EllipsoidalPlanet->toCartesian(center);
  const Vector3D normal            = EllipsoidalPlanet->geodeticSurfaceNormal(center);
  const Vector3D northInPlane      = Vector3D::upZ().projectionInPlane(normal).normalized().times(radiusInMeters);
  
  for (int i = 0; i < splits; i++) {
    const double angleInRadians = startAngleInRadians + (deltaInRadians * i);
    
    const Vector3D finalVector = northInPlane.rotateAroundAxis(normal,
                                                               Angle::fromRadians(angleInRadians));
    const Vector3D cartesianPosition = cartesianCenter.add(finalVector);
    
    result.push_back( new Geodetic3D( EllipsoidalPlanet->toGeodetic3D(cartesianPosition) ) );
  }
  
  return result;
}


std::vector<Geodetic2D*> LayoutUtils::splitOverCircle(const EllipsoidalPlanet* EllipsoidalPlanet,
                                                      const Geodetic2D& center,
                                                      double radiusInMeters,
                                                      int splits,
                                                      const Angle& startAngle) {
  std::vector<Geodetic2D*> result2D;
  std::vector<Geodetic3D*> result3D = splitOverCircle(EllipsoidalPlanet, Geodetic3D(center, 0), radiusInMeters, splits, startAngle);
  
  for (int i = 0; i < splits; i++) {    
    result2D.push_back( new Geodetic2D(result3D[i]->asGeodetic2D()));
    delete result3D[i];
  }
  
  return result2D;
}
