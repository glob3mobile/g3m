//
//  IImage.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 01/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_IImage_h
#define G3MiOSSDK_IImage_h

//#include <vector>
//#include "RectangleI.hpp"
#include "Vector2I.hpp"
//class RectangleI;
//class IImageListener;

class IImage {
public:
  virtual ~IImage() {
  }

  virtual int getWidth() const = 0;
  virtual int getHeight() const = 0;
  virtual const Vector2I getExtent() const = 0;

  virtual const std::string description() const = 0;

  virtual IImage* shallowCopy() const = 0;
};

#endif
