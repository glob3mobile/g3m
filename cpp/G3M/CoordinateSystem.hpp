//
//  CoordinateSystem.hpp
//  G3M
//
//  Created by Jose Miguel SN on 29/01/14.
//
//

#ifndef __G3M__CoordinateSystem__
#define __G3M__CoordinateSystem__

#include "Vector3D.hpp"

class TaitBryanAngles;
class Mesh;
class Color;
class Camera;


class CoordinateSystem {
private:
  
  static bool checkConsistency(const Vector3D& x, const Vector3D& y, const Vector3D& z);
  
//  static bool areOrtogonal(const Vector3D& x, const Vector3D& y, const Vector3D& z);


public:
  const Vector3D _x;
  const Vector3D _y;
  const Vector3D _z;
  const Vector3D _origin;
  

  static CoordinateSystem global();

  static CoordinateSystem fromCamera(const Camera& camera);

  CoordinateSystem(const CoordinateSystem& that);

  CoordinateSystem(const Vector3D& x,
                   const Vector3D& y,
                   const Vector3D& z,
                   const Vector3D& origin);

  ~CoordinateSystem() {

  }

  CoordinateSystem applyTaitBryanAngles(const TaitBryanAngles& angles) const;

  CoordinateSystem applyTaitBryanAngles(const Angle& heading,
                                        const Angle& pitch,
                                        const Angle& roll) const;

  CoordinateSystem changeOrigin(const Vector3D& newOrigin) const;

  TaitBryanAngles getTaitBryanAngles(const CoordinateSystem& global) const;

  bool isEquals(const CoordinateSystem& that) const;
  
  CoordinateSystem applyRotation(const MutableMatrix44D& m) const;
  
  MutableMatrix44D getRotationMatrix() const;
  MutableMatrix44D getMatrix() const;

  void copyValueOfRotationMatrix(MutableMatrix44D& m) const;
  
  bool isConsistent() const;

  const std::string description() const;

#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

  Mesh* createMesh(double size,
                   const Color& xColor,
                   const Color& yColor,
                   const Color& zColor) const;

  Mesh* createMesh(double size) const;

};

#endif
