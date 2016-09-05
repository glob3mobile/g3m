//
//  Trail.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/5/16.
//
//

#include "Trail.hpp"


#define MAX_POSITIONS_PER_SEGMENT 64

#include "Vector3D.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "Planet.hpp"
#include "DirectMesh.hpp"


Trail::Segment::~Segment() {
  delete _previousSegmentLastPosition;
  delete _nextSegmentFirstPosition;

  delete _mesh;

  const size_t positionsSize = _positions.size();
  for (size_t i = 0; i < positionsSize; i++) {
    const Geodetic3D* position = _positions[i];
    delete position;
  }
}

void Trail::Segment::addPosition(const Angle& latitude,
                                 const Angle& longitude,
                                 const double height) {
  _positionsDirty = true;
  _positions.push_back(new Geodetic3D(latitude,
                                      longitude,
                                      height));
}

void Trail::Segment::addPosition(const Geodetic3D& position) {
  addPosition(position._latitude,
              position._longitude,
              position._height);
}

void Trail::Segment::setNextSegmentFirstPosition(const Angle& latitude,
                                                 const Angle& longitude,
                                                 const double height) {
  _positionsDirty = true;
  delete _nextSegmentFirstPosition;
  _nextSegmentFirstPosition = new Geodetic3D(latitude,
                                             longitude,
                                             height);
}

void Trail::Segment::setPreviousSegmentLastPosition(const Geodetic3D& position) {
  _positionsDirty = true;
  delete _previousSegmentLastPosition;
  _previousSegmentLastPosition = new Geodetic3D(position);
}

Geodetic3D Trail::Segment::getLastPosition() const {
#ifdef C_CODE
  return *(_positions[ _positions.size() - 1]);
#endif
#ifdef JAVA_CODE
  return _positions.get(_positions.size() - 1);
#endif
}

Geodetic3D Trail::Segment::getPreLastPosition() const {
#ifdef C_CODE
  return *(_positions[ _positions.size() - 2]);
#endif
#ifdef JAVA_CODE
  return _positions.get(_positions.size() - 2);
#endif
}

Mesh* Trail::Segment::getMesh(const Planet* planet) {
  if (_positionsDirty || (_mesh == NULL)) {
    delete _mesh;
    _mesh = createMesh(planet);
    _positionsDirty = false;
  }
  return _mesh;
}

Mesh* Trail::Segment::createMesh(const Planet* planet) {
  const size_t positionsSize = _positions.size();

  if (positionsSize < 2) {
    return NULL;
  }


  std::vector<double> anglesInRadians = std::vector<double>();
  for (size_t i = 1; i < positionsSize; i++) {
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
    const size_t lastPositionIndex = positionsSize - 1;
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
  for (size_t i = 0; i < positionsSize; i++) {
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
                                     NULL,  // colors
                                     0.0f,  // colorsIntensity
                                     true   // depthTest
                                     );

  delete vertices;

  return surfaceMesh;
}

void Trail::Segment::render(const G3MRenderContext* rc,
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


void Trail::clear() {
  const size_t segmentsSize = _segments.size();
  for (size_t i = 0; i < segmentsSize; i++) {
    Segment* segment = _segments[i];
    delete segment;
  }
  _segments.clear();
}

void Trail::addPosition(const Angle& latitude,
                        const Angle& longitude,
                        const double height) {
  const size_t segmentsSize = _segments.size();

  Segment* currentSegment;
  if (segmentsSize == 0) {
    Segment* newSegment = new Segment(_color, _ribbonWidth);
    _segments.push_back(newSegment);
    currentSegment = newSegment;
  }
  else {
    Segment* previousSegment = _segments[segmentsSize - 1];
    if (previousSegment->getSize() >= MAX_POSITIONS_PER_SEGMENT) {
      Segment* newSegment = new Segment(_color, _ribbonWidth);

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
  const size_t segmentsSize = _segments.size();
  for (size_t i = 0; i < segmentsSize; i++) {
    Segment* segment = _segments[i];
    delete segment;
  }
}

void Trail::render(const G3MRenderContext* rc,
                   const Frustum* frustum,
                   const GLState* state) {
  if (_visible) {
    const size_t segmentsSize = _segments.size();
    for (size_t i = 0; i < segmentsSize; i++) {
      Segment* segment = _segments[i];
      segment->render(rc, frustum, state);
    }
  }
}

