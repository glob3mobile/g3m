//
//  IImageUtils.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 19/04/13.
//
//

#ifndef __G3MiOSSDK__IImageUtils__
#define __G3MiOSSDK__IImageUtils__

#include <iostream>

#include "RectangleF.hpp"
#include "IImage.hpp"

class IImageUtils{
public:
  static void scale(const IImage* image, const Vector2I& size,
                       IImageListener* listener,
                       bool autodelete);
  
  static void subImage(const IImage* image, const RectangleF& rect,
                          IImageListener* listener,
                          bool autodelete);
  
  static void combine(const std::vector<const IImage*>& images,
                         const std::vector<RectangleF*>& sourceRects,
                         const std::vector<RectangleF*>& destRects,
                         const Vector2I& size,
                         IImageListener* listener,
                         bool autodelete);
  
};

#endif /* defined(__G3MiOSSDK__IImageUtils__) */
