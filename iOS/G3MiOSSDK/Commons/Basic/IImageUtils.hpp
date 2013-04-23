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

class IImageUtils {
private:
  IImageUtils() {

  }

  static void createShallowCopy(const IImage* image,
                                IImageListener* listener,
                                bool autodelete);

public:
  static void scale(const int width,
                    const int height,
                    const IImage* image,
                    IImageListener* listener,
                    bool autodelete);

  static void subImage(const IImage* image,
                       const RectangleF& rect,
                       IImageListener* listener,
                       bool autodelete);

  static void combine(const int width,
                      const int height,
                      const std::vector<const IImage*>& images,
                      const std::vector<RectangleF*>& sourceRects,
                      const std::vector<RectangleF*>& destRects,
                      IImageListener* listener,
                      bool autodelete);

};

#endif
