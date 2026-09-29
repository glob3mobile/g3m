//
//  Trail.cpp
//  G3M
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
#include "IMathUtils.hpp"
#include "Geodetic2D.hpp"
#include "Geodetic3D.hpp"
#include "GLState.hpp"
#include "GLFeature.hpp"
#include "Camera.hpp"


const int Trail::SEGMENT_ALPHA_STATUS_UNKNOWN       = 1;
const int Trail::SEGMENT_ALPHA_STATUS_FULL_HIDDEN   = 2;
const int Trail::SEGMENT_ALPHA_STATUS_HALF          = 3;
const int Trail::SEGMENT_ALPHA_STATUS_FULL_VISIBLE  = 4;

Trail::Segment::Segment(const Color& color,
                        const float  ribbonWidth,
                        const float  minWidthPixels,
                        const bool   depthTest,
                        const bool   polygonOffsetFill,
                        const float  polygonOffsetFactor,
                        const float  polygonOffsetUnits,
                        const double visibleAlpha) :
_color(color),
_ribbonWidth(ribbonWidth),
_minWidthPixels(minWidthPixels),
_depthTest(depthTest),
_polygonOffsetFill(polygonOffsetFill),
_polygonOffsetFactor(polygonOffsetFactor),
_polygonOffsetUnits(polygonOffsetUnits),
_visibleAlpha(visibleAlpha),
_alphaStatus(Trail::SEGMENT_ALPHA_STATUS_UNKNOWN),
_minAlpha( IMathUtils::instance()->maxDouble() ),
_maxAlpha( IMathUtils::instance()->minDouble() ),
_positionsDirty(true),
_mesh(NULL),
_ribbonSides(NULL),
_ribbonGLState(NULL),
_nextSegmentFirstPosition(NULL),
_previousSegmentLastPosition(NULL)
{
}

Trail::Segment::~Segment() {
  delete _previousSegmentLastPosition;
  delete _nextSegmentFirstPosition;

  delete _mesh;
  setRibbonSides(NULL);

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
                                 const double alpha,
                                 const Angle& heading) {
  _positionsDirty = true;
  _positions.push_back(new Position(latitude,
                                    longitude,
                                    height,
                                    alpha,
                                    heading));
  if (alpha < _minAlpha) { _minAlpha = alpha; _alphaStatus = Trail::SEGMENT_ALPHA_STATUS_UNKNOWN; }
  if (alpha > _maxAlpha) { _maxAlpha = alpha; _alphaStatus = Trail::SEGMENT_ALPHA_STATUS_UNKNOWN; }
}

void Trail::Segment::addPosition(const Position& position) {
  addPosition(position._latitude,
              position._longitude,
              position._height,
              position._alpha,
              position._heading);
}

void Trail::Segment::setNextSegmentFirstPosition(const Angle& latitude,
                                                 const Angle& longitude,
                                                 const double height,
                                                 const double alpha,
                                                 const Angle& heading) {
  _positionsDirty = true;
  delete _nextSegmentFirstPosition;
  _nextSegmentFirstPosition = new Position(latitude,
                                           longitude,
                                           height,
                                           alpha,
                                           heading);
}

void Trail::Segment::setPreviousSegmentLastPosition(const Position& position) {
  _positionsDirty = true;
  delete _previousSegmentLastPosition;
  _previousSegmentLastPosition = new Position(position._latitude,
                                              position._longitude,
                                              position._height,
                                              position._alpha,
                                              position._heading);
}

Trail::Position Trail::Segment::getLastPosition() const {
#ifdef C_CODE
  return *(_positions[ _positions.size() - 1]);
#endif
#ifdef JAVA_CODE
  return _positions.get(_positions.size() - 1);
#endif
}

Trail::Position Trail::Segment::getPenultimatePosition() const {
#ifdef C_CODE
  return *(_positions[ _positions.size() - 2]);
#endif
#ifdef JAVA_CODE
  return _positions.get(_positions.size() - 2);
#endif
}

