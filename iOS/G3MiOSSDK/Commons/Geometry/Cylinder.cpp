//
//  Cylinder.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 22/09/2017.
//
//

#include "Cylinder.hpp"

#include "IndexedMesh.hpp"
#include "Color.hpp"
#include "MutableMatrix44D.hpp"
#include "MutableVector3D.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "FloatBufferBuilder.hpp"
#include "ShortBufferBuilder.hpp"
#include "CompositeMesh.hpp"
#include "DirectMesh.hpp"

#warning Chano adding stuff
#include "MeshRenderer.hpp"
#include "Camera.hpp"
#include "FloatBufferBuilderFromColor.hpp"

#define PROXIMITY_VALUE 25

int Cylinder::DISTANCE_METHOD = 2;
int Cylinder::DISTANCE_VALUE = 100;
bool Cylinder::DEPTH_ENABLED = false;
bool Cylinder::DITCH_ENABLED = true;

void Cylinder::setDistanceMethod(int method){
    Cylinder::DISTANCE_METHOD = method;
}

int Cylinder::getDistanceMethod() {
    return Cylinder::DISTANCE_METHOD;
}

void Cylinder::setDepthEnabled(bool enabled){
    Cylinder::DEPTH_ENABLED = enabled;
}
bool Cylinder::getDepthEnabled(){
    return Cylinder::DEPTH_ENABLED;
}

bool Cylinder::getDitchEnabled(){
    return Cylinder::DITCH_ENABLED;
}

int Cylinder::getDistance(){
    return Cylinder::DISTANCE_VALUE;
}

void Cylinder::setDistance(int meters){
    Cylinder::DISTANCE_VALUE = meters;
}

void Cylinder::setDitchEnabled(bool enabled){
    Cylinder::DITCH_ENABLED = enabled;
}

Mesh* Cylinder::createMesh(const Color& color, const int nSegments, const Planet *planet){
    
    Vector3D d = _end.sub(_start);
    Vector3D r = d._z == 0? Vector3D(0.0,0.0,1.0) : Vector3D(1.0, 1.0, (-d._x -d._y) / d._z);
    
    Vector3D p = _start.add(r.times(_radius / r.length()));
    
    const double totalAngle = _endAngle - _startAngle;
    
    
    MutableMatrix44D m = MutableMatrix44D::createGeneralRotationMatrix(
                                                                       Angle::fromDegrees(totalAngle / nSegments),
                                                                       d,
                                                                       _start);
    
    FloatBufferBuilderFromCartesian3D* fbb = FloatBufferBuilderFromCartesian3D::builderWithFirstVertexAsCenter();
    FloatBufferBuilderFromCartesian3D* fbbC1 = FloatBufferBuilderFromCartesian3D::builderWithFirstVertexAsCenter();
    fbbC1->add(_start);
    FloatBufferBuilderFromCartesian3D* fbbC2 = FloatBufferBuilderFromCartesian3D::builderWithFirstVertexAsCenter();
    fbbC2->add(_end);
    
    FloatBufferBuilderFromCartesian3D* normals = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();
    FloatBufferBuilderFromCartesian3D* normalsC1 = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();
    normalsC1->add(d.times(-1.0));
    FloatBufferBuilderFromCartesian3D* normalsC2 = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();
    normalsC2->add(d);
    
    FloatBufferBuilderFromColor colors;
    
    MutableVector3D x(p);
    x = x.transformedBy(
    MutableMatrix44D::createGeneralRotationMatrix(Angle::fromDegrees(_startAngle),
                                                  d, _start), 1.0);
    std::vector<Vector3D *> vs;
    
    for (int i = 0; i < nSegments; ++i){
        
        //Tube
        Vector3D newStartPoint = x.asVector3D().transformedBy(m, 1.0);
        Vector3D newEndPoint = newStartPoint.add(d);
        x.set(newStartPoint._x, newStartPoint._y, newStartPoint._z);
        
        fbb->add(newStartPoint); vs.push_back(new Vector3D(newStartPoint));
        fbb->add(newEndPoint); vs.push_back(new Vector3D(newEndPoint));
        
        Geodetic3D stPoint = planet->toGeodetic3D(newStartPoint);
        Geodetic3D endPoint = planet->toGeodetic3D(newEndPoint);
        _info.addLatLng(stPoint._latitude._degrees, stPoint._longitude._degrees, stPoint._height);
        _info.addLatLng(endPoint._latitude._degrees, endPoint._longitude._degrees, endPoint._height);
        
        normals->add(newStartPoint.sub(_start));
        normals->add(newEndPoint.sub(_end));
        
        //Cover1
        fbbC1->add(newStartPoint);
        normalsC1->add(d.times(-1.0));
        //Cover2
        fbbC2->add(newEndPoint);
        normalsC2->add(d);
        
        colors.add(color);
        colors.add(color);
    }
    
    //Still covers
    /*Vector3D newStartPoint = x.asVector3D().transformedBy(m, 1.0);
     fbbC1->add(newStartPoint);
     normalsC1->add(d.times(-1.0));
     Vector3D newEndPoint = newStartPoint.add(d);
     fbbC2->add(newEndPoint);
     normalsC2->add(d);*/
    
    
    ShortBufferBuilder ind;
    for (int i = 0; i < nSegments*2; ++i){
        ind.add((short)i);
    }
    ind.add((short)0);
    ind.add((short)1);
    
    IFloatBuffer* vertices = fbb->create();
    IndexedMesh* im = new IndexedMesh(GLPrimitive::triangleStrip(),
                                      fbb->getCenter(),
                                      vertices,
                                      true,
                                      ind.create(),
                                      true,
                                      1.0,
                                      1.0,
                                      NULL,//new Color(color),
                                      colors.create(),//NULL,
                                      1.0f,
                                      DEPTH_ENABLED,
                                      normals->create(),
                                      false,
                                      0,
                                      0);
    
    createSphere(vs);
    
    
    delete normals;
    delete fbb;
    
    
    //return cm;
    return im;
    
}

