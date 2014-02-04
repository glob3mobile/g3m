//
//  ReferenceSystem.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 29/01/14.
//
//

#ifndef __G3MiOSSDK__ReferenceSystem__
#define __G3MiOSSDK__ReferenceSystem__

#include "Vector3D.hpp"

#include <iostream>
#include <string>

class Mesh;
class Color;

class TaitBryanAngles{
public:
  const Angle _heading;
  const Angle _pitch;
  const Angle _roll;

  TaitBryanAngles(const Angle& heading, const Angle& pitch, const Angle& roll):
  _heading(heading),
  _pitch(pitch),
  _roll(roll)
  {
  }

  static TaitBryanAngles fromRadians(double heading, double pitch, double roll){
    return TaitBryanAngles(Angle::fromRadians(heading), Angle::fromRadians(pitch), Angle::fromRadians(roll));
  }

  static TaitBryanAngles fromDegrees(double heading, double pitch, double roll){
    return TaitBryanAngles(Angle::fromDegrees(heading),
                           Angle::fromDegrees(pitch),
                           Angle::fromDegrees(roll));
  }

  std::string description() const;
};

class ReferenceSystem{

public:

  const Vector3D _x;
  const Vector3D _y;
  const Vector3D _z;
  const Vector3D _origin;

  static ReferenceSystem global(){
    return ReferenceSystem(Vector3D::upX(), Vector3D::upY(), Vector3D::upZ(), Vector3D::zero);
  }

  ReferenceSystem(const Vector3D& x, const Vector3D& y, const Vector3D& z, const Vector3D& origin):
  _x(x.normalized()),_y(y.normalized()),_z(z.normalized()), _origin(origin)
  {
    //TODO CHECK CONSISTENCY
    if (!areOrtogonal(x, y, z)){
      ILogger::instance()->logError("Inconsistent ReferenceSystem created.");
    }
  }

  //For camera
  ReferenceSystem(const Vector3D& viewDirection, const Vector3D& up, const Vector3D& origin):
  _x(viewDirection.cross(up).normalized()),
  _y(viewDirection.normalized()),
  _z(up.normalized()),
  _origin(origin)
  {
  }

  static bool areOrtogonal(const Vector3D& x, const Vector3D& y, const Vector3D& z);

  Mesh* createMesh(double size, const Color& xColor, const Color& yColor, const Color& zColor) const;

  ReferenceSystem applyTaitBryanAngles(const TaitBryanAngles& angles) const{
    return applyTaitBryanAngles(angles._heading, angles._pitch, angles._roll);
  }

  ReferenceSystem applyTaitBryanAngles(const Angle& heading, const Angle& pitch, const Angle& roll) const;

  ReferenceSystem changeOrigin(const Vector3D& newOrigin) const;

  TaitBryanAngles getTaitBryanAngles(const ReferenceSystem& global) const;

  bool isEqualsTo(const ReferenceSystem& that) const;

};

#endif /* defined(__G3MiOSSDK__ReferenceSystem__) */
