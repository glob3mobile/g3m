//
//  TranslateScaleGizmo.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 20/2/21.
//

#ifndef TranslateScaleGizmo_hpp
#define TranslateScaleGizmo_hpp

#include <stdio.h>
#include "CompositeRenderer.hpp"
#include "Arrow.hpp"
#include "CoordinateSystem.hpp"
//#include "Sphere.hpp"
//#include "MeshRenderer.hpp"
//#include "Box.hpp"

class TranslateScaleGizmo: public CompositeRenderer{
  CoordinateSystem _cs;
  double _size;
  
  Arrow* _xArrow, *_yArrow, *_zArrow;
public:
  TranslateScaleGizmo(const CoordinateSystem& cs, double size): _cs(cs), _size(size){
    
    double lineWidth = size * 0.01;
    Vector3D x = _cs._x.normalized().times(_size);
    Vector3D y = _cs._y.normalized().times(_size);
    Vector3D z = _cs._z.normalized().times(_size);
    
    _xArrow = new Arrow(_cs._origin,
                        _cs._origin.add(x),
                        lineWidth,
                        Color::RED,
                        size * 0.05,
                        1.3);
    addRenderer(_xArrow);
    
    _yArrow = new Arrow(_cs._origin,
                        _cs._origin.add(y),
                        lineWidth,
                        Color::GREEN,
                        size * 0.05,
                        1.3);
    addRenderer(_yArrow);
    
    _zArrow = new Arrow(_cs._origin,
                        _cs._origin.add(z),
                        lineWidth,
                        Color::BLUE,
                        size * 0.05,
                        1.3);
    addRenderer(_zArrow);
    
//    Vector3D boxSize(100,100,100);
//    Box box(_cs._origin.sub(boxSize),
//            _cs._origin.add(boxSize));
//    MeshRenderer* mr = new MeshRenderer();
//    mr->addMesh(box.createMesh(Color::GRAY));
//    addRenderer(mr);
  }
  
};

#endif /* TranslateScaleGizmo_hpp */
