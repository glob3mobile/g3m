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
private:
    static int DISTANCE_METHOD;
    static bool DEPTH_ENABLED;
    static bool DITCH_ENABLED;
public:
    
    
    class CylinderMeshInfo {
    public:
        std::vector<double> _latlng;
        int _cylId;
        std::string internalMat;
        std::string externalMat;
        std::string internalWidth;
        std::string externalWidth;
        std::string cylinderClass;
        std::string cylinderType;
        bool isTransport;
        bool isCommunication;
        int officialId;
        
        CylinderMeshInfo() {_cylId = 0; isTransport = false; isCommunication = false; officialId = -1;}
        CylinderMeshInfo(int id) {_cylId = id;isTransport = false; isCommunication = false; officialId = -1;};
        CylinderMeshInfo(const CylinderMeshInfo &cylInfo){
            _latlng.reserve(cylInfo._latlng.size());
            copy(cylInfo._latlng.begin(),cylInfo._latlng.end(),back_inserter(_latlng));
            _cylId = cylInfo._cylId;
            internalMat = cylInfo.internalMat;
            externalMat = cylInfo.externalMat;
            internalWidth = cylInfo.internalWidth;
            externalWidth = cylInfo.externalWidth;
            cylinderClass = cylInfo.cylinderClass;
            cylinderType = cylInfo.cylinderType;
            isTransport = cylInfo.isTransport;
            isCommunication = cylInfo.isCommunication;
            officialId = cylInfo.officialId;
        }
        void addLatLng(double lat, double lng, double hgt) {
            _latlng.push_back(lat);
            _latlng.push_back(lng);
            _latlng.push_back(hgt);
        }
        
        void setID (int theId){
            officialId = theId;
        }
        
        void setMaterials(std::string extMat, std::string intMat){
            //Ad hoc
            externalMat = extMat;
            internalMat = intMat;
        }
        
        void setWidths(double intWidth, double extWidth){
            //Ad hoc
            std::stringstream sstm,sstm2;
            sstm << extWidth << " cm.";
            externalWidth = sstm.str();
            sstm2 << intWidth << " cm.";
            internalWidth = sstm2.str();
        }
        
        void setClassAndType (std::string cClass, std::string cType){
            //Ad hoc
            cylinderClass = cClass;
            cylinderType = cType;
        }
        
        void setTransportComm (bool transport, bool communication){
            //Ad hoc;
            isTransport = transport;
            isCommunication = communication;
        }
        
        std::string getMessage(){
            std::stringstream sstm;
            sstm << "ID: " << officialId << "\n";
            sstm << "Class: " << cylinderClass << "\n";
            sstm << "Type: " << cylinderType + "\n";
            if (cylinderClass == "Cable"){
                sstm << "Internal Material: " << internalMat << "\n";
                sstm << "External Material: " << externalMat << "\n";
                sstm <<  "Cross section: " << externalWidth << "\n";
                sstm << "Is Transmission: " << isTransport << "\n";
                sstm <<  "Is Communication: " << isCommunication;
            }
            else {
                sstm << "Material: " << externalMat << "\n";
                sstm << "Internal Width: " << internalWidth << "\n";
                sstm << "External Width: " << externalWidth;
            }
            return sstm.str();
        }
        
    } _info;
    
    Sphere *s;
    
    Cylinder(const Vector3D& start, const Vector3D& end,
             const double radius,
             const double startAngle = 0.0,
             const double endAngle = 360.0):
    _start(start), _end(end), _radius(radius), s(NULL),
    _startAngle(startAngle), _endAngle(endAngle){}
    
    Mesh* createMesh(const Color& color, const int nSegments,const Planet *planet);
    
    static std::string adaptMeshes(MeshRenderer *meshRenderer,
                                   std::vector<CylinderMeshInfo> *cylInfo,
                                   const Camera *camera,
                                   const Planet *planet);
    
    static void setDistanceMethod(int method);
    static int getDistanceMethod();
    static void setDepthEnabled(bool enabled);
    static bool getDepthEnabled();
    static void setDitchEnabled(bool enabled);
    static bool getDitchEnabled();
    
private:
    
    const Vector3D _start;
    const Vector3D _end;
    const double _radius;
    const double _startAngle;
    const double _endAngle;
    
    static std::vector<Mesh *> visibleMeshes(MeshRenderer *mr, const Camera *camera,
                                             const Planet *planet,
                                             std::vector<CylinderMeshInfo> *cylInfo,
                                             std::vector<CylinderMeshInfo> &visibleInfo);
    static std::vector<double> distances(CylinderMeshInfo info,
                                         const Camera *camera,
                                         const Planet *planet);
    
    void createSphere(std::vector<Vector3D*> &vs);
    
    static double getAlpha(double distance, double proximityThreshold, bool divide);
    static double rawAlpha(double distance, double proximityThreshold, bool divide);
    static double linearAlpha(double distance, double proximityThreshold, bool divide);
    static double smoothstepAlpha(double distance, double proximityThreshold, bool divide);
    static double perlinSmootherstepAlpha (double distance, double proximityThreshold, bool divide);
    static double mcDonaldSmootheststepAlpha (double distance, double proximityThreshold, bool divide);
    static double sigmoidAlpha (double distance, double proximityThreshold, bool divide);
    static double tanhAlpha (double distance, double proximityThreshold, bool divide);
    static double arctanAlpha (double distance, double proximityThreshold, bool divide);
    static double softsignAlpha (double distance, double proximityThreshold, bool divide);
    
};

#endif /* Cylinder_hpp */
