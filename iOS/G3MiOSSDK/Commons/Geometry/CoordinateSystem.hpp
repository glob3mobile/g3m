//
//  CoordinateSystem.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 29/01/14.
//
//

#ifndef __G3MiOSSDK__CoordinateSystem__
#define __G3MiOSSDK__CoordinateSystem__

#include "Vector3D.hpp"
#include "TaitBryanAngles.hpp"

class Mesh;
class Color;

class CoordinateSystem {
public:

  const Vector3D _x;
  const Vector3D _y;
  const Vector3D _z;
  const Vector3D _origin;

  static bool areOrtogonal(const Vector3D& x, const Vector3D& y, const Vector3D& z);

  static CoordinateSystem global();

  CoordinateSystem(const Vector3D& x, const Vector3D& y, const Vector3D& z, const Vector3D& origin);

  //For camera
  CoordinateSystem(const Vector3D& viewDirection, const Vector3D& up, const Vector3D& origin);

  Mesh* createMesh(double size, const Color& xColor, const Color& yColor, const Color& zColor) const;

  CoordinateSystem applyTaitBryanAngles(const TaitBryanAngles& angles) const;

  CoordinateSystem applyTaitBryanAngles(const Angle& heading, const Angle& pitch, const Angle& roll) const;

  CoordinateSystem changeOrigin(const Vector3D& newOrigin) const;

  TaitBryanAngles getTaitBryanAngles(const CoordinateSystem& global) const;

  bool isEqualsTo(const CoordinateSystem& that) const;

};

#endif
