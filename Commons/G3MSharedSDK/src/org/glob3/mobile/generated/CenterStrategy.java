package org.glob3.mobile.generated; 
//
//  FloatBufferBuilderFromCartesian3D.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 06/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



public class CenterStrategy
{
  private static final int _noCenter = 0;
  private static final int _firstVertex = 1;
  private static final int _givenCenter = 2;

  private CenterStrategy()
  {
  }


  public static int noCenter()
  {
	  return _noCenter;
  }
  public static int firstVertex()
  {
	  return _firstVertex;
  }
  public static int givenCenter()
  {
	  return _givenCenter;
  }
}