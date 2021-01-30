//
//  Measure.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/25/21.
//

#ifndef Measure_hpp
#define Measure_hpp

#include <vector>

#include "Color.hpp"

class Geodetic3D;
class ShapesRenderer;
class MeshRenderer;
class MarksRenderer;
class Planet;
class MeasureVertexShape;
class MeasureVertexSelectionHandler;


class Measure {
private:
  const double _vertexSphereRadius;
  const Color  _vertexColor;
  const Color  _vertexSelectedColor;
  const float  _segmentLineWidth;
  const Color  _segmentColor;

  std::vector<const Geodetic3D*> _vertices;

  ShapesRenderer* _shapesRenderer;
  MeshRenderer*   _meshRenderer;
  MarksRenderer*  _marksRenderer;

  const Planet* _planet;

  void reset();

  int _selectedVertexIndex;
  std::vector<MeasureVertexShape*> _verticesSpheres;

  MeasureVertexSelectionHandler* _measureVertexSelectionHandler;
  const bool                     _deleteMeasureVertexSelectionHandler;

public:
#warning TODO: add vertexSelectionHandler   onVertexSelection(measure, geodetic, i);

  Measure(const double vertexSphereRadius,
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
          const bool deleteMeasureVertexSelectionHandler);

  const size_t getVexticesCount() const;

  void addVertex(const Geodetic3D* vertex);

  void setVertex(const size_t i,
                 const Geodetic3D* vertex);

  bool removeVertex(const size_t i);

  ~Measure();

  void touchedOn(const int vertexIndex);

};

#endif
