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

class MeasureVertex;
class ShapesRenderer;
class MeshRenderer;
class MarksRenderer;
class Planet;
class MeasureVertexShape;
class MeasureVertexSelectionHandler;
class Geodetic3D;


class Measure {
private:
  static long long INSTANCE_COUNTER;


  const std::string _instanceID;


  const double _vertexSphereRadius;
  const Color  _vertexColor;
  const Color  _vertexSelectedColor;
  const float  _segmentLineWidth;
  const Color  _segmentColor;

  std::vector<const MeasureVertex*> _vertices;

  ShapesRenderer* _shapesRenderer;
  MeshRenderer*   _meshRenderer;
  MarksRenderer*  _marksRenderer;

  const Planet* _planet;

  void reset();
  void createVerticesSpheres();
  void createEdgeLines();
  void createEdgeDistanceLabels();
  void createVertexAngleLabels();

  int _selectedVertexIndex;
  std::vector<MeasureVertexShape*> _verticesSpheres;

  MeasureVertexSelectionHandler* _measureVertexSelectionHandler;
  const bool                     _deleteMeasureVertexSelectionHandler;

  void resetSelection();

public:

  Measure(const double vertexSphereRadius,
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
          const bool deleteMeasureVertexSelectionHandler);

  const size_t getVerticesCount() const;

  void addVertex(const Geodetic3D& vertex);

  void setVertex(const size_t i,
                 const Geodetic3D& vertex);

  bool removeVertex(const size_t i);

  ~Measure();

  void touchedOn(const int vertexIndex);

};

#endif
