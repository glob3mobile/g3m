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
//class Vector2I;
#include "Vector2I.hpp"

class IImageUtils {
private:
  IImageUtils() {

  }

  static void createShallowCopy(const IImage* image,
                                IImageListener* listener,
                                bool autodelete);

public:

  static void scale(int width,
                    int height,
                    const IImage* image,
                    IImageListener* listener,
                    bool autodelete);

  static void scale(const Vector2I& extent,
                    const IImage* image,
                    IImageListener* listener,
                    bool autodelete) {
    scale(extent._x, extent._y,
          image,
          listener, autodelete);
  }


  static void subImage(const IImage* image,
                       const RectangleF& rect,
                       IImageListener* listener,
                       bool autodelete);


  static void combine(int width,
                      int height,
                      const std::vector<const IImage*>& images,
                      const std::vector<RectangleF*>& sourceRects,
                      const std::vector<RectangleF*>& destRects,
                      const std::vector<float>& transparencies,
                      IImageListener* listener,
                      bool autodelete);

  static void combine(const Vector2I& extent,
                      const std::vector<const IImage*>& images,
                      const std::vector<RectangleF*>& sourceRects,
                      const std::vector<RectangleF*>& destRects,
                      const std::vector<float>& transparencies,
                      IImageListener* listener,
                      bool autodelete) {
    combine(extent._x, extent._y,
            images,
            sourceRects,
            destRects,
            transparencies,
            listener,
            autodelete);
  }
  
};

#endif
