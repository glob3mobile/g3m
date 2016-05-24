//
//  Polygon3D.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 24/3/16.
//
//

#ifndef Polygon3D_hpp
#define Polygon3D_hpp

#include "Vector3D.hpp"
#include "Vector2D.hpp"
#include "Polygon2D.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "ShortBufferBuilder.hpp"
#include <vector>


int numberOfP3D = 0;
int numberOfP3D_4 = 0;

class Polygon3D{
  
  std::vector<Vector3D*> _coor3D;
  Vector3D _normal;
  Polygon2D* _polygon2D;
  
  Vector3D getNormalOfFirstVertex(std::vector<Vector3D*> coor3D){
#ifdef C_CODE
    const Vector3D e1 = coor3D[0]->sub(*coor3D[1]);
    const Vector3D e2 = coor3D[2]->sub(*coor3D[1]);
#endif
#ifdef JAVA_CODE
    final Vector3D e1 = coor3D.get(0).sub(coor3D.get(1));
    final Vector3D e2 = coor3D.get(2).sub(coor3D.get(1));
#endif
    
    return e1.cross(e2);
  }
  
public:
  
  Polygon3D(const std::vector<Vector3D*> coor3D):
  _coor3D(coor3D),
  _normal(getNormalOfFirstVertex(coor3D)){
    std::vector<Vector2D*> _coor2D = createCoordinates2D();
    _polygon2D = new Polygon2D(_coor2D);
  }
  
  ~Polygon3D() {
    delete _polygon2D;
#ifdef C_CODE
    for (int j = 0; j < (int)_coor3D.size(); j++) {
      delete _coor3D[j];
    }
#endif
  }
  
  Vector3D getCCWNormal() const{
    if (_polygon2D->areVerticesCounterClockWise()){
      return _normal.times(-1);
    }
    return _normal;
  }
  
  
  std::vector<Vector2D*> createCoordinates2D();
  
  short addTrianglesByEarClipping(FloatBufferBuilderFromCartesian3D& fbb,
                                  FloatBufferBuilderFromCartesian3D& normals,
                                  ShortBufferBuilder& indexes,
                                  const short firstIndex) const;
  
  
};

#endif /* Polygon3D_hpp */
