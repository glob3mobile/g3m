//
//  TrailsRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/23/12.
//
//

#include "TrailsRenderer.hpp"

#include "Mesh.hpp"
#include "DirectMesh.hpp"
#include "Planet.hpp"
#include "GLConstants.hpp"
#include "GLGlobalState.hpp"
#include "IFactory.hpp"
#include "IFloatBuffer.hpp"
#include "Camera.hpp"

Trail::~Trail() {
  delete _mesh;

  const int positionsSize = _positions.size();
  for (int i = 0; i < positionsSize; i++) {
    const Geodetic3D* position = _positions[i];
    delete position;
  }
}

Mesh* Trail::createMesh(const Planet* planet) {
  const int positionsSize = _positions.size();

  if (positionsSize < 2) {
    return NULL;
  }


  std::vector<double> anglesInRadians = std::vector<double>();
  for (int i = 1; i < positionsSize; i++) {
    const Geodetic3D* current  = _positions[i];
    const Geodetic3D* previous = _positions[i - 1];

    const double angleInRadians =  Geodetic2D::bearingInRadians(previous->_latitude, previous->_longitude,
                                                                current->_latitude, current->_longitude);
    if (i == 1) {
      anglesInRadians.push_back(angleInRadians);
      anglesInRadians.push_back(angleInRadians);
    }
    else {
      anglesInRadians.push_back(angleInRadians);
      const double avr = (angleInRadians + anglesInRadians[i - 1]) / 2.0;
      anglesInRadians[i - 1] = avr;
    }
  }


  float centerX = 0;
  float centerY = 0;
  float centerZ = 0;
  const Vector3D offsetP(_ribbonWidth/2, 0, 0);
  const Vector3D offsetN(-_ribbonWidth/2, 0, 0);

  IFloatBuffer* vertices = IFactory::instance()->createFloatBuffer(positionsSize * 3 * 2);

  const Vector3D rotationAxis = Vector3D::downZ();
  for (int i = 0; i < positionsSize; i++) {
    const Geodetic3D* position = _positions[i];

    const MutableMatrix44D rotationMatrix = MutableMatrix44D::createRotationMatrix(Angle::fromRadians(anglesInRadians[i]),
                                                                                   rotationAxis);
    const MutableMatrix44D geoMatrix = planet->createGeodeticTransformMatrix(*position);
    const MutableMatrix44D matrix = geoMatrix.multiply(rotationMatrix);

    const int i6 = i * 6;
    const Vector3D offsetNTransformed = offsetN.transformedBy(matrix, 1);
    if (i == 0) {
      centerX = (float) offsetNTransformed._x;
      centerY = (float) offsetNTransformed._y;
      centerZ = (float) offsetNTransformed._z;
    }
    vertices->rawPut(i6    , (float) offsetNTransformed._x - centerX);
    vertices->rawPut(i6 + 1, (float) offsetNTransformed._y - centerY);
    vertices->rawPut(i6 + 2, (float) offsetNTransformed._z - centerZ);


    const Vector3D offsetPTransformed = offsetP.transformedBy(matrix, 1);
    vertices->rawPut(i6 + 3, (float) offsetPTransformed._x - centerX);
    vertices->rawPut(i6 + 4, (float) offsetPTransformed._y - centerY);
    vertices->rawPut(i6 + 5, (float) offsetPTransformed._z - centerZ);
  }

  const Vector3D center(centerX, centerY, centerZ);
  Mesh* surfaceMesh = new DirectMesh(GLPrimitive::triangleStrip(),
                                     true,
                                     center,
                                     vertices,
                                     1,
                                     1,
                                     new Color(_color));

  // Debug unions
//  Mesh* edgesMesh = new DirectMesh(GLPrimitive::lines(),
//                                   false,
//                                   center,
//                                   vertices,
//                                   2,
//                                   1,
//                                   Color::newFromRGBA(1, 1, 1, 0.7f));
//
//  CompositeMesh* cm = new CompositeMesh();
//
//  cm->addMesh(surfaceMesh);
//  cm->addMesh(edgesMesh);
//
//  return cm;

  return surfaceMesh;
}

Mesh* Trail::getMesh(const Planet* planet) {
  if (_positionsDirty || (_mesh == NULL)) {
    delete _mesh;
    _mesh = createMesh(planet);
    _positionsDirty = false;
  }
  return _mesh;
}

void Trail::updateGLState(const G3MRenderContext* rc){

  const Camera* cam = rc->getCurrentCamera();
  if (_projection == NULL){
    _projection = new ProjectionGLFeature(cam->getProjectionMatrix44D());
    _glState.addGLFeature(_projection, true);
  } else{
    _projection->setMatrix(cam->getProjectionMatrix44D());
  }

  if (_model == NULL){
    _model = new ModelGLFeature(cam->getModelMatrix44D());
    _glState.addGLFeature(_model, true);
  } else{
    _model->setMatrix(cam->getModelMatrix44D());
  }
}

void Trail::render(const G3MRenderContext* rc) {
  if (_visible) {

    Mesh* mesh = getMesh(rc->getPlanet());
    if (mesh != NULL) {
      
      updateGLState(rc);

      mesh->render(rc, &_glState);
    }
  }
}

#pragma mark TrailsRenderer

TrailsRenderer::~TrailsRenderer() {
  const int trailsCount = _trails.size();
  for (int i = 0; i < trailsCount; i++) {
    Trail* trail = _trails[i];
    delete trail;
  }
  _trails.clear();
}

void TrailsRenderer::addTrail(Trail* trail) {
  if (trail != NULL) {
    _trails.push_back(trail);
  }
}

void TrailsRenderer::render(const G3MRenderContext* rc) {
  const int trailsCount = _trails.size();
  for (int i = 0; i < trailsCount; i++) {
    Trail* trail = _trails[i];
    trail->render(rc);
  }
}

void TrailsRenderer::initialize(const G3MContext* context) {
}

