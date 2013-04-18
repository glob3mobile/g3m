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

class CPUTextureBuilder:public TextureBuilder {
public:

  const void createTextureFromImage(GL* gl,
                                    const IFactory* factory,
                                    IImage* image,
                                    const Vector2I& textureResolution,
                                    IImageListener* listener,
                                    bool autodelete) const;

  const void createTextureFromImages(GL* gl,
                                     const IFactory* factory,
                                     const std::vector<IImage*>& images,
                                     const std::vector<RectangleI*>& srcRectangles,
                                     const std::vector<RectangleI*>& destRectangles,
                                     const Vector2I& textureResolution,
                                     IImageListener* listener,
                                     bool autodelete) const;

};

#endif
