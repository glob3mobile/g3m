//
//  CanvasTileImageProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/18/14.
//
//

#ifndef __G3MiOSSDK__CanvasTileImageProvider__
#define __G3MiOSSDK__CanvasTileImageProvider__

#include "TileImageProvider.hpp"
class Color;
class ICanvas;

class CanvasTileImageProvider : public TileImageProvider {
private:
  mutable ICanvas* _canvas;
  mutable int      _canvasWidth;
  mutable int      _canvasHeight;

  const Color* _transparent;

protected:
  CanvasTileImageProvider();

  ~CanvasTileImageProvider();

  ICanvas* getCanvas(int width,
                     int height) const;

};

#endif
