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
public:
    Static2ColorScheme(IFloatBuffer* valuesInColorRange,
                       const Color& colorRangeAt0,
                       const Color& colorRangeAt1){
        _feat = new ColorRangeGLFeature(colorRangeAt0,
                                        colorRangeAt1,
                                        valuesInColorRange);
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
public:
    
    InterpolatedColorScheme(IFloatBuffer* valuesInColorRange,
                        IFloatBuffer* nextValuesInColorRange){
        _valuesInColorRange = valuesInColorRange;
        _nextValuesInColorRange = nextValuesInColorRange;
        _time = NANF;
    }
    
    void setColorRangeDynamicValues(IFloatBuffer* values,
                                    IFloatBuffer* nextValues) const{
        ((DynamicColorRangeGLFeature*)_feat)->setValues(values, nextValues);
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
public:
    
    Dynamic2ColorScheme(IFloatBuffer* valuesInColorRange,
                        IFloatBuffer* nextValuesInColorRange,
                        const Color& colorRangeAt0,
                        const Color& colorRangeAt1):
    InterpolatedColorScheme(valuesInColorRange, nextValuesInColorRange){
        _feat = new DynamicColorRangeGLFeature(colorRangeAt0,
                                               colorRangeAt1,
                                               valuesInColorRange,
                                               nextValuesInColorRange,
                                               0.0f);
    }
    virtual void setTime(float time) const{
        _time = time;
        ((DynamicColorRangeGLFeature*)_feat)->setTime(time);
    }
};


class Dynamic3ColorScheme: public InterpolatedColorScheme{
public:
    Dynamic3ColorScheme(IFloatBuffer* valuesInColorRange,
                        IFloatBuffer* nextValuesInColorRange,
                        const Color& colorRangeAt0,
                        const Color& colorRangeAt0_5,
                        const Color& colorRangeAt1)
    : InterpolatedColorScheme(valuesInColorRange, nextValuesInColorRange){
        _feat = new DynamicColorRange3GLFeature(colorRangeAt0,
                                                colorRangeAt0_5,
                                                colorRangeAt1,
                                                valuesInColorRange,
                                                nextValuesInColorRange,
                                                0.0f);
    }
    
    virtual void setTime(float time) const{
        _time = time;
        ((DynamicColorRange3GLFeature*)_feat)->setTime(time);
    }
};


#endif /* VertexColorScheme_hpp */
