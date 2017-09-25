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

Mesh* Cylinder::createMesh(const Color& color, const int nSegments){
  
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
  
  MutableVector3D x(p);
  for (int i = 0; i < nSegments; ++i){
    
    //Tube
    Vector3D newStartPoint = x.asVector3D().transformedBy(m, 1.0);
    Vector3D newEndPoint = newStartPoint.add(d);
    x.set(newStartPoint._x, newStartPoint._y, newStartPoint._z);
    
    fbb->add(newStartPoint);
    fbb->add(newEndPoint);
    
    normals->add(newStartPoint.sub(_start));
    normals->add(newEndPoint.sub(_end));
    
    //Cover1
    fbbC1->add(newStartPoint);
    normalsC1->add(d.times(-1.0));
    //Cover2
    fbbC2->add(newEndPoint);
    normalsC2->add(d);
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
  
  IndexedMesh* im = new IndexedMesh(GLPrimitive::triangleStrip(),
                                    fbb->getCenter(),
                                    vertices,
                                    true,
                                    ind.create(),
                                    true,
                                    1.0,
                                    1.0,
                                    new Color(color),
                                    NULL,
                                    1.0f,
                                    true,
                                    normals->create(),
                                    false,
                                    0,
                                    0);
  
  
  CompositeMesh* cm = new CompositeMesh();
  cm->addMesh(im);
  
  //Covers
  if (true){
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
