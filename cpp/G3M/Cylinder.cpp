//
//  Cylinder.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 18/2/21.
//

#include "Cylinder.hpp"

#include "Angle.hpp"
#include "MutableMatrix44D.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "FloatBufferBuilderFromColor.hpp"
#include "MutableVector3D.hpp"
#include "ShortBufferBuilder.hpp"
#include "IndexedMesh.hpp"
#include "Color.hpp"


Mesh* Cylinder::createMesh(const Color& color, const int nSegments) const{
  
  Vector3D d = _end.sub(_start);
  Vector3D r = (d._z == 0) ? Vector3D(0.0,0.0,1.0) : Vector3D(1.0, 1.0, (-d._x -d._y) / d._z);
  
  Vector3D pStart = _start.add(r.times(_startRadius / r.length()));
  Vector3D pEnd = _end.add(r.times(_endRadius / r.length()));
  MutableMatrix44D mStart = MutableMatrix44D::createGeneralRotationMatrix(Angle::fromDegrees(360.0 / nSegments),
                                                                          d,
                                                                          _start);
  MutableMatrix44D mEnd = MutableMatrix44D::createGeneralRotationMatrix(Angle::fromDegrees(360.0 / nSegments),
                                                                        d,
                                                                        _end);
  
  FloatBufferBuilderFromCartesian3D* vertices = FloatBufferBuilderFromCartesian3D::builderWithFirstVertexAsCenter();
  FloatBufferBuilderFromCartesian3D* normals  = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();

  MutableVector3D newStartPoint(pStart), newEndPoint(pEnd);
  for (int i = 0; i < nSegments; ++i){
    
    //Tube
    newStartPoint.set(newStartPoint.transformedBy(mStart, 1.0));
    newEndPoint.set(newEndPoint.transformedBy(mEnd, 1.0));
    
    vertices->add(newStartPoint.asVector3D());
    vertices->add(newEndPoint.asVector3D());
    
    normals->add(newStartPoint.sub(_start));
    normals->add(newEndPoint.sub(_end));
  }
  
  ShortBufferBuilder indices;
  for (int i = 0; i < nSegments*2; ++i){
    indices.add((short) i);
  }
  indices.add((short) 0);
  indices.add((short) 1);
  
  IndexedMesh* im = new IndexedMesh(GLPrimitive::triangleStrip(),
                                    vertices->getCenter(),
                                    vertices->create(),
                                    true,
                                    indices.create(),
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
  delete vertices;

  return im;
}
