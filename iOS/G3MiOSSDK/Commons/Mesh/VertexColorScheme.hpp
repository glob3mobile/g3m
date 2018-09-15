//
//  VertexColorScheme.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel S N on 13/07/2018.
//

#ifndef VertexColorScheme_hpp
#define VertexColorScheme_hpp


#include "Vector3D.hpp"
#include "GLFeature.hpp"
#include "IFloatBuffer.hpp"
#include "IMathUtils.hpp"

class MutableMatrix44D;
class IFloatBuffer;
class Color;

class VertexColorScheme{
protected:
    GLFeature* _feat;
    const bool _isParametric;
public:
    
    virtual ~VertexColorScheme(){
        _feat->_release();
    }
    
    VertexColorScheme(bool isParametric):
    _isParametric(isParametric){}
    
    GLFeature* getGLFeature(){
        return _feat;
    }
    virtual void setTime(float time) const{};
    virtual void setColorRangeStaticValues(IFloatBuffer* values) const{};
    virtual void setColorRangeDynamicValues(IFloatBuffer* values,
                                            IFloatBuffer* nextValues) const{};
    
    bool isParametric() const{ return _isParametric;}
    
    static VertexColorScheme* createDynamicParametric(IFloatBuffer* v0,
                                                      IFloatBuffer* v1,
                                                      const std::vector<Color*>& vc);
    
    static VertexColorScheme* createStaticParametric(IFloatBuffer* v0,
                                                     const std::vector<Color*>& vc);
};

class StaticParametric2ColorScheme: public VertexColorScheme{
    ColorRangeGLFeature* _specificFeatureHandler;
public:
    StaticParametric2ColorScheme(IFloatBuffer* valuesInColorRange,
                                 const Color& colorRangeAt0,
                                 const Color& colorRangeAt1):
    VertexColorScheme(true)
    {
        _specificFeatureHandler = new ColorRangeGLFeature(colorRangeAt0,
                                                          colorRangeAt1,
                                                          valuesInColorRange);
        _feat = _specificFeatureHandler;
    }
    
    void setColorRangeStaticValues(IFloatBuffer* values) const{
        ((ColorRangeGLFeature*)_feat)->setValues(values);
    }
};

class StaticColorScheme: public VertexColorScheme{
    ColorGLFeature* _specificFeatureHandler;
public:
    StaticColorScheme(IFloatBuffer* vertexColors):
    VertexColorScheme(false)
    {
        _specificFeatureHandler = new ColorGLFeature(vertexColors, // The attribute is a float vector of 4 elements RGBA
                                                     4,// Our buffer contains elements of 4
                                                     0,            // Index 0
                                                     false,        // Not normalized
                                                     0,            // Stride 0
                                                     true,
                                                     GLBlendFactor::srcAlpha(),
                                                     GLBlendFactor::oneMinusSrcAlpha());
        _feat = _specificFeatureHandler;
    }
    
    void setColorRangeStaticValues(IFloatBuffer* values) const{
        _specificFeatureHandler->setColors(values);
    }
};

class Static3ColorScheme: public VertexColorScheme{
    Color3RangeGLFeature* _specificFeatureHandler;
public:
    Static3ColorScheme(IFloatBuffer* valuesInColorRange,
                       const Color& colorRangeAt0,
                       const Color& colorRangeAt0_5,
                       const Color& colorRangeAt1):
    VertexColorScheme(true){
        _specificFeatureHandler = new Color3RangeGLFeature(colorRangeAt0,
                                                           colorRangeAt0_5,
                                                           colorRangeAt1,
                                                           valuesInColorRange);
        _feat = _specificFeatureHandler;
    }
    
    void setColorRangeStaticValues(IFloatBuffer* values) const{
        ((ColorRangeGLFeature*)_feat)->setValues(values);
    }
};


class InterpolatedColorScheme: public VertexColorScheme{
protected:
    virtual void setValues(IFloatBuffer* values,
                           IFloatBuffer* nextValues) const = 0;
public:
    
    InterpolatedColorScheme(IFloatBuffer* valuesInColorRange,
                            IFloatBuffer* nextValuesInColorRange):
    VertexColorScheme(true){}
    
    void setColorRangeDynamicValues(IFloatBuffer* values,
                                    IFloatBuffer* nextValues) const{
        setValues(values, nextValues);
    }
};


class Dynamic2ColorScheme: public InterpolatedColorScheme{
    DynamicColorRangeGLFeature* _specificFeatureHandler;
protected:
    void setValues(IFloatBuffer* values,
                   IFloatBuffer* nextValues) const{
        _specificFeatureHandler->setValues(values, nextValues);
    }
    
public:
    
    Dynamic2ColorScheme(IFloatBuffer* valuesInColorRange,
                        IFloatBuffer* nextValuesInColorRange,
                        const Color& colorRangeAt0,
                        const Color& colorRangeAt1):
    InterpolatedColorScheme(valuesInColorRange, nextValuesInColorRange){
        _specificFeatureHandler = new DynamicColorRangeGLFeature(colorRangeAt0,
                                                                 colorRangeAt1,
                                                                 valuesInColorRange,
                                                                 nextValuesInColorRange,
                                                                 0.0f);
        _feat = _specificFeatureHandler;
    }
    virtual void setTime(float time) const{
        _specificFeatureHandler->setTime(time);
    }
};


class Dynamic3ColorScheme: public InterpolatedColorScheme{
    DynamicColorRange3GLFeature* _specificFeatureHandler;
protected:
    void setValues(IFloatBuffer* values,
                   IFloatBuffer* nextValues) const{
        _specificFeatureHandler->setValues(values, nextValues);
    }
public:
    Dynamic3ColorScheme(IFloatBuffer* valuesInColorRange,
                        IFloatBuffer* nextValuesInColorRange,
                        const Color& colorRangeAt0,
                        const Color& colorRangeAt0_5,
                        const Color& colorRangeAt1)
    : InterpolatedColorScheme(valuesInColorRange, nextValuesInColorRange){
        _specificFeatureHandler = new DynamicColorRange3GLFeature(colorRangeAt0,
                                                                  colorRangeAt0_5,
                                                                  colorRangeAt1,
                                                                  valuesInColorRange,
                                                                  nextValuesInColorRange,
                                                                  0.0f);
        _feat = _specificFeatureHandler;
    }
    
    virtual void setTime(float time) const{
        _specificFeatureHandler->setTime(time);
    }
};


#endif /* VertexColorScheme_hpp */
