//
//  Canvas_iOS.mm
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/9/13.
//
//

#include "Canvas_iOS.hpp"

#include "Color.hpp"
#include "Image_iOS.hpp"
#include "IImageListener.hpp"
#include "GFont.hpp"

#import <UIKit/UIKit.h>
#include "Image_iOS.hpp"


Canvas_iOS::~Canvas_iOS() {
  if (_context) {
    CGContextRelease( _context );
    _context = NULL;
  }
}

void Canvas_iOS::tryToSetCurrentFontToContext() {
  if ((_currentUIFont != NULL) &&
      (_context != NULL)) {
    CGContextSelectFont(_context,
                        [[_currentUIFont fontName] UTF8String],
                        [_currentUIFont pointSize],
                        kCGEncodingMacRoman);
  }
}

void Canvas_iOS::_initialize(int width, int height) {
  CGColorSpaceRef colorSpace = CGColorSpaceCreateDeviceRGB();
  
  _context = CGBitmapContextCreate(NULL,       // memory created by Quartz
                                   width,
                                   height,
                                   8,          // bits per component
                                   width * 4,  // bitmap bytes per row: 4 bytes per pixel
                                   colorSpace,
                                   kCGImageAlphaPremultipliedLast);
  
  CGColorSpaceRelease( colorSpace );
  
  if (_context == NULL) {
    ILogger::instance()->logError("Can't create CGContext");
    return;
  }
  
  tryToSetCurrentFontToContext();
}

CGColorRef Canvas_iOS::createCGColor(const Color& color) {
  return CGColorCreateCopy( [[UIColor colorWithRed: color.getRed()
                                             green: color.getGreen()
                                              blue: color.getBlue()
                                             alpha: color.getAlpha()] CGColor] );
}

void Canvas_iOS::_setFillColor(const Color& color) {
  CGContextSetRGBFillColor(_context,
                           color.getRed(),
                           color.getGreen(),
                           color.getBlue(),
                           color.getAlpha());
}

void Canvas_iOS::_setStrokeColor(const Color& color) {
  CGContextSetRGBStrokeColor(_context,
                             color.getRed(),
                             color.getGreen(),
                             color.getBlue(),
                             color.getAlpha());
}

void Canvas_iOS::_setStrokeWidth(float width) {
  CGContextSetLineWidth(_context, width);
}

void Canvas_iOS::_setShadow(const Color& color,
                            float blur,
                            float offsetX,
                            float offsetY) {
  CGColorRef cgColor = createCGColor(color);
  CGContextSetShadowWithColor(_context,
                              CGSizeMake(offsetX, offsetY),
                              blur,
                              cgColor);
  CGColorRelease(cgColor);
}

void Canvas_iOS::_removeShadow() {
  CGContextSetShadowWithColor(_context,
                              CGSizeMake(0, 0),
                              0,
                              NULL);
}

void Canvas_iOS::_createImage(IImageListener* listener,
                              bool autodelete) {
  CGImageRef cgImage = CGBitmapContextCreateImage(_context);
  UIImage* image = [UIImage imageWithCGImage: cgImage];
  CFRelease(cgImage);
  
  IImage* result = new Image_iOS(image, NULL);
  listener->imageCreated(result);
  if (autodelete) {
    delete listener;
  }
}

void Canvas_iOS::_fillRectangle(float left, float top,
                                float width, float height) {
  CGContextFillRect(_context,
                    CGRectMake(left, _canvasHeight - top,
                               width, -height));
}


void Canvas_iOS::_strokeRectangle(float left, float top,
                                  float width, float height) {
  CGContextStrokeRect(_context,
                      CGRectMake(left, _canvasHeight - top,
                                 width, -height));
}

void Canvas_iOS::drawRoundedRectangle(float left, float top,
                                      float width, float height,
                                      float radius,
                                      CGPathDrawingMode mode) {
  CGRect rrect = CGRectMake(left, _canvasHeight - top,
                            width, -height);
  
	const float minx = CGRectGetMinX(rrect);
  const float midx = CGRectGetMidX(rrect);
  const float maxx = CGRectGetMaxX(rrect);
	const float miny = CGRectGetMinY(rrect);
  const float midy = CGRectGetMidY(rrect);
  const float maxy = CGRectGetMaxY(rrect);
  
	CGContextMoveToPoint(_context, minx, midy);
	CGContextAddArcToPoint(_context, minx, miny, midx, miny, radius);
	CGContextAddArcToPoint(_context, maxx, miny, maxx, midy, radius);
	CGContextAddArcToPoint(_context, maxx, maxy, midx, maxy, radius);
	CGContextAddArcToPoint(_context, minx, maxy, minx, midy, radius);
	CGContextClosePath(_context);
	CGContextDrawPath(_context, mode);
}

void Canvas_iOS::_fillRoundedRectangle(float left, float top,
                                       float width, float height,
                                       float radius) {
  drawRoundedRectangle(left, top,
                       width, height,
                       radius,
                       kCGPathFill);
}

void Canvas_iOS::_strokeRoundedRectangle(float left, float top,
                                         float width, float height,
                                         float radius) {
  drawRoundedRectangle(left, top,
                       width, height,
                       radius,
                       kCGPathStroke);
}

void Canvas_iOS::_fillAndStrokeRectangle(float left, float top,
                                         float width, float height) {
  _fillRectangle(left, top, width, height);
  _strokeRectangle(left, top, width, height);
}

void Canvas_iOS::_fillAndStrokeRoundedRectangle(float left, float top,
                                                float width, float height,
                                                float radius) {
  drawRoundedRectangle(left, top,
                       width, height,
                       radius,
                       kCGPathFillStroke);
}

UIFont* Canvas_iOS::createUIFont(const GFont& font) {
  const bool bold   = font.isBold();
  const bool italic = font.isItalic();
  
  NSString* fontName = nil;
  if (font.isSansSerif()) {
    if (bold) {
      if (italic) {
        fontName = @"Helvetica-BoldOblique";
      }
      else {
        fontName = @"Helvetica-Bold";
      }
    }
    else {
      if (italic) {
        fontName = @"Helvetica-Oblique";
      }
      else {
        fontName = @"Helvetica";
      }
    }
  }
  else if (font.isSerif()) {
    if (bold) {
      if (italic) {
        fontName = @"TimesNewRomanPS-BoldItalicMT";
      }
      else {
        fontName = @"TimesNewRomanPS-BoldMT";
      }
    }
    else {
      if (italic) {
        fontName = @"TimesNewRomanPS-ItalicMT";
      }
      else {
        fontName = @"TimesNewRomanPSMT";
      }
    }
  }
  else if (font.isMonospaced()) {
    if (bold) {
      if (italic) {
        fontName = @"Courier-BoldOblique";
      }
      else {
        fontName = @"Courier-Bold";
      }
    }
    else {
      if (italic) {
        fontName = @"Courier-Oblique";
      }
      else {
        fontName = @"Courier";
      }
    }
  }
  else {
    ILogger::instance()->logError("Unsupported Font type");
    return nil;
  }
  
  return [UIFont fontWithName: fontName
                         size: font.getSize()];
}

void Canvas_iOS::_setFont(const GFont& font) {
  _currentUIFont = createUIFont(font);
  
  tryToSetCurrentFontToContext();
}

const Vector2F Canvas_iOS::_textExtent(const std::string& text) {
  NSString* nsString = [NSString stringWithCString: text.c_str()
                                          encoding: NSUTF8StringEncoding];
  
  CGSize cgSize = [nsString sizeWithFont: _currentUIFont];
  
  return Vector2F(cgSize.width, cgSize.height);
}

void Canvas_iOS::_fillText(const std::string& text,
                           float left, float top) {
  //  const Vector2F textExtent = _textExtent(text);
  //
  //  CGContextShowTextAtPoint(_context,
  //                           left, _canvasHeight - top - textExtent._y,
  //                           text.c_str(),
  //                           text.size());
  
  UIGraphicsPushContext(_context);
  
  CGContextSaveGState(_context);
  CGContextTranslateCTM(_context, 0.0f, _canvasHeight);
  CGContextScaleCTM(_context, 1.0f, -1.0f);
  
  NSString* nsText = [NSString stringWithCString: text.c_str()
                                        encoding: NSUTF8StringEncoding];
  
  [nsText drawAtPoint: CGPointMake(left, top)
             withFont: _currentUIFont];
  
  CGContextRestoreGState(_context);
  
  UIGraphicsPopContext();
}

void Canvas_iOS::_drawImage(const IImage* image,
                            float destLeft, float destTop){
UIImage* uiImage = ((Image_iOS*) image)->getUIImage();
CGImage* cgImage = [uiImage CGImage];

CGContextDrawImage(_context,
                   CGRectMake(destLeft,
                              destTop,
                              image->getWidth(),
                              image->getHeight()),
                   cgImage);
}

void Canvas_iOS::_drawImage(const IImage* image,
                            float destLeft, float destTop, float destWidth, float destHeight){
  
  UIImage* uiImage = ((Image_iOS*) image)->getUIImage();
  CGImage* cgImage = [uiImage CGImage];
  
  CGContextDrawImage(_context,
                     CGRectMake(destLeft,
                                destTop,
                                destWidth,
                                destHeight),
                     cgImage);
}

void Canvas_iOS::_drawImage(const IImage* image,
                            float srcLeft, float srcTop, float srcWidth, float srcHeight,
                            float destLeft, float destTop, float destWidth, float destHeight){
  
  UIImage* uiImage = ((Image_iOS*) image)->getUIImage();
  CGImage* cgImage = [uiImage CGImage];
  
  CGRect destRect = CGRectMake(destLeft,
                               _canvasHeight - (destTop + destHeight), // Bottom
                               destWidth,
                               destHeight);
  
  if ((srcLeft == 0) &&
      (srcTop == 0) &&
      (srcWidth == image->getWidth()) &&
      (srcHeight == image->getHeight())) {
    CGContextDrawImage(_context,
                       destRect,
                       cgImage);
  }
  else {
    // Cropping image
    CGRect cropRect = CGRectMake(srcLeft,
                                 srcTop,
                                 srcWidth,
                                 srcHeight);
    
    CGImage* cgCropImage = CGImageCreateWithImageInRect(cgImage, cropRect);
    
    CGContextDrawImage(_context,
                       destRect,
                       cgCropImage);
    
    CGImageRelease(cgCropImage);
  }
}
