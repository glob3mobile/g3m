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
#include "IByteBuffer.hpp"

class GLImage{
  
  const GLFormat _format;
  
  IByteBuffer* const _data;
  
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
  
  bool isTransparent() const{
    if (_format == RGBA){
      return true;        //Always true
    }
    
    return false;
  }
  
  IByteBuffer* getByteBuffer() const{
    return _data;
  }
  
  int getWidth() const {
    return _width;
  }
  
  int getHeight() const {
    return _height;
  }
  
  GLFormat getFormat() const {
    return _format;
  }
  
};

#endif
