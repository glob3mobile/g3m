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
class CompositeRenderer;
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

  float  _verticalExaggeration;
  double _deltaHeight;
  bool _closed;
  
  std::vector<const MeasureVertex*> _vertices;
  
  ShapesRenderer*    _shapesRenderer;
  MeshRenderer*      _meshRenderer;
  MarksRenderer*     _marksRenderer;
  CompositeRenderer* _compositeRenderer;

#ifdef C_CODE
  const Planet* _planet;
#else
  Planet*       _planet;
#endif

  void resetUI();
  void createVerticesSpheres();
  void createEdgeLines();
  void createDistanceLabel(const size_t vertexIndexFrom,
                           const size_t vertexIndexTo);

  void createEdgeDistanceLabels();
  void createVertexAngleLabels();
  
  int _selectedVertexIndex;
  std::vector<Measure_VertexShape*> _verticesSpheres;
  
  MeasureHandler* _measureHandler;
  const bool      _deleteMeasureHandler;

public:

  Measure(const double vertexSphereRadius,
          const Color& vertexColor,
          const Color& vertexSelectedColor,
          const float segmentLineWidth,
          const Color& segmentColor,
          const Geodetic3D& firstVertex,
          const float verticalExaggeration,
          const double deltaHeight,
          const bool closed,
          MeasureHandler* measureHandler,
          const bool deleteMeasureHandler);

  void initialize(ShapesRenderer*    shapesRenderer,
                  MeshRenderer*      meshRenderer,
                  MarksRenderer*     marksRenderer,
                  CompositeRenderer* compositeRenderer,
                  const Planet*      planet);

  const double getVertexSphereRadius() const {
    return _vertexSphereRadius;
  }
  
  const size_t getVerticesCount() const;
  
  void addVertex(const Geodetic3D& vertex);
  
  void setVertex(const size_t i,
                 const Geodetic3D& vertex);
  
  bool removeVertex(const size_t i);

  const Geodetic3D getVertex(const size_t i) const;

  const float getVerticalExaggeration() const;
  const double getDeltaHeight() const;

  void setVerticalExaggeration(float verticalExaggeration);
  void setDeltaHeight(double deltaHeight);

  ~Measure();

  void clearSelection();

  void cleanUI();

  void toggleSelection(const int vertexIndex);

  void setClosed(const bool closed);

  bool isClosed() const {
    return _closed;
  }

};

#endif
