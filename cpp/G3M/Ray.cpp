//
//  Ray.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 3/24/17.
//
//

#include "Ray.hpp"

#include "IStringBuilder.hpp"

#include "DirectMesh.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "Color.hpp"


Ray::Ray(const Vector3D& origin,
         const Vector3D& direction):
_origin(origin),
_direction(direction.normalized()),
_mesh(NULL)
{
}

Ray::~Ray() {
  delete _mesh;
}

double Ray::distanceTo(const Vector3D& point) const {
  return _direction.cross( point.sub(_origin) ).length();
}

double Ray::squaredDistanceTo(const Vector3D& point) const {
  return _direction.cross( point.sub(_origin) ).squaredLength();
}

const std::string Ray::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("(Ray origin=");
  isb->addString(_origin.description());
  isb->addString(", direction=");
  isb->addString(_direction.description());
  isb->addString(")");
  const std::string s = isb->getString();
  delete isb;
  return s;
}

void Ray::render(const G3MRenderContext* rc,
                 const GLState* parentState,
                 const Color& color) const {
  if (_mesh == NULL) {
    FloatBufferBuilderFromCartesian3D* vertices = FloatBufferBuilderFromCartesian3D::builderWithGivenCenter(_origin);

    vertices->add( _origin );
    vertices->add( _origin.add(_direction.times(100000)) );

    _mesh = new DirectMesh(GLPrimitive::lineStrip(),
                           true,
                           vertices->getCenter(),
                           vertices->create(),
                           2,
                           1,
                           new Color(color),
                           NULL, // const IFloatBuffer* colors
                           true  // bool depthTest
                           );

    delete vertices;
  }

  _mesh->render(rc, parentState);
}
