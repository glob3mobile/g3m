//
//  ImageBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/12/13.
//
//

#include "ImageBuilder.hpp"

#include "IFactory.hpp"
#include "CanvasElement.hpp"
#include "ICanvas.hpp"
#include "IMathUtils.hpp"

void ImageBuilder::build(CanvasElement* element,
                         IImageListener* listener,
                         bool autodelete) {
  ICanvas* canvas = IFactory::instance()->createCanvas();

  const Vector2F extent = element->getExtent(canvas);

  const IMathUtils* mu = IMathUtils::instance();

  canvas->initialize(mu->round(extent._x),
                     mu->round(extent._y));

  element->drawAt(0, 0, canvas);

  canvas->createImage(listener, autodelete);

  delete element;

  delete canvas;
}
