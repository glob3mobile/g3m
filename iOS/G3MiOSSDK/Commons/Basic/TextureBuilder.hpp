//
//  TextureBuilder.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 23/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_TextureBuilder_hpp
#define G3MiOSSDK_TextureBuilder_hpp

#include "IImage.hpp"
#include "GL.hpp"
#include "IFactory.hpp"
#include <vector>

class TextureBuilder
{
public:
  
  virtual const IImage* createTextureFromImage(GL * gl,
                                               const IFactory* factory,
                                               const IImage* image,
                                               int width,
                                               int height) const = 0;
  
  virtual const IImage* createTextureFromImages(GL * gl,
                                                const IFactory* factory,
                                                const std::vector<const IImage*> images,
                                                int width,
                                                int height) const = 0;
  
  virtual const IImage* createTextureFromImages(GL * gl,
                                                const IFactory* factory,
                                                const std::vector<const IImage*> images,
                                                const std::vector<const Rectangle*> rectangles,
                                                int width,
                                                int height) const = 0;
  virtual ~TextureBuilder() {}
};



#endif
