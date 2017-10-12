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

Mesh* Cylinder::createMesh(const Color& color, const int nSegments, const Planet *planet){
  
  Vector3D d = _end.sub(_start);
  Vector3D r = d._z == 0? Vector3D(0.0,0.0,1.0) : Vector3D(1.0, 1.0, (-d._x -d._y) / d._z);
  
  Vector3D p = _start.add(r.times(_radius / r.length()));
  
  MutableMatrix44D m = MutableMatrix44D::createGeneralRotationMatrix(Angle::fromDegrees(360.0 / nSegments),
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
  for (int i = 0; i < nSegments; ++i){
    
    //Tube
    Vector3D newStartPoint = x.asVector3D().transformedBy(m, 1.0);
    Vector3D newEndPoint = newStartPoint.add(d);
    x.set(newStartPoint._x, newStartPoint._y, newStartPoint._z);
    
    fbb->add(newStartPoint);
    fbb->add(newEndPoint);
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
  Vector3D newStartPoint = x.asVector3D().transformedBy(m, 1.0);
  fbbC1->add(newStartPoint);
  normalsC1->add(d.times(-1.0));
  Vector3D newEndPoint = newStartPoint.add(d);
  fbbC2->add(newEndPoint);
  normalsC2->add(d);
  

  ShortBufferBuilder ind;
  for (int i = 0; i < nSegments*2; ++i){
    ind.add((short)i);
  }
  ind.add((short)0);
  ind.add((short)1);
  
  IFloatBuffer* vertices = fbb->create();
#warning Tercer parámetro booleano == Depth Test. True oculta todo lo subterráneo.
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
                                    false,
                                    normals->create(),
                                    false,
                                    0,
                                    0);
  
  
  CompositeMesh* cm = new CompositeMesh();
  cm->addMesh(im);
  
  //Covers
  if (/* DISABLES CODE */ (false)){
    DirectMesh* c1 = new DirectMesh(GLPrimitive::triangleFan(),
                                    true,
                                    fbbC1->getCenter(),
                                    fbbC1->create(),
                                    1.0,
                                    1.0,
                                    new Color(color));
    cm->addMesh(c1);
    
    DirectMesh* c2 = new DirectMesh(GLPrimitive::triangleFan(),
                                    true,
                                    fbbC2->getCenter(),
                                    fbbC2->create(),
                                    1.0,
                                    1.0,
                                    new Color(color));
    cm->addMesh(c2);
  }
  
  
  delete normals;
  delete fbb;
  
  
  return cm;
  
}

#warning Chano adding stuff


std::vector<Mesh *> Cylinder::visibleMeshes(MeshRenderer *mr, const Camera *camera,
                                            const Planet *planet,
                                            std::vector<CylinderMeshInfo> *cylInfo,
                                            std::vector<CylinderMeshInfo> &visibleInfo){
    std::vector<Mesh *> theMeshes = mr->getMeshes();
    std::vector<Mesh *> theVisibleMeshes;
    for (size_t i=0;i<theMeshes.size();i++){
        CompositeMesh *cm = (CompositeMesh *) theMeshes[i];
        IndexedMesh *im = (IndexedMesh *) cm->getChildAtIndex(0);
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
    
    double maxDt = 100;
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
        IndexedMesh *im = (IndexedMesh *) theMeshes[i];
        std::vector<double> dt = distances(visibleInfo.at(i),camera,planet);
        IFloatBuffer *colors = im->getColorsFloatBuffer();
        for (size_t j=3, c=0; j<colors->size(); j=j+4, c++){
            double ndt = (dt[c] > 100)? 0: 1 - ((dt[c] / maxDt));
            colors->put(j,(float)ndt); //Suponiendo que sea un valor de alpha
        }
    }
    return text;
}

