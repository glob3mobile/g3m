//
//  Measure.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/25/21.
//

#include "Measure.hpp"

#include "Geodetic3D.hpp"
#include "ErrorHandling.hpp"

#include "ShapesRenderer.hpp"
#include "MeshRenderer.hpp"
#include "MarksRenderer.hpp"
#include "EllipsoidShape.hpp"
#include "FloatBufferBuilderFromGeodetic.hpp"
#include "DirectMesh.hpp"
#include "GLConstants.hpp"
#include "Mark.hpp"
#include "Planet.hpp"
#include "IndexedMesh.hpp"
#include "MeasureVertexSelectionHandler.hpp"
#include "IStringUtils.hpp"
#include "ShapeFilter.hpp"
#include "MeshFilter.hpp"
#include "MarkFilter.hpp"


long long Measure::INSTANCE_COUNTER = 0;


class MeasureVertex {
public:
  const Geodetic3D _geodetic;
  const Vector3D   _cartesian;

  MeasureVertex(const Geodetic3D& geodetic,
                const Planet* planet) :
  _geodetic(geodetic),
  _cartesian( planet->toCartesian(geodetic) )
  {

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


class MeasureVertexShape : public EllipsoidShape {
private:
  const Color  _color;
  const Color  _selectedColor;
  Measure*     _measure;
  const int    _vertexIndex;

public:

  MeasureVertexShape(Geodetic3D* position,
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
    _measure->touchedOn(_vertexIndex);

    return true;
  }

};

Measure::Measure(const double vertexSphereRadius,
                 const Color& vertexColor,
                 const Color& vertexSelectedColor,
                 const float segmentLineWidth,
                 const Color& segmentColor,
                 const Geodetic3D& firstVertex,
                 ShapesRenderer* shapesRenderer,
                 MeshRenderer* meshRenderer,
                 MarksRenderer* marksRenderer,
                 const Planet* planet,
                 MeasureVertexSelectionHandler* measureVertexSelectionHandler,
                 const bool deleteMeasureVertexSelectionHandler) :
_instanceID( "_Measure_" + IStringUtils::instance()->toString(INSTANCE_COUNTER++) ),
_vertexSphereRadius(vertexSphereRadius),
_vertexColor(vertexColor),
_vertexSelectedColor(vertexSelectedColor),
_segmentLineWidth(segmentLineWidth),
_segmentColor(segmentColor),
_shapesRenderer(shapesRenderer),
_meshRenderer(meshRenderer),
_marksRenderer(marksRenderer),
_planet(planet),
_selectedVertexIndex(-1),
_measureVertexSelectionHandler(measureVertexSelectionHandler),
_deleteMeasureVertexSelectionHandler(deleteMeasureVertexSelectionHandler)
{
  addVertex( firstVertex );
}

Measure::~Measure() {
  for (size_t i = 0; i < _vertices.size(); i++) {
    delete _vertices[i];
  }

  if (_deleteMeasureVertexSelectionHandler) {
    delete _measureVertexSelectionHandler;
  }
}

void Measure::touchedOn(const int vertexIndex) {
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

  if (_measureVertexSelectionHandler != NULL) {
    if (_selectedVertexIndex < 0) {
      _measureVertexSelectionHandler->onVertexDeselection(this);
    }
    else {
      const MeasureVertex* vertex = _vertices[_selectedVertexIndex];

      _measureVertexSelectionHandler->onVertexSelection(this,
                                                        vertex->_geodetic,
                                                        vertex->_cartesian,
                                                        _selectedVertexIndex);
    }
  }
}

void Measure::resetSelection() {
  if (_selectedVertexIndex < 0) {
    return;
  }

  _verticesSpheres[_selectedVertexIndex]->setSelected(false);
  _selectedVertexIndex = -1;

  if (_measureVertexSelectionHandler != NULL) {
    _measureVertexSelectionHandler->onVertexDeselection(this);
  }
}


void Measure::createVerticesSpheres() {
  const size_t verticesCount = _vertices.size();

  for (int i = 0; i < verticesCount; i++) {
    const MeasureVertex* vertex = _vertices[i];

    MeasureVertexShape* vertexSphere = new MeasureVertexShape(new Geodetic3D(vertex->_geodetic),
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
  const size_t verticesCount = _vertices.size();
  if (verticesCount < 2) {
    return;
  }

  // create edges lines
  FloatBufferBuilderFromGeodetic* fbb = FloatBufferBuilderFromGeodetic::builderWithFirstVertexAsCenter(_planet);

  for (size_t i = 0; i < verticesCount; i++) {
    fbb->add(_vertices[i]->_geodetic);
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


void Measure::createEdgeDistanceLabels() {
  const size_t verticesCount = _vertices.size();
  if (verticesCount < 2) {
    return;
  }

  const IStringUtils* su = IStringUtils::instance();
  for (size_t i = 1; i < verticesCount; i++) {
    const MeasureVertex* previous = _vertices[i - 1];
    const MeasureVertex* current  = _vertices[i];

    const std::string label = su->toString( (float) previous->_cartesian.distanceTo(current->_cartesian) ) + "m";

    const Geodetic3D position = Geodetic3D::linearInterpolation(previous->_geodetic, current->_geodetic, 0.5);

    Mark* mark = new Mark(label,
                          Geodetic3D(position._latitude,
                                     position._longitude,
                                     position._height + _vertexSphereRadius),
                          ABSOLUTE);

    mark->setToken(_instanceID);

    _marksRenderer->addMark(mark);
  }
}

void Measure::createVertexAngleLabels() {
  const size_t verticesCount = _vertices.size();
  if (verticesCount < 3) {
    return;
  }

  const IStringUtils* su = IStringUtils::instance();
  for (size_t i = 1; i < verticesCount - 1; i++) {
    const MeasureVertex* previous = _vertices[i - 1];
    const MeasureVertex* current  = _vertices[i];
    const MeasureVertex* next     = _vertices[i + 1];

    const Vector3D v0 = current->_cartesian.sub(previous->_cartesian);
    const Vector3D v1 = current->_cartesian.sub(next->_cartesian);

    const Angle angle = v0.angleBetween(v1);
    const std::string label = su->toString( (float) angle._degrees ) + "d";

    Mark* mark = new Mark(label,
                          Geodetic3D(current->_geodetic._latitude,
                                     current->_geodetic._longitude,
                                     current->_geodetic._height + _vertexSphereRadius*2),
                          ABSOLUTE);

    mark->setToken(_instanceID);

    _marksRenderer->addMark(mark);
  }
}

void Measure::reset() {
  {
    // clean up
    _shapesRenderer->removeAllShapes(Measure_ShapeFilter(_instanceID), true /* deleteShapes */);
    _meshRenderer->removeAllMeshes(Measure_MeshFilter(_instanceID), true /* deleteMeshes */);
    _marksRenderer->removeAllMarks(Measure_MarkFilter(_instanceID), false /* animated */, true /* deleteMarks */);

    _verticesSpheres.clear();
  }

  {
    // create 3d objects
    createVerticesSpheres();
    createEdgeLines();
    createEdgeDistanceLabels();
    createVertexAngleLabels();
  }
}

const size_t Measure::getVerticesCount() const {
  return _vertices.size();
}

void Measure::addVertex(const Geodetic3D& vertex) {
  resetSelection();

  _vertices.push_back( new MeasureVertex(vertex, _planet) );

  reset();
}

void Measure::setVertex(const size_t i,
                        const Geodetic3D& vertex) {
  resetSelection();

  delete _vertices[i];

  _vertices[i] = new MeasureVertex(vertex, _planet);

  reset();
}

bool Measure::removeVertex(const size_t i) {
  if (_vertices.size() == 1) {
    return false;
  }

  resetSelection();

#ifdef C_CODE
  delete _vertices[i];
  _vertices.erase(_vertices.begin() + i);
#endif
#ifdef JAVA_CODE
  _vertices.remove(i);
#endif

  reset();

  return true;
}
