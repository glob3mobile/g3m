//
//  Cylinder.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 22/09/2017.
//
//

#ifndef Cylinder_hpp
#define Cylinder_hpp

#include "Vector3D.hpp"
class Color;
class Mesh;

class Cylinder{
  const Vector3D _start;
  const Vector3D _end;
  const double _radius;
public:
  Cylinder(const Vector3D& start, const Vector3D& end, const double radius):
  _start(start), _end(end), _radius(radius){}
  
  Mesh* createMesh(const Color& color, const int nSegments);
  
};

#endif /* Cylinder_hpp */
