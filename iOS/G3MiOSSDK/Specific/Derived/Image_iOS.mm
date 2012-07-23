//
//  Image_iOS.mm
//  G3MiOSSDK
//
//  Created by José Miguel S N on 13/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "Image_iOS.hpp"

IImage* Image_iOS::combineWith(const IImage& transparent, int width, int height) const
{
  UIImage* transIm = ((Image_iOS&)transparent).getUIImage();
  
  CGColorSpaceRef colorSpace = CGColorSpaceCreateDeviceRGB();
  void *imageData = new unsigned char[height * width * 4 ];
  
  CGContextRef context = CGBitmapContextCreate(imageData,
                                               width, height,
                                               8, 4 * width,
                                               colorSpace,
                                               kCGImageAlphaPremultipliedLast | kCGBitmapByteOrder32Big );
  CGColorSpaceRelease( colorSpace );
  CGContextClearRect( context, CGRectMake( 0, 0, width, height ) );
  
  //We draw the images one over the other
  CGContextDrawImage( context, CGRectMake( 0, 0, width, height ), _image.CGImage );
  CGContextDrawImage( context, CGRectMake( 0, 0, width, height ), transIm.CGImage );
  
  CGImageRef imgRef = CGBitmapContextCreateImage(context);
  UIImage* img = [UIImage imageWithCGImage:imgRef];
  CGImageRelease(imgRef);
  CGContextRelease(context);

  return new Image_iOS(img);
}