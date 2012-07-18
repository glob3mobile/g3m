//
//  Image_iOS.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 13/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


#ifndef G3MiOSSDK_Image_iOS_h
#define G3MiOSSDK_Image_iOS_h

#import <UIKit/UIKit.h>

#include <string>

#include "IImage.hpp"

class Image_iOS: public IImage {
private:
  UIImage* _image;
  
public:

  Image_iOS(UIImage* image) : _image(image) {
    
  }
  
  UIImage* getUIImage() const {
    return _image;
  }
  
  int getWidth() const {
    return (_image == NULL) ? 0 : _image.size.width;
  }
  
  int getHeight() const {
    return (_image == NULL) ? 0 : _image.size.height;
  }

};

#endif