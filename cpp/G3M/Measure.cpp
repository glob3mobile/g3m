//
//  Measure.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/25/21.
//

#include "Measure.hpp"

#include "Geodetic3D.hpp"
#include "Vector3D.hpp"
#include "Planet.hpp"
#include "ShapeFilter.hpp"
#include "MeshFilter.hpp"
#include "MarkFilter.hpp"
#include "EllipsoidShape.hpp"
#include "DirectMesh.hpp"
#include "Mark.hpp"
#include "MeasureHandler.hpp"
#include "ShapesRenderer.hpp"
#include "MeshRenderer.hpp"
#include "MarksRenderer.hpp"
#include "CompositeRenderer.hpp"
#include "FloatBufferBuilderFromGeodetic.hpp"


long long Measure::INSTANCE_COUNTER = 0;


class MeasureVertex {
private:
  mutable Vector3D* _cartesian;

public:
  const Geodetic3D _geodetic;
  const float      _verticalExaggeration;
  const double     _deltaHeight;

  MeasureVertex(const Geodetic3D& geodetic,
                const float  verticalExaggeration,
                const double deltaHeight) :
  _geodetic(geodetic),
  _verticalExaggeration(verticalExaggeration),
  _deltaHeight(deltaHeight),
  _cartesian(NULL)
  {

  }


  const Vector3D getCartesian(const Planet* planet) const {
    if (_cartesian == NULL) {
      _cartesian = new Vector3D(planet->toCartesian(_geodetic));
    }
    return *_cartesian;
  }

  ~MeasureVertex() {
    delete _cartesian;
  }

};



class Measure_ShapeFilter : public ShapeFilter {
private:
  const std::string _token;

public:
  Measure_ShapeFilter(const std::string& token) :
  _token(token)
  {
  }

  bool test(const Shape* shape) const {
    return (shape->getToken() == _token);
  }
};


class Measure_MeshFilter : public MeshFilter {
private:
  const std::string _token;

public:
  Measure_MeshFilter(const std::string& token) :
  _token(token)
  {
  }

  bool test(const Mesh* mesh) const {
    return (mesh->getToken() == _token);
  }
};

class Measure_MarkFilter : public MarkFilter {
private:
  const std::string _token;

public:
  Measure_MarkFilter(const std::string& token) :
  _token(token)
  {
  }

  bool test(const Mark* mark) const {
    return (mark->getToken() == _token);
  }
};


class Measure_VertexShape : public EllipsoidShape {
private:
  const Color  _color;
  const Color  _selectedColor;
  Measure*     _measure;
  const int    _vertexIndex;

public:

  Measure_VertexShape(Geodetic3D* position,
                      const double radius,
                      const Color& color,
                      const Color& selectedColor,
                      Measure* measure,
                      const std::string& instanceID,
                      const int vertexIndex) :
  EllipsoidShape(position,
                 AltitudeMode::ABSOLUTE,
                 Vector3D(radius, radius, radius),
                 (short) 16,  // resolution
                 0,           // float borderWidth
                 false,       // bool texturedInside
                 false,       // bool mercator
                 color),
  _color(color),
  _selectedColor(selectedColor),
  _measure(measure),
  _vertexIndex(vertexIndex)
  {
    setToken(instanceID);
  }

  void setSelected(const bool selected) {
    setSurfaceColor( selected ? _selectedColor : _color );
  }

  bool touched(const G3MEventContext* ec) {
    _measure->toggleSelection(_vertexIndex);

    return true;
  }

};

Measure::Measure(const double vertexSphereRadius,
                 const Color& vertexColor,
                 const Color& vertexSelectedColor,
                 const float segmentLineWidth,
                 const Color& segmentColor,
                 const Geodetic3D& firstVertex,
                 const float firstVerticalExaggeration,
                 const double firstVertexDeltaHeight,
                 const bool closed,
                 MeasureHandler* measureHandler,
                 const bool deleteMeasureHandler) :
_instanceID( "_Measure_" + IStringUtils::instance()->toString(INSTANCE_COUNTER++) ),
_vertexSphereRadius(vertexSphereRadius),
_vertexColor(vertexColor),
_vertexSelectedColor(vertexSelectedColor),
_segmentLineWidth(segmentLineWidth),
_segmentColor(segmentColor),
_closed(closed),
_measureHandler(measureHandler),
_deleteMeasureHandler(deleteMeasureHandler),
_selectedVertexIndex(-1),
_shapesRenderer(NULL),
_meshRenderer(NULL),
_marksRenderer(NULL),
_compositeRenderer(NULL),
_planet(NULL)
{
  addVertex(firstVertex,
            firstVerticalExaggeration,
            firstVertexDeltaHeight);
}

