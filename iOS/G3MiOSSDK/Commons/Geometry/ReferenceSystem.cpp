//
//  ReferenceSystem.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 29/01/14.
//
//

#include "ReferenceSystem.hpp"

#include "DirectMesh.hpp"
#include "Color.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "FloatBufferBuilderFromColor.hpp"
#include "Plane.hpp"

bool ReferenceSystem::areOrtogonal(const Vector3D& x, const Vector3D& y, const Vector3D& z){
  return x.isPerpendicularTo(y) && x.isPerpendicularTo(z) && y.isPerpendicularTo(z);
}

ReferenceSystem ReferenceSystem::changeOrigin(const Vector3D& newOrigin) const{
  return ReferenceSystem(_x, _y, _z, newOrigin);
}

Mesh* ReferenceSystem::createMesh(double size, const Color& xColor, const Color& yColor, const Color& zColor) const{

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
                                  5.0,
                                  1.0,
                                  NULL,
                                  colors.create(),
                                  1.0,
                                  false,
                                  NULL);

  delete fbb;

  return dm;
}

TaitBryanAngles ReferenceSystem::getTaitBryanAngles(const ReferenceSystem& global) const{

  //We know...

  const Vector3D u = global._x;
  const Vector3D v = global._y;
  const Vector3D w = global._z;

  const Vector3D uppp = _x;
  const Vector3D vppp = _y;
  const Vector3D wppp = _z;

  const Vector3D wp = w;
  const Vector3D vpp = vppp;

  //We calculate

  //Pitch "especial" positions
  double x = vpp.dot(wp);
  if (x == -1.0){
    //Pitch -90
    Angle pitch = Angle::fromDegrees(-90);
    Angle roll = Angle::zero();
    Angle heading = u.angleBetween(uppp);

    return TaitBryanAngles(heading,
                           pitch,
                           roll);

  } else if (x > 0.99999 && x < 1.000001){
    //Pitch 90
    Angle pitch = Angle::fromDegrees(90);
    Angle roll = Angle::zero();
    Angle heading = u.angleBetween(uppp);

    return TaitBryanAngles(heading,
                           pitch,
                           roll);
  }

  //Normal formula
  const Vector3D up = wp.cross(vpp);
  const Vector3D vp = up.cross(wp);

  const Vector3D upp = up;
  const Vector3D wpp = vpp.cross(upp);

  //Calculating Angles
  Angle heading = u.angleBetween(up);
  Angle pitch = vp.angleBetween(vpp);
  Angle roll = uppp.angleBetween(upp);

  return TaitBryanAngles(Angle::fromDegrees(180).sub(heading),
                         pitch,
                         Angle::fromDegrees(180).sub(roll));
}

ReferenceSystem ReferenceSystem::applyTaitBryanAngles(const Angle& heading,
                                                      const Angle& pitch,
                                                      const Angle& roll) const{

  //Check out Agustin Trujillo's review of this topic
  //This implementation is purposely explicit on every step

  const Vector3D u = _x;
  const Vector3D v = _y;
  const Vector3D w = _z;

  /*
  if (!areOrtogonal(u, v, w)){
    printf("PROBLEM");
  }

  printf("U: %s,\nV: %s,\nW: %s\n",
         u.description().c_str(),
         v.description().c_str(),
         w.description().c_str());
   */

  //Heading rotation
  bool isHeadingZero = heading.isZero();

  MutableMatrix44D hm = isHeadingZero ? MutableMatrix44D::invalid() :
  MutableMatrix44D::createGeneralRotationMatrix(heading, w, Vector3D::zero);

  const Vector3D up = isHeadingZero ? u : u.transformedBy(hm, 1.0);
  const Vector3D vp = isHeadingZero ? v : v.transformedBy(hm, 1.0);
  const Vector3D wp = w;

  /*
  if (!areOrtogonal(up, vp, wp)){
    printf("PROBLEM P\n");
  }

  printf("UP: %s,\nVP: %s,\nWP: %s\n",
         up.description().c_str(),
         vp.description().c_str(),
         wp.description().c_str());
   */

  //Pitch rotation
  bool isPitchZero = pitch.isZero();

  MutableMatrix44D pm = isPitchZero?  MutableMatrix44D::invalid() :\
  MutableMatrix44D::createGeneralRotationMatrix(pitch, up, Vector3D::zero);

  const Vector3D upp = up;
  const Vector3D vpp = isPitchZero? vp : vp.transformedBy(pm, 1.0);
  const Vector3D wpp = isPitchZero? wp : wp.transformedBy(pm, 1.0);

  /*
  if (!areOrtogonal(upp, vpp, wpp)){
    printf("PROBLEM PP\n");
  }

  printf("UPP: %s,\nVPP: %s,\nWPP: %s\n",
         upp.description().c_str(),
         vpp.description().c_str(),
         wpp.description().c_str());
   */


  //Roll rotation
  bool isRollZero = roll.isZero();

  MutableMatrix44D rm = isRollZero? MutableMatrix44D::invalid() :
  MutableMatrix44D::createGeneralRotationMatrix(roll, vpp, Vector3D::zero);

  const Vector3D uppp = isRollZero? upp : upp.transformedBy(rm, 1.0);
  const Vector3D vppp = vpp;
  const Vector3D wppp = isRollZero? wpp : wpp.transformedBy(rm, 1.0);

  /*
  if (!areOrtogonal(uppp, vppp, wppp)){
    printf("PROBLEM PPP\n");
  }

  printf("UPPP: %s,\nVPPP: %s,\nWPPP: %s\n",
         uppp.description().c_str(),
         vppp.description().c_str(),
         wppp.description().c_str());
   */

  return ReferenceSystem(uppp, vppp, wppp, _origin);
}