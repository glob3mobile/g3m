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

#include "MutableVector3D.hpp"
class MeshRenderer;
class Camera;
class Planet;
class IndexedMesh;
class FloatBufferBuilderFromCartesian3D;

#include "Sphere.hpp"
#include <sstream>

class Cylinder{
public:
    Sphere *s;
    
    Cylinder(const Vector3D& start, const Vector3D& end,
             const double startRadius,
             const double endRadius,
             const double startAngle = 0.0,
             const double endAngle = 360.0):
    _start(start), _end(end), _startRadius(startRadius),
    _endRadius(endRadius), s(NULL),
    _startAngle(startAngle), _endAngle(endAngle){}
    
    Mesh* createMesh(const Color& color,
                     const int nSegments,
                     bool depthTest,
                     const Planet *planet);
    
private:
    
    const Vector3D _start;
    const Vector3D _end;
    const double _startRadius;
    const double _endRadius;
    const double _startAngle;
    const double _endAngle;
    
    void createSphere(std::vector<Vector3D*> &vs);
    
};

#endif /* Cylinder_hpp */
