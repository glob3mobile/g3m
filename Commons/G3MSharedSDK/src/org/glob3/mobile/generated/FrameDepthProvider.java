package org.glob3.mobile.generated; 
//
//  FrameDepthProvider.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 1/9/15.
//
//

//
//  FrameDepthProvider.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 1/9/15.
//
//


public interface FrameDepthProvider
{
  void dispose();

  double getDepthForPixel(float x, float y);
}