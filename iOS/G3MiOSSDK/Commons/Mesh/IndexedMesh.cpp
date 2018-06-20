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
                         IFloatBuffer* valuesInColorRange,
                         const Color* colorRangeAt0,
                         const Color* colorRangeAt1,
                         IFloatBuffer* nextValuesInColorRange,
                         float currentTime,
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
             valuesInColorRange,
             colorRangeAt0,
             colorRangeAt1,
             nextValuesInColorRange,
             currentTime,
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
                                             MutableVector3D& v3) const{
    t *= 3;
    short i1 = _indices->get(t);
    short i2 = _indices->get(t+1);
    short i3 = _indices->get(t+2);
    
    v1.copyFrom(getVertex(i1));
    v2.copyFrom(getVertex(i2));
    v3.copyFrom(getVertex(i3));
}


const Vector3D IndexedMesh::getHitWithRayForTrianglePrimitive(const Vector3D& origin,
                                                 const Vector3D& ray,
                                                              short firstTriangle,
                                                              short lastTriangle) const{
    
    if (firstTriangle < 0){
        firstTriangle = 0;
    }
    if (lastTriangle < 0){
        lastTriangle = (short)_indices->size() / 3;
    }
    
    MutableVector3D v1,v2,v3, closestP = MutableVector3D::nan();
    double minDist = IMathUtils::instance()->maxDouble();
    //Triangle intersection
    for(short t = firstTriangle; t < lastTriangle; ++t){
        getTrianglePrimitive(t,v1,v2,v3);
        
        Vector3D p = Vector3D::rayIntersectsTriangle(origin,
                                                     ray,
                                                     v1.asVector3D(),
                                                     v2.asVector3D(),
                                                     v3.asVector3D());
        
        if (!p.isNan()){
            double dist = p.squaredDistanceTo(origin);
            if (dist < minDist){
                closestP.copyFrom(p);
                minDist = dist;
            }
        }
    }
    return closestP.asVector3D();
}
