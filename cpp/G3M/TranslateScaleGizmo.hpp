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

class TranslateScaleGizmo: public CompositeRenderer, public ArrowListener{
  CoordinateSystem* _cs;
  double _size;
  
  Arrow  *_xArrow, *_yArrow, *_zArrow, *_scaleArrow;
public:
  TranslateScaleGizmo(const CoordinateSystem& cs, double scale, double size);
  
  void onBaseChanged(const Arrow& arrow) override;
  
};

#endif /* TranslateScaleGizmo_hpp */
