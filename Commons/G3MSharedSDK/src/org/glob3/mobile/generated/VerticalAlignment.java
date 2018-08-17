package org.glob3.mobile.generated;import java.util.*;

//
//  ICanvasUtils.cpp
//  G3MiOSSDK
//
//  Created by Mari Luz Mateo on 07/05/14.
//
//

//
//  ICanvasUtils.hpp
//  G3MiOSSDK
//
//  Created by Mari Luz Mateo on 07/05/14.
//
//



///#include "ILogger.hpp"
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class ICanvas;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GFont;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Vector2F;

public enum VerticalAlignment
{
  Top,
  Middle,
  Bottom;

	public int getValue()
	{
		return this.ordinal();
	}

	public static VerticalAlignment forValue(int value)
	{
		return values()[value];
	}
}
