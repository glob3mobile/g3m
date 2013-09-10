package org.glob3.mobile.generated; 
//
//  Frustum.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 15/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

//
//  Frustum.h
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 15/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//



//class Box;

public class FrustumData
{
  public double _left;
  public double _right;
  public double _bottom;
  public double _top;
  public double _znear;
  public double _zfar;

  public FrustumData(double left, double right, double bottom, double top, double znear, double zfar)
  {
     _left = left;
     _right = right;
     _bottom = bottom;
     _top = top;
     _znear = znear;
     _zfar = zfar;
  }

  public FrustumData(FrustumData fd)
  {
     _left = fd._left;
     _right = fd._right;
     _bottom = fd._bottom;
     _top = fd._top;
     _znear = fd._znear;
     _zfar = fd._zfar;
  }

  public FrustumData()
  {
     _left = -1;
     _right = 1;
     _bottom = -1;
     _top = 1;
     _znear = 1;
     _zfar = 10;
  }

}