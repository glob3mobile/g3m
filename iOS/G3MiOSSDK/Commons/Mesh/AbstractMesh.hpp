//
//  AbstractMesh.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/1/12.
//
//

#ifndef __G3MiOSSDK__AbstractMesh__
#define __G3MiOSSDK__AbstractMesh__

#include "Mesh.hpp"

#include "VertexColorScheme.hpp"

class AbstractMesh : public Mesh {
protected:
    const int               _primitive;
    const bool              _owner;
    const Vector3D                _center;
    const MutableMatrix44D* _translationMatrix;
    const IFloatBuffer*           _vertices;
    const Color*            _flatColor;
    const IFloatBuffer*           _colors;
    const float             _colorsIntensity;
    const float             _lineWidth;
    const float             _pointSize;
    const bool              _depthTest;
    const IFloatBuffer*           _normals;
    
    VertexColorScheme* _vertexColorScheme;
    
    float _transparencyDistanceThreshold;
    ModelGLFeature* _model;
    
    ModelTransformGLFeature* _modelTransform;
    
    mutable BoundingVolume* _boundingVolume;
    BoundingVolume* computeBoundingVolume() const;
    
    const bool _polygonOffsetFill;
    const float _polygonOffsetFactor;
    const float _polygonOffsetUnits;
    
    AbstractMesh(const int primitive,
                 bool owner,
                 const Vector3D& center,
                 const IFloatBuffer* vertices,
                 float lineWidth,
                 float pointSize,
                 const Color* flatColor,
                 const IFloatBuffer* colors,
                 const float colorsIntensity,
                 bool depthTest,
                 const IFloatBuffer* normals,
                 bool polygonOffsetFill,
                 float polygonOffsetFactor,
                 float polygonOffsetUnits,
                 VertexColorScheme* vertexColorScheme,
                 float transparencyDistanceThreshold);
    
    virtual void rawRender(const G3MRenderContext* rc) const = 0;
    
    GLState* _glState;
    
    void createGLState();
    
    mutable bool _showNormals;
    mutable Mesh* _normalsMesh;
    Mesh* createNormalsMesh() const;
    
public:
    ~AbstractMesh();
    
    BoundingVolume* getBoundingVolume() const;
    
    size_t getVertexCount() const;
    
    const Vector3D getVertex(size_t i) const;
    
    bool isTransparent(const G3MRenderContext* rc) const;
    
    void rawRender(const G3MRenderContext* rc,
                   const GLState* parentGLState) const;
    
    Vector3D getCenter() const{
        return _center;
    }
    
    void showNormals(bool v) const {
        _showNormals = v;
    }
    
    IFloatBuffer* getColorsFloatBuffer() const{
        return (IFloatBuffer*)_colors;
    }
    
#warning Chano adding stuff.
    
    IFloatBuffer* getVerticesFloatBuffer() const{
        return (IFloatBuffer*) _vertices;
    }
    
    virtual void setColorTransparency(const std::vector<double>& transparency){
        IFloatBuffer *colors = (IFloatBuffer*)_colors;
        if (colors != NULL){
            for (size_t j=3, c=0; j<colors->size(); j=j+4, c++){
                double ndt = transparency[c];
                colors->put(j,(float)ndt); //Suponiendo que sea un valor de alpha
            }
        }
    }
    
    VertexColorScheme* getVertexColorScheme() const{
        return _vertexColorScheme;
    }
    
    void setTransformation(Matrix44D* matrix){
        _modelTransform->setMatrix(matrix);
    }
    
    
};

#endif
