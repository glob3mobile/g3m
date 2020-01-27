//
//  CoordinateSystem.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 29/01/14.
//
//

#include "CoordinateSystem.hpp"

#include "Camera.hpp"
#include "ErrorHandling.hpp"
#include "Angle.hpp"
#include "FloatBufferBuilderFromColor.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "DirectMesh.hpp"
#include "GLConstants.hpp"
#include "TaitBryanAngles.hpp"
#include "IStringBuilder.hpp"
#include "Color.hpp"


CoordinateSystem CoordinateSystem::global() {
  return CoordinateSystem(Vector3D::UP_X,
                          Vector3D::UP_Y,
                          Vector3D::UP_Z,
                          Vector3D::ZERO);
}

CoordinateSystem CoordinateSystem::fromCamera(const Camera& camera) {
  const Vector3D viewDirection = camera.getViewDirection();
  const Vector3D up            = camera.getUp();
  const Vector3D origin        = camera.getCartesianPosition();

  return CoordinateSystem(viewDirection.cross(up).normalized(),
                          viewDirection.normalized(),
                          up.normalized(),
                          origin);
}

CoordinateSystem::CoordinateSystem(const CoordinateSystem& that) :
_x(that._x),
_y(that._y),
_z(that._z),
_origin(that._origin)
{
}

CoordinateSystem::CoordinateSystem(const Vector3D& x,
                                   const Vector3D& y,
                                   const Vector3D& z,
                                   const Vector3D& origin):
_x(x.normalized()),
_y(y.normalized()),
_z(z.normalized()),
_origin(origin)
{
  if (!checkConsistency(x, y, z)) {
    THROW_EXCEPTION("Inconsistent CoordinateSystem created.");
  }
}

bool CoordinateSystem::checkConsistency(const Vector3D& x,
                                        const Vector3D& y,
                                        const Vector3D& z) {
  if (x.isNan() || y.isNan() || z.isNan()) {
    return false;
  }
  return true;
  //  return areOrtogonal(x, y, z);
}

//bool CoordinateSystem::areOrtogonal(const Vector3D& x,
//                                    const Vector3D& y,
//                                    const Vector3D& z) {
//  return x.isPerpendicularTo(y) && x.isPerpendicularTo(z) && y.isPerpendicularTo(z);
//}

CoordinateSystem CoordinateSystem::changeOrigin(const Vector3D& newOrigin) const {
  return CoordinateSystem(_x, _y, _z, newOrigin);
}

Mesh* CoordinateSystem::createMesh(double size) const {
  return createMesh(size,
                    Color::RED,
                    Color::GREEN,
                    Color::BLUE);
}

Mesh* CoordinateSystem::createMesh(double size,
                                   const Color& xColor,
                                   const Color& yColor,
                                   const Color& zColor) const {
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
                                  5.0f,
                                  1.0f,
                                  NULL,
                                  colors.create(),
                                  false,
                                  NULL);

  delete fbb;

  return dm;
}

TaitBryanAngles CoordinateSystem::getTaitBryanAngles(const CoordinateSystem& global) const {

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

    Angle pitch = Angle::_MINUS_HALF_PI;
    Angle roll = Angle::_ZERO;
    Angle heading = v.signedAngleBetween(wpp, w);

    return TaitBryanAngles(heading,
                           pitch,
                           roll);

  }
  else if (x > 0.99999 && x < 1.000001) {
    //Pitch 90
    const Vector3D wpp = wppp; //No Roll

    Angle pitch = Angle::_HALF_PI;
    Angle roll  = Angle::_ZERO;
    Angle heading = v.signedAngleBetween(wpp, w).sub(Angle::_PI);

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

CoordinateSystem CoordinateSystem::applyTaitBryanAngles(const TaitBryanAngles& angles) const {
  return applyTaitBryanAngles(angles._heading, angles._pitch, angles._roll);
}

CoordinateSystem CoordinateSystem::applyTaitBryanAngles(const Angle& heading,
                                                        const Angle& pitch,
                                                        const Angle& roll) const {

  //Check out Agustin Trujillo's review of this topic
  //This implementation is purposely explicit on every step

  const Vector3D u = _x;
  const Vector3D v = _y;
  const Vector3D w = _z;

  //Heading rotation
  bool isHeadingZero = heading.isZero();

  MutableMatrix44D hm = isHeadingZero ? MutableMatrix44D::invalid() :
  MutableMatrix44D::createGeneralRotationMatrix(heading, w, Vector3D::ZERO);

  const Vector3D up = isHeadingZero ? u : u.transformedBy(hm, 1.0);
  const Vector3D vp = isHeadingZero ? v : v.transformedBy(hm, 1.0);
  const Vector3D wp = w;

  //Pitch rotation
  bool isPitchZero = pitch.isZero();

  MutableMatrix44D pm = isPitchZero?  MutableMatrix44D::invalid() :\
  MutableMatrix44D::createGeneralRotationMatrix(pitch, up, Vector3D::ZERO);

  const Vector3D upp = up;
  const Vector3D vpp = isPitchZero? vp : vp.transformedBy(pm, 1.0);
  const Vector3D wpp = isPitchZero? wp : wp.transformedBy(pm, 1.0);

  //Roll rotation
  bool isRollZero = roll.isZero();

  MutableMatrix44D rm = isRollZero? MutableMatrix44D::invalid() :
  MutableMatrix44D::createGeneralRotationMatrix(roll, vpp, Vector3D::ZERO);

  const Vector3D uppp = isRollZero? upp : upp.transformedBy(rm, 1.0);
  const Vector3D vppp = vpp;
  const Vector3D wppp = isRollZero? wpp : wpp.transformedBy(rm, 1.0);

  return CoordinateSystem(uppp, vppp, wppp, _origin);
}

bool CoordinateSystem::isEqualsTo(const CoordinateSystem& that) const {
  return _x.isEquals(that._x) && _y.isEquals(that._y) && _z.isEquals(that._z);
}

CoordinateSystem CoordinateSystem::applyRotation(const MutableMatrix44D& m) const {
  return CoordinateSystem(_x.transformedBy(m, 1.0),
                          _y.transformedBy(m, 1.0),
                          _z.transformedBy(m, 1.0),
                          _origin);//.transformedBy(m, 1.0));
}

MutableMatrix44D CoordinateSystem::getRotationMatrix() const {
  return MutableMatrix44D(_x._x, _x._y, _x._z, 0,
                          _y._x, _y._y, _y._z, 0,
                          _z._x, _z._y, _z._z, 0,
                          0,     0,     0, 1);
}

MutableMatrix44D CoordinateSystem::getMatrix() const {
  const MutableMatrix44D translation = MutableMatrix44D::createTranslationMatrix( _origin );
  const MutableMatrix44D rotation    = getRotationMatrix();
  return translation.multiply(rotation);
}

void CoordinateSystem::copyValueOfRotationMatrix(MutableMatrix44D& m) const {
  m.setValue(_x._x, _x._y, _x._z, 0,
             _y._x, _y._y, _y._z, 0,
             _z._x, _z._y, _z._z, 0,
             0,0,0,1);
}

bool CoordinateSystem::isConsistent() const {
  return checkConsistency(_x, _y, _z);
}

const std::string CoordinateSystem::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("CoordinateSystem x: ");
  isb->addString(_x.description());
  isb->addString(", y: ");
  isb->addString(_y.description());
  isb->addString(", z: ");
  isb->addString(_z.description());
  isb->addString(", origin: ");
  isb->addString(_origin.description());
  const std::string s = isb->getString();
  delete isb;
  return s;
}
