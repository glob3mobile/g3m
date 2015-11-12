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

#import "NSString_CppAdditions.h"

Canvas_iOS::~Canvas_iOS() {
  _currentUIFont = nil;

  if (_path != NULL) {
    CGPathRelease(_path);
    _path = NULL;
  }

  if (_context != NULL) {
    CGContextRelease( _context );
    _context = NULL;
  }

  delete [] _dataRGBA8888;
}

void Canvas_iOS::tryToSetCurrentFontToContext() {
  if ((_currentUIFont != nil) &&
      (_context != NULL)) {
    CGContextSelectFont(_context,
                        [[_currentUIFont fontName] UTF8String],
                        [_currentUIFont pointSize],
                        kCGEncodingMacRoman);
  }
}

void Canvas_iOS::_initialize(int width, int height) {
  CGColorSpaceRef colorSpace = CGColorSpaceCreateDeviceRGB();

//  _dataRGBA8888 = new unsigned char[4 * width * height];
//  _context = CGBitmapContextCreate(_dataRGBA8888,
//                                   width,
//                                   height,
//                                   8,          // bits per component
//                                   4 * width,  // bytes per row
//                                   colorSpace,
//                                   kCGImageAlphaPremultipliedLast | kCGBitmapByteOrder32Big);
//  if (_context == NULL) {
//    delete _dataRGBA8888;
//    _dataRGBA8888 = NULL;
//    ILogger::instance()->logError("Can't create CGContext");
//    return;
//  }
//
//  CGContextClearRect( _context, CGRectMake( 0, 0, width, height ) );

  _context = CGBitmapContextCreate(NULL,       // memory created by Quartz
                                   width,
                                   height,
                                   8,          // bits per component
                                   0,          // bytes per row
                                   colorSpace,
                                   kCGImageAlphaPremultipliedLast | kCGBitmapByteOrder32Big);

  CGColorSpaceRelease( colorSpace );

  if (_context == NULL) {
    ILogger::instance()->logError("Can't create CGContext");
    return;
  }

  CGContextSetShouldAntialias(_context, YES);
  CGContextSetAllowsFontSmoothing(_context, YES);
  CGContextSetShouldSmoothFonts(_context, YES);
  CGContextSetShouldSubpixelPositionFonts(_context, NO);
  CGContextSetShouldSubpixelQuantizeFonts(_context, NO);
  CGContextSetInterpolationQuality(_context, kCGInterpolationHigh);


  tryToSetCurrentFontToContext();
}

CGColorRef Canvas_iOS::createCGColor(const Color& color) {
  return CGColorCreateCopy( [[UIColor colorWithRed: color._red
                                             green: color._green
                                              blue: color._blue
                                             alpha: color._alpha] CGColor] );
}

void Canvas_iOS::_setFillColor(const Color& color) {
  CGContextSetRGBFillColor(_context,
                           color._red,
                           color._green,
                           color._blue,
                           color._alpha);
}

void Canvas_iOS::_setLineColor(const Color& color) {
  CGContextSetRGBStrokeColor(_context,
                             color._red,
                             color._green,
                             color._blue,
                             color._alpha);
}

void Canvas_iOS::_setLineWidth(float width) {
  CGContextSetLineWidth(_context, width);
}

void Canvas_iOS::_setLineCap(StrokeCap cap) {
  switch (cap) {
    case CAP_BUTT:
      CGContextSetLineCap(_context, kCGLineCapButt);
      break;
    case CAP_ROUND:
      CGContextSetLineCap(_context, kCGLineCapRound);
      break;
    case CAP_SQUARE:
      CGContextSetLineCap(_context, kCGLineCapSquare);
      break;
  }
}

void Canvas_iOS::_setLineJoin(StrokeJoin join) {
  switch (join) {
    case JOIN_MITER:
      CGContextSetLineJoin(_context, kCGLineJoinMiter);
      break;
    case JOIN_ROUND:
      CGContextSetLineJoin(_context, kCGLineJoinRound);
      break;
    case JOIN_BEVEL:
      CGContextSetLineJoin(_context, kCGLineJoinBevel);
      break;
  }
}

