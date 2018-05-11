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

#include "Vector3D.hpp"
#include "GLState.hpp"

class MutableMatrix44D;
class IFloatBuffer;
class Color;

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
    
    IFloatBuffer*     _valuesInColorRange;
    IFloatBuffer*     _nextValuesInColorRange;
    const Color* _colorRangeAt0;
    const Color* _colorRangeAt1;
    ColorRangeGLFeature* _colorRangeGLFeature;
    DynamicColorRangeGLFeature* _dynamicColorRangeGLFeature;
    float _currentTime;
    
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
                 IFloatBuffer* valuesInColorRange = NULL,
                 const Color* colorRangeAt0 = NULL,
                 const Color* colorRangeAt1 = NULL,
                 IFloatBuffer* nextValuesInColorRange = NULL,
                 float currentTime = 0.0f);
    
    virtual void rawRender(const G3MRenderContext* rc) const = 0;
    //  virtual void rawRender(const G3MRenderContext* rc, const GLState* parentGLState) const = 0;
    
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
    
    void setColorRangeValues(IFloatBuffer* values){
        if (_colorRangeGLFeature != NULL){
            _colorRangeGLFeature->setValues(values);
        }
    }
    
    void setTime(float time){
        if (_currentTime != time){
            _currentTime = time;
            _dynamicColorRangeGLFeature->setTime(time);
        }
    }
    
    
};

#endif
