//
//  Image_iOS.mm
//  G3MiOSSDK
//
//  Created by José Miguel S N on 13/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "Image_iOS.hpp"

#include "IFactory.hpp"
#include "IStringBuilder.hpp"

Image_iOS::Image_iOS(int width, int height) {
  CGColorSpaceRef colorSpace = CGColorSpaceCreateDeviceRGB();
  unsigned char* imageData = new unsigned char[height * width * 4];
  
  CGContextRef context = CGBitmapContextCreate(imageData,
                                               width, height,
                                               8, 4 * width,
                                               colorSpace,
                                               kCGImageAlphaPremultipliedLast | kCGBitmapByteOrder32Big );
  CGColorSpaceRelease( colorSpace );
  CGContextClearRect( context, CGRectMake( 0, 0, width, height ) );
  
  CGImageRef imgRef = CGBitmapContextCreateImage(context);
  _image = [UIImage imageWithCGImage:imgRef];
  CGImageRelease(imgRef);
  CGContextRelease(context);
  
  delete[] imageData;
}

IImage* Image_iOS::combineWith(const IImage& other,
                               int width, int height) const {
  UIImage* transIm = ((Image_iOS&)other).getUIImage();
  
  CGColorSpaceRef colorSpace = CGColorSpaceCreateDeviceRGB();
  unsigned char* imageData = new unsigned char[height * width * 4];
  
  CGContextRef context = CGBitmapContextCreate(imageData,
                                               width, height,
                                               8, 4 * width,
                                               colorSpace,
                                               kCGImageAlphaPremultipliedLast | kCGBitmapByteOrder32Big );
  CGColorSpaceRelease( colorSpace );
  CGRect bounds = CGRectMake( 0, 0, width, height );
  CGContextClearRect( context, bounds );
  
  //We draw the images one over the other
  CGContextDrawImage( context, bounds, _image.CGImage );
  CGContextDrawImage( context, bounds, transIm.CGImage );
  
  CGImageRef imgRef = CGBitmapContextCreateImage(context);
  UIImage* img = [UIImage imageWithCGImage:imgRef];
  CGImageRelease(imgRef);
  CGContextRelease(context);
  
  delete[] imageData;
  
  return new Image_iOS(img, NULL);
}

IImage* Image_iOS::combineWith(const IImage& other,
                               const Rectangle& rect,
                               int width, int height) const {
  UIImage* otherIm = ((Image_iOS&)other).getUIImage();
  
  CGColorSpaceRef colorSpace = CGColorSpaceCreateDeviceRGB();
  unsigned char* imageData = new unsigned char[height * width * 4];
  
  CGContextRef context = CGBitmapContextCreate(imageData,
                                               width, height,
                                               8, 4 * width,
                                               colorSpace,
                                               kCGImageAlphaPremultipliedLast | kCGBitmapByteOrder32Big );
  
  CGRect bounds = CGRectMake( 0, 0, width, height );
  
  CGColorSpaceRelease( colorSpace );
  CGContextClearRect( context, bounds );
  
  //We draw the images one over the other
  CGContextDrawImage(context,
                     bounds,
                     _image.CGImage);
  CGContextDrawImage(context,
                     CGRectMake((float) rect._x,
                                (float) rect._y,
                                (float) rect._width,
                                (float) rect._height ),
                     otherIm.CGImage);
  
  //SAVING IMAGE
  CGImageRef imgRef = CGBitmapContextCreateImage(context);
  UIImage* img = [UIImage imageWithCGImage:imgRef];
  CGImageRelease(imgRef);
  CGContextRelease(context);
  
  delete[] imageData;
  
  return new Image_iOS(img, NULL);
}

IImage* Image_iOS::subImage(const Rectangle& rect) const {
  CGRect cropRect = CGRectMake((float) rect._x,
                               (float) rect._y,
                               (float) rect._width,
                               (float) rect._height);
  
  //Cropping image
  CGImageRef imageRef = CGImageCreateWithImageInRect([this->_image CGImage], cropRect);
  
  Image_iOS* image = new Image_iOS([UIImage imageWithCGImage:imageRef], NULL);
  
  CGImageRelease(imageRef);
  return image;
}

unsigned char* Image_iOS::createByteArrayRGBA8888() const{
  const int width  = getWidth();
  const int height = getHeight();
  
  unsigned char* result = new unsigned char[4 * width * height];
  
  CGColorSpaceRef colorSpace = CGColorSpaceCreateDeviceRGB();
  CGContextRef context = CGBitmapContextCreate(result,
                                               width, height,
                                               8, 4 * width,
                                               colorSpace,
                                               kCGImageAlphaPremultipliedLast | kCGBitmapByteOrder32Big );
  
  CGColorSpaceRelease( colorSpace );
  CGRect bounds = CGRectMake( 0, 0, width, height );
  CGContextClearRect( context, bounds );
  
  CGContextDrawImage( context, bounds, _image.CGImage );
  
  CGContextRelease(context);
  
  return result;
}

IImage* Image_iOS::scale(int width, int height) const{
  
  CGSize newSize = CGSizeMake(width, height);
  
  UIGraphicsBeginImageContextWithOptions(newSize, NO, 0.0);
  [_image drawInRect:CGRectMake(0, 0, newSize.width, newSize.height)];
  UIImage *newImage = UIGraphicsGetImageFromCurrentImageContext();    
  UIGraphicsEndImageContext();
  
  return new Image_iOS(newImage, NULL);
}

const std::string Image_iOS::description() const {
  IStringBuilder *isb = IStringBuilder::newStringBuilder();
  isb->addString("Image_iOS ");
  isb->addInt(getWidth());
  isb->addString("x");
  isb->addInt(getHeight());
  isb->addString(", _image=(");
  isb->addString( [[_image description] cStringUsingEncoding:NSUTF8StringEncoding] );
  isb->addString(")");
  std::string s = isb->getString();
  delete isb;
  return s;
}

IImage* Image_iOS::shallowCopy() const {
  return new Image_iOS(_image, _sourceBuffer);
}
