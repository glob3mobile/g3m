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
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "Camera.hpp"
//#include "CompositeMesh.hpp"

#define MAX_POSITIONS_PER_SEGMENT 128

TrailSegment::~TrailSegment() {
  delete _previousSegmentLastPosition;
  delete _nextSegmentFirstPosition;

  delete _mesh;

  const int positionsSize = _positions.size();
  for (int i = 0; i < positionsSize; i++) {
    const Geodetic3D* position = _positions[i];
    delete position;
  }
}

Mesh* TrailSegment::getMesh(const Planet* planet) {
  if (_positionsDirty || (_mesh == NULL)) {
    delete _mesh;
    _mesh = createMesh(planet);
    _positionsDirty = false;
  }
  return _mesh;
}

Mesh* TrailSegment::createMesh(const Planet* planet) {
  const int positionsSize = _positions.size();

  if (positionsSize < 2) {
    return NULL;
  }


  std::vector<double> anglesInRadians = std::vector<double>();
  for (int i = 1; i < positionsSize; i++) {
    const Geodetic3D* current  = _positions[i];
    const Geodetic3D* previous = _positions[i - 1];

    const double angleInRadians = Geodetic2D::bearingInRadians(previous->_latitude,
                                                               previous->_longitude,
                                                               current->_latitude,
                                                               current->_longitude);
    if (i == 1) {
      if (_previousSegmentLastPosition == NULL) {
        anglesInRadians.push_back(angleInRadians);
        anglesInRadians.push_back(angleInRadians);
      }
      else {
        const double angle2InRadians = Geodetic2D::bearingInRadians(_previousSegmentLastPosition->_latitude,
                                                                    _previousSegmentLastPosition->_longitude,
                                                                    previous->_latitude,
                                                                    previous->_longitude);
        const double avr = (angleInRadians + angle2InRadians) / 2.0;

        anglesInRadians.push_back(avr);
        anglesInRadians.push_back(avr);
      }
    }
    else {
      anglesInRadians.push_back(angleInRadians);
      const double avr = (angleInRadians + anglesInRadians[i - 1]) / 2.0;
      anglesInRadians[i - 1] = avr;
    }
  }

  if (_nextSegmentFirstPosition != NULL) {
    const int lastPositionIndex = positionsSize - 1;
    const Geodetic3D* lastPosition = _positions[lastPositionIndex];
    const double angleInRadians =  Geodetic2D::bearingInRadians(lastPosition->_latitude,
                                                                lastPosition->_longitude,
                                                                _nextSegmentFirstPosition->_latitude,
                                                                _nextSegmentFirstPosition->_longitude);

    const double avr = (angleInRadians + anglesInRadians[lastPositionIndex]) / 2.0;
    anglesInRadians[lastPositionIndex] = avr;
  }

  
  const Vector3D offsetP(_ribbonWidth/2, 0, 0);
  const Vector3D offsetN(-_ribbonWidth/2, 0, 0);

  FloatBufferBuilderFromCartesian3D* vertices = FloatBufferBuilderFromCartesian3D::builderWithFirstVertexAsCenter();


  const Vector3D rotationAxis = Vector3D::downZ();
  for (int i = 0; i < positionsSize; i++) {
    const Geodetic3D* position = _positions[i];

    const MutableMatrix44D rotationMatrix = MutableMatrix44D::createRotationMatrix(Angle::fromRadians(anglesInRadians[i]),
                                                                                   rotationAxis);
    const MutableMatrix44D geoMatrix = planet->createGeodeticTransformMatrix(*position);
    const MutableMatrix44D matrix = geoMatrix.multiply(rotationMatrix);

    vertices->add(offsetN.transformedBy(matrix, 1));
    vertices->add(offsetP.transformedBy(matrix, 1));
  }

  Mesh* surfaceMesh = new DirectMesh(GLPrimitive::triangleStrip(),
                                     true,
                                     vertices->getCenter(),
                                     vertices->create(),
                                     1,
                                     1,
                                     new Color(_color),
                                     NULL, // colors
                                     0.0f, // colorsIntensity
                                     true // depthTest
                                     );

  delete vertices;

  return surfaceMesh;
}