Measure::~Measure() {
  for (size_t i = 0; i < _vertices.size(); i++) {
    delete _vertices[i];
  }

  if (_deleteMeasureHandler) {
    delete _measureHandler;
  }
}

void Measure::initialize(ShapesRenderer*    shapesRenderer,
                         MeshRenderer*      meshRenderer,
                         MarksRenderer*     marksRenderer,
                         CompositeRenderer* compositeRenderer,
                         const Planet*      planet) {
  _shapesRenderer    = shapesRenderer;
  _meshRenderer      = meshRenderer;
  _marksRenderer     = marksRenderer;
  _compositeRenderer = compositeRenderer;
  _planet            = planet;

  resetUI();
}

void Measure::toggleSelection(const int vertexIndex) {
  if (vertexIndex == _selectedVertexIndex) {
    _verticesSpheres[_selectedVertexIndex]->setSelected(false);
    _selectedVertexIndex = -1;
  }
  else {
    if (_selectedVertexIndex >= 0) {
      _verticesSpheres[_selectedVertexIndex]->setSelected(false);
    }
    _selectedVertexIndex = vertexIndex;
    _verticesSpheres[_selectedVertexIndex]->setSelected(true);
  }

  if (_measureHandler != NULL) {
    if (_selectedVertexIndex < 0) {
      _measureHandler->onVertexDeselection(this);
    }
    else {
      const MeasureVertex* vertex = _vertices[_selectedVertexIndex];

      _measureHandler->onVertexSelection(this,
                                         vertex->_geodetic,
                                         vertex->getCartesian(_planet),
                                         _selectedVertexIndex);
    }
  }
}

void Measure::clearSelection() {
  if (_selectedVertexIndex < 0) {
    return;
  }

  _verticesSpheres[_selectedVertexIndex]->setSelected(false);
  _selectedVertexIndex = -1;

  if (_measureHandler != NULL) {
    _measureHandler->onVertexDeselection(this);
  }
}


void Measure::createVerticesSpheres() {
  if (_shapesRenderer == NULL) {
    return;
  }

  const size_t verticesCount = _vertices.size();

  for (int i = 0; i < verticesCount; i++) {
    const MeasureVertex* vertex = _vertices[i];

    Measure_VertexShape* vertexSphere = new Measure_VertexShape(new Geodetic3D(vertex->_geodetic),
                                                                _vertexSphereRadius,
                                                                _vertexColor,
                                                                _vertexSelectedColor,
                                                                this,
                                                                _instanceID,
                                                                i);

    _verticesSpheres.push_back( vertexSphere );

    _shapesRenderer->addShape( vertexSphere );
  }
}

void Measure::createEdgeLines() {
  if (_meshRenderer == NULL) {
    return;
  }

  const size_t verticesCount = _vertices.size();
  if (verticesCount < 2) {
    return;
  }

  // create edges lines
  FloatBufferBuilderFromGeodetic* fbb = FloatBufferBuilderFromGeodetic::builderWithFirstVertexAsCenter(_planet);

  for (size_t i = 0; i < verticesCount; i++) {
    fbb->add(_vertices[i]->_geodetic);
  }

  if (_closed && (verticesCount >= 3) ) {
    fbb->add(_vertices[0]->_geodetic);
  }

  Mesh* edgesLines = new DirectMesh(GLPrimitive::lineStrip(),
                                    true,
                                    fbb->getCenter(),
                                    fbb->create(),
                                    _segmentLineWidth,
                                    1.0f,  // float pointSize
                                    new Color(_segmentColor),
                                    NULL,  // const IFloatBuffer* colors
                                    false  // depthTest
                                    );

  edgesLines->setToken(_instanceID);
  _meshRenderer->addMesh(edgesLines);

  delete fbb;
}

void Measure::createDistanceLabel(const size_t vertexIndexFrom,
                                  const size_t vertexIndexTo) {
  const MeasureVertex* from = _vertices[vertexIndexFrom];
  const MeasureVertex* to   = _vertices[vertexIndexTo];

  const double distanceInMeters = from->getCartesian(_planet).distanceTo(to->getCartesian(_planet));

  const std::string label = _measureHandler->getDistanceLabel(this,
                                                              vertexIndexFrom,
                                                              vertexIndexTo,
                                                              distanceInMeters);
#ifdef JAVA_CODE
  if (label == null) {
    return;
  }
#endif
  if (label.empty()) {
    return;
  }

  const Geodetic3D position = Geodetic3D::linearInterpolation(from->_geodetic,
                                                              to->_geodetic,
                                                              0.5);

  Mark* mark = new Mark(label,
                        Geodetic3D(position._latitude,
                                   position._longitude,
                                   position._height + _vertexSphereRadius),
                        ABSOLUTE);
  mark->setZoomInAppears(false);

  mark->setToken(_instanceID);

  _marksRenderer->addMark(mark);
}

