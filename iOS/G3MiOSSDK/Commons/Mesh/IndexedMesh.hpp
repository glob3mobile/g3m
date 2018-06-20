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
                IFloatBuffer* valuesInColorRange = NULL,
                const Color* colorRangeAt0 = NULL,
                const Color* colorRangeAt1 = NULL,
                IFloatBuffer* nextValuesInColorRange = NULL,
                float currentTime = 0.0f,
                float transparencyDistanceThreshold = -1.0f);
    
    ~IndexedMesh();
    
    const IShortBuffer* getIndices() const {
        return _indices;
    }
    
    const void getTrianglePrimitive(short t,
                                    MutableVector3D& v1,
                                    MutableVector3D& v2,
                                    MutableVector3D& v3) const;
    
};

#endif
