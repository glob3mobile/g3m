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
#include "IImageListener.hpp"
#include "RectangleI.hpp"

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
/*
void Image_iOS::combineWith(const IImage& other,
                            const RectangleI& rect,
                            int width, int height,
                            IImageListener* listener,
                            bool autodelete) const {
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
                     CGRectMake(rect._x,
                                rect._y,
                                rect._width,
                                rect._height ),
                     otherIm.CGImage);
  
  //SAVING IMAGE
  CGImageRef imgRef = CGBitmapContextCreateImage(context);
  UIImage* img = [UIImage imageWithCGImage:imgRef];
  CGImageRelease(imgRef);
  CGContextRelease(context);
  
  delete[] imageData;
  
  //return new Image_iOS(img, NULL);
  listener->imageCreated( new Image_iOS(img, NULL) );
  if (autodelete) {
    delete listener;
  }
}
*/
void Image_iOS::drawUIImageOnContext(const CGContextRef& context,
                                     const IImage& other,
                                     const RectangleI& sourceRect,
                                     const RectangleI& destRect) const{
  
  UIImage* otherIm = ((Image_iOS&)other).getUIImage();
  
  //Cropping other image if neccesary
  CGImage* CGotherIm = NULL;
  if (sourceRect._width != other.getWidth() || sourceRect._height != other.getHeight() ||
      sourceRect._x != 0 || sourceRect._y != 0 ){
    CGRect cropRect = CGRectMake(sourceRect._x,
                                 sourceRect._y,
                                 sourceRect._width,
                                 sourceRect._height);
    
    CGotherIm = CGImageCreateWithImageInRect([otherIm CGImage], cropRect);
  } else{
    CGotherIm = [otherIm CGImage];
  }
  //TODO: CHECK THIS
  CGContextDrawImage(context,
                     CGRectMake(destRect._x,
                                destRect._y,
                                destRect._width,
                                destRect._height),
                     CGotherIm);
  
  //Releasing if cropped
  if (CGotherIm != otherIm.CGImage){
    CGImageRelease(CGotherIm);
  }
  
}

void Image_iOS::combineWith(const IImage& other,
                            const RectangleI& sourceRect,
                            const RectangleI& destRect,
                            const Vector2I& size,
                            IImageListener* listener,
                            bool autodelete) const {
  
  CGColorSpaceRef colorSpace = CGColorSpaceCreateDeviceRGB();
  unsigned char* imageData = new unsigned char[size._x * size._y * 4];
  
  CGContextRef context = CGBitmapContextCreate(imageData,
                                               size._x, size._y,
                                               8,
                                               4 * size._x,
                                               colorSpace,
                                               kCGImageAlphaPremultipliedLast | kCGBitmapByteOrder32Big );
  
  CGRect bounds = CGRectMake( 0, 0, size._x, size._y);
  
  CGColorSpaceRelease( colorSpace );
  //CGContextClearRect( context, bounds );
  
  //We draw the images one over the other
  CGContextDrawImage(context,
                     bounds,
                     _image.CGImage);
  
  drawUIImageOnContext(context, other, sourceRect, destRect);
  
  //SAVING IMAGE
  CGImageRef imgRef = CGBitmapContextCreateImage(context);
  UIImage* img = [UIImage imageWithCGImage:imgRef];
  CGImageRelease(imgRef);
  CGContextRelease(context);
  
  delete[] imageData;
  
  //return new Image_iOS(img, NULL);
  listener->imageCreated( new Image_iOS(img, NULL) );
  if (autodelete) {
    delete listener;
  }
}

/*
void Image_iOS::combineWith(const std::vector<const IImage*>& images,
                            const std::vector<RectangleI*>& rectangles,
                            int width, int height,
                            IImageListener* listener,
                            bool autodelete) const {
  
  const int imagesSize = images.size();
  if (imagesSize == 0 || imagesSize != rectangles.size()) {
    if (getWidth() == width && getHeight() == height) {
      listener->imageCreated( shallowCopy() );
      if (autodelete) {
        delete listener;
      }
    }
    else {
      scale(width, height, listener, autodelete);
    }
  }
  else {
    
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
    
    // draw the receiver image
    CGContextDrawImage(context,
                       bounds,
                       _image.CGImage);
    
    // draw the images one over the other
    for (int i = 0; i < imagesSize; i++) {
      UIImage* image = ((Image_iOS*) images[i])->getUIImage();
      const RectangleI* rect = rectangles[i];
      
      CGContextDrawImage(context,
                         CGRectMake(rect->_x,
                                    rect->_y,
                                    rect->_width,
                                    rect->_height ),
                         image.CGImage);
    }
    
    //SAVING IMAGE
    CGImageRef imgRef = CGBitmapContextCreateImage(context);
    UIImage* img = [UIImage imageWithCGImage:imgRef];
    CGImageRelease(imgRef);
    CGContextRelease(context);
    
    delete[] imageData;
    
    listener->imageCreated( new Image_iOS(img, NULL) );
    if (autodelete) {
      delete listener;
    }
  }
  
}
*/

void Image_iOS::combineWith(const RectangleI& thisSourceRect,
                            const std::vector<const IImage*>& images,
                            const std::vector<RectangleI*>& sourceRects,
                            const std::vector<RectangleI*>& destRects,
                            const Vector2I& size,
                            IImageListener* listener,
                            bool autodelete) const {
  
  CGImage * thisCGImage = _image.CGImage;
  if (thisSourceRect._x != 0 || thisSourceRect._y != 0 ||
      thisSourceRect._width != this->getWidth() || thisSourceRect._height != this->getHeight()){
    
    //We have to crop this image first
    CGRect cropRect = CGRectMake(thisSourceRect._x,
                                 thisSourceRect._y,
                                 thisSourceRect._width,
                                 thisSourceRect._height);
    
    //Cropping image
    thisCGImage = CGImageCreateWithImageInRect(thisCGImage, cropRect);
  }

  int width = size._x;
  int height = size._y;
  
  const int imagesSize = images.size();
  if (imagesSize == 0 || imagesSize != sourceRects.size() || imagesSize != destRects.size() ) {
    if (getWidth() == width && getHeight() == height) {
      
      if (thisCGImage == _image.CGImage){
        listener->imageCreated( shallowCopy() );
      } else{
        listener->imageCreated(new Image_iOS([[UIImage alloc] initWithCGImage:thisCGImage], NULL)); //TODO:Check
      }
      if (autodelete) {
        delete listener;
      }
    }
    else {
      scale(width, height, listener, autodelete);
    }
  }
  else {
    
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
    
    // draw the receiver image
    CGContextDrawImage(context,
                       bounds,
                       thisCGImage);
    
    if (thisCGImage != _image.CGImage){
      CGImageRelease(thisCGImage);
    }
    
    // draw the images one over the other
    for (int i = 0; i < imagesSize; i++) {
      const IImage* other = images[i];
      const RectangleI *sourceRect = sourceRects[i];
      const RectangleI *destRect = destRects[i];      //Destination in final image pixels
      
      drawUIImageOnContext(context, *other, *sourceRect, *destRect);
    }
    
    //SAVING IMAGE
    CGImageRef imgRef = CGBitmapContextCreateImage(context);
    UIImage* img = [UIImage imageWithCGImage:imgRef];
    CGImageRelease(imgRef);
    CGContextRelease(context);
    
    delete[] imageData;
    
    listener->imageCreated( new Image_iOS(img, NULL) );
    if (autodelete) {
      delete listener;
    }
  }
  
}

void Image_iOS::subImage(const RectangleI& rect,
                         IImageListener* listener,
                         bool autodelete) const {
  IImage* image;
  if (rect._x == 0 && rect._y == 0 && rect._width == getWidth() && rect._height == getHeight() ) {
    image = shallowCopy();
  }
  else {
    CGRect cropRect = CGRectMake(rect._x,
                                 rect._y,
                                 rect._width,
                                 rect._height);
    
    //Cropping image
    CGImageRef imageRef = CGImageCreateWithImageInRect([this->_image CGImage], cropRect);
    
    image = new Image_iOS([UIImage imageWithCGImage:imageRef], NULL);
    
    CGImageRelease(imageRef);
  }
  
  //  return image;
  listener->imageCreated( image );
  if (autodelete) {
    delete listener;
  }
}

unsigned char* Image_iOS::createByteArrayRGBA8888() const {
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

void Image_iOS::scale(int width, int height,
                      IImageListener* listener,
                      bool autodelete) const {
  IImage* result;
  if ( width == getWidth() && height == getHeight() ) {
    result = shallowCopy();
  }
  else {
    CGSize newSize = CGSizeMake(width, height);
    
    UIGraphicsBeginImageContextWithOptions(newSize, NO, 0.0);
    [_image drawInRect:CGRectMake(0, 0, newSize.width, newSize.height)];
    UIImage *newImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    result = new Image_iOS(newImage, NULL);
  }
  
  //  return new Image_iOS(newImage, NULL);
  listener->imageCreated( result );
  if (autodelete) {
    delete listener;
  }
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
