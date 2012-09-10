//
//  GLImage.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 10/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_GLImage_hpp
#define G3MiOSSDK_GLImage_hpp

#include "INativeGL.hpp"
#include "IByteBuffer.h"

class GLImage{
  
  const GLFormat _format;
  
  const IByteBuffer* _data;
  
  int _width;
  int _height;
  
public:
  
  GLImage(GLFormat format, IByteBuffer* data, int width, int height):
  _format(format), _data(data), _width(width), _height(height)
  {
    
  }
  
  ~GLImage(){
    delete _data;
  }
  
  bool isTransparent(){
    if (_format == RGBA){
      return true;        //Always true
    }
    
    return false;
  }
  
};

#endif
