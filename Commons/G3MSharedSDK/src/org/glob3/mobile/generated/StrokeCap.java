package org.glob3.mobile.generated;import java.util.*;

//
//  ICanvas.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/9/13.
//
//

//
//  ICanvas.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/9/13.
//
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Color;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IImageListener;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GFont;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IImage;


public enum StrokeCap
{
  CAP_BUTT,
  CAP_ROUND,
  CAP_SQUARE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static StrokeCap forValue(int value)
	{
		return values()[value];
	}
}
