//
//  ICanvasUtils.h
//  G3MiOSSDK
//
//  Created by Mari Luz Mateo on 07/05/14.
//
//

#ifndef __G3MiOSSDK__ICanvasUtils__
#define __G3MiOSSDK__ICanvasUtils__

#include <vector>
#include <string>

#include "ILogger.hpp"
#include "Color.hpp"
class ICanvas;
class GFont;
class Vector2F;

enum VerticalAlignment {
  Top,
  Middle,
  Bottom
};

enum HorizontalAlignment {
  Left,
  Center,
  Right
};

class ICanvasUtils {
public:
  static Vector2F drawStringsOn(const std::vector<std::string> &strings,
                            ICanvas *canvas,
                            const int width,
                            const int height,
                            const VerticalAlignment vAlign,
                            const HorizontalAlignment hAlign,
                            const Color& color,
                            const int maxFontSize=18,
                            const int minFontSize=2,
                            const Color& backgroundColor=Color::transparent(),
                            const Color& shadowColor=Color::black(),
                            const int padding=16,
                            const int cornerRadius=8);
};



#endif /* defined(__G3MiOSSDK__ICanvasUtils__) */
