//
//  IndexedMesh.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 22/06/12.
//

#ifndef G3MiOSSDK_IndexedMesh
#define G3MiOSSDK_IndexedMesh

#include "AbstractMesh.hpp"
#include "MutableVector3D.hpp"

class IShortBuffer;

struct HitTestResult{
public:
    MutableVector3D _position = MutableVector3D::nan();
    short _t0=0, _t1=0, _t2=0;
};

class IndexedMesh : public AbstractMesh {
private:
    IShortBuffer*       _indices;
    bool _ownsIndices;
protected:
    void rawRender(const G3MRenderContext* rc) const;
    
public:
    IndexedMesh(const int primitive,
                const Vector3D& center,
                IFloatBuffer* vertices,
                bool ownsVertices,
                IShortBuffer* indices,
                bool ownsIndices,
                float lineWidth,
                float pointSize = 1,
                const Color* flatColor = NULL,
                IFloatBuffer* colors = NULL,
                const float colorsIntensity = 0.0f,
                bool depthTest = true,
                IFloatBuffer* normals = NULL,
                bool polygonOffsetFill = false,
                float polygonOffsetFactor = 0,
                float polygonOffsetUnits = 0,
                VertexColorScheme* vertexColorScheme = NULL,
                float transparencyDistanceThreshold = -1.0f);
    
    ~IndexedMesh();
    
    const IShortBuffer* getIndices() const {
        return _indices;
    }
    
    const void getTrianglePrimitive(short t,
                                    MutableVector3D& v1,
                                    MutableVector3D& v2,
                                    MutableVector3D& v3,
                                    short& i1,
                                    short& i2,
                                    short& i3) const;
    
    //if lastTriangle < firstTriangle -> All triangles will be tested
    const HitTestResult getHitWithRayForTrianglePrimitive(const Vector3D& origin,
                                                     const Vector3D& ray,
                                                     short firstTriangle=(short)1,
                                                     short lastTriangle=(short)0) const;
    
};

#endif
