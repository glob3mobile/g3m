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
  fbb->add(_origin.add(_x));
  colors.add(xColor);
  colors.add(xColor);

  fbb->add(_origin);
  fbb->add(_origin.add(_y));
  colors.add(yColor);
  colors.add(yColor);

  fbb->add(_origin);
  fbb->add(_origin.add(_z));
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