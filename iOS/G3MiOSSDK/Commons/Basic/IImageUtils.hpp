//
//  IImageUtils.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 19/04/13.
//
//

#ifndef __G3MiOSSDK__IImageUtils__
#define __G3MiOSSDK__IImageUtils__

#include <vector>

class RectangleF;
class IImage;
class IImageListener;
class Vector2I;

class IImageUtils {
private:
  IImageUtils() {

  }

  static void createShallowCopy(const IImage* image,
                                IImageListener* listener,
                                bool autodelete);

public:
  static void scale(const IImage* image,
                    const Vector2I& size,
                    IImageListener* listener,
                    bool autodelete);

  static void subImage(const IImage* image,
                       const RectangleF& rect,
                       IImageListener* listener,
                       bool autodelete);

  static void combine(const std::vector<const IImage*>& images,
                      const std::vector<RectangleF*>& sourceRects,
                      const std::vector<RectangleF*>& destRects,
                      const Vector2I& size,
                      IImageListener* listener,
                      bool autodelete);
  
};

#endif
