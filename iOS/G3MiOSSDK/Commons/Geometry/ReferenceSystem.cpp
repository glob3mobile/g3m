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

ReferenceSystem ReferenceSystem::applyTaitBryanAngles(const Angle& heading,
                                                      const Angle& pitch,
                                                      const Angle& roll) const{

  //Check out Agustin Trujillo's review of this topic
  //This implementation is purposely explicit on every step

  const Vector3D u = _x;
  const Vector3D v = _y;
  const Vector3D w = _z;

  //Heading rotation
  bool isHeadingZero = heading.isZero();

  MutableMatrix44D hm = isHeadingZero ? MutableMatrix44D::invalid() :
                                        MutableMatrix44D::createGeneralRotationMatrix(heading, w, _origin);

  const Vector3D up = isHeadingZero ? u : u.transformedBy(hm, 1.0);
  const Vector3D vp = isHeadingZero ? v : v.transformedBy(hm, 1.0);
  const Vector3D wp = w;

  //Pitch rotation
  bool isPitchZero = pitch.isZero();

  MutableMatrix44D pm = isPitchZero?  MutableMatrix44D::invalid() :
                                      MutableMatrix44D::createGeneralRotationMatrix(pitch, up, _origin);

  const Vector3D upp = up;
  const Vector3D vpp = isPitchZero? vp : vp.transformedBy(pm, 1.0);
  const Vector3D wpp = isPitchZero? wp : wp.transformedBy(pm, 1.0);

  //Roll rotation
  bool isRollZero = roll.isZero();

  MutableMatrix44D rm = isRollZero? MutableMatrix44D::invalid() :
                                    MutableMatrix44D::createGeneralRotationMatrix(roll, vpp, _origin);

  const Vector3D uppp = isRollZero? upp : upp.transformedBy(rm, 1.0);
  const Vector3D vppp = vpp;
  const Vector3D wppp = isRollZero? wpp : wpp.transformedBy(rm, 1.0);

  return ReferenceSystem(uppp, vppp, wppp, _origin);
}