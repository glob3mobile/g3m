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
                 const Geodetic3D* firstVertex,
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
    const Geodetic3D* vertex = _vertices[i];
    delete vertex;
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
    const Geodetic3D* geodetic = (_selectedVertexIndex < 0) ? NULL : _vertices[_selectedVertexIndex];

    _measureVertexSelectionHandler->onVextexSelection(this,
                                                      geodetic,
                                                      _selectedVertexIndex);
  }
}

void Measure::resetSelection() {
  if (_selectedVertexIndex < 0) {
    return;
  }

  _verticesSpheres[_selectedVertexIndex]->setSelected(false);
  _selectedVertexIndex = -1;

  if (_measureVertexSelectionHandler != NULL) {
    _measureVertexSelectionHandler->onVextexSelection(this,
                                                      NULL,
                                                      _selectedVertexIndex);
  }
}


void Measure::reset() {
  _shapesRenderer->removeAllShapes(Measure_ShapeFilter(_instanceID), true /* deleteShapes */);
  _meshRenderer->removeAllMeshes(Measure_MeshFilter(_instanceID), true /* deleteMeshes */);
  _marksRenderer->removeAllMarks(Measure_MarkFilter(_instanceID), false /* animated */, true /* deleteMarks */);

  _verticesSpheres.clear();

  const size_t verticesCount = _vertices.size();

  // create vertices spheres
  for (int i = 0; i < verticesCount; i++) {
    const Geodetic3D* geodetic = _vertices[i];

    MeasureVertexShape* vertexSphere = new MeasureVertexShape(new Geodetic3D(*geodetic),
                                                              _vertexSphereRadius,
                                                              _vertexColor,
                                                              _vertexSelectedColor,
                                                              this,
                                                              _instanceID,
                                                              i);

    _verticesSpheres.push_back( vertexSphere );

    _shapesRenderer->addShape( vertexSphere );
  }


  if (verticesCount > 1) {
    {
      // create edges lines
      FloatBufferBuilderFromGeodetic* fbb = FloatBufferBuilderFromGeodetic::builderWithFirstVertexAsCenter(_planet);

      for (size_t i = 0; i < verticesCount; i++) {
        const Geodetic3D* geodetic = _vertices[i];
        fbb->add(*geodetic);
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

    {
      // create edges distance labels
#ifdef C_CODE
      const Geodetic3D* previousGeodetic  = _vertices[0];
      const Vector3D*   previousCartesian = new Vector3D(_planet->toCartesian(*previousGeodetic));
#else
      Geodetic3D* previousGeodetic  = _vertices[0];
      Vector3D*   previousCartesian = new Vector3D(_planet->toCartesian(*previousGeodetic));
#endif
      for (size_t i = 1; i < verticesCount; i++) {
        const Geodetic3D* currentGeodetic  = _vertices[i];
        const Vector3D*   currentCartesian = new Vector3D(_planet->toCartesian(*currentGeodetic));

        Geodetic3D middle = Geodetic3D::linearInterpolation(*previousGeodetic, *currentGeodetic, 0.5);
        Mark* distanceLabel = new Mark( IStringUtils::instance()->toString( (float) previousCartesian->distanceTo(*currentCartesian) ) + "m",
                                       middle,
                                       ABSOLUTE);

        distanceLabel->setToken(_instanceID);
        _marksRenderer->addMark(distanceLabel);

        previousGeodetic = currentGeodetic;

        delete previousCartesian;
        previousCartesian = currentCartesian;
      }

      delete previousCartesian;
    }

#warning TODO:   vertices angle labels
  }

}

const size_t Measure::getVexticesCount() const {
  return _vertices.size();
}

void Measure::addVertex(const Geodetic3D* vertex) {
  resetSelection();

  _vertices.push_back( vertex );

  reset();
}

void Measure::setVertex(const size_t i,
                        const Geodetic3D* vertex) {
  const Geodetic3D* current = _vertices[i];
  if (vertex != current) {
    resetSelection();

    delete current;
    _vertices[i] = vertex;

    reset();
  }
}

bool Measure::removeVertex(const size_t i) {
  if (_vertices.size() == 1) {
    return false;
  }

  resetSelection();

#ifdef C_CODE
  _vertices.erase(_vertices.begin() + i);
#endif
#ifdef JAVA_CODE
  _vertices.remove(i);
#endif

  reset();

  return true;
}
