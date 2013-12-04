//
//  CanvasTileRasterizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/11/13.
//
//

#ifndef __G3MiOSSDK__CanvasTileRasterizer__
#define __G3MiOSSDK__CanvasTileRasterizer__

#include "TileRasterizer.hpp"

class ICanvas;
class Color;

class CanvasTileRasterizer : public TileRasterizer {
private:
  mutable ICanvas* _canvas;
  mutable int      _canvasWidth;
  mutable int      _canvasHeight;

  Color* _transparent;

protected:

  CanvasTileRasterizer();

  ~CanvasTileRasterizer();

  virtual ICanvas* getCanvas(int width, int height) const;

};

#endif
