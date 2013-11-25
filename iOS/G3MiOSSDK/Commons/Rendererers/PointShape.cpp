//
//  PointShape.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 22/11/13.
//
//

#include "PointShape.hpp"

#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "GLConstants.hpp"
#include "CompositeMesh.hpp"
#include "DirectMesh.hpp"
#include "OrientedBox.hpp"
#include "Camera.hpp"


PointShape::~PointShape() {
  delete _color;
  delete _originalColor;
  if (_boundingVolume)
    delete _boundingVolume;

#ifdef JAVA_CODE
  super.dispose();
#endif
  
}


Mesh* PointShape::createMesh(const G3MRenderContext* rc) {
  FloatBufferBuilderFromCartesian3D* vertices = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();
  
  vertices->add(0.0f, 0.0f, 0.0f);
  Color* color = (_color == NULL) ? NULL : new Color(*_color);
  
  Mesh* mesh = new DirectMesh(GLPrimitive::points(),
                              true,
                              vertices->getCenter(),
                              vertices->create(),
                              1,
                              _width,
                              color);
  
  delete vertices;
  return mesh;
}


std::vector<double> PointShape::intersectionsDistances(const Planet* planet,
                                                       const Camera* camera,
                                                       const Vector3D& origin,
                                                       const Vector3D& direction) {
  if (_boundingVolume != NULL)
    delete _boundingVolume;
  _boundingVolume = computeOrientedBox(planet, camera);
  return _boundingVolume->intersectionsDistances(origin, direction);
}


bool PointShape::isVisible(const G3MRenderContext *rc)
{
  return getBoundingVolume(rc)->touchesFrustum(rc->getCurrentCamera()->getFrustumInModelCoordinates());
}


OrientedBox* PointShape::computeOrientedBox(const Planet* planet,
                                            const Camera* camera) const
{
  const Vector3D upper = Vector3D(1000, 1000, 1000);
  const Vector3D lower = upper.times(-1);
  return new OrientedBox(lower, upper, *getTransformMatrix(planet));
}


BoundingVolume* PointShape::getBoundingVolume(const G3MRenderContext *rc)
{
  if (_boundingVolume != NULL)
    delete _boundingVolume;
  _boundingVolume = computeOrientedBox(rc->getPlanet(), rc->getCurrentCamera());
  return _boundingVolume;
}

