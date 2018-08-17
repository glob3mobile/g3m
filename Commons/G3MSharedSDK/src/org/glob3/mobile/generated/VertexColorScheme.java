package org.glob3.mobile.generated;import java.util.*;

//
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




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class MutableMatrix44D;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IFloatBuffer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
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
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void setTime(float time) const
	public void setTime(float time)
	{
	}
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void setColorRangeStaticValues(IFloatBuffer* values) const
	public void setColorRangeStaticValues(IFloatBuffer values)
	{
	}
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void setColorRangeDynamicValues(IFloatBuffer* values, IFloatBuffer* nextValues) const
	public void setColorRangeDynamicValues(IFloatBuffer values, IFloatBuffer nextValues)
	{
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual float getValue(int index) const
	public float getValue(int index)
	{
//C++ TO JAVA CONVERTER TODO TASK: The #define macro NANF was defined in alternate ways and cannot be replaced in-line:
		return NANF;
	}
}
