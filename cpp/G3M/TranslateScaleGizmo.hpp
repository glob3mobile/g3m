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

class TranslateScaleGizmo;

class TranslateScaleGizmoListener{
public:
  virtual ~TranslateScaleGizmoListener(){}
  virtual void onChanged(const TranslateScaleGizmo& gizmo) = 0;
};

class TranslateScaleGizmo: public CompositeRenderer, public ArrowListener{
  CoordinateSystem* _cs;
  double _scale;
  const double _maxScale;
  const double _size;
  
  TranslateScaleGizmoListener* _listener;
  
  Arrow  *_xArrow, *_yArrow, *_zArrow, *_scaleArrow;
  
  Vector3D getScale1Vector() const{
    return _cs->_x.add(_cs->_y).add(_cs->_z).normalized().times(_size);
  }

public:
  TranslateScaleGizmo(const CoordinateSystem& cs, double scale, double size, double maxScale = 2.0);
  
  ~TranslateScaleGizmo(){
    delete _cs;
  }
  
  void onBaseChanged(const Arrow& arrow) override;
  
  const CoordinateSystem getCoordinateSystem() const{
    return *_cs;
  }
  
  double getScale() const{
    return _scale;
  }
  
  void setListener(TranslateScaleGizmoListener* listener){
      _listener = listener;
  }
  
};

#endif /* TranslateScaleGizmo_hpp */
