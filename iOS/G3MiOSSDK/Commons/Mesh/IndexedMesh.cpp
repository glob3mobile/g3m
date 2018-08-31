//
//  IndexedMesh.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 22/06/12.
//

#include <stdlib.h>

#include "IndexedMesh.hpp"
#include "GL.hpp"
#include "IShortBuffer.hpp"


IndexedMesh::~IndexedMesh() {
    if (_ownsIndices) {
        delete _indices;
    }
    
#ifdef JAVA_CODE
    super.dispose();
#endif
    
}

IndexedMesh::IndexedMesh(const int primitive,
                         const Vector3D& center,
                         IFloatBuffer* vertices,
                         bool ownsVertices,
                         IShortBuffer* indices,
                         bool ownsIndices,
                         float lineWidth,
                         float pointSize,
                         const Color* flatColor,
                         IFloatBuffer* colors,
                         const float colorsIntensity,
                         bool depthTest,
                         IFloatBuffer* normals,
                         bool polygonOffsetFill,
                         float polygonOffsetFactor,
                         float polygonOffsetUnits,
                         VertexColorScheme* vertexColorScheme,
                         float transparencyDistanceThreshold) :
AbstractMesh(primitive,
             ownsVertices,
             center,
             vertices,
             lineWidth,
             pointSize,
             flatColor,
             colors,
             colorsIntensity,
             depthTest,
             normals,
             polygonOffsetFill,
             polygonOffsetFactor,
             polygonOffsetUnits,
             vertexColorScheme,
             transparencyDistanceThreshold),
_indices(indices),
_ownsIndices(ownsIndices)
{
    
}

void IndexedMesh::rawRender(const G3MRenderContext* rc) const {
    GL* gl = rc->getGL();
    gl->drawElements(_primitive, _indices, _glState, *rc->getGPUProgramManager());
}


const void IndexedMesh::getTrianglePrimitive(short t,
                                             MutableVector3D& v1,
                                             MutableVector3D& v2,
                                             MutableVector3D& v3,
                                             short& i1,
                                             short& i2,
                                             short& i3) const{
    t *= 3;
    i1 = _indices->get(t);
    i2 = _indices->get(t+1);
    i3 = _indices->get(t+2);
    
    v1.copyFrom(getVertex(i1));
    v2.copyFrom(getVertex(i2));
    v3.copyFrom(getVertex(i3));
}


const HitTestResult IndexedMesh::getHitWithRayForTrianglePrimitive(const Vector3D& origin,
                                                 const Vector3D& ray,
                                                              short firstTriangle,
                                                              short lastTriangle) const{
    if (_primitive != GLPrimitive::triangles()
        && _primitive != GLPrimitive::triangleStrip()){
        ILogger::instance()->logError("getHitWithRayForTrianglePrimitive(): Primitive not supported");
    }
    
    if (firstTriangle > lastTriangle){
        firstTriangle = 0;
        lastTriangle = (short)(_indices->size() / 3);
    }
    
    MutableVector3D v1,v2,v3, closestP = MutableVector3D::nan();
    double minDist = IMathUtils::instance()->maxDouble();
    //Triangle intersection
    HitTestResult hit;
    for(short t = firstTriangle; t < lastTriangle; ++t){
        short i1, i2,i3;
        getTrianglePrimitive(t,v1,v2,v3, i1,i2,i3);
        
        Vector3D p = Vector3D::rayIntersectsTriangle(origin,
                                                     ray,
                                                     v1.asVector3D(),
                                                     v2.asVector3D(),
                                                     v3.asVector3D());
        
        if (!p.isNan()){
            double dist = p.squaredDistanceTo(origin);
            if (dist < minDist){
                hit._position.copyFrom(p);
                hit._t0 = i1;
                hit._t1 = i2;
                hit._t2 = i3;
                minDist = dist;
                
            }
        }
    }
    
    return hit;
}
