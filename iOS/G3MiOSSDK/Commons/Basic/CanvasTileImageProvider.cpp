//
//  CanvasTileImageProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/18/14.
//
//

#include "CanvasTileImageProvider.hpp"

#include "IFactory.hpp"
#include "ICanvas.hpp"
#include "Color.hpp"

CanvasTileImageProvider::CanvasTileImageProvider() :
_canvas(NULL),
_transparent(Color::newFromRGBA(0, 0, 0, 0))
{
}

CanvasTileImageProvider::~CanvasTileImageProvider() {
  delete _canvas;
  delete _transparent;

#ifdef JAVA_CODE
  super.dispose();
#endif
}


ICanvas* CanvasTileImageProvider::getCanvas(int width,
                                            int height) const {
  if ((_canvas       == NULL)  ||
      (_canvasWidth  != width) ||
      (_canvasHeight != height)) {
    delete _canvas;

    _canvas = IFactory::instance()->createCanvas();
    _canvas->initialize(width, height);

    _canvasWidth  = width;
    _canvasHeight = height;
  }
  else {
    _canvas->clearRect(0, 0, width, height);
    _canvas->setFillColor(*_transparent);
    _canvas->fillRectangle(0, 0, width, height);
  }
  return _canvas;
}
