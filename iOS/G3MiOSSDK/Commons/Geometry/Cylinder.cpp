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

Mesh* Cylinder::createMesh(const Color& color, const int nSegments){
  
  Vector3D d = _end.sub(_start);
  Vector3D r = d._z == 0? Vector3D(0.0,0.0,1.0) : Vector3D(1.0, 1.0, (-d._x -d._y) / d._z);
  
  Vector3D p = _start.add(r.times(_radius / r.length()));
  
  MutableMatrix44D m = MutableMatrix44D::createGeneralRotationMatrix(Angle::fromDegrees(360.0 / nSegments),
                                                                     d,
                                                                     _start);
  
  FloatBufferBuilderFromCartesian3D* fbb = FloatBufferBuilderFromCartesian3D::builderWithFirstVertexAsCenter();
  
  FloatBufferBuilderFromCartesian3D* normals = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();
  MutableVector3D x(p);
  for (int i = 0; i < nSegments; ++i){
    
    Vector3D newStartPoint = x.asVector3D().transformedBy(m, 1.0);
    Vector3D newEndPoint = newStartPoint.add(d);
    x.set(newStartPoint._x, newStartPoint._y, newStartPoint._z);
    
    fbb->add(newStartPoint);
    fbb->add(newEndPoint);
    
    normals->add(newStartPoint.sub(_start));
    normals->add(newEndPoint.sub(_end));
    
  }
  
  ShortBufferBuilder ind;
  for (int i = 0; i < nSegments*2; ++i){
    ind.add((short)i);
  }
  ind.add((short)0);
  ind.add((short)1);
  
  IndexedMesh* im = new IndexedMesh(GLPrimitive::triangleStrip(),
                           fbb->getCenter(),
                           fbb->create(),
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
  
  delete normals;
  delete fbb;
  return im;
  
}
