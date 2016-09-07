//
//  Trail.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/5/16.
//
//

#include "Trail.hpp"


#include "Vector3D.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "Planet.hpp"
#include "DirectMesh.hpp"
#include "IFactory.hpp"
#include "G3MRenderContext.hpp"
#include "BoundingVolume.hpp"
#include "IFloatBuffer.hpp"
#include "MutableMatrix44D.hpp"
#include "GLConstants.hpp"


Trail::Segment::Segment(const Color& color,
                        float ribbonWidth,
                        double visibleAlpha) :
_color(color),
_ribbonWidth(ribbonWidth),
_visibleAlpha(visibleAlpha),
_alphaStatus(Trail::SegmentAlphaStatus::UNKNOWN),
_minAlpha( IMathUtils::instance()->maxDouble() ),
_maxAlpha( IMathUtils::instance()->minDouble() ),
_positionsDirty(true),
_mesh(NULL),
_nextSegmentFirstPosition(NULL),
_previousSegmentLastPosition(NULL)
{
}

Trail::Segment::~Segment() {
  delete _previousSegmentLastPosition;
  delete _nextSegmentFirstPosition;

  delete _mesh;

  const size_t positionsSize = _positions.size();
  for (size_t i = 0; i < positionsSize; i++) {
    const Position* position = _positions[i];
    delete position;
  }
}

size_t Trail::Segment::getSize() const {
  return _positions.size();
}

void Trail::Segment::addPosition(const Angle& latitude,
                                 const Angle& longitude,
                                 const double height,
                                 const double alpha) {
  _positionsDirty = true;
  _positions.push_back(new Position(latitude,
                                    longitude,
                                    height,
                                    alpha));
  if (alpha < _minAlpha) { _minAlpha = alpha; _alphaStatus = Trail::SegmentAlphaStatus::UNKNOWN; }
  if (alpha > _maxAlpha) { _maxAlpha = alpha; _alphaStatus = Trail::SegmentAlphaStatus::UNKNOWN; }
}

void Trail::Segment::addPosition(const Position& position) {
  addPosition(position._latitude,
              position._longitude,
              position._height,
              position._alpha);
}

void Trail::Segment::setNextSegmentFirstPosition(const Angle& latitude,
                                                 const Angle& longitude,
                                                 const double height,
                                                 const double alpha) {
  _positionsDirty = true;
  delete _nextSegmentFirstPosition;
  _nextSegmentFirstPosition = new Position(latitude,
                                           longitude,
                                           height,
                                           alpha);
}

void Trail::Segment::setPreviousSegmentLastPosition(const Position& position) {
  _positionsDirty = true;
  delete _previousSegmentLastPosition;
  _previousSegmentLastPosition = new Position(position._latitude,
                                              position._longitude,
                                              position._height,
                                              position._alpha);
}

Trail::Position Trail::Segment::getLastPosition() const {
#ifdef C_CODE
  return *(_positions[ _positions.size() - 1]);
#endif
#ifdef JAVA_CODE
  return _positions.get(_positions.size() - 1);
#endif
}

Trail::Position Trail::Segment::getPreLastPosition() const {
#ifdef C_CODE
  return *(_positions[ _positions.size() - 2]);
#endif
#ifdef JAVA_CODE
  return _positions.get(_positions.size() - 2);
#endif
}

bool Trail::SegmentMeshUserData::isValid(const SegmentAlphaStatus status,
                                         const double visibleAlpha) const {
  if (status != _status) {
    return false;
  }

  if ((status == Trail::SegmentAlphaStatus::HALF) && (visibleAlpha != _visibleAlpha)) {
    return false;
  }

  return true;
}

bool Trail::Segment::isMeshValid() const {
  if (_positionsDirty || (_mesh == NULL)) {
    return false;
  }

  const SegmentMeshUserData* userData = (SegmentMeshUserData*) _mesh->getUserData();
  return userData->isValid(_alphaStatus, _visibleAlpha);
}

Mesh* Trail::Segment::getMesh(const Planet* planet) {
  if (!isMeshValid()) {
    delete _mesh;
    _mesh = createMesh(planet);
    _positionsDirty = false;
  }
  return _mesh;
}

const IFloatBuffer* Trail::Segment::getBearingsInRadians() const {
  const size_t positionsSize = _positions.size();

  IFloatBuffer* bearingsInRadians = IFactory::instance()->createFloatBuffer(positionsSize);

  for (size_t i = 1; i < positionsSize; i++) {
    const Position* current  = _positions[i];
    const Position* previous = _positions[i - 1];

    const float angleInRadians = (float) Geodetic2D::bearingInRadians(previous->_latitude,
                                                                      previous->_longitude,
                                                                      current->_latitude,
                                                                      current->_longitude);
    if (i == 1) {
      if (_previousSegmentLastPosition == NULL) {
        bearingsInRadians->rawPut(0, angleInRadians);
        bearingsInRadians->rawPut(1, angleInRadians);
      }
      else {
        const float angle2InRadians = (float) Geodetic2D::bearingInRadians(_previousSegmentLastPosition->_latitude,
                                                                           _previousSegmentLastPosition->_longitude,
                                                                           previous->_latitude,
                                                                           previous->_longitude);
        const float avr = (angleInRadians + angle2InRadians) / 2.0f;

        bearingsInRadians->rawPut(0, avr);
        bearingsInRadians->rawPut(1, avr);
      }
    }
    else {
      bearingsInRadians->rawPut(i, angleInRadians);
      const float avr = (angleInRadians + bearingsInRadians->get(i - 1)) / 2.0f;
      bearingsInRadians->rawPut(i - 1, avr);
    }
  }

  if (_nextSegmentFirstPosition != NULL) {
    const size_t lastPositionIndex = positionsSize - 1;
    const Position* lastPosition = _positions[lastPositionIndex];
    const float angleInRadians = (float) Geodetic2D::bearingInRadians(lastPosition->_latitude,
                                                                      lastPosition->_longitude,
                                                                      _nextSegmentFirstPosition->_latitude,
                                                                      _nextSegmentFirstPosition->_longitude);

    const float avr = (angleInRadians + bearingsInRadians->get(lastPositionIndex)) / 2.0f;
    bearingsInRadians->rawPut(lastPositionIndex, avr);
  }

  return bearingsInRadians;
}

