//
//  IMathUtils.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 29/08/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "IMathUtils.hpp"
#include "Angle.hpp"
#include "Geodetic2D.hpp"
#include "Vector2D.hpp"


IMathUtils* IMathUtils::_instance = NULL;


Geodetic2D IMathUtils::greatCircleIntermediatePoint(const Angle& fromLat,
                                                    const Angle& fromLon,
                                                    const Angle& toLat,
                                                    const Angle& toLon,
                                                    const double alpha) const {
  
  const double fromLatRad = fromLat._radians;
  const double toLatRad   = toLat._radians;
  const double fromLonRad = fromLon._radians;
  const double toLonRad   = toLon._radians;
  
  const double cosFromLat = cos(fromLatRad);
  const double cosToLat   = cos(toLatRad);
  
  const double d = 2 * asin(sqrt(pow((sin((fromLatRad - toLatRad) / 2)), 2)
                                 + (cosFromLat * cosToLat * pow(sin((fromLonRad - toLonRad) / 2), 2))));
  
  const double A = sin((1 - alpha) * d) / sin(d);
  const double B = sin(alpha * d) / sin(d);
  const double x = (A * cosFromLat * cos(fromLonRad)) + (B * cosToLat * cos(toLonRad));
  const double y = (A * cosFromLat * sin(fromLonRad)) + (B * cosToLat * sin(toLonRad));
  const double z = (A * sin(fromLatRad)) + (B * sin(toLatRad));
  const double latRad = atan2(z, sqrt(pow(x, 2) + pow(y, 2)));
  const double lngRad = atan2(y, x);
  
  return Geodetic2D(Angle::fromRadians(latRad),
                    Angle::fromRadians(lngRad));
}

double IMathUtils::closestSegmentsIntersection(const Vector2D& p, const Vector2D& r, //P -> P+R
                                               const Vector2D& q, const Vector2D& s) //Q -> Q+S
{
  //As seen here: http://stackoverflow.com/questions/563198/how-do-you-detect-where-two-line-segments-intersect
  double rs = r.crossProduct(s);
  double qpr = (q.sub(p)).crossProduct(r);
  
  if (rs == 0){
    if (qpr == 0){
      //Colinear
      double r2 = r.crossProduct(r);
      double t0 = qpr / r2;
      double t1 = t0 + s.crossProduct(r) / r2;
      
      if (t0 > t1){
        double a = t0;
        t0 = t1;
        t1 = a;
      }
      
      if (isBetween((float)t0, 0, 1)){
        return t0;
      }
      if (isBetween((float)t1, 0, 1) || (t0 < 0 && t1 > 1))
      {
        return 0;
      }
      
      return NAND;
    } else{
      //Parallel
      return NAND;
    }
  }
  
  //Lines crossing at 1 point
  double t = qpr / rs;
  if (isBetween((float)t, 0, 1)){
    return t;
  }
  return NAND;
}

