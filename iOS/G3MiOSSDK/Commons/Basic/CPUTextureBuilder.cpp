//
//  CPUTextureBuilder.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 23/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "CPUTextureBuilder.hpp"

int CPUTextureBuilder::createTextureFromImages(GL * gl, 
                                               const std::vector<const IImage*>& images,
                                               int width, int height) const {
  const int imagesSize = images.size();
  
  if (imagesSize == 0) {
    return -1;
  }
  
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
  
  int texID = gl->uploadTexture(im, width, height);
  
  if (imagesSize > 1) {
    delete im;
  }
  
  return texID;
  
//  const int imagesSize = images.size();
//
//  if (imagesSize > 0) {
//    const IImage* im = images[0], *im2 = NULL;
//    for (int i = 1; i < imagesSize; i++) {
//      const IImage* imTrans = images[i];
//      im2 = im->combineWith(*imTrans, width, height);
//      if (i > 1) {
//        delete im;
//      }
//      im = im2;
//    }
//    
//    int texID = gl->uploadTexture(im, width, height);
//    
//    if (imagesSize > 1){
//      delete im;
//    }
//    return texID;
//  }
//  else {
//    return -1;
//  }
}

int CPUTextureBuilder::createTextureFromImages(GL * gl, const IFactory* factory,
                                               const std::vector<const IImage*>& vImages, 
                                               const std::vector<const Rectangle*>& vRectangles, 
                                               int width, int height) const {
  const IImage* base;
  int i = 0; //First image to merge
  Rectangle baseRec(0,0, width, height);
  if (vRectangles[0]->equalTo(baseRec)){
    base = vImages[0];
    i = 1;
  } else{
    base = factory->createImageFromSize(width, height);
  }
  
  for (; i < vImages.size(); i++) {
    IImage* im2 = base->combineWith(*vImages[i], *vRectangles[i], width, height);
    
    if (base != vImages[0]) {
      delete base;
    }
    
    base = im2;
  }
  
  int texID = gl->uploadTexture(base, width, height);
  
  if (base != vImages[0]) {
    delete base;
  }

  return texID;
}
