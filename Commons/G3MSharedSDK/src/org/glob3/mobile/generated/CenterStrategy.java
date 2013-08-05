package org.glob3.mobile.generated; 
//
//  FloatBufferBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/09/12.
//
//

//
//  FloatBufferBuilder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/09/12.
//
//



//class IFloatBuffer;


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