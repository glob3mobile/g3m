//
//  CPUTextureBuilder.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 23/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "CPUTextureBuilder.hpp"

const IImage* CPUTextureBuilder::createTextureFromImage(GL * gl,
                                                         const IFactory* factory,
                                                         const IImage* image,
                                                         int width,
                                                         int height) const{
  if (image == NULL) {
    ILogger::instance()->logWarning("Creating blank Image");
    return factory->createImageFromSize(width, height);
  } else{
    if (image->getHeight() == height && image->getWidth() == width){
      return image;
    } else {
      return image->scale(width, height);
    }
  }
}

const IImage* CPUTextureBuilder::createTextureFromImages(GL * gl,
                                                          const IFactory* factory,
                                                          const std::vector<const IImage*> images,
                                                          int width,
                                                          int height) const{
  
  const int imagesSize = images.size();
  
  if (imagesSize == 0) {
    ILogger::instance()->logWarning("Creating blank Image");
    return factory->createImageFromSize(width, height);
  } else{
    const IImage* im = images[0];
    const IImage* im2 = NULL;
    for (int i = 1; i < imagesSize; i++) {
      const IImage* imTrans = images[i];
      im2 = im->combineWith(*imTrans, width, height);
      if (i > 1) {
        delete im;
      }
      im = im2;
    }
    return im;
  }
}

const IImage* CPUTextureBuilder::createTextureFromImages(GL * gl,
                                                          const IFactory* factory,
                                                          const std::vector<const IImage*> images,
                                                          const std::vector<const Rectangle*> rectangles,
                                                          int width,
                                                          int height) const{
  
  
  const int imagesSize = images.size();
  
  if (imagesSize == 0 || images.size() != rectangles.size()) {
    ILogger::instance()->logWarning("Creating blank Image");
    return factory->createImageFromSize(width, height);
  } else{
    
    const IImage* base;
    int i = 0; //First image to merge
    Rectangle baseRec(0,0, width, height);
    if (rectangles.size() > 0 && rectangles[0]->equalTo(baseRec)){
      base = images[0];
      i = 1;
    }
    else {
      base = factory->createImageFromSize(width, height);
    }
    
    for (; i < images.size(); i++) {
      const IImage* newIm = images[i];
      const Rectangle* newRect = rectangles[i];
      
      IImage* im2 = base->combineWith(*newIm, *newRect, width, height);
      if (base != images[0]) {
        delete base;
      }
      base = im2;
    }
    return base;
  }
}
