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


class Cylinder{
public:
    class CylinderMeshInfo {
    public:
        std::vector<double> _latlng;
        int _cylId;
        
        CylinderMeshInfo() {_cylId = 0;}
        CylinderMeshInfo(int id) {_cylId = id;};
        CylinderMeshInfo(const CylinderMeshInfo &cylInfo){
            _latlng.reserve(cylInfo._latlng.size());
            copy(cylInfo._latlng.begin(),cylInfo._latlng.end(),back_inserter(_latlng));
            _cylId = cylInfo._cylId;
        }
        void addLatLng(double lat, double lng, double hgt) {
            _latlng.push_back(lat);
            _latlng.push_back(lng);
            _latlng.push_back(hgt);
        }
    } _info;
    
    Cylinder(const Vector3D& start, const Vector3D& end, const double radius):
    _start(start), _end(end), _radius(radius){}
    
    Mesh* createMesh(const Color& color, const int nSegments,const Planet *planet);
    
    static std::string adaptMeshes(MeshRenderer *meshRenderer,
                            std::vector<CylinderMeshInfo> *cylInfo,
                            const Camera *camera,
                            const Planet *planet);
private:
    
  const Vector3D _start;
  const Vector3D _end;
  const double _radius;
    
  static std::vector<Mesh *> visibleMeshes(MeshRenderer *mr, const Camera *camera,
                                           const Planet *planet,
                                           std::vector<CylinderMeshInfo> *cylInfo,
                                           std::vector<CylinderMeshInfo> &visibleInfo);
  static std::vector<double> distances(CylinderMeshInfo info,
                                       const Camera *camera,
                                       const Planet *planet);
    

};

#endif /* Cylinder_hpp */
