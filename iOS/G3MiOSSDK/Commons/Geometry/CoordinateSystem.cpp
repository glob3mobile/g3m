//
//  CoordinateSystem.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 29/01/14.
//
//

#include "CoordinateSystem.hpp"

#include "Color.hpp"
#include "Mesh.hpp"
#include "DirectMesh.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "FloatBufferBuilderFromColor.hpp"

CoordinateSystem CoordinateSystem::global() {
  return CoordinateSystem(Vector3D::upX(), Vector3D::upY(), Vector3D::upZ(), Vector3D::zero);
}

CoordinateSystem::CoordinateSystem(const Vector3D& x, const Vector3D& y, const Vector3D& z, const Vector3D& origin):
_x(x.normalized()),_y(y.normalized()),_z(z.normalized()), _origin(origin)
{
  //TODO CHECK CONSISTENCY
  if (!areOrtogonal(x, y, z)) {
    ILogger::instance()->logError("Inconsistent CoordinateSystem created.");
  }
}

//For camera
CoordinateSystem::CoordinateSystem(const Vector3D& viewDirection, const Vector3D& up, const Vector3D& origin):
_x(viewDirection.cross(up).normalized()),
_y(viewDirection.normalized()),
_z(up.normalized()),
_origin(origin)
{
}

bool CoordinateSystem::areOrtogonal(const Vector3D& x, const Vector3D& y, const Vector3D& z) {
  return x.isPerpendicularTo(y) && x.isPerpendicularTo(z) && y.isPerpendicularTo(z);
}

CoordinateSystem CoordinateSystem::changeOrigin(const Vector3D& newOrigin) const{
  return CoordinateSystem(_x, _y, _z, newOrigin);
}

Mesh* CoordinateSystem::createMesh(double size, const Color& xColor, const Color& yColor, const Color& zColor) const{

  FloatBufferBuilderFromColor colors;

  FloatBufferBuilderFromCartesian3D* fbb = FloatBufferBuilderFromCartesian3D::builderWithGivenCenter(_origin);
  fbb->add(_origin);
  fbb->add(_origin.add(_x.normalized().times(size)));
  colors.add(xColor);
  colors.add(xColor);

  fbb->add(_origin);
  fbb->add(_origin.add(_y.normalized().times(size)));
  colors.add(yColor);
  colors.add(yColor);

  fbb->add(_origin);
  fbb->add(_origin.add(_z.normalized().times(size)));
  colors.add(zColor);
  colors.add(zColor);

  DirectMesh* dm = new DirectMesh(GLPrimitive::lines(),
                                  true,
                                  fbb->getCenter(),
                                  fbb->create(),
                                  (float)5.0,
                                  (float)1.0,
                                  NULL,
                                  colors.create(),
                                  (float)1.0,
                                  false,
                                  NULL);

  delete fbb;

  return dm;
}

TaitBryanAngles CoordinateSystem::getTaitBryanAngles(const CoordinateSystem& global) const{

  //We know...

  const Vector3D u = global._x;
  const Vector3D v = global._y;
  const Vector3D w = global._z;

  //const Vector3D uppp = _x;
  const Vector3D vppp = _y;
  const Vector3D wppp = _z;

  const Vector3D wp = w;
  const Vector3D vpp = vppp;

  //We calculate

  //Pitch "especial" positions
  double x = vpp.dot(wp);
  if (x < -0.99999 && x > -1.000001) {
    //Pitch -90
    const Vector3D wpp = wppp; //No Roll

    Angle pitch = Angle::fromDegrees(-90);
    Angle roll = Angle::zero();
    Angle heading = v.signedAngleBetween(wpp, w);

    return TaitBryanAngles(heading,
                           pitch,
                           roll);

  } else if (x > 0.99999 && x < 1.000001) {
    //Pitch 90
    const Vector3D wpp = wppp; //No Roll

    Angle pitch = Angle::fromDegrees(90);
    Angle roll = Angle::zero();
    Angle heading = v.signedAngleBetween(wpp, w).sub(Angle::fromDegrees(180));

    return TaitBryanAngles(heading,
                           pitch,
                           roll);
  }

  //Normal formula
  const Vector3D up = vpp.cross(wp);
  const Vector3D vp = wp.cross(up);

  const Vector3D upp = up;
  const Vector3D wpp = upp.cross(vpp);

  Angle heading = u.signedAngleBetween(up, w);
  Angle pitch = vp.signedAngleBetween(vpp, up);
  Angle roll = wpp.signedAngleBetween(wppp, vpp);

  return TaitBryanAngles(heading,
                         pitch,
                         roll);
}

CoordinateSystem CoordinateSystem::applyTaitBryanAngles(const TaitBryanAngles& angles) const{
  return applyTaitBryanAngles(angles._heading, angles._pitch, angles._roll);
}

CoordinateSystem CoordinateSystem::applyTaitBryanAngles(const Angle& heading, const Angle& pitch, const Angle& roll) const{

  //Check out Agustin Trujillo's review of this topic
  //This implementation is purposely explicit on every step

  const Vector3D u = _x;
  const Vector3D v = _y;
  const Vector3D w = _z;

  //Heading rotation
  bool isHeadingZero = heading.isZero();

  MutableMatrix44D hm = isHeadingZero ? MutableMatrix44D::invalid() :
  MutableMatrix44D::createGeneralRotationMatrix(heading, w, Vector3D::zero);

  const Vector3D up = isHeadingZero ? u : u.transformedBy(hm, 1.0);
  const Vector3D vp = isHeadingZero ? v : v.transformedBy(hm, 1.0);
  const Vector3D wp = w;

  //Pitch rotation
  bool isPitchZero = pitch.isZero();

  MutableMatrix44D pm = isPitchZero?  MutableMatrix44D::invalid() :\
  MutableMatrix44D::createGeneralRotationMatrix(pitch, up, Vector3D::zero);

  const Vector3D upp = up;
  const Vector3D vpp = isPitchZero? vp : vp.transformedBy(pm, 1.0);
  const Vector3D wpp = isPitchZero? wp : wp.transformedBy(pm, 1.0);

  //Roll rotation
  bool isRollZero = roll.isZero();

  MutableMatrix44D rm = isRollZero? MutableMatrix44D::invalid() :
  MutableMatrix44D::createGeneralRotationMatrix(roll, vpp, Vector3D::zero);

  const Vector3D uppp = isRollZero? upp : upp.transformedBy(rm, 1.0);
  const Vector3D vppp = vpp;
  const Vector3D wppp = isRollZero? wpp : wpp.transformedBy(rm, 1.0);

  return CoordinateSystem(uppp, vppp, wppp, _origin);
}


bool CoordinateSystem::isEqualsTo(const CoordinateSystem& that) const{
  return _x.isEquals(that._x) && _y.isEquals(that._y) && _z.isEquals(that._z);
}

