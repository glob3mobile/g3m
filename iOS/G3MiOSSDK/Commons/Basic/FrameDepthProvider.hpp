//
//  FrameDepthProvider.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 1/9/15.
//
//

#ifndef __G3MiOSSDK__FrameDepthProvider__
#define __G3MiOSSDK__FrameDepthProvider__

class FrameDepthProvider {
public:
#ifdef C_CODE
  virtual ~FrameDepthProvider() { }
#endif
#ifdef JAVA_CODE
  void dispose();
#endif
  
  virtual double getDepthForPixel(float x, float y) = 0;
};
#endif /* defined(__G3MiOSSDK__FrameDepthProvider__) */
