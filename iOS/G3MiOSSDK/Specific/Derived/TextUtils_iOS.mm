//
//  TextUtils_iOS.mm
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/10/13.
//
//

#include "TextUtils_iOS.hpp"


#include "Image_iOS.hpp"
#include "Color.hpp"
#include "IImageListener.hpp"

#include <math.h>
#include "IFactory.hpp"
#import "NSString_CppAdditions.h"


CGColorRef TextUtils_iOS::toCGColor(const Color* color) {
  if (color == NULL) {
    return NULL;
  }

  return [[UIColor colorWithRed: color->_red
                          green: color->_green
                           blue: color->_blue
                          alpha: color->_alpha] CGColor];
}


void TextUtils_iOS::createLabelImage(const std::string& label,
                                     float fontSize,
                                     const Color* color,
                                     const Color* shadowColor,
                                     IImageListener* listener,
                                     bool autodelete) {
  NSString* text = [NSString stringWithCppString: label];

  
  UIFont *font = [UIFont systemFontOfSize: fontSize];
  CGSize textSize = [text sizeWithFont: font];

  CGSize imageSize = (shadowColor == NULL) ? textSize : CGSizeMake(textSize.width + 2,
                                                                   textSize.height + 2);

  UIGraphicsBeginImageContextWithOptions(imageSize, NO, 0.0);

  CGContextRef ctx = UIGraphicsGetCurrentContext();

//  int __REMOVE;
//  CGContextSetRGBFillColor(ctx, 0, 0, 0, 0.5f);
//  CGContextFillRect(ctx, CGRectMake(0, 0, imageSize.width, imageSize.height));

  CGContextSetFillColorWithColor(ctx, toCGColor(color));

  if (shadowColor != NULL) {
    CGContextSetShadowWithColor(ctx, CGSizeMake(2.0, 2.0), 1.0, toCGColor(shadowColor));
  }

  [text drawAtPoint: CGPointMake(0.0, 0.0)
           withFont: font];

  // transfer image
  UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
  UIGraphicsEndImageContext();

  IImage* result = new Image_iOS(image, NULL);
  listener->imageCreated(result);
  if (autodelete) {
    delete listener;
  }
}


void TextUtils_iOS::labelImage(const IImage* image,
                               const std::string& label,
                               const LabelPosition labelPosition,
                               int separation,
                               float fontSize,
                               const Color* color,
                               const Color* shadowColor,
                               IImageListener* listener,
                               bool autodelete) {

  if (image == NULL) {
    createLabelImage(label,
                     fontSize,
                     color,
                     shadowColor,
                     listener,
                     autodelete);
  }
  else {
    NSString* text = [NSString stringWithCppString: label];

    UIFont *font = [UIFont systemFontOfSize: fontSize];
    CGSize textSize = [text sizeWithFont: font];

    CGSize labelSize = (shadowColor == NULL) ? textSize : CGSizeMake(textSize.width + 2,
                                                                     textSize.height + 2);

    float resultWidth;
    float resultHeight;
    if (labelPosition == Bottom) {
      resultWidth  = fmaxf(labelSize.width, image->getWidth());
      resultHeight = labelSize.height + separation + image->getHeight();
    }
    else if (labelPosition == Right) {
      resultWidth  = labelSize.width + separation + image->getWidth();
      resultHeight = fmaxf(labelSize.height, image->getHeight());
    }
    else {
      ILogger::instance()->logError("Unsupported LabelPosition");
      listener->imageCreated(NULL);
      if (autodelete) {
        delete listener;
      }
      return;
    }


    UIGraphicsBeginImageContextWithOptions(CGSizeMake(resultWidth, resultHeight), NO, 0.0);

    CGContextRef ctx = UIGraphicsGetCurrentContext();

    UIImage* uiImage = ((const Image_iOS*) image)->getUIImage();
    if (labelPosition == Bottom) {
      [uiImage drawAtPoint: CGPointMake((resultWidth - image->getWidth()) / 2,
                                        0.0) ];
    }
    else if (labelPosition == Right) {
      [uiImage drawAtPoint: CGPointMake(0.0,
                                        (resultHeight - image->getHeight()) / 2) ];
    }

    CGContextSetFillColorWithColor(ctx, toCGColor(color));

    if (shadowColor != NULL) {
      CGContextSetShadowWithColor(ctx, CGSizeMake(2.0, 2.0), 1.0, toCGColor(shadowColor));
    }

    if (labelPosition == Bottom) {
      [text drawAtPoint: CGPointMake((resultWidth - labelSize.width) / 2,
                                     image->getHeight() + separation)
               withFont: font];
    }
    else if (labelPosition == Right) {
      [text drawAtPoint: CGPointMake(image->getWidth() + separation,
                                     (resultHeight - textSize.height) / 2)
               withFont: font];
    }

    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();

    IImage* result = new Image_iOS(image, NULL);
    listener->imageCreated(result);
    if (autodelete) {
      delete listener;
    }
  }
}
