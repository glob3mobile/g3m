//
//  GeoMeter.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 19/12/13.
//
//

#ifndef __G3MiOSSDK__GeoMeter__
#define __G3MiOSSDK__GeoMeter__

#include <iostream>

#include <vector>

class Planet;
class Geodetic2D;
class Sector;

class GeoMeter{
#ifdef C_CODE
  static const Planet* _planet;
#endif
#ifdef JAVA_CODE
  private static Planet _planet = null;
#endif
public:

  static double getDistance(const Geodetic2D& g1, const Geodetic2D& g2); //Meters

  static double getArea(const std::vector<Geodetic2D*>& polygon); //Meters^2

  static double getArea(const Sector& sector); //Meters^2

};

#endif /* defined(__G3MiOSSDK__GeoMeter__) */
