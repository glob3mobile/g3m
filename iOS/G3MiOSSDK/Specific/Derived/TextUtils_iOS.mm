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

CGColorRef TextUtils_iOS::toCGColor(const Color* color) {
  if (color == NULL) {
    return NULL;
  }

  return [[UIColor colorWithRed: color->getRed()
                          green: color->getGreen()
                           blue: color->getBlue()
                          alpha: color->getAlpha()] CGColor];
}

IImage* TextUtils_iOS::createLabelBitmap(const std::string& label,
                                         float fontSize,
                                         const Color* color,
                                         const Color* shadowColor) {
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

  // draw in context, you can use also drawInRect:withFont:
  [text drawAtPoint: CGPointMake(0.0, 0.0)
           withFont: font];

  // transfer image
  UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
  UIGraphicsEndImageContext();

  return new Image_iOS(image, NULL);
}