void Cylinder::createSphere(std::vector<Vector3D*> &vs){
    /*std::vector<Vector3D*> vs;
     for (int i=0; i < fbb->size(); i++) {
     vs.push_back(new Vector3D(fbb->getVector3D(i)));
     }*/
    
    Sphere sphere = Sphere::createSphereContainingPoints(vs);
    s = new Sphere(sphere);
    for (size_t i = 0; i < vs.size(); i++) {
        delete vs.at(i);
    }
    vs.clear(); 
}

#warning Chano adding stuff


std::vector<Mesh *> Cylinder::visibleMeshes(MeshRenderer *mr, const Camera *camera,
                                            const Planet *planet,
                                            std::vector<CylinderMeshInfo> *cylInfo,
                                            std::vector<CylinderMeshInfo> &visibleInfo){
    int ori = 0, inc = 1;
    if (getDitchEnabled()){
        ori = 1, inc = 2;
    }
    std::vector<Mesh *> theMeshes = mr->getMeshes(ori,inc);
    std::vector<Mesh *> theVisibleMeshes;
    
    
    for (size_t i=0;i<theMeshes.size();i++){
        IndexedMesh *im = NULL;
        im = (IndexedMesh *) theMeshes[i];
        
        //        CompositeMesh *cm = (CompositeMesh *) theMeshes[i];
       // IndexedMesh *im = (IndexedMesh *) theMeshes[i]; // cm->getChildAtIndex(0);
        // Pregunta: ¿el cash devuelve un puntero diferente a la misma dirección de memoria o otra dirección de memoria?
        cylInfo->at(i)._cylId = (int) i;
        CylinderMeshInfo info = cylInfo->at(i);
        // IFloatBuffer *vertices = im->getVerticesFloatBuffer();
        //const size_t numberVertices = vertices->size();
        bool visible = false;
        int vpWidth = camera->getViewPortWidth();
        int vpHeight = camera->getViewPortHeight();
        for (size_t j=0; j<info._latlng.size(); j+=3){
            //¿Cómo se definen los vértices? //
            Vector3D vPos = planet->toCartesian(Geodetic3D::fromDegrees(info._latlng[j], info._latlng[j+1], info._latlng[j+2]));
            Vector2F pixel = camera->point2Pixel(vPos);
            if (pixel._x >= 0 and pixel._x < vpWidth and pixel._y >=0 and pixel._y <= vpHeight){
                //            if (pixel._x >= 0 and pixel._x < vpHeight and pixel._y >=0 and pixel._y <= vpWidth){
                visible = true;
                Vector3D ray = camera->pixel2Ray(pixel).normalized();
                break;
            }
        }
        if (visible){
            theVisibleMeshes.push_back(im);
            visibleInfo.push_back(CylinderMeshInfo(cylInfo->at(i)));
        }
    }
    return theVisibleMeshes;
}

