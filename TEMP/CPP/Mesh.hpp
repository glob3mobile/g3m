//
//  Mesh.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/06/12.
//

#ifndef G3MiOSSDK_Mesh
#define G3MiOSSDK_Mesh

#include "G3MContext.hpp"
#include "BoundingVolume.hpp"

#include "GLState.hpp"

class Vector3D;
class GPUProgramState;

class Mesh {
private:
    bool _enable;
public:
    Mesh() :
    _enable(true)
    {
    }
    
    void setEnable(bool enable) {
        _enable = enable;
    }
    
    bool isEnable() const {
        return _enable;
    }
    
    virtual ~Mesh() {
    }
    
    virtual size_t getVertexCount() const = 0;
    
    virtual const Vector3D getVertex(size_t i) const = 0;
    
    virtual BoundingVolume* getBoundingVolume() const = 0;
    
    virtual bool isTransparent(const G3MRenderContext* rc) const = 0;
    
    virtual void rawRender(const G3MRenderContext* rc,
                           const GLState* parentGLState) const = 0;
    
    void render(const G3MRenderContext* rc,
                const GLState* parentGLState) const {
        if (_enable) {
            rawRender(rc, parentGLState);
        }
    }
    
    virtual void showNormals(bool v) const = 0;
    
    virtual void setColorTransparency(const std::vector<double>& transparency){}
    
};


#endif
