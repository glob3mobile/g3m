//
//  CPUTextureBuilder.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 23/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "CPUTextureBuilder.hpp"

#include "IImageUtils.hpp"


const void CPUTextureBuilder::createTextureFromImages(const Vector2I& textureExtent,
                                                      const std::vector<const IImage*>& images,
                                                      const std::vector<RectangleF*>& srcRectangles,
                                                      const std::vector<RectangleF*>& destRectangles,
                                                      IImageListener* listener,
                                                      bool autodelete) const{
  IImageUtils::combine(textureExtent,
                       images,
                       srcRectangles,
                       destRectangles,
                       listener,
                       autodelete);
}
