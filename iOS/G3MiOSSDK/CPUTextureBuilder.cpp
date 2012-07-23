//
//  CPUTextureBuilder.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 23/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "CPUTextureBuilder.hpp"

int CPUTextureBuilder::createTextureFromImages(IGL * gl, 
                                               const std::vector<const IImage*>& vImages, 
                                               int width, int height) const
{
  if (vImages.size() > 0){
  
    const IImage* im = vImages[0];
    for (int i = 1; i < vImages.size(); i++) {
      const IImage* imTrans = vImages[i];
      im = im->combineWith(*imTrans, width, height);
    }
    
    return gl->uploadTexture(im, width, height);
    
  } else{
    return -1;
  }
}
