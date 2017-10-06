//
//  Image_iOS.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 13/06/12.
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

  mutable unsigned char* _rawData;

  Image_iOS(const Image_iOS& that);
  void operator=(const Image_iOS& that);

public:

  virtual ~Image_iOS() {
    printf("DELETING Image_iOS: %p\n", this);
    
    //TODO: JM Delete this
    if (_image != nil){
      printf("DELETING Image_iOS: %p with UIImage: %p\n", this, _image);
      
      //_image        = nil;
    }
    if (_sourceBuffer != nil){
      _sourceBuffer = nil;
    }

    delete [] _rawData;
  }

  Image_iOS(UIImage* image,
            NSData* sourceBuffer) :
  _image(image),
  _sourceBuffer(sourceBuffer),
  _rawData(NULL)
  {
    printf("CREATED Image_IOS with UIImage: %p\n", _image);
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

  bool isPremultiplied() const {
    return true;
  }

  void getPixel(int x, int y,
                MutableColor255& pixel) const;


};

#endif
