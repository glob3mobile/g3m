//
//  CPUTextureBuilder.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 23/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "CPUTextureBuilder.hpp"

int CPUTextureBuilder::createTextureFromImages(IGL * gl, 
                                               const std::vector<const IImage*>& images,
                                               int width, int height) const {
  const int imagesSize = images.size();
  if (imagesSize > 0) {
    const IImage* im = images[0];
    for (int i = 1; i < imagesSize; i++) {
      const IImage* imTrans = images[i];
      im = im->combineWith(*imTrans, width, height);
    }
    
    return gl->uploadTexture(im, width, height);
  }
  else {
    return -1;
  }
}