Mesh* Trail::Segment::createMesh(const Planet* planet) {
  const size_t positionsSize = _positions.size();

  if (positionsSize < 2) {
    return NULL;
  }

  const IFloatBuffer* bearings = getBearingsInRadians();

  const Vector3D offsetP(_ribbonWidth/2, 0, 0);
  const Vector3D offsetN(-_ribbonWidth/2, 0, 0);

  FloatBufferBuilderFromCartesian3D* vertices = FloatBufferBuilderFromCartesian3D::builderWithFirstVertexAsCenter();

  const Vector3D rotationAxis = Vector3D::downZ();
  for (size_t i = 0; i < positionsSize; i++) {
    const Position* position = _positions[i];

    const MutableMatrix44D rotationMatrix = MutableMatrix44D::createRotationMatrix(Angle::fromRadians(bearings->get(i)),
                                                                                   rotationAxis);
    const MutableMatrix44D geoMatrix = planet->createGeodeticTransformMatrix(position->_latitude,
                                                                             position->_longitude,
                                                                             position->_height);
    const MutableMatrix44D matrix = geoMatrix.multiply(rotationMatrix);

    vertices->add(offsetN.transformedBy(matrix, 1));
    vertices->add(offsetP.transformedBy(matrix, 1));
  }

  delete bearings;

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

  surfaceMesh->setUserData(new SegmentMeshUserData(_alphaStatus, _visibleAlpha));

  return surfaceMesh;
}


Trail::SegmentAlphaStatus Trail::Segment::calculateAlphaStatus() {
  if (_visibleAlpha <= _minAlpha) {
    return Trail::SegmentAlphaStatus::FULL_HIDDEN;
  }
  else if (_visibleAlpha >= _maxAlpha) {
    return Trail::SegmentAlphaStatus::FULL_VISIBLE;
  }
  else {
    return Trail::SegmentAlphaStatus::HALF;
  }
}


void Trail::Segment::render(const G3MRenderContext* rc,
                            const Frustum* frustum,
                            const GLState* state) {

  if (_alphaStatus == Trail::SegmentAlphaStatus::UNKNOWN) {
    _alphaStatus = calculateAlphaStatus();
  }

  switch (_alphaStatus) {
    case UNKNOWN:
    case FULL_HIDDEN:
      return;

    case HALF:
      break;

    case FULL_VISIBLE:
      break;
  }

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

void Trail::Segment::setVisibleAlpha(double visibleAlpha) {
  if (visibleAlpha != _visibleAlpha) {
    _visibleAlpha = visibleAlpha;
    _alphaStatus = Trail::SegmentAlphaStatus::UNKNOWN;
  }
}



Trail::Trail(const Color& color,
             float ribbonWidth,
             double deltaHeight,
             int maxPositionsPerSegment):
_visible(true),
_color(color),
_ribbonWidth(ribbonWidth),
_deltaHeight(deltaHeight),
_maxPositionsPerSegment(maxPositionsPerSegment),
_alpha(1.0)
{
}

Trail::~Trail() {
  const size_t segmentsSize = _segments.size();
  for (size_t i = 0; i < segmentsSize; i++) {
    Segment* segment = _segments[i];
    delete segment;
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
                        const double height,
                        const double alpha) {
  Segment* currentSegment;

  const size_t segmentsSize = _segments.size();
  if (segmentsSize == 0) {
    currentSegment = new Segment(_color, _ribbonWidth, _alpha);
    _segments.push_back(currentSegment);
  }
  else {
    currentSegment = _segments[segmentsSize - 1];

    if (currentSegment->getSize() >= _maxPositionsPerSegment) {
      Segment* newSegment = new Segment(_color, _ribbonWidth, _alpha);
      _segments.push_back(newSegment);

      currentSegment->setNextSegmentFirstPosition(latitude,
                                                  longitude,
                                                  height + _deltaHeight,
                                                  alpha);
      newSegment->setPreviousSegmentLastPosition( currentSegment->getPreLastPosition() );
      newSegment->addPosition( currentSegment->getLastPosition() );

      currentSegment = newSegment;
    }
  }

  currentSegment->addPosition(latitude,
                              longitude,
                              height + _deltaHeight,
                              alpha);
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

void Trail::setAlpha(const double alpha) {
  const double a = IMathUtils::instance()->clamp(alpha, 0.0, 1.0);
  if (a != _alpha) {
    _alpha = a;
    const size_t segmentsSize = _segments.size();
    for (size_t i = 0; i < segmentsSize; i++) {
      Segment* segment = _segments[i];
      segment->setVisibleAlpha(_alpha);
    }
  }
}
