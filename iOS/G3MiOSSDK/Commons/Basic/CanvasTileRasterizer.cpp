//
//  CanvasTileRasterizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/11/13.
//
//

#include "CanvasTileRasterizer.hpp"

#include "ICanvas.hpp"
#include "IFactory.hpp"
#include "Color.hpp"


CanvasTileRasterizer::CanvasTileRasterizer() :
_canvas(NULL),
_canvasWidth(-1),
_canvasHeight(-1),
_transparent(Color::newFromRGBA(0, 0, 0, 0))
{

}


CanvasTileRasterizer::~CanvasTileRasterizer() {
  delete _canvas;
  delete _transparent;

#ifdef JAVA_CODE
  super.dispose();
#endif
}

ICanvas* CanvasTileRasterizer::getCanvas(int width, int height) const {
  if ((_canvas == NULL) ||
      (_canvasWidth  != width) ||
      (_canvasHeight != height)) {
    delete _canvas;

    _canvas = IFactory::instance()->createCanvas();
    _canvas->initialize(width, height);

    _canvasWidth  = width;
    _canvasHeight = height;
  }
  else {
    _canvas->setFillColor(*_transparent);
    _canvas->fillRectangle(0, 0, width, height);
  }
  return _canvas;
}
