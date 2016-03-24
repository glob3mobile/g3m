//
//  Polygon2D.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 24/3/16.
//
//

#ifndef Polygon2D_hpp
#define Polygon2D_hpp


#include "Vector2D.hpp"
#include <vector>
#include "ShortBufferBuilder.hpp"

class Polygon2D{
  
  std::vector<Vector2D*> _coor2D;
  const int _nVertices;
  bool _verticesCCW;
  
  
  bool isConvexPolygonCounterClockWise() {
    const Vector2D* v0 = _coor2D[0];
    const Vector2D* v1 = _coor2D[1];
    const Vector2D* v2 = _coor2D[2];
    
    const double z = ((v1->_x - v0->_x) * (v2->_y - v1->_y)) - ((v1->_y - v0->_y) * (v2->_x - v1->_x));
    return z > 0;
  }
  
  
  double angleInRadiansOfCorner(const int i) {
    
    int isub1 = (i - 1) % (_nVertices - 2);
    if (isub1 == -1) {
      isub1 = _nVertices - 2;
    }
    const int iadd1 = (i + 1) % (_nVertices - 2); //Last one is repeated
    
#ifdef C_CODE
    const Vector2D v1 = _coor2D[iadd1]->sub(*_coor2D[i]);
    const Vector2D v2 = _coor2D[isub1]->sub(*_coor2D[i]);
#endif
#ifdef JAVA_CODE
    final Vector2D v1 = _coor2D.get(iadd1).sub(_coor2D.get(i));
    final Vector2D v2 = _coor2D.get(isub1).sub(_coor2D.get(i));
#endif
    
    return IMathUtils::instance()->atan2(v2._y - v1._x, v2._x - v1._x);
  }
  
  
  bool isConcave() {
    const double a0 = angleInRadiansOfCorner(0);
    for (int i = 1; i < (_nVertices - 1); i++) {
      const double ai = angleInRadiansOfCorner(i);
      if ((ai * a0) < 0) {
        return true;
      }
    }
    return false;
  }
  
  
  double concavePolygonArea() {
    double sum = 0;
    for (int i = 0; i < (_nVertices - 1); i++) {
      const Vector2D* vi = _coor2D[i];
      const Vector2D* vi1 = _coor2D[i + 1];
      sum += ((vi->_x * vi1->_y) - (vi1->_x * vi->_y));
    }
    
    return sum / 2;
  }
  
  
  bool isConcavePolygonCounterClockWise() {
    const double area = concavePolygonArea();
    return area > 0;
  }
  
  
  bool isPolygonCounterClockWise() {
    if (isConcave()) {
      return isConcavePolygonCounterClockWise();
    }
    return isConvexPolygonCounterClockWise();
    
  }
  
  
  static bool isEdgeInside(const int i,
                           const int j,
                           const std::vector<Vector2D*> remainingCorners,
                           bool counterClockWise);
  
  
  
  static bool edgeIntersectsAnyOtherEdge(const int i,
                                         const int j,
                                         const std::vector<Vector2D*> remainingCorners) {
    
    const Vector2D* a = remainingCorners[i];
    const Vector2D* b = remainingCorners[j];
    
    for (int k = 0; k < (remainingCorners.size() - 2); k++) {
      
      const int kadd1 = (k + 1) % (remainingCorners.size() - 1);
      
      if ((i == k) || (i == kadd1) || (j == k) || (j == kadd1)) {
        continue;
      }
      
      const Vector2D* c = remainingCorners[k];
      const Vector2D* d = remainingCorners[kadd1];
      
      if (Vector2D::segmentsIntersect(*a, *b, *c, *d)) {
        return true;
      }
      
      
    }
    
    return false;
  }
  
  
  static bool isAnyVertexInsideTriangle(const int i1,
                                        const int i2,
                                        const int i3,
                                        const std::vector<Vector2D*> remainingCorners) {
    
    const Vector2D* cornerA = remainingCorners[i1];
    const Vector2D* cornerB = remainingCorners[i2];
    const Vector2D* cornerC = remainingCorners[i3];
    
    for (int j = 0; j < remainingCorners.size(); j++) {
      if ((j != i1) && (j != i2) && (j != i3)) {
        const Vector2D* p = remainingCorners[j];
        if (Vector2D::isPointInsideTriangle(*p, *cornerA, *cornerB, *cornerC)) {
          return true;
        }
      }
    }
    
    return false;
    
  }
  
  ///////
  
public:
  
  Polygon2D(const std::vector<Vector2D*> coor): _nVertices(coor.size())  {
    //POLYGON MUST BE DEFINED CCW AND LAST VERTEX == FIRST VERTEX
    _coor2D = coor;
    _verticesCCW = isPolygonCounterClockWise();
  }
  
  bool areVerticesCounterClockWise(){
    return _verticesCCW;
  }
  
  ~Polygon2D() {
#ifdef C_CODE
    for (int j = 0; j < _nVertices; j++) {
      delete _coor2D[j];
    }
#endif
  }
  
  short addTrianglesIndexesByEarClipping(ShortBufferBuilder& indexes, const short firstIndex);
  
};

#endif /* Polygon2D_hpp */
