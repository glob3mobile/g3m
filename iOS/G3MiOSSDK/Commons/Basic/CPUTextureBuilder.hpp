//
//  CPUTextureBuilder.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 23/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_CPUTextureBuilder_hpp
#define G3MiOSSDK_CPUTextureBuilder_hpp

#include "TextureBuilder.hpp"

class CPUTextureBuilder:public TextureBuilder
{
public:
  const GLTextureId createTextureFromImages(GL * gl,
                                            const std::vector<const IImage*> images,
                                            int width,
                                            int height) const;
  
  const GLTextureId createTextureFromImages(GL * gl,
                                            const IFactory* factory,
                                            const std::vector<const IImage*> images,
                                            const std::vector<const Rectangle*> rectangles,
                                            int width,
                                            int height) const;
  
};

#endif
