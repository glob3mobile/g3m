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
#include "GLState.hpp"
#include "IFactory.hpp"
#include "IFloatBuffer.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "Camera.hpp"
//#include "CompositeMesh.hpp"

#define MAX_POSITIONS_PER_SEGMENT 64

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

  FloatBufferBuilderFromCartesian3D vertices(CenterStrategy::firstVertex(),
                                             Vector3D::zero());

  const Vector3D rotationAxis = Vector3D::downZ();
  for (int i = 0; i < positionsSize; i++) {
    const Geodetic3D* position = _positions[i];

    const MutableMatrix44D rotationMatrix = MutableMatrix44D::createRotationMatrix(Angle::fromRadians(anglesInRadians[i]),
                                                                                   rotationAxis);
    const MutableMatrix44D geoMatrix = planet->createGeodeticTransformMatrix(*position);
    const MutableMatrix44D matrix = geoMatrix.multiply(rotationMatrix);

    vertices.add(offsetN.transformedBy(matrix, 1));
    vertices.add(offsetP.transformedBy(matrix, 1));
  }

  Mesh* surfaceMesh = new DirectMesh(GLPrimitive::triangleStrip(),
                                     true,
                                     vertices.getCenter(),
                                     vertices.create(),
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

  //  FloatBufferBuilderFromGeodetic vertices(CenterStrategy::firstVertex(),
  //                                          planet,
  //                                          Geodetic3D::fromDegrees(0, 0, 0));
  //
  //  const int positionsSize = _positions.size();
  //  for (int i = 0; i < positionsSize; i++) {
  //#ifdef C_CODE
  //    vertices.add( *(_positions[i]) );
  //#endif
  //#ifdef JAVA_CODE
  //	  vertices.add( _positions.get(i) );
  //#endif
  //  }
  //
  //  return new DirectMesh(GLPrimitive::lineStrip(),
  //                        true,
  //                        vertices.getCenter(),
  //                        vertices.create(),
  //                        _lineWidth,
  //                        1,
  //                        new Color(_color));
}

void Trail::addPosition(const Geodetic3D& position) {

  const int lastSegmentIndex = _segments.size() - 1;

  TrailSegment* currentSegment;
  if ((lastSegmentIndex < 0) ||
      (_segments[lastSegmentIndex]->getSize() > MAX_POSITIONS_PER_SEGMENT)) {

    TrailSegment* newSegment = new TrailSegment(_color, _ribbonWidth);
    if (lastSegmentIndex >= 0) {
      TrailSegment* previousSegment = _segments[lastSegmentIndex];
      previousSegment->setNextSegmentFirstPosition( position );
      newSegment->setPreviousSegmentLastPosition( previousSegment->getPreLastPosition() );
      newSegment->addPosition( previousSegment->getLastPosition() );
    }
    _segments.push_back(newSegment);
    currentSegment = newSegment;
  }
  else {
    currentSegment = _segments[lastSegmentIndex];
  }

  currentSegment->addPosition(position);
}

Trail::~Trail() {
  //  delete _mesh;
  //
  //  const int positionsSize = _positions.size();
  //  for (int i = 0; i < positionsSize; i++) {
  //    const Geodetic3D* position = _positions[i];
  //    delete position;
  //  }
  const int segmentsSize = _segments.size();
  for (int i = 0; i < segmentsSize; i++) {
    TrailSegment* segment = _segments[i];
    delete segment;
  }
}

void TrailSegment::render(const G3MRenderContext* rc,
                          const GLState& parentState,
                          const Frustum* frustum) {
  Mesh* mesh = getMesh(rc->getPlanet());
  if (mesh != NULL) {
    BoundingVolume* bounding = mesh->getBoundingVolume();
    if (bounding != NULL) {
      if (bounding->touchesFrustum(frustum)) {
        mesh->render(rc, parentState);
      }
    }
  }
}


void Trail::render(const G3MRenderContext* rc,
                   const GLState& parentState,
                   const Frustum* frustum) {
//  if (_visible) {
//    Mesh* mesh = getMesh(rc->getPlanet());
//    if (mesh != NULL) {
//      mesh->render(rc, parentState);
//    }
//  }

  if (_visible) {
    const int segmentsSize = _segments.size();
    for (int i = 0; i < segmentsSize; i++) {
      TrailSegment* segment = _segments[i];
      segment->render(rc, parentState, frustum);
    }
  }
}

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

void TrailsRenderer::render(const G3MRenderContext* rc,
                            const GLState& parentState) {
  const int trailsCount = _trails.size();
  const Frustum* frustum = rc->getCurrentCamera()->getFrustumInModelCoordinates();
  for (int i = 0; i < trailsCount; i++) {
    Trail* trail = _trails[i];
    if (trail != NULL) {
      trail->render(rc, parentState, frustum);
    }
  }
}
