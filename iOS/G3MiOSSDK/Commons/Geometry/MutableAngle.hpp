//
//  MutableAngle.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/4/17.
//
//

#ifndef MutableAngle_hpp
#define MutableAngle_hpp

#include <string>

class Angle;


class MutableAngle {
private:
  double _degrees;
  double _radians;

  MutableAngle(const double degrees,
               const double radians) :
  _degrees( degrees ),
  _radians( radians )
  {
  }

  MutableAngle& operator=(const MutableAngle& that);

public:
  MutableAngle(const MutableAngle& angle):
  _degrees(angle._degrees),
  _radians(angle._radians)
  {
  }

  static MutableAngle fromDegrees(double degrees);

  static MutableAngle fromRadians(double radians);

  void setDegrees(double degrees);

  void setRadians(double radians);

  const std::string description() const;

#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

  Angle asAngle() const;

};

#endif
