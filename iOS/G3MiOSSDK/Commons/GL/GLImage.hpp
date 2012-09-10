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

class GLImage{
  
  const GLFormat _format;
  
  const void* _data;
  
  int _width;
  int _height;
  
public:
  
  GLImage(GLFormat format, void* data):
  _format(format), _data(data)
  {
    
  }
  
  static GLImage invalid(){
    return GLImage(RGBA, NULL);
  }
  
  bool isValid(){
    return _data != NULL;
  }
  
  bool isTransparent(){
    if (_format == RGBA){
      return true;        //Always true
    }
    
    return false;
  }
  
};

#endif
