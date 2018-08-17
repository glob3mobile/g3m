package org.glob3.mobile.generated;import java.util.*;

//
//  GLFeatureGroup.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/07/13.
//
//

//
//  GLFeatureGroup.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/07/13.
//
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GLFeature;

public enum GLFeatureGroupName
{
  UNRECOGNIZED_GROUP(-1),
  NO_GROUP(0),
  CAMERA_GROUP(1),
  COLOR_GROUP(2),
  LIGHTING_GROUP(3);

	private int intValue;
	private static java.util.HashMap<Integer, GLFeatureGroupName> mappings;
	private static java.util.HashMap<Integer, GLFeatureGroupName> getMappings()
	{
		if (mappings == null)
		{
			synchronized (GLFeatureGroupName.class)
			{
				if (mappings == null)
				{
					mappings = new java.util.HashMap<Integer, GLFeatureGroupName>();
				}
			}
		}
		return mappings;
	}

	private GLFeatureGroupName(int value)
	{
		intValue = value;
		GLFeatureGroupName.getMappings().put(value, this);
	}

	public int getValue()
	{
		return intValue;
	}

	public static GLFeatureGroupName forValue(int value)
	{
		return getMappings().get(value);
	}
}
