//
//  IImage.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 01/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_IImage
#define G3MiOSSDK_IImage

#include "Vector2I.hpp"

class IImage {
public:
  virtual ~IImage() {
  }

  virtual int getWidth() const = 0;
  virtual int getHeight() const = 0;
  virtual const Vector2I getExtent() const = 0;

  virtual const std::string description() const = 0;
#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

  virtual IImage* shallowCopy() const = 0;
};

#endif