void Measure::createEdgeDistanceLabels() {
  if (_marksRenderer == NULL) {
    return;
  }

  const size_t verticesCount = _vertices.size();
  if (verticesCount < 2) {
    return;
  }

  if (_measureHandler == NULL) {
    return;
  }

  for (size_t i = 1; i < verticesCount; i++) {
    createDistanceLabel(i - 1, i);
  }

  if (_closed && (verticesCount >= 3) ) {
    createDistanceLabel(verticesCount - 1, 0);
  }
}

void Measure::createVertexAngleLabels() {
  if (_marksRenderer == NULL) {
    return;
  }

  const size_t verticesCount = _vertices.size();
  if (verticesCount < 3) {
    return;
  }

  for (size_t i = 1; i < verticesCount - 1; i++) {
    const MeasureVertex* previous = _vertices[i - 1];
    const MeasureVertex* current  = _vertices[i];
    const MeasureVertex* next     = _vertices[i + 1];

    const Vector3D v0 = current->getCartesian(_planet).sub(previous->getCartesian(_planet));
    const Vector3D v1 = current->getCartesian(_planet).sub(next->getCartesian(_planet));

    const Angle angle = v0.angleBetween(v1);

    const std::string label = _measureHandler->getAngleLabel(this, i, angle);
#ifdef JAVA_CODE
    if (label == null) {
      continue;
    }
#endif
    if (label.empty()) {
      continue;
    }

    Mark* mark = new Mark(label,
                          Geodetic3D(current->_geodetic._latitude,
                                     current->_geodetic._longitude,
                                     current->_geodetic._height + _vertexSphereRadius*2),
                          ABSOLUTE);
    mark->setZoomInAppears(false);

    mark->setToken(_instanceID);

    _marksRenderer->addMark(mark);
  }
}

void Measure::cleanUI() {
  _verticesSpheres.clear();

  if (_shapesRenderer != NULL) {
    _shapesRenderer->removeAllShapes(Measure_ShapeFilter(_instanceID), true /* deleteShapes */);
  }
  if (_meshRenderer != NULL) {
    _meshRenderer->removeAllMeshes(Measure_MeshFilter(_instanceID), true /* deleteMeshes */);
  }
  if (_marksRenderer != NULL) {
    _marksRenderer->removeAllMarks(Measure_MarkFilter(_instanceID), false /* animated */, true /* deleteMarks */);
  }

  if (_compositeRenderer != NULL) {
    _compositeRenderer->removeAllRenderers();
  }
}

void Measure::resetUI() {
  // clean up
  cleanUI();

  // create 3d objects
  createVerticesSpheres();
  createEdgeLines();
  createEdgeDistanceLabels();
  createVertexAngleLabels();
}

const size_t Measure::getVerticesCount() const {
  return _vertices.size();
}

void Measure::addVertex(const Geodetic3D& vertex,
                        const float verticalExaggeration,
                        const double deltaHeight) {
  clearSelection();

  _vertices.push_back( new MeasureVertex(vertex, verticalExaggeration, deltaHeight) );

  resetUI();
}

void Measure::setVertex(const size_t i,
                        const Geodetic3D& vertex,
                        const float verticalExaggeration,
                        const double deltaHeight) {
  clearSelection();

  delete _vertices[i];

  _vertices[i] = new MeasureVertex(vertex, verticalExaggeration, deltaHeight);

  resetUI();
}

bool Measure::removeVertex(const size_t i) {
  if (_vertices.size() == 1) {
    return false;
  }

  clearSelection();

#ifdef C_CODE
  delete _vertices[i];
  _vertices.erase(_vertices.begin() + i);
#endif
#ifdef JAVA_CODE
  _vertices.remove(i);
#endif

  resetUI();

  return true;
}

const Geodetic3D Measure::getVertex(const size_t i) const {
  return _vertices[i]->_geodetic;
}

const double Measure::getDeltaHeight(const size_t i) const {
  return _vertices[i]->_deltaHeight;
}

const float Measure::getVerticalExaggeration(const size_t i) const {
  return _vertices[i]->_verticalExaggeration;
}

void Measure::setClosed(const bool closed) {
  if (_closed != closed) {
    _closed = closed;

    resetUI();
  }
}
