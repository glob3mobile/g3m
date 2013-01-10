//
//  TextUtils_iOS.mm
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/10/13.
//
//

#include "TextUtils_iOS.hpp"

#import <UIKit/UIKit.h>

#include "Image_iOS.hpp"

IImage* TextUtils_iOS::createLabelBitmap(const std::string& label) {
  NSString* text = [NSString stringWithCString: label.c_str()
                                      encoding: NSUTF8StringEncoding];

  // set the font type and size
  //UIFont *font = [UIFont systemFontOfSize:20.0];
  UIFont *font = [UIFont systemFontOfSize:22.0];
  CGSize textSize  = [text sizeWithFont:font];

  CGSize imageSize = CGSizeMake(textSize.width + 2, textSize.height + 2);

  UIGraphicsBeginImageContextWithOptions(imageSize, NO, 0.0);

  // optional: add a shadow, to avoid clipping the shadow you should make the context size bigger
  CGContextRef ctx = UIGraphicsGetCurrentContext();
  CGContextSetFillColorWithColor(ctx, [[UIColor yellowColor] CGColor]);
  //CGContextSetShadowWithColor(ctx, CGSizeMake(1.0, 1.0), 5.0, [[UIColor blackColor] CGColor]);
  CGContextSetShadowWithColor(ctx, CGSizeMake(2.0, 2.0), 2.0, [[UIColor blackColor] CGColor]);

  // draw in context, you can use also drawInRect:withFont:
  [text drawAtPoint:CGPointMake(0.0, 0.0) withFont:font];

  // transfer image
  UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
  UIGraphicsEndImageContext();

  return new Image_iOS(image, NULL);
}
