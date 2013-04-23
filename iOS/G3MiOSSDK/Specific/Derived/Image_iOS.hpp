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
  UIImage*        _image;
  mutable NSData* _sourceBuffer;

  Image_iOS(const Image_iOS& that);
  void operator=(const Image_iOS& that);

//  unsigned char* rgba8888_to_rgba4444(unsigned char* src, // IN, pointer to source buffer
//                                      int sizeInBytes) const;    // IN size of source buffer, in bytes

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

  Image_iOS(int width, int height);

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

  Vector2I getExtent() const {
    return Vector2I(getWidth(), getHeight());
  }

  void combineWith(const IImage& other,
                   const RectangleI& rect,
                   int width, int height,
                   IImageListener* listener,
                   bool autodelete) const;

  void combineWith(const std::vector<const IImage*>& images,
                   const std::vector<RectangleI*>& rectangles,
                   int width, int height,
                   IImageListener* listener,
                   bool autodelete) const;

  void subImage(const RectangleI& rect,
                IImageListener* listener,
                bool autodelete) const;

  unsigned char* createByteArrayRGBA8888() const;

  void scale(int width, int height,
             IImageListener* listener,
             bool autodelete) const;

  const std::string description() const;

  IImage* shallowCopy() const;

};

#endif
