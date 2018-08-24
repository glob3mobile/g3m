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
public:
    
    virtual ~VertexColorScheme(){
        _feat->_release();
    }
    
    GLFeature* getGLFeature(){
        return _feat;
    }
    virtual void setTime(float time) const{};
    virtual void setColorRangeStaticValues(IFloatBuffer* values) const{};
    virtual void setColorRangeDynamicValues(IFloatBuffer* values,
                                            IFloatBuffer* nextValues) const{};
    
    virtual float getValue(size_t index) const{return NANF;}
};

class Static2ColorScheme: public VertexColorScheme{
    IFloatBuffer* _valuesInColorRange;
    ColorRangeGLFeature* _specificFeatureHandler;
public:
    Static2ColorScheme(IFloatBuffer* valuesInColorRange,
                       const Color& colorRangeAt0,
                       const Color& colorRangeAt1){
        _specificFeatureHandler = new ColorRangeGLFeature(colorRangeAt0,
                                        colorRangeAt1,
                                        valuesInColorRange);
        _feat = _specificFeatureHandler;
        _valuesInColorRange = valuesInColorRange;
    }
    
    void setColorRangeStaticValues(IFloatBuffer* values) const{
        ((ColorRangeGLFeature*)_feat)->setValues(values);
    }
    
    float getValue(size_t index) const{
        return _valuesInColorRange->get(index);
    }
};

class InterpolatedColorScheme: public VertexColorScheme{
protected:
    mutable IFloatBuffer* _valuesInColorRange;
    mutable IFloatBuffer* _nextValuesInColorRange;
    mutable float _time;
    
    virtual void setValues(IFloatBuffer* values,
                           IFloatBuffer* nextValues) const = 0;
public:
    
    InterpolatedColorScheme(IFloatBuffer* valuesInColorRange,
                        IFloatBuffer* nextValuesInColorRange){
        _valuesInColorRange = valuesInColorRange;
        _nextValuesInColorRange = nextValuesInColorRange;
        _time = NANF;
    }
    
    void setColorRangeDynamicValues(IFloatBuffer* values,
                                    IFloatBuffer* nextValues) const{
        setValues(values, nextValues);
        _valuesInColorRange = values;
        _nextValuesInColorRange = nextValues;
    }
    
    float getValue(size_t index) const{
        
        return IMathUtils::instance()->linearInterpolation(_valuesInColorRange->get(index),
                                                           _nextValuesInColorRange->get(index),
                                                    _time);
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
        _time = time;
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
        _time = time;
        _specificFeatureHandler->setTime(time);
    }
};


#endif /* VertexColorScheme_hpp */
