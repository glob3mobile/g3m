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
    
    Vector3D p = _start.add(r.times(_radius / r.length()));
    
    const double totalAngle = _endAngle._radians - _startAngle._radians;
    
    MutableMatrix44D m = MutableMatrix44D::createGeneralRotationMatrix(Angle::fromDegrees(totalAngle / nSegments),
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
    x = x.transformedBy(MutableMatrix44D::createGeneralRotationMatrix(_startAngle, d, _start), 1.0);

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
                                      NULL,
                                      colors.create(),
                                      true,
                                      normals->create(),
                                      false,
                                      0,
                                      0);
    delete normals;
    delete fbb;
    return im;
}
