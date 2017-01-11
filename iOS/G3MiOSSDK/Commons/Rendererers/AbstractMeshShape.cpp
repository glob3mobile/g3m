//
//  AbstractMeshShape.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/5/12.
//
//

#include "AbstractMeshShape.hpp"
#include "Mesh.hpp"
#include "Camera.hpp"

AbstractMeshShape::~AbstractMeshShape() {
  delete _mesh;

#ifdef JAVA_CODE
  super.dispose();
#endif

}

bool AbstractMeshShape::touchesFrustum(const G3MRenderContext* rc) {
  Sphere* bound = (Sphere*) getBoundingSphere(rc);
  
  Vector3D centerInModelCoordinates = bound->_center.transformedBy(getTransformMatrix(rc->getPlanet()), 1.0);
  
  // bounding sphere in model coordinates
  Sphere* bsInModelCoordinates = new Sphere(centerInModelCoordinates,
                                           bound->_radius);
  
  const Frustum* frustum = rc->getCurrentCamera()->getFrustumInModelCoordinates();
  
  return bsInModelCoordinates->touchesFrustum(frustum);
}


BoundingVolume* AbstractMeshShape::getBoundingSphere(const G3MRenderContext* rc) {
  if (boundingSphere == NULL) {
    boundingSphere = getMesh(rc)->getBoundingVolume()->createSphere();
  }
  return boundingSphere;
}


void AbstractMeshShape::cleanMesh() {
  delete _mesh;
  _mesh = NULL;
}


Mesh* AbstractMeshShape::getMesh(const G3MRenderContext* rc) {
  if (_mesh == NULL) {
    _mesh = createMesh(rc);
  }
  return _mesh;
}

bool AbstractMeshShape::isReadyToRender(const G3MRenderContext* rc) {
  const Mesh* mesh = getMesh(rc);
  return (mesh != NULL);
}

void AbstractMeshShape::rawRender(const G3MRenderContext* rc,
               GLState* parentState,
               bool renderNotReadyShapes) {
  Mesh* mesh = getMesh(rc);
  if (mesh != NULL) {
    mesh->render(rc, parentState);
  }
}

bool AbstractMeshShape::isTransparent(const G3MRenderContext* rc) {
  const Mesh* mesh = getMesh(rc);
  if (mesh == NULL) {
    return false;
  }
  return mesh->isTransparent(rc);
}
