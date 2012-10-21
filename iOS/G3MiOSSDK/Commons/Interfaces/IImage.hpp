//
//  IImage.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 01/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_IImage_h
#define G3MiOSSDK_IImage_h

#include <vector>
#include "RectangleD.hpp"
#include "Vector2I.hpp"

class IImage {
public:
  // a virtual destructor is needed for conversion to Java
  virtual ~IImage() {}
  
  virtual int getWidth() const = 0;
  virtual int getHeight() const = 0;
  virtual Vector2I getExtent() const = 0;
  
  virtual IImage* combineWith(const IImage& other,
                              int width, int height) const = 0;
  
  virtual IImage* combineWith(const IImage& other,
                              const RectangleD& rect,
                              int width, int height) const = 0;
  
  virtual IImage* subImage(const RectangleD& rect) const = 0;
  
  virtual IImage* scale(int width, int height) const = 0;

  virtual const std::string description() const = 0;
  
  virtual IImage* shallowCopy() const = 0;
};

#endif