bool Trail::SegmentMeshUserData::isValid(const int    status,
                                         const double visibleAlpha) const {
  if (status != _status) {
    return false;
  }

  if ((status == Trail::SEGMENT_ALPHA_STATUS_HALF) && (visibleAlpha != _visibleAlpha)) {
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

void Trail::Segment::setRibbonSides(IFloatBuffer* ribbonSides) {
  if (_ribbonGLState != NULL) {
    _ribbonGLState->_release();
    _ribbonGLState = NULL;
  }
  delete _ribbonSides;
  _ribbonSides = ribbonSides;

  if (_ribbonSides != NULL) {
    _ribbonGLState = new GLState();
    _ribbonGLState->addGLFeature(new RibbonSideGLFeature(_ribbonSides), false);
  }
}

const GLState* Trail::Segment::getMeshGLState(const GLState* parent) {
  if (_ribbonGLState == NULL) {
    return parent;
  }
  _ribbonGLState->setParent(parent);
  return _ribbonGLState;
}

const IFloatBuffer* Trail::Segment::getBearingsInRadians() const {
  const size_t positionsSize = _positions.size();

  IFloatBuffer* bearingsInRadians = IFactory::instance()->createFloatBuffer(positionsSize);

  for (size_t i = 1; i < positionsSize; i++) {
    const Position* previous = _positions[i - 1];
    const Position* current  = _positions[i];

    const float angleInRadians = (float) (current->_heading.isNan()
                                          ? Geodetic2D::bearingInRadians(previous->_latitude,
                                                                         previous->_longitude,
                                                                         current->_latitude,
                                                                         current->_longitude)
                                          : current->_heading._radians);
    if (i == 1) {
      if (_previousSegmentLastPosition == NULL) {
        bearingsInRadians->rawPut(0, angleInRadians);
        bearingsInRadians->rawPut(1, angleInRadians);
      }
      else {
        const float previousAngleInRadians = (float) (previous->_heading.isNan()
                                                      ?  Geodetic2D::bearingInRadians(_previousSegmentLastPosition->_latitude,
                                                                                      _previousSegmentLastPosition->_longitude,
                                                                                      previous->_latitude,
                                                                                      previous->_longitude)
                                                      : previous->_heading._radians);
        const float avr = (previousAngleInRadians + angleInRadians) / 2.0f;

        bearingsInRadians->rawPut(0, avr);
        bearingsInRadians->rawPut(1, avr);
      }
    }
    else {
      const float previousAngleInRadians = bearingsInRadians->get(i - 1);

      const float avr = (previousAngleInRadians + angleInRadians) / 2.0f;
      bearingsInRadians->rawPut(i - 1, avr);

      bearingsInRadians->rawPut(i, angleInRadians);
    }
  }

  if (_nextSegmentFirstPosition != NULL) {
    const size_t lastPositionIndex = positionsSize - 1;
    const Position* lastPosition = _positions[lastPositionIndex];
    const float angleInRadians = (float) (_nextSegmentFirstPosition->_heading.isNan()
                                          ? Geodetic2D::bearingInRadians(lastPosition->_latitude,
                                                                         lastPosition->_longitude,
                                                                         _nextSegmentFirstPosition->_latitude,
                                                                         _nextSegmentFirstPosition->_longitude)
                                          : _nextSegmentFirstPosition->_heading._radians);

    const float avr = (angleInRadians + bearingsInRadians->get(lastPositionIndex)) / 2.0f;
    bearingsInRadians->rawPut(lastPositionIndex, avr);
  }

  return bearingsInRadians;
}

const MutableMatrix44D Trail::Segment::createMatrix(const Angle& bearing,
                                                    const Angle& latitude,
                                                    const Angle& longitude,
                                                    const double height,
                                                    const Vector3D& rotationAxis,
                                                    const Planet* planet) const {
  const MutableMatrix44D rotationMatrix = MutableMatrix44D::createRotationMatrix(bearing,
                                                                                 rotationAxis);

  const MutableMatrix44D geoMatrix = planet->createGeodeticTransformMatrix(latitude,
                                                                           longitude,
                                                                           height);
  return geoMatrix.multiply(rotationMatrix);
}

void Trail::Segment::addRibbonVertices(const MutableMatrix44D& matrix,
                                       FloatBufferBuilderFromCartesian3D* vertices,
                                       FloatBufferBuilderFromCartesian3D* sideVectors) const {
  if (sideVectors == NULL) {
    const Vector3D offsetN(-_ribbonWidth/2, 0, 0);
    const Vector3D offsetP( _ribbonWidth/2, 0, 0);
    vertices->add(offsetN.transformedBy(matrix, 1));
    vertices->add(offsetP.transformedBy(matrix, 1));
  }
  else {
    // both edges sit on the center line; RibbonMesh.vsh pushes them apart along ±side
    const Vector3D center = Vector3D::ZERO.transformedBy(matrix, 1);
    const Vector3D side   = Vector3D::UP_X.transformedBy(matrix, 0).normalized();
    vertices->add(center);
    sideVectors->add(side.times(-1));
    vertices->add(center);
    sideVectors->add(side);
  }
}

Mesh* Trail::Segment::createMesh(const Planet* planet) {
  const size_t positionsSize = _positions.size();

  if (positionsSize < 2) {
    return NULL;
  }

  const IFloatBuffer* bearings = getBearingsInRadians();

  FloatBufferBuilderFromCartesian3D* vertices = FloatBufferBuilderFromCartesian3D::builderWithFirstVertexAsCenter();
  FloatBufferBuilderFromCartesian3D* sideVectors = (_minWidthPixels > 0)
  ? FloatBufferBuilderFromCartesian3D::builderWithoutCenter()
  : NULL;

  double lastAlpha = 0;

  const Vector3D rotationAxis = Vector3D::DOWN_Z;
  for (size_t i = 0; i < positionsSize; i++) {
    const Position* position = _positions[i];

    if (_alphaStatus == Trail::SEGMENT_ALPHA_STATUS_HALF) {
      if (position->_alpha > _visibleAlpha) {
        if (lastAlpha < _visibleAlpha) {
          if (i > 0) {
            const Position* previousPosition = _positions[i-1];
            const double normalizedAlpha = (_visibleAlpha - previousPosition->_alpha) / (position->_alpha - previousPosition->_alpha);

            const MutableMatrix44D matrix = createMatrix(Angle::fromRadians(bearings->get(i)),
                                                         Angle::linearInterpolation(previousPosition->_latitude,
                                                                                    position->_latitude,
                                                                                    normalizedAlpha),
                                                         Angle::linearInterpolation(previousPosition->_longitude,
                                                                                    position->_longitude,
                                                                                    normalizedAlpha),
                                                         IMathUtils::instance()->linearInterpolation(previousPosition->_height,
                                                                                                     position->_height,
                                                                                                     normalizedAlpha),
                                                         rotationAxis,
                                                         planet);

            addRibbonVertices(matrix, vertices, sideVectors);
          }
        }
        break;
      }
    }

    lastAlpha = position->_alpha;

    const MutableMatrix44D matrix = createMatrix(Angle::fromRadians(bearings->get(i)),
                                                 position->_latitude,
                                                 position->_longitude,
                                                 position->_height,
                                                 rotationAxis,
                                                 planet);

    addRibbonVertices(matrix, vertices, sideVectors);
  }

  delete bearings;

  Mesh* surfaceMesh = new DirectMesh(GLPrimitive::triangleStrip(),  // primitive
                                     true,                          // owner
                                     vertices->getCenter(),         // center
                                     vertices->create(),            // vertices
                                     1,                             // lineWidth
                                     1,                             // pointSize
                                     new Color(_color),             // flatColor
                                     NULL,                          // colors
                                     _depthTest,                    // depthTest
                                     NULL,                          // normals
                                     _polygonOffsetFill,            // polygonOffsetFill
                                     _polygonOffsetFactor,          // polygonOffsetFactor
                                     _polygonOffsetUnits,           // polygonOffsetUnits
                                     false,                         // cullFace
                                     GLCullFace::back()             // culledFace
                                     );

  delete vertices;

  setRibbonSides((sideVectors == NULL) ? NULL : sideVectors->create());
  delete sideVectors;

  surfaceMesh->setUserData(new SegmentMeshUserData(_alphaStatus, _visibleAlpha));

  return surfaceMesh;
}

int Trail::Segment::calculateAlphaStatus() {
  if (_visibleAlpha <= _minAlpha) {
    return Trail::SEGMENT_ALPHA_STATUS_FULL_HIDDEN;
  }
  else if (_visibleAlpha >= _maxAlpha) {
    return Trail::SEGMENT_ALPHA_STATUS_FULL_VISIBLE;
  }
  else {
    return Trail::SEGMENT_ALPHA_STATUS_HALF;
  }
}

void Trail::Segment::render(const G3MRenderContext* rc,
                            const Frustum* frustum,
                            const GLState* state) {

  if (_alphaStatus == Trail::SEGMENT_ALPHA_STATUS_UNKNOWN) {
    _alphaStatus = calculateAlphaStatus();
  }

  if ((_alphaStatus != Trail::SEGMENT_ALPHA_STATUS_UNKNOWN) &&
      (_alphaStatus != Trail::SEGMENT_ALPHA_STATUS_FULL_HIDDEN)) {
    Mesh* mesh = getMesh(rc->getPlanet());
    if (mesh != NULL) {
      BoundingVolume* bounding = mesh->getBoundingVolume();
      if (bounding != NULL) {
        if (bounding->touchesFrustum(frustum)) {
          mesh->render(rc, getMeshGLState(state));
        }
      }
    }
  }
}

void Trail::Segment::setVisibleAlpha(double visibleAlpha) {
  if (visibleAlpha != _visibleAlpha) {
    _visibleAlpha = visibleAlpha;
    _alphaStatus = Trail::SEGMENT_ALPHA_STATUS_UNKNOWN;
  }
}



Trail::Trail(const Color& color,
             const float  ribbonWidth,
             const bool   depthTest,
             const bool   polygonOffsetFill,
             const float  polygonOffsetFactor,
             const float  polygonOffsetUnits,
             const double deltaHeight,
             const int    maxPositionsPerSegment,
             const float  minWidthPixels) :
_visible(true),
_color(color),
_ribbonWidth(ribbonWidth),
_minWidthPixels(minWidthPixels),
_depthTest(depthTest),
_polygonOffsetFill(polygonOffsetFill),
_polygonOffsetFactor(polygonOffsetFactor),
_polygonOffsetUnits(polygonOffsetUnits),
_deltaHeight(deltaHeight),
_maxPositionsPerSegment(maxPositionsPerSegment),
_alpha(1.0),
_ribbonGLState(NULL),
_ribbonViewportExtent(NULL)
{
  if (_minWidthPixels > 0) {
    _ribbonGLState = new GLState();
    _ribbonGLState->addGLFeature(new RibbonWidthGLFeature(_ribbonWidth, _minWidthPixels),
                                 false);
    _ribbonViewportExtent = new ViewportExtentGLFeature(0, 0); // real extent set on every render
    _ribbonGLState->addGLFeature(_ribbonViewportExtent, false);
  }
}

Trail::~Trail() {
  const size_t segmentsSize = _segments.size();
  for (size_t i = 0; i < segmentsSize; i++) {
    Segment* segment = _segments[i];
    delete segment;
  }

  if (_ribbonGLState != NULL) {
    _ribbonGLState->_release();
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

void Trail::addPosition(const Geodetic2D& position,
                        const double height,
                        const double alpha,
                        const Angle& heading) {
  addPosition(position._latitude,
              position._longitude,
              height,
              alpha,
              heading);
}

void Trail::addPosition(const Geodetic3D& position,
                        const double alpha,
                        const Angle& heading) {
  addPosition(position._latitude,
              position._longitude,
              position._height,
              alpha,
              heading);
}

void Trail::addPosition(const Angle& latitude,
                        const Angle& longitude,
                        const double height,
                        const double alpha,
                        const Angle& heading) {
  Segment* currentSegment;

  const size_t segmentsSize = _segments.size();
  if (segmentsSize == 0) {
    currentSegment = new Segment(_color,
                                 _ribbonWidth,
                                 _minWidthPixels,
                                 _depthTest,
                                 _polygonOffsetFill,
                                 _polygonOffsetFactor,
                                 _polygonOffsetUnits,
                                 _alpha);
    _segments.push_back(currentSegment);
  }
  else {
    currentSegment = _segments[segmentsSize - 1];

    if (currentSegment->getSize() >= _maxPositionsPerSegment) {
      Segment* newSegment = new Segment(_color,
                                        _ribbonWidth,
                                        _minWidthPixels,
                                        _depthTest,
                                        _polygonOffsetFill,
                                        _polygonOffsetFactor,
                                        _polygonOffsetUnits,
                                        _alpha);
      _segments.push_back(newSegment);

      currentSegment->setNextSegmentFirstPosition(latitude,
                                                  longitude,
                                                  height + _deltaHeight,
                                                  alpha,
                                                  heading);
      newSegment->setPreviousSegmentLastPosition( currentSegment->getPenultimatePosition() );
      newSegment->addPosition( currentSegment->getLastPosition() );

      currentSegment = newSegment;
    }
  }

  currentSegment->addPosition(latitude,
                              longitude,
                              height + _deltaHeight,
                              alpha,
                              heading);
}

const GLState* Trail::getSegmentsGLState(const G3MRenderContext* rc,
                                         const GLState* parent) {
  if (_ribbonGLState == NULL) {
    return parent;
  }

  const Camera* camera = rc->getCurrentCamera();
  int logicWidth = camera->getViewPortWidth();
  if (rc->getViewMode() == STEREO) {
    logicWidth /= 2;
  }
  _ribbonViewportExtent->changeExtent(logicWidth, camera->getViewPortHeight());

  _ribbonGLState->setParent(parent);
  return _ribbonGLState;
}

void Trail::render(const G3MRenderContext* rc,
                   const Frustum* frustum,
                   const GLState* state) {
  if (_visible) {
    const GLState* segmentsState = getSegmentsGLState(rc, state);

    const size_t segmentsSize = _segments.size();
    for (size_t i = 0; i < segmentsSize; i++) {
      Segment* segment = _segments[i];
      segment->render(rc, frustum, segmentsState);
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
