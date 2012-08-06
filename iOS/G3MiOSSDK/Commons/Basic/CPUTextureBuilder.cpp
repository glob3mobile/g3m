//
//  CPUTextureBuilder.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 23/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "CPUTextureBuilder.hpp"

int CPUTextureBuilder::createTextureFromImages(GL * gl, 
                                               const std::vector<const IImage*>& vImages, 
                                               int width, int height) const {
  if (vImages.size() > 0){
    
    const IImage* im = vImages[0], *im2 = NULL;
    for (int i = 1; i < vImages.size(); i++) {
      const IImage* imTrans = vImages[i];
      im2 = im->combineWith(*imTrans, width, height);
      if (i > 1) delete im;
      im = im2;
    }
    
    int texID = gl->uploadTexture(im, width, height);
    
    if (1 < vImages.size()){
      delete im;
    }
    return texID;
  } else{
    return -1;
  }
}

int CPUTextureBuilder::createTextureFromImages(GL * gl, const IFactory* factory,
                                               const std::vector<const IImage*>& vImages, 
                                               const std::vector<const Rectangle*>& vRectangles, 
                                               int width, int height) const {
  
  int todo_JM = 0;
  const IImage* base;
  if (vRectangles[0]->_width == width && vRectangles[0]->_height == height){
    base = vImages[0];
  } else{
    base = factory->createImageFromSize(width, height);
  }
  
  for (int i = 1; i < vImages.size(); i++) {
    IImage* im2 = base->combineWith(*vImages[i], *vRectangles[i], width, height);
    delete base;
    base = im2;
  }
  
  return todo_JM;
  
}
