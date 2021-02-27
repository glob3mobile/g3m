//
//  Cylinder.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 18/2/21.
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
#include "Angle.hpp"


Mesh* Cylinder::createMesh(const Color& color, const int nSegments) const{
  
  Vector3D d = _end.sub(_start);
  Vector3D r = d._z == 0? Vector3D(0.0,0.0,1.0) : Vector3D(1.0, 1.0, (-d._x -d._y) / d._z);
  
  Vector3D pStart = _start.add(r.times(_startRadius / r.length()));
  Vector3D pEnd = _end.add(r.times(_endRadius / r.length()));
  MutableMatrix44D mStart = MutableMatrix44D::createGeneralRotationMatrix(Angle::fromDegrees(360.0 / nSegments),
                                                                     d,
                                                                     _start);
  MutableMatrix44D mEnd = MutableMatrix44D::createGeneralRotationMatrix(Angle::fromDegrees(360.0 / nSegments),
                                                                     d,
                                                                     _end);
  
  FloatBufferBuilderFromCartesian3D* fbb = FloatBufferBuilderFromCartesian3D::builderWithFirstVertexAsCenter();
  FloatBufferBuilderFromCartesian3D* normals = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();
  FloatBufferBuilderFromColor colors;
  
  MutableVector3D newStartPoint(pStart), newEndPoint(pEnd);
  for (int i = 0; i < nSegments; ++i){
    
    //Tube
    newStartPoint.set(newStartPoint.transformedBy(mStart, 1.0));
    newEndPoint.set(newEndPoint.transformedBy(mEnd, 1.0));
    
    fbb->add(newStartPoint.asVector3D());
    fbb->add(newEndPoint.asVector3D());
    
    normals->add(newStartPoint.sub(_start));
    normals->add(newEndPoint.sub(_end));
    
    colors.add(color);
    colors.add(color);
  }
  
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
                                    1.0f,
                                    1.0f,
                                    new Color(color),
                                    NULL,//colors.create(),
                                    true,
                                    normals->create(),
                                    false,
                                    0,
                                    0);
  delete normals;
  delete fbb;
  return im;
}
