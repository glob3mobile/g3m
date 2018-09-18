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
#include "MeshRenderer.hpp"
#include "Camera.hpp"
#include "FloatBufferBuilderFromColor.hpp"

Mesh* Cylinder::createMesh(const Color& color,
                           const int nSegments,
                           bool depthTest,
                           const Planet *planet){
    
    Vector3D cylinderAxis = _end.sub(_start);
    Vector3D r = cylinderAxis._z == 0? Vector3D(0.0,0.0,1.0) : Vector3D(1.0, 1.0, (-cylinderAxis._x -cylinderAxis._y) / cylinderAxis._z);
    
    Vector3D pointAtStart = _start.add(r.times(_startRadius / r.length()));
    Vector3D pointAtEnd = _end.add(r.times(_endRadius / r.length()));
    
    const double totalAngle = _endAngle - _startAngle;
    Angle stepAngle = Angle::fromDegrees(totalAngle / nSegments);
    
    MutableMatrix44D startTransf = MutableMatrix44D::createGeneralRotationMatrix(
                                                                       stepAngle,
                                                                       cylinderAxis,
                                                                       _start);
    MutableMatrix44D endTransf = MutableMatrix44D::createGeneralRotationMatrix(stepAngle,
                                                                  cylinderAxis, _end);
    
    FloatBufferBuilderFromCartesian3D* fbb = FloatBufferBuilderFromCartesian3D::builderWithFirstVertexAsCenter();
    FloatBufferBuilderFromCartesian3D* fbbC1 = FloatBufferBuilderFromCartesian3D::builderWithFirstVertexAsCenter();
    fbbC1->add(_start);
    FloatBufferBuilderFromCartesian3D* fbbC2 = FloatBufferBuilderFromCartesian3D::builderWithFirstVertexAsCenter();
    fbbC2->add(_end);
    
    FloatBufferBuilderFromCartesian3D* normals = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();
    FloatBufferBuilderFromCartesian3D* normalsC1 = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();
    normalsC1->add(cylinderAxis.times(-1.0));
    FloatBufferBuilderFromCartesian3D* normalsC2 = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();
    normalsC2->add(cylinderAxis);
    
    FloatBufferBuilderFromColor colors;
    
    MutableVector3D currentPointAtStart(pointAtStart);
    currentPointAtStart = currentPointAtStart.transformedBy(
    MutableMatrix44D::createGeneralRotationMatrix(Angle::fromDegrees(_startAngle),
                                                  cylinderAxis, _start), 1.0);
    
    MutableVector3D currentPointAtEnd(pointAtEnd);
    currentPointAtEnd = currentPointAtEnd.transformedBy(
    MutableMatrix44D::createGeneralRotationMatrix(Angle::fromDegrees(_startAngle), cylinderAxis, _end), 1.0);
    
    std::vector<Vector3D *> vs;
    
    for (int i = 0; i < nSegments; ++i){
        
        //Tube
        Vector3D newStartPoint = currentPointAtStart.asVector3D().transformedBy(startTransf, 1.0);
        //Vector3D newEndPoint = newStartPoint.add(cylinderAxis);
        Vector3D newEndPoint = currentPointAtEnd.asVector3D().transformedBy(endTransf, 1.0);
        
        currentPointAtStart.set(newStartPoint._x, newStartPoint._y, newStartPoint._z);
        currentPointAtEnd.set(newEndPoint._x, newEndPoint._y, newEndPoint._z);
        
        fbb->add(newStartPoint); vs.push_back(new Vector3D(newStartPoint));
        fbb->add(newEndPoint); vs.push_back(new Vector3D(newEndPoint));
        
        Geodetic3D stPoint = planet->toGeodetic3D(newStartPoint);
        Geodetic3D endPoint = planet->toGeodetic3D(newEndPoint);
        
        normals->add(newStartPoint.sub(_start));
        normals->add(newEndPoint.sub(_end));
        
        //Cover1
        fbbC1->add(newStartPoint);
        normalsC1->add(cylinderAxis.times(-1.0));
        //Cover2
        fbbC2->add(newEndPoint);
        normalsC2->add(cylinderAxis);
        
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
                                      depthTest, //depth_test
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
