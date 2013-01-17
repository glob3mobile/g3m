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

CGColorRef TextUtils_iOS::toCGColor(const Color* color) {
  if (color == NULL) {
    return NULL;
  }

  return [[UIColor colorWithRed: color->getRed()
                          green: color->getGreen()
                           blue: color->getBlue()
                          alpha: color->getAlpha()] CGColor];
}

void TextUtils_iOS::createLabelImage(const std::string& label,
                                     float fontSize,
                                     const Color* color,
                                     const Color* shadowColor,
                                     IImageListener* listener,
                                     bool autodelete) {
  NSString* text = [NSString stringWithCString: label.c_str()
                                      encoding: NSUTF8StringEncoding];

  UIFont *font = [UIFont systemFontOfSize: fontSize];
  CGSize textSize = [text sizeWithFont: font];

  CGSize imageSize = (shadowColor == NULL) ? textSize : CGSizeMake(textSize.width + 2,
                                                                   textSize.height + 2);

  UIGraphicsBeginImageContextWithOptions(imageSize, NO, 0.0);

  CGContextRef ctx = UIGraphicsGetCurrentContext();

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
    NSString* text = [NSString stringWithCString: label.c_str()
                                        encoding: NSUTF8StringEncoding];

    UIFont *font = [UIFont systemFontOfSize: fontSize];
    CGSize textSize = [text sizeWithFont: font];

    CGSize labelSize = (shadowColor == NULL) ? textSize : CGSizeMake(textSize.width + 2,
                                                                     textSize.height + 2);

    float imageWidth;
    float imageHeight;
    if (labelPosition == Bottom) {
      imageWidth = fmaxf(labelSize.width, image->getWidth());
      imageHeight = labelSize.height + separation + image->getHeight();
    }
    else if (labelPosition == Right) {
      imageWidth = labelSize.width + separation + image->getWidth();
      imageHeight = fmaxf(labelSize.height, image->getHeight());
    }
    else {
      ILogger::instance()->logError("Unsupported LabelPosition");
      listener->imageCreated(NULL);
      if (autodelete) {
        delete listener;
      }
      return;
    }


    UIGraphicsBeginImageContextWithOptions(CGSizeMake(imageWidth, imageHeight), NO, 0.0);

    CGContextRef ctx = UIGraphicsGetCurrentContext();

    UIImage* uiImage = ((const Image_iOS*) image)->getUIImage();
    if (labelPosition == Bottom) {
      [uiImage drawAtPoint: CGPointMake((imageWidth - image->getWidth()) / 2,
                                        0.0) ];
    }
    else if (labelPosition == Right) {
      [uiImage drawAtPoint: CGPointMake(0.0,
                                        (imageHeight - image->getHeight()) / 2) ];
    }

    CGContextSetFillColorWithColor(ctx, toCGColor(color));

    if (shadowColor != NULL) {
      CGContextSetShadowWithColor(ctx, CGSizeMake(2.0, 2.0), 1.0, toCGColor(shadowColor));
    }

    if (labelPosition == Bottom) {
      [text drawAtPoint: CGPointMake((imageWidth - labelSize.width) / 2,
                                     image->getHeight() + separation)
               withFont: font];
    }
    else if (labelPosition == Right) {
      [text drawAtPoint: CGPointMake(image->getWidth() + separation,
                                     (imageHeight - textSize.height) / 2)
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