void Canvas_iOS::_setLineMiterLimit(float limit) {
  CGContextSetMiterLimit(_context, limit);
}

void Canvas_iOS::_setLineDash(float lengths[],
                              int count,
                              float phase) {
#if __LP64__
  double* lengthsD = new double[count];
  for (int i = 0; i < count; i++) {
    lengthsD[i] = lengths[i];
  }
  CGContextSetLineDash(_context,
                       phase,
                       lengthsD, count);
  delete [] lengthsD;
#else
  CGContextSetLineDash(_context,
                       phase,
                       lengths, count);
#endif
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

void Canvas_iOS::_clearRect(float left, float top,
                            float width, float height) {
  CGContextClearRect(_context, CGRectMake(left, _canvasHeight - top,
                                          width, -height));
}

void Canvas_iOS::_createImage(IImageListener* listener,
                              bool autodelete) {
  CGImageRef cgImage = CGBitmapContextCreateImage(_context);
  UIImage* image = [UIImage imageWithCGImage: cgImage];
  CFRelease(cgImage);

  IImage* result = new Image_iOS(image, NULL, _dataRGBA8888);
  _dataRGBA8888 = NULL; // moved ownership to image
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

  const CGFloat minx = CGRectGetMinX(rrect);
  const CGFloat midx = CGRectGetMidX(rrect);
  const CGFloat maxx = CGRectGetMaxX(rrect);
  const CGFloat miny = CGRectGetMinY(rrect);
  const CGFloat midy = CGRectGetMidY(rrect);
  const CGFloat maxy = CGRectGetMaxY(rrect);

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
  NSString* nsString = [NSString stringWithCppString: text];

  CGSize cgSize = [nsString sizeWithFont: _currentUIFont];

  return Vector2F((float) cgSize.width,
                  (float) cgSize.height);
}

void Canvas_iOS::_fillText(const std::string& text,
                           float left, float top) {
  UIGraphicsPushContext(_context);

  CGContextSaveGState(_context);
  CGContextTranslateCTM(_context, 0.0f, _canvasHeight);
  CGContextScaleCTM(_context, 1.0f, -1.0f);

  NSString* nsText = [NSString stringWithCppString: text.c_str()];

  [nsText drawAtPoint: CGPointMake(left, top)
             withFont: _currentUIFont];

  CGContextRestoreGState(_context);

  UIGraphicsPopContext();
}


void Canvas_iOS::_drawImage(const IImage* image,
                            float destLeft, float destTop) {
  UIImage* uiImage = ((Image_iOS*) image)->getUIImage();
  CGImage* cgImage = [uiImage CGImage];

  CGContextDrawImage(_context,
                     CGRectMake(destLeft,
                                _canvasHeight - (destTop + image->getHeight()),
                                image->getWidth(),
                                image->getHeight()),
                     cgImage);
}

void Canvas_iOS::_drawImage(const IImage* image,
                            float destLeft, float destTop,
                            float transparency) {
  UIImage* uiImage = ((Image_iOS*) image)->getUIImage();
  CGImage* cgImage = [uiImage CGImage];

  CGContextSetAlpha(_context, transparency);

  CGContextDrawImage(_context,
                     CGRectMake(destLeft,
                                _canvasHeight - (destTop + image->getHeight()),
                                image->getWidth(),
                                image->getHeight()),
                     cgImage);

  CGContextSetAlpha(_context, 1.0);
}

void Canvas_iOS::_drawImage(const IImage* image,
                            float destLeft, float destTop, float destWidth, float destHeight) {
  UIImage* uiImage = ((Image_iOS*) image)->getUIImage();
  CGImage* cgImage = [uiImage CGImage];

  CGContextDrawImage(_context,
                     CGRectMake(destLeft,
                                _canvasHeight - (destTop + destHeight),
                                destWidth,
                                destHeight),
                     cgImage);
}

void Canvas_iOS::_drawImage(const IImage* image,
                            float destLeft, float destTop, float destWidth, float destHeight,
                            float transparency) {

  UIImage* uiImage = ((Image_iOS*) image)->getUIImage();
  CGImage* cgImage = [uiImage CGImage];

  CGContextSetAlpha(_context, transparency);


  CGContextDrawImage(_context,
                     CGRectMake(destLeft,
                                _canvasHeight - (destTop + destHeight),
                                destWidth,
                                destHeight),
                     cgImage);

  CGContextSetAlpha(_context, 1.0);
}

void Canvas_iOS::_drawImage(const IImage* image,
                            float srcLeft, float srcTop, float srcWidth, float srcHeight,
                            float destLeft, float destTop, float destWidth, float destHeight) {
  UIImage* uiImage = ((Image_iOS*) image)->getUIImage();
  CGImage* cgImage = [uiImage CGImage];

  CGRect destRect = CGRectMake(destLeft,
                               _canvasHeight - (destTop + destHeight),
                               destWidth,
                               destHeight);

  if ((srcLeft   == 0) &&
      (srcTop    == 0) &&
      (srcWidth  == image->getWidth()) &&
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

void Canvas_iOS::_drawImage(const IImage* image,
                            float srcLeft, float srcTop, float srcWidth, float srcHeight,
                            float destLeft, float destTop, float destWidth, float destHeight,
                            float transparency) {
  UIImage* uiImage = ((Image_iOS*) image)->getUIImage();
  CGImage* cgImage = [uiImage CGImage];

  CGRect destRect = CGRectMake(destLeft,
                               _canvasHeight - (destTop + destHeight),
                               destWidth,
                               destHeight);

  if ((srcLeft   == 0) &&
      (srcTop    == 0) &&
      (srcWidth  == image->getWidth()) &&
      (srcHeight == image->getHeight())) {

    CGContextSetAlpha(_context, transparency);

    CGContextDrawImage(_context,
                       destRect,
                       cgImage);

    CGContextSetAlpha(_context, 1.0);
  }
  else {
    // Cropping image
    CGRect cropRect = CGRectMake(srcLeft,
                                 srcTop,
                                 srcWidth,
                                 srcHeight);

    CGImage* cgCropImage = CGImageCreateWithImageInRect(cgImage, cropRect);

    CGContextSetAlpha(_context, transparency);

    CGContextDrawImage(_context,
                       destRect,
                       cgCropImage);

    CGContextSetAlpha(_context, 1.0);

    CGImageRelease(cgCropImage);
  }

}

void Canvas_iOS::_beginPath() {
  if (_path != NULL) {
    CGPathRelease(_path);
  }

  _path = CGPathCreateMutable();

  _transform = CGAffineTransformMake(1, 0, -0, -1, 0, _canvasHeight);
}

void Canvas_iOS::_closePath() {
  CGPathCloseSubpath(_path);
}

void Canvas_iOS::_stroke() {
  CGContextAddPath(_context, _path);
  CGContextDrawPath(_context, kCGPathStroke);

  CGPathRelease(_path);
  _path = NULL;
}

void Canvas_iOS::_fill() {
  CGContextAddPath(_context, _path);
  CGContextDrawPath(_context, kCGPathEOFill);

  CGPathRelease(_path);
  _path = NULL;
}

void Canvas_iOS::_fillAndStroke() {
  CGContextAddPath(_context, _path);
  CGContextDrawPath(_context, kCGPathEOFillStroke);

  CGPathRelease(_path);
  _path = NULL;
}

void Canvas_iOS::_moveTo(float x, float y) {
  CGPathMoveToPoint(_path, &_transform, x, y);
}

void Canvas_iOS::_lineTo(float x, float y) {
  CGPathAddLineToPoint(_path, &_transform, x, y);
}


void Canvas_iOS::_fillEllipse(float left, float top,
                              float width, float height) {
  CGContextFillEllipseInRect(_context,
                             CGRectMake(left, _canvasHeight - top,
                                        width, -height));
}

void Canvas_iOS::_strokeEllipse(float left, float top,
                                float width, float height) {
  CGContextStrokeEllipseInRect(_context,
                               CGRectMake(left, _canvasHeight - top,
                                          width, -height));
}

void Canvas_iOS::_fillAndStrokeEllipse(float left, float top,
                                       float width, float height) {
  _fillEllipse(left, top, width, height);
  _strokeEllipse(left, top, width, height);
}
