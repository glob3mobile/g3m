//
//  Cylinder.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 18/2/21.
//

#ifndef Cylinder_hpp
#define Cylinder_hpp

#include <stdio.h>

#include "Vector3D.hpp"
#include "Planet.hpp"
#include "Color.hpp"
#include "Mesh.hpp"
#include "Angle.hpp"

class Cylinder{
  
public:
  const Vector3D _start;
  const Vector3D _end;
  const double _radius;
  const Angle _startAngle;
  const Angle _endAngle;
  
  Cylinder(const Vector3D& start,
           const Vector3D& end,
           const double radius,
           const Angle& startAngle,
           const Angle& endAngle):
  _start(start), _end(end), _radius(radius), _startAngle(startAngle), _endAngle(endAngle){}
  
  Mesh* createMesh(const Color& color, const int nSegments) const;
  
};

#endif /* Cylinder_hpp */