void Trail::clear() {
  const int segmentsSize = _segments.size();
  for (int i = 0; i < segmentsSize; i++) {
    TrailSegment* segment = _segments[i];
    delete segment;
  }
  _segments.clear();
}

void Trail::addPosition(const Angle& latitude,
                        const Angle& longitude,
                        const double height) {
  const int lastSegmentIndex = _segments.size() - 1;

  TrailSegment* currentSegment;
  if (lastSegmentIndex < 0) {
    TrailSegment* newSegment = new TrailSegment(_color, _ribbonWidth);
    _segments.push_back(newSegment);
    currentSegment = newSegment;
  }
  else {
    TrailSegment* previousSegment = _segments[lastSegmentIndex];
    if (previousSegment->getSize() > MAX_POSITIONS_PER_SEGMENT) {
      TrailSegment* newSegment = new TrailSegment(_color, _ribbonWidth);

      previousSegment->setNextSegmentFirstPosition(latitude,
                                                   longitude,
                                                   height  + _heightDelta);
      newSegment->setPreviousSegmentLastPosition( previousSegment->getPreLastPosition() );
      newSegment->addPosition( previousSegment->getLastPosition() );

      _segments.push_back(newSegment);
      currentSegment = newSegment;
    }
    else {
      currentSegment = previousSegment;
    }
  }

  currentSegment->addPosition(latitude,
                              longitude,
                              height  + _heightDelta);
}

Trail::~Trail() {
  const int segmentsSize = _segments.size();
  for (int i = 0; i < segmentsSize; i++) {
    TrailSegment* segment = _segments[i];
    delete segment;
  }
}


void TrailsRenderer::updateGLState(const G3MRenderContext* rc) {

  const Camera* cam = rc->getCurrentCamera();
  if (_projection == NULL) {
    _projection = new ProjectionGLFeature(cam->getProjectionMatrix44D());
    _glState->addGLFeature(_projection, true);
  }
  else {
    _projection->setMatrix(cam->getProjectionMatrix44D());
  }

  if (_model == NULL) {
    _model = new ModelGLFeature(cam->getModelMatrix44D());
    _glState->addGLFeature(_model, true);
  }
  else {
    _model->setMatrix(cam->getModelMatrix44D());
  }
}

void TrailSegment::render(const G3MRenderContext* rc,
                          const Frustum* frustum, const GLState* state) {
  Mesh* mesh = getMesh(rc->getPlanet());
  if (mesh != NULL) {
    BoundingVolume* bounding = mesh->getBoundingVolume();
    if (bounding != NULL) {
      if (bounding->touchesFrustum(frustum)) {
        mesh->render(rc, state);
      }
    }
  }
}


void Trail::render(const G3MRenderContext* rc,
                   const Frustum* frustum,
                   const GLState* state) {
  if (_visible) {
    const int segmentsSize = _segments.size();
    for (int i = 0; i < segmentsSize; i++) {
      TrailSegment* segment = _segments[i];
      segment->render(rc, frustum, state);
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

  _glState->_release();
}

void TrailsRenderer::addTrail(Trail* trail) {
  if (trail != NULL) {
    _trails.push_back(trail);
  }
}

void TrailsRenderer::render(const G3MRenderContext* rc, GLState* glState) {
  const int trailsCount = _trails.size();
  const Frustum* frustum = rc->getCurrentCamera()->getFrustumInModelCoordinates();
  updateGLState(rc);
  for (int i = 0; i < trailsCount; i++) {
    Trail* trail = _trails[i];
    if (trail != NULL) {
      trail->render(rc, frustum, _glState);
    }
  }
}

