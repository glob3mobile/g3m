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
                                                     IImage* image,
                                                     const Vector2I& textureResolution,
                                                     IImageListener* listener,
                                                     bool autodelete) const {
  const int width  = textureResolution._x;
  const int height = textureResolution._y;
  if (image == NULL) {
    ILogger::instance()->logWarning("Creating blank Image");
    factory->createImageFromSize(width, height, listener, autodelete);
  }
  else if (image->getHeight() == height && image->getWidth() == width) {
    listener->imageCreated( image->shallowCopy() );
    if (autodelete) {
      delete listener;
    }
  }
  else {
    image->scale(width, height, listener, autodelete);
  }
}

class ImageDeleterImageLister : public IImageListener {
private:
  IImage*         _imageToDelete;
  IImageListener* _listener;
  const bool      _autodelete;
  
public:
  ImageDeleterImageLister(IImage* imageToDelete,
                          IImageListener* listener,
                          bool autodelete) :
  _imageToDelete(imageToDelete),
  _listener(listener),
  _autodelete(autodelete)
  {
    
  }
  
  void imageCreated(IImage* image) {
    if (_imageToDelete != NULL) {
      IFactory::instance()->deleteImage(_imageToDelete);
      _imageToDelete = NULL;
    }
    
    if (_listener != NULL) {
      _listener->imageCreated(image);
      if (_autodelete) {
        delete _listener;
        _listener = NULL;
      }
    }
  }
};

class CPUTextureBuilderSubImageImageLister : public IImageListener {
private:
  const int _width;
  const int _height;
  
  IImageListener* _listener;
  const bool      _autodelete;
  
public:
  CPUTextureBuilderSubImageImageLister(int width, int height,
                                       IImageListener* listener,
                                       bool autodelete) :
  _width(width),
  _height(height),
  _listener(listener),
  _autodelete(autodelete)
  {
    
  }
  
  void imageCreated(IImage* image) {
    //    image->scale(_width, _height,
    //                 _listener, _autodelete);
    image->scale(_width, _height,
                 new ImageDeleterImageLister(image, _listener, _autodelete), true);
    
    //    IFactory::instance()->deleteImage(image);
  }
};


const void CPUTextureBuilder::createTextureFromImages(GL* gl,
                                                      const IFactory* factory,
                                                      const std::vector<IImage*>& images,
                                                      const std::vector<RectangleI*>& srcRectangles,
                                                      const std::vector<RectangleI*>& destRectangles,
                                                      const Vector2I& textureResolution,
                                                      IImageListener* listener,
                                                      bool autodelete) const{
  const int width  = textureResolution._x;
  const int height = textureResolution._y;
  
  const int imagesSize = images.size();
  
  if (imagesSize == 0 || imagesSize != destRectangles.size() || imagesSize != srcRectangles.size()) {
    ILogger::instance()->logWarning("Creating blank Image");
    //return factory->createImageFromSize(width, height);
    factory->createImageFromSize(width, height,
                                 listener, autodelete);
  }
  /*else if (imagesSize == 1) {
   RectangleI* rectangle = destRectangles[0];
   images[0]->subImage(*rectangle,
   new CPUTextureBuilderSubImageImageLister(width, height,
   listener, autodelete),
   true);
   }*/
  else {
    std::vector<const IImage*> tailImages;
    std::vector<RectangleI*> tailSourceRectangles;
    std::vector<RectangleI*> tailDestRectangles;
    for (int i = 1; i < imagesSize; i++) {
      tailImages.push_back( images[i] );
      tailSourceRectangles.push_back(srcRectangles[i]);
      tailDestRectangles.push_back( destRectangles[i] );
    }
    
    images[0]->combineWith(*srcRectangles[0],
                           tailImages,
                           tailSourceRectangles,
                           tailDestRectangles,
                           Vector2I(width, height),
                           listener,
                           autodelete);
  }
}






