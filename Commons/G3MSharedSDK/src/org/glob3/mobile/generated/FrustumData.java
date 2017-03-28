package org.glob3.mobile.generated;
//
//  FrustumData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/25/17.
//
//

//
//  FrustumData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/25/17.
//
//


public class FrustumData
{
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  FrustumData operator =(FrustumData that);

  public final double _left;
  public final double _right;
  public final double _bottom;
  public final double _top;
  public final double _zNear;
  public final double _zFar;

  public FrustumData(double left, double right, double bottom, double top, double zNear, double zFar)
  {
     _left = left;
     _right = right;
     _bottom = bottom;
     _top = top;
     _zNear = zNear;
     _zFar = zFar;
  }

  public FrustumData(FrustumData fd)
  {
     _left = fd._left;
     _right = fd._right;
     _bottom = fd._bottom;
     _top = fd._top;
     _zNear = fd._zNear;
     _zFar = fd._zFar;
  }

  public void dispose()
  {
  
  }

}