std::vector<double> Cylinder::distances(CylinderMeshInfo info,
                                        const Camera *camera,
                                        const Planet *planet){
    std::vector<double> dt;
    
    for (size_t i=0; i<info._latlng.size(); i=i+3){
        Geodetic2D userPosition = camera->getGeodeticPosition().asGeodetic2D();
        Geodetic2D vertexPosition = Geodetic2D::fromDegrees(info._latlng[i],info._latlng[i+1]);
        dt.push_back(planet->computeFastLatLonDistance(userPosition, vertexPosition));
    }
    return dt;
}


std::string Cylinder::adaptMeshes(MeshRenderer *mr,
                                  std::vector<CylinderMeshInfo> * cylInfo,
                                  const Camera *camera,
                                  const Planet *planet){
    
    std::vector<CylinderMeshInfo> visibleInfo;
    std::vector<Mesh*> theMeshes = visibleMeshes(mr,camera,planet,cylInfo,visibleInfo);
    
    double maxDt = DISTANCE_VALUE;
    std::string text = "";
    
    for (size_t i=0;i<theMeshes.size();i++){
        std::vector<double> dt = distances(visibleInfo.at(i),camera,planet);
        //bool textWritten = false;
        for (size_t j=0;j< dt.size(); j++) {
            //maxDt = fmax(maxDt,dt[j]);
            if ((dt[j] < PROXIMITY_VALUE)){// && (!textWritten)){
                char buffer [75];
                sprintf(buffer, "You are close to a visible pipe with id %d \n",visibleInfo.at(i)._cylId);
                text.append(buffer);
                // textWritten = true;
                break;
            }
        }
    }
    
    for (size_t i=0;i<theMeshes.size();i++){
        Mesh *im = (Mesh *) theMeshes[i];
        std::vector<double> dt = distances(visibleInfo.at(i),camera,planet);
        for (size_t i = 0; i < dt.size(); ++i){
            dt[i] = (getDitchEnabled())? 1.0: getAlpha(dt[i], maxDt, true);
        }
        im->setColorTransparency(dt);
    }
    return text;
}

double Cylinder::getAlpha(double distance, double proximityThreshold, bool divide){
    if (getDepthEnabled() == true){
        return rawAlpha(distance,10000,false); //This should cover a whole city without problems.
    }
    else switch (getDistanceMethod()) {
        case 1:
            return rawAlpha(distance,proximityThreshold,divide);
        case 2:
            return linearAlpha(distance,proximityThreshold,divide);
        case 3:
            return smoothstepAlpha(distance,proximityThreshold,divide);
        case 4:
            return perlinSmootherstepAlpha(distance,proximityThreshold,divide);
        case 5:
            return mcDonaldSmootheststepAlpha(distance,proximityThreshold,divide);
        case 6:
            return sigmoidAlpha(distance,proximityThreshold,divide);
        case 7:
            return tanhAlpha(distance,proximityThreshold,divide);
        case 8:
            return arctanAlpha(distance,proximityThreshold,divide);
        case 9:
            return softsignAlpha(distance,proximityThreshold,divide);
    }
    
    return NAND; // NAN
}

double Cylinder::rawAlpha(double distance, double proximityThreshold, bool divide){
    double ndt = (distance > proximityThreshold)? 0: 1;
    if (divide) ndt = ndt / 2;
    return ndt;
}

