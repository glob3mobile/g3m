//
//  Image_iOS.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 13/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


#ifndef G3MiOSSDK_Image_iOS
#define G3MiOSSDK_Image_iOS

#import <UIKit/UIKit.h>

#include <string>

#include "IImage.hpp"

class Image_iOS: public IImage {
private:
  UIImage*        _image;
  mutable NSData* _sourceBuffer;
  
  Image_iOS(const Image_iOS& that);
  void operator=(const Image_iOS& that);

public:
  
  virtual ~Image_iOS() {
    _image        = NULL;
    _sourceBuffer = NULL;
  }
  
  Image_iOS(UIImage* image,
            NSData* sourceBuffer) :
  _image(image),
  _sourceBuffer(sourceBuffer)
  {
  }

  UIImage* getUIImage() const {
    return _image;
  }
  
  NSData* getSourceBuffer() const {
    return _sourceBuffer;
  }
  
  void releaseSourceBuffer() const {
    _sourceBuffer = NULL;
  }
  
  int getWidth() const {
    return (_image == NULL) ? 0 : (int) _image.size.width;
  }
  
  int getHeight() const {
    return (_image == NULL) ? 0 : (int) _image.size.height;
  }
  
  const Vector2I getExtent() const {
    return Vector2I(getWidth(), getHeight());
  }

  
  unsigned char* createByteArrayRGBA8888() const;

  const std::string description() const;
  
  IImage* shallowCopy() const;

};

#endif
