//
//  IImageUtils.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 19/04/13.
//
//

#include "IImageUtils.hpp"

#include "ICanvas.hpp"
#include "IFactory.hpp"
#include "IImage.hpp"
#include "RectangleI.hpp"
#include "IImageListener.hpp"

void IImageUtils::scale(const IImage* image, const Vector2I& size,
                        IImageListener* listener,
                        bool autodelete){
  ICanvas* canvas = IFactory::instance()->createCanvas();
  canvas->initialize(size._x, size._y);
  canvas->drawImage(image, (float)0.0, (float)0.0, (float)size._x, (float)size._y);
  
  canvas->createImage(listener, autodelete);
  delete canvas;
}

void IImageUtils::subImage(const IImage* image, const RectangleF& rect,
                           IImageListener* listener,
                           bool autodelete){
  
  ICanvas* canvas = IFactory::instance()->createCanvas();
  canvas->initialize((int)rect._width, (int)rect._height);
  canvas->drawImage(image,
                    (float)rect._x, (float)rect._y, (float)rect._width, (float)rect._height,
                    (float)0, (float)0, (float)rect._width, (float)rect._height);
  
  canvas->createImage(listener, autodelete);
  delete canvas;
}

void IImageUtils::combine(const std::vector<const IImage*>& images,
                          const std::vector<RectangleF*>& sourceRects,
                          const std::vector<RectangleF*>& destRects,
                          const Vector2I& size,
                          IImageListener* listener,
                          bool autodelete){
  
  const int imagesSize = images.size();
  if (imagesSize == 0 || imagesSize != sourceRects.size() || imagesSize != destRects.size()){
    ILogger::instance()->logError("Failure at combine images.");
    return;
  }
  
  if (imagesSize == 1){
    int im0Width = images[0]->getWidth();
    int im0Height = images[0]->getHeight();
    
    if (im0Width == size._x && im0Height == size._y &&
        sourceRects[0]->_x == 0 && sourceRects[0]->_y == 0 &&
        sourceRects[0]->_width == im0Width && sourceRects[0]->_height == im0Height) {
      listener->imageCreated( images[0]->shallowCopy() );
      if (autodelete) {
        delete listener;
      }
    } else{
      scale(images[0], size,listener, autodelete);
    }
  } else{
    
    
    ICanvas* canvas = IFactory::instance()->createCanvas();
    canvas->initialize((int)size._x, (int)size._y);
    
    for (int i = 0; i < imagesSize ; i++) {
      const IImage* image = images[i];
      RectangleF* srcRect = sourceRects[i];
      RectangleF* dstRect = destRects[i];
      
      if (image == NULL || srcRect == NULL || dstRect == NULL){
        ILogger::instance()->logError("Null parameter passed to IImageUtils::combine()");
      } else{
        canvas->drawImage(image,
                          srcRect->_x, srcRect->_y, srcRect->_width, srcRect->_height,
                          dstRect->_x, dstRect->_y, dstRect->_width, dstRect->_height);
      }
    }
    
    
    canvas->createImage(listener, autodelete);
    delete canvas;
  }
}