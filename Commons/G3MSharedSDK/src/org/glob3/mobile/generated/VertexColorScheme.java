package org.glob3.mobile.generated;//
//  VertexColorScheme.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel S N on 13/07/2018.
//

//
//  VertexColorScheme.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel S N on 13/07/2018.
//




//class MutableMatrix44D;
//class IFloatBuffer;
//class Color;

public class VertexColorScheme
{
    protected GLFeature _feat;

    public void dispose()
    {
        _feat._release();
    }

    public final GLFeature getGLFeature()
    {
        return _feat;
    }
    public void setTime(float time)
    {
    }
    public void setColorRangeStaticValues(IFloatBuffer values)
    {
    }
    public void setColorRangeDynamicValues(IFloatBuffer values, IFloatBuffer nextValues)
    {
    }

    public float getValue(int index)
    {
       return java.lang.Float.NaN;
    }
}
