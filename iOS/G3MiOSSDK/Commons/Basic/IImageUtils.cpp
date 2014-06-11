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
#include "IFactory.hpp"
#include "ICanvas.hpp"
#include "RectangleF.hpp"


void IImageUtils::createShallowCopy(const IImage* image,
                                    IImageListener* listener,
                                    bool autodelete) {
  listener->imageCreated( image->shallowCopy() );
  if (autodelete) {
    delete listener;
  }
}

void IImageUtils::scale(int width,
                        int height,
                        const IImage* image,
                        IImageListener* listener,
                        bool autodelete) {
  if (width == image->getWidth() &&
      height == image->getHeight()) {
    createShallowCopy(image,
                      listener,
                      autodelete);
  }
  else {
    ICanvas* canvas = IFactory::instance()->createCanvas();
    canvas->initialize(width, height);

    canvas->drawImage(image,
                      0, 0, width, height);

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
    canvas->initialize(mu->round(rect._width),
                       mu->round(rect._height));

    canvas->drawImage(image,
                      rect._x, rect._y, rect._width, rect._height,
                      0, 0, rect._width, rect._height);

    canvas->createImage(listener, autodelete);
    delete canvas;
  }
}

void IImageUtils::combine(int width,
                          int height,
                          const std::vector<const IImage*>& images,
                          const std::vector<RectangleF*>& sourceRects,
                          const std::vector<RectangleF*>& destRects,
                          const std::vector<float>& transparencies,
                          IImageListener* listener,
                          bool autodelete) {

  const int imagesSize = images.size();

  if (imagesSize != sourceRects.size() || imagesSize != destRects.size()) {
    ILogger::instance()->logError("Failure at combine images.");
    return;
  }

  if (imagesSize == 1) {
    const IImage* image = images[0];
    const RectangleF* sourceRect = sourceRects[0];
    const RectangleF* destRect   = destRects[0];

    if ((sourceRect->_x      == 0) &&
        (sourceRect->_y      == 0) &&
        (sourceRect->_width  == image->getWidth()) &&
        (sourceRect->_height == image->getHeight()) &&
        (destRect->_x        == 0) &&
        (destRect->_y        == 0) &&
        (destRect->_width    == width) &&
        (destRect->_height   == height)) {
      scale(width,
            height,
            image,
            listener,
            autodelete);
      return;
    }
  }


  ICanvas* canvas = IFactory::instance()->createCanvas();
  canvas->initialize(width, height);

  for (int i = 0; i < imagesSize ; i++) {
    const IImage* image = images[i];
    const RectangleF* srcRect = sourceRects[i];
    const RectangleF* dstRect = destRects[i];
    const float transparency = transparencies[i];

    if (transparency == 1.0) {
      canvas->drawImage(image,
                        srcRect->_x, srcRect->_y, srcRect->_width, srcRect->_height,
                        dstRect->_x, dstRect->_y, dstRect->_width, dstRect->_height);
    } else{
      canvas->drawImage(image,
                        srcRect->_x, srcRect->_y, srcRect->_width, srcRect->_height,
                        dstRect->_x, dstRect->_y, dstRect->_width, dstRect->_height,
                        transparency);
    }
  }

  canvas->createImage(listener, autodelete);
  delete canvas;
}
