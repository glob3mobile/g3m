//
//  Quaternion.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/27/17.
//
//

#ifndef QuaternionF_hpp
#define QuaternionF_hpp

class MutableMatrix44D;
class TaitBryanAngles;

class Quaternion {
public:
  static const Quaternion NANQD;

  const double _x;
  const double _y;
  const double _z;
  const double _w;

  Quaternion(double x,
             double y,
             double z,
             double w);

  ~Quaternion();

  double dot(const Quaternion& that) const;

  Quaternion times(const Quaternion& that) const;

  Quaternion slerp(const Quaternion& that,
                   double t) const;

  double norm() const;

  Quaternion inversed() const;

  void toRotationMatrix(MutableMatrix44D& result) const;

  TaitBryanAngles toTaitBryanAngles() const;

};

#endif
