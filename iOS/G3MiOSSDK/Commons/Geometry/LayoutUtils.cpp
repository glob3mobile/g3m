//
//  LayoutUtils.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 18/03/13.
//
//

#include "LayoutUtils.h"
#include "Geodetic3D.hpp"
#include "Vector3D.hpp"
#include "Ellipsoid.hpp"


std::vector<Geodetic3D> LayoutUtils::splitOverCircle(const Ellipsoid* ellipsoid, Geodetic3D center,
                                                     double radiusInMeters, int splits, const Angle& startAngle)
{
  std::vector<Geodetic3D> positionList;
  Angle angle = startAngle;
  Angle delta = Angle::pi().times(2).div(splits);

  const Vector3D cartesianCenter  = ellipsoid->toCartesian(center);
  const Vector3D normal           = ellipsoid->geodeticSurfaceNormal(center);
  const Vector3D northInPlane     = Vector3D::upZ().projectionInPlane(normal).normalized().times(radiusInMeters);
  
  for (unsigned int i=0; i<splits; i++) {
    const Vector3D cartesianPosition = northInPlane.rotateAroundAxis(normal, angle);
    const Geodetic3D geodeticPosition = ellipsoid->toGeodetic3D(cartesianPosition);
    //positionList.push_back(ellipsoid->toGeodetic3D(cartesianPosition));
    //angle = angle.add(delta);
  }
  
  return positionList;
}