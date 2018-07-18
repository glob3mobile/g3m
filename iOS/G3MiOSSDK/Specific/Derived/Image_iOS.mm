//
//  Image_iOS.mm
//  G3MiOSSDK
//
//  Created by José Miguel S N on 13/06/12.
//

#include "Image_iOS.hpp"

#include "IStringBuilder.hpp"
#include "IImageListener.hpp"
#include "RectangleI.hpp"
#include "MutableColor255.hpp"


unsigned char* Image_iOS::createByteArrayRGBA8888() const {
  const int width  = getWidth();
  const int height = getHeight();

  unsigned char* result = new unsigned char[4 * width * height];

  CGColorSpaceRef colorSpace = CGColorSpaceCreateDeviceRGB();
  CGContextRef context = CGBitmapContextCreate(result,
                                               width,
                                               height,
                                               8,         // bits per component
                                               4 * width, // bytes per row
                                               colorSpace,
                                               kCGImageAlphaPremultipliedLast | kCGBitmapByteOrder32Big );

  CGColorSpaceRelease( colorSpace );

  CGRect bounds = CGRectMake( 0, 0, width, height );
  CGContextClearRect( context, bounds );

  CGContextDrawImage( context, bounds, _image.CGImage );

  CGContextRelease(context);

  return result;
}

const std::string Image_iOS::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("Image_iOS ");
  isb->addInt(getWidth());
  isb->addString("x");
  isb->addInt(getHeight());
  isb->addString(", _image=(");
  isb->addString( [[_image description] cStringUsingEncoding:NSUTF8StringEncoding] );
  isb->addString(")");
  const std::string s = isb->getString();
  delete isb;
  return s;
}

IImage* Image_iOS::shallowCopy() const {
  return new Image_iOS(_image, _sourceBuffer);
}

void Image_iOS::getPixel(int x, int y,
                         MutableColor255& pixel) const {
  if (_image == NULL) {
    return;
  }

  if (_rawData == NULL) {
    _rawData = createByteArrayRGBA8888();
  }

  const CGSize imageSize = _image.size;

  const CGFloat width  = imageSize.width;
  const CGFloat height = imageSize.height;

  const NSUInteger bytesPerPixel = 4;
  const NSUInteger bytesPerRow   = (NSUInteger) (bytesPerPixel * width);

  const NSUInteger byteIndex = (NSUInteger) ((bytesPerRow * (height-1-y)) + x * bytesPerPixel);
  pixel._red   = _rawData[byteIndex];
  pixel._green = _rawData[byteIndex + 1];
  pixel._blue  = _rawData[byteIndex + 2];
  pixel._alpha = _rawData[byteIndex + 3];
}
