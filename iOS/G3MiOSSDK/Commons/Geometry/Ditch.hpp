//
//  Ditch.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 22/09/2017.
//
//

#ifndef Ditch_hpp
#define Ditch_hpp

#include "Vector3D.hpp"
class Color;
class Mesh;

#include "MutableVector3D.hpp"
#include "ElevationData.hpp"

class MeshRenderer;
class Camera;
class Planet;
class IndexedMesh;
class FloatBufferBuilderFromCartesian3D;

#include "Sphere.hpp"
#include "Geodetic3D.hpp"
#include <sstream>

class Ditch{
public:
    
    Ditch(const Geodetic3D& start, const Geodetic3D& end,
             const double width):
    _start(start), _end(end), _width(width){}
    
    Mesh* createMesh(const Color& color, const int nSegments,const Planet *planet, const ElevationData *ed);
    
private:
    
    const Geodetic3D _start;
    const Geodetic3D _end;
    const double _width;
};

#endif /* Ditch_hpp */
