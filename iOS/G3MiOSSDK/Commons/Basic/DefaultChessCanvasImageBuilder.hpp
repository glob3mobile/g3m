//
//  DefaultChessCanvasImageBuilder.hpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 21/08/14.
//
//

#ifndef __G3MiOSSDK__DefaultChessCanvasImageBuilder__
#define __G3MiOSSDK__DefaultChessCanvasImageBuilder__

class Color;
class G3MContext;
class ICanvas;

#include "CanvasImageBuilder.hpp"
#include "Color.hpp"

class DefaultChessCanvasImageBuilder : public CanvasImageBuilder {

private:
  const Color _backgroundColor;
  const Color _boxColor;
  const int _splits;

protected:
  void buildOnCanvas(const G3MContext* context,
                     ICanvas* canvas);
public:
  DefaultChessCanvasImageBuilder(int width,
                                 int height,
                                 const Color& backgroundColor,
                                 const Color& boxColor,
                                 int splits);

  bool isMutable() const {
    return false;
  }
};

#endif
