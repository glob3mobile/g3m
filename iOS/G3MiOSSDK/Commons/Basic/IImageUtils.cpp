//
//  IImageUtils.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 19/04/13.
//
//

#include "IImageUtils.hpp"

#include "IImage.hpp"
#include "IImageListener.hpp"
#include "ICanvas.hpp"
#include "IFactory.hpp"
#include "RectangleF.hpp"


//delete_canvas;

void IImageUtils::createShallowCopy(const IImage* image,
                                    IImageListener* listener,
                                    bool autodelete) {
  listener->imageCreated( image->shallowCopy() );
  if (autodelete) {
    delete listener;
  }
}

void IImageUtils::scale(const IImage* image,
                        const Vector2I& size,
                        IImageListener* listener,
                        bool autodelete) {
  if (size._x == image->getWidth() &&
      size._y == image->getHeight()) {
    createShallowCopy(image,
                      listener,
                      autodelete);
  }
  else {
    ICanvas* canvas = IFactory::instance()->createCanvas();
    canvas->initialize(size._x, size._y);

    canvas->drawImage(image,
                      0, 0, size._x, size._y);

    canvas->createImage(listener, autodelete);
    delete canvas;
  }
}

void IImageUtils::subImage(const IImage* image,
                           const RectangleF& rect,
                           IImageListener* listener,
                           bool autodelete) {

  if (rect._x == 0 &&
      rect._y == 0 &&
      rect._width == image->getWidth() &&
      rect._height == image->getHeight()) {
    createShallowCopy(image,
                      listener,
                      autodelete);
  }
  else {
    ICanvas* canvas = IFactory::instance()->createCanvas();

    IMathUtils* mu = IMathUtils::instance();
    canvas->initialize((int) mu->round(rect._width),
                       (int) mu->round(rect._height));

    canvas->drawImage(image,
                      rect._x, rect._y, rect._width, rect._height,
                      0, 0, rect._width, rect._height);

    canvas->createImage(listener, autodelete);
    delete canvas;
  }
}

void IImageUtils::combine(const std::vector<const IImage*>& images,
                          const std::vector<RectangleF*>& sourceRects,
                          const std::vector<RectangleF*>& destRects,
                          const Vector2I& size,
                          IImageListener* listener,
                          bool autodelete){

  const int imagesSize = images.size();
  if (imagesSize == 0 || imagesSize != sourceRects.size() || imagesSize != destRects.size()) {
    ILogger::instance()->logError("Failure at combine images.");
    return;
  }

  if (imagesSize == 1) {
    const IImage* image = images[0];
    const RectangleF* sourceRect = sourceRects[0];
    const RectangleF* destRect   = destRects[0];

    if (sourceRect->_x == 0 &&
        sourceRect->_y == 0 &&
        sourceRect->_width == image->getWidth() &&
        sourceRect->_height == image->getHeight() &&
        destRect->_x == 0 &&
        destRect->_y == 0 &&
        destRect->_width == size._x &&
        destRect->_height == size._y) {
      scale(image,
            size,
            listener,
            autodelete);
      return;
    }
  }


  ICanvas* canvas = IFactory::instance()->createCanvas();
  canvas->initialize(size._x, size._y);

  for (int i = 0; i < imagesSize ; i++) {
    const IImage* image = images[i];
    const RectangleF* srcRect = sourceRects[i];
    const RectangleF* dstRect = destRects[i];

    canvas->drawImage(image,
                      srcRect->_x, srcRect->_y, srcRect->_width, srcRect->_height,
                      dstRect->_x, dstRect->_y, dstRect->_width, dstRect->_height);
  }

  canvas->createImage(listener, autodelete);
  delete canvas;
}
