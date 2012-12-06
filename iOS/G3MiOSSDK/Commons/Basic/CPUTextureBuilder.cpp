//
//  CPUTextureBuilder.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 23/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "CPUTextureBuilder.hpp"

#include "IFactory.hpp"
#include "IImage.hpp"
#include "IImageListener.hpp"
#include "RectangleI.hpp"

const void CPUTextureBuilder::createTextureFromImage(GL* gl,
                                                     const IFactory* factory,
                                                     const IImage* image,
                                                     int width, int height,
                                                     IImageListener* listener,
                                                     bool autodelete) const{
  if (image == NULL) {
    ILogger::instance()->logWarning("Creating blank Image");
    factory->createImageFromSize(width, height, listener, autodelete);
  }
  else if (image->getHeight() == height && image->getWidth() == width) {
    listener->imageCreated( image->shallowCopy() );
#ifdef C_CODE
    if (autodelete) {
      delete listener;
    }
#endif
  }
  else {
    image->scale(width, height, listener, autodelete);
  }
}

//const void CPUTextureBuilder::createTextureFromImages(GL* gl,
//                                                      const IFactory* factory,
//                                                      const std::vector<const IImage*>& images,
//                                                      int width, int height,
//                                                      IImageListener* listener,
//                                                      bool autodelete) const{
//
//  const int imagesSize = images.size();
//
//  if (imagesSize == 0) {
//    ILogger::instance()->logWarning("Creating blank Image");
//    // return factory->createImageFromSize(width, height);
//    factory->createImageFromSize(width, height, listener, autodelete);
//  }
//  else {
//    IImage* im = images[0]->shallowCopy();
//    IImage* im2 = NULL;
//    for (int i = 1; i < imagesSize; i++) {
//      const IImage* imTrans = images[i];
//      im2 = im->combineWith(*imTrans, width, height);
//      delete im;
//      im = im2;
//    }
//    // return im;
//    listener->imageCreated( im );
//    if (autodelete) {
//      delete listener;
//    }
//  }
//}


class SubImageImageLister : public IImageListener {
private:
  const int _width;
  const int _height;

  IImageListener* _listener;
  const bool            _autodelete;

public:
  SubImageImageLister(int width, int height,
                      IImageListener* listener,
                      bool autodelete) :
  _width(width),
  _height(height),
  _listener(listener),
  _autodelete(autodelete)
  {

  }

  void imageCreated(IImage* image) {
    image->scale(_width, _height,
                 _listener, _autodelete);
    delete image;
  }
};


const void CPUTextureBuilder::createTextureFromImages(GL* gl,
                                                      const IFactory* factory,
                                                      const std::vector<const IImage*>& images,
                                                      const std::vector<RectangleI*>& rectangles,
                                                      int width, int height,
                                                      IImageListener* listener,
                                                      bool autodelete) const{

  const int imagesSize = images.size();

  if (imagesSize == 0 || imagesSize != rectangles.size()) {
    ILogger::instance()->logWarning("Creating blank Image");
    //return factory->createImageFromSize(width, height);
    factory->createImageFromSize(width, height,
                                 listener, autodelete);
  }
  else if (imagesSize == 1) {
    RectangleI* rectangle = rectangles[0];
    images[0]->subImage(*rectangle,
                        new SubImageImageLister(width, height,
                                                listener, autodelete),
                        true);
  }
  else {
    std::vector<const IImage*> tailImages;
    std::vector<RectangleI*> tailRectangles;
    for (int i = 0; i < imagesSize; i++) {
      tailImages.push_back( images[i] );
      tailRectangles.push_back( rectangles[i] );
    }

    images[0]->combineWith(tailImages,
                           tailRectangles,
                           width, height,
                           listener, autodelete);
    
//    const IImage* base;
//    int i;
//    const RectangleI baseRec(0, 0, width, height);
//    // if (rectangles.size() > 0 && rectangles[0]->equalTo(baseRec)){
//    if (rectangles[0]->equalTo(baseRec)){
//      base = images[0]->shallowCopy();
//      i = 1;
//    }
//    else {
//      base = factory->createImageFromSize(width, height,
//                                          new CPUTextureBuilderIImageListener(),
//                                          true);
//      i = 0;
//    }
//
//    for (; i < images.size(); i++) {
//      const IImage* currentImage = images[i];
//      const RectangleI* currentRect = rectangles[i];
//
//      IImage* im2 = base->combineWith(*currentImage, *currentRect, width, height);
//      delete base;
//      base = im2;
//    }
//    return base;
  }
}





 
