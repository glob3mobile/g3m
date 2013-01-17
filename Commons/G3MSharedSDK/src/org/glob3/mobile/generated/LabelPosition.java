package org.glob3.mobile.generated; 
//
//  ITextUtils.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/10/13.
//
//

//
//  ITextUtils.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/10/13.
//
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IImage;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Color;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IImageListener;


public enum LabelPosition
{
  Bottom,
  Right;

	public int getValue()
	{
		return this.ordinal();
	}

	public static LabelPosition forValue(int value)
	{
		return values()[value];
	}
}