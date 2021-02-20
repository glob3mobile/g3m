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
        double headLengthRatio = 0.3,
        double headWidthRatio = 1.2){
    Vector3D headBase = tip.add(tip.sub(base).times(1.0 - headLengthRatio));
    Cylinder cylinder(base, headBase, radius, radius);
    
    Cylinder arrowTip(headBase,
                      tip,
                      radius * headWidthRatio, 0.0);
    
    addMesh(cylinder.createMesh(color, 10));
    addMesh(arrowTip.createMesh(color, 20));
  }
  
};

#endif /* Arrow_hpp */
