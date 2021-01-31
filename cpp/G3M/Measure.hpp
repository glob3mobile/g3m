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
class Measure_VertexShape;
class MeasureHandler;
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
  
  void resetUI();
  void createVerticesSpheres();
  void createEdgeLines();
  void createEdgeDistanceLabels();
  void createVertexAngleLabels();
  
  int _selectedVertexIndex;
  std::vector<Measure_VertexShape*> _verticesSpheres;
  
  MeasureHandler* _measureHandler;
  const bool                     _deleteMeasureHandler;
  
  void resetSelection();
  
public:
  
#warning TODO: closed measure
  
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
          MeasureHandler* measureHandler,
          const bool deleteMeasureHandler);
  
  const size_t getVerticesCount() const;
  
  void addVertex(const Geodetic3D& vertex);
  
  void setVertex(const size_t i,
                 const Geodetic3D& vertex);
  
  bool removeVertex(const size_t i);
  
  ~Measure();
  
  void touchedOn(const int vertexIndex);
  
};

#endif
