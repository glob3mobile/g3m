//
//  CanvasImageFactory.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/27/13.
//
//

#include "CanvasImageFactory.hpp"

#include "Context.hpp"
#include "ICanvas.hpp"
#include "IFactory.hpp"

void CanvasImageFactory::create(const G3MRenderContext* rc,
                                int width,
                                int height,
                                IImageListener* listener,
                                bool deleteListener) {

  ICanvas* canvas = rc->getFactory()->createCanvas();
  canvas->initialize(width, height);

  drawOn(canvas, width, height);

  canvas->createImage(listener, deleteListener);

  delete canvas;
}
