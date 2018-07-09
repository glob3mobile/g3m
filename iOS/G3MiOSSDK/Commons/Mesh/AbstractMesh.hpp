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

class VertexColorScheme{
protected:
    GLFeature* _feat;
public:
    virtual ~VertexColorScheme(){
        _feat->_release();
    }
    virtual GLFeature* getGLFeature() = 0;
    virtual void setTime(float time){};
    virtual void setColorRangeStaticValues(IFloatBuffer* values) const{};
    virtual void setColorRangeDynamicValues(IFloatBuffer* values,
                                            IFloatBuffer* nextValues) const{};
};

class Static2ColorScheme: public VertexColorScheme{
public:
    Static2ColorScheme(IFloatBuffer* valuesInColorRange,
                        const Color& colorRangeAt0,
                        const Color& colorRangeAt1){
        _feat = new ColorRangeGLFeature(colorRangeAt0,
                                               colorRangeAt1,
                                               valuesInColorRange);
    }
    GLFeature* getGLFeature(){
        return _feat;
    }
    
    void setColorRangeStaticValues(IFloatBuffer* values) const{
        ((ColorRangeGLFeature*)_feat)->setValues(values);
    }
};


class Dynamic2ColorScheme: public VertexColorScheme{
public:
    
    DynamicColorRangeGLFeature* _feat;
    
    Dynamic2ColorScheme(IFloatBuffer* valuesInColorRange,
                        IFloatBuffer* nextValuesInColorRange,
                        const Color& colorRangeAt0,
                        const Color& colorRangeAt1){
        _feat = new DynamicColorRangeGLFeature(colorRangeAt0,
                                                colorRangeAt1,
                                                valuesInColorRange,
                                                nextValuesInColorRange,
                                                0.0f);
    }
    GLFeature* getGLFeature(){
        return _feat;
    }
    
    virtual void setTime(float time) const{
        ((DynamicColorRangeGLFeature*)_feat)->setTime(time);
    }
    
    void setColorRangeDynamicValues(IFloatBuffer* values,
                                            IFloatBuffer* nextValues) const{
        ((DynamicColorRangeGLFeature*)_feat)->setValues(values, nextValues);
    }
};


class Dynamic3ColorScheme: public VertexColorScheme{
public:
    
    DynamicColorRange3GLFeature* _feat;
    
    Dynamic3ColorScheme(IFloatBuffer* valuesInColorRange,
                        IFloatBuffer* nextValuesInColorRange,
                        const Color& colorRangeAt0,
                        const Color& colorRangeAt0_5,
                        const Color& colorRangeAt1){
        _feat = new DynamicColorRange3GLFeature(colorRangeAt0,
                                              colorRangeAt0_5,
                                              colorRangeAt1,
                                              valuesInColorRange,
                                              nextValuesInColorRange,
                                              0.0f);
    }
    
    
    GLFeature* getGLFeature(){
        return _feat;
    }
    
    virtual void setTime(float time) const{
        ((DynamicColorRange3GLFeature*)_feat)->setTime(time);
    }
    
    void setColorRangeDynamicValues(IFloatBuffer* values,
                                            IFloatBuffer* nextValues) const{
        ((DynamicColorRange3GLFeature*)_feat)->setValues(values, nextValues);
    }
};

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
