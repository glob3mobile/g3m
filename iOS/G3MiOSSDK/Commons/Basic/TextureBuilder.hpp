//
//  TextureBuilder.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 23/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_TextureBuilder_hpp
#define G3MiOSSDK_TextureBuilder_hpp


class IImage;
class IFactory;
class RectangleI;
class IImageListener;
class RectangleF;
class Vector2I;
#include <vector>

class TextureBuilder {
public:
  virtual const void createTextureFromImages(const Vector2I& textureExtent,
                                             const std::vector<const IImage*>& images,
                                             const std::vector<RectangleF*>& srcRectangles,
                                             const std::vector<RectangleF*>& destRectangles,
                                             IImageListener* listener,
                                             bool autodelete) const = 0;

  virtual ~TextureBuilder() {
  }

};

#endif
