//
//  TextureBuilder.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 23/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_TextureBuilder_hpp
#define G3MiOSSDK_TextureBuilder_hpp

//#include "IImage.hpp"
//#include "GL.hpp"
//#include "IFactory.hpp"
class IImage;
class GL;
class IFactory;
class RectangleI;
class IImageListener;
class Vector2I;
class RectangleF;

#include <vector>

class TextureBuilder {
public:
  
  virtual const void createTextureFromImage(GL* gl,
                                            const IFactory* factory,
                                            IImage* image,
                                            const Vector2I& textureResolution,
                                            IImageListener* listener,
                                            bool autodelete) const = 0;
  
  virtual const void createTextureFromImages(GL* gl,
                                             const IFactory* factory,
                                             const std::vector<const IImage*>& images,
                                             const std::vector<RectangleF*>& srcRectangles,
                                             const std::vector<RectangleF*>& destRectangles,
                                             const Vector2I& textureResolution,
                                             IImageListener* listener,
                                             bool autodelete) const = 0;
  
  virtual ~TextureBuilder() {
  }
  
};

#endif