double Cylinder::linearAlpha(double distance, double proximityThreshold, bool divide){
    double ndt = (distance > proximityThreshold)? 0: 1 - ((distance / proximityThreshold));
    //    ¿Dividir entre 2 para alpha inicial 0.5 ? //
    if (divide) ndt = ndt / 2;
    
    return ndt;
}

double Cylinder::smoothstepAlpha(double distance, double proximityThreshold, bool divide){
    double softDistance = distance / proximityThreshold;
    double ndt = (distance > proximityThreshold)? 0: 1 - ( 3*pow(softDistance,2) - 2*pow(softDistance,3));
    //    ¿Dividir entre 2 para alpha inicial 0.5 ? //
    if (divide) ndt = ndt / 2;
    
    return ndt;
}
double Cylinder::perlinSmootherstepAlpha (double distance, double proximityThreshold, bool divide){
    double softDistance = distance / proximityThreshold;
    double ndt = (distance > proximityThreshold)? 0: 1 - ( 6*pow(softDistance,5) - 15*pow(softDistance,4) + 10*pow(softDistance,3));
    //    ¿Dividir entre 2 para alpha inicial 0.5 ? //
    if (divide) ndt = ndt / 2;
    
    return ndt;
    
}
double Cylinder::mcDonaldSmootheststepAlpha (double distance, double proximityThreshold, bool divide){
    double softDistance = distance / proximityThreshold;
    double ndt = (distance > proximityThreshold)? 0: 1 - ( -20*pow(softDistance,7) + 70*pow(softDistance,6) - 84*pow(softDistance,5) + 35*pow(softDistance,4));
    //    ¿Dividir entre 2 para alpha inicial 0.5 ? //
    if (divide) ndt = ndt / 2;
    
    return ndt;
}
double Cylinder::sigmoidAlpha (double distance, double proximityThreshold, bool divide){
    double softDistance = distance / proximityThreshold;
    softDistance = (softDistance * 10) - 5;
    
    double ndt = (distance > proximityThreshold)? 0: 1 - ( 1 /(1 + exp(-softDistance)));
    //    ¿Dividir entre 2 para alpha inicial 0.5 ? //
    if (divide) ndt = ndt / 2;
    
    return ndt;
    
}
double Cylinder::tanhAlpha (double distance, double proximityThreshold, bool divide){
    double softDistance = distance / proximityThreshold;
    softDistance = (softDistance * 10) - 5;
    
    double factor = 2 /(1 + exp(-2*softDistance));
    factor = factor / 2; // Para convertir de 0-2 a 0-1
    
    double ndt = (distance > proximityThreshold)? 0: 1 - factor;
    //    ¿Dividir entre 2 para alpha inicial 0.5 ? //
    if (divide) ndt = ndt / 2;
    
    return ndt;
}

double Cylinder::arctanAlpha (double distance, double proximityThreshold, bool divide){
    double softDistance = distance / proximityThreshold;
    softDistance = (softDistance * 20) - 10;
    
    double factor = atan(softDistance);
    factor = factor + 1.5;
    factor = factor / 3; // Para convertir de -1.5 / 1.5 a 0 / 1
    
    double ndt = (distance > proximityThreshold)? 0: 1 - factor;
    //    ¿Dividir entre 2 para alpha inicial 0.5 ? //
    if (divide) ndt = ndt / 2;
    
    return ndt;
}

double Cylinder::softsignAlpha (double distance, double proximityThreshold, bool divide){
    double softDistance = distance / proximityThreshold;
    softDistance = (softDistance * 100) - 50;
    
    double factor = softDistance /(1 + fabs(softDistance));
    factor = factor + 1;
    factor = factor / 2; // Para convertir de -1 / 1 a 0 / 1
    
    double ndt = (distance > proximityThreshold)? 0: 1 - factor;
    //    ¿Dividir entre 2 para alpha inicial 0.5 ? //
    if (divide) ndt = ndt / 2;
    
    return ndt;
}
