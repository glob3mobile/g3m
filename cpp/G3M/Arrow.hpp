//
//  Arrow.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 20/2/21.
//

#ifndef Arrow_hpp
#define Arrow_hpp

#include <stdio.h>
#include "MeshRenderer.hpp"
#include "Vector3D.hpp"
#include "Cylinder.hpp"

class Arrow: public MeshRenderer{
  
public:
  Arrow(const Vector3D& base,
        const Vector3D& tip,
        double radius,
        const Color& color,
        double headLength = 3.0,
        double headWidthRatio = 1.2 ){
    Vector3D dir = tip.sub(base);
    
    Vector3D headBase = base.add(dir.normalized().times(dir.length() - headLength));
    Cylinder cylinder(base, headBase, radius, radius);
    
    Cylinder arrowTip(headBase,
                      tip,
                      radius * headWidthRatio, 0.0);
    
    Cylinder arrowTipCover(headBase,
                           headBase.sub(dir.normalized().times(0.0001)),
                           radius * headWidthRatio, 0.0);
    
    addMesh(cylinder.createMesh(color, 10));
    addMesh(arrowTip.createMesh(color, 20));
    addMesh(arrowTipCover.createMesh(color, 20));
  }
  
  bool onTouchEvent(const G3MEventContext* ec, const TouchEvent* touchEvent) override {
    return false;
  }
  
};

#endif /* Arrow_hpp */
