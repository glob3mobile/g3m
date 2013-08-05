//
//  TextUtils_iOS.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/10/13.
//
//

#ifndef __G3MiOSSDK__TextUtils_iOS__
#define __G3MiOSSDK__TextUtils_iOS__

#include "ITextUtils.hpp"

#import <UIKit/UIKit.h>


class TextUtils_iOS : public ITextUtils {
private:
  CGColorRef toCGColor(const Color* color);

public:
  void createLabelImage(const std::string& label,
                        float fontSize,
                        const Color* color,
                        const Color* shadowColor,
                        IImageListener* listener,
                        bool autodelete);

  void labelImage(const IImage* image,
                  const std::string& label,
                  const LabelPosition labelPosition,
                  int separation,
                  float fontSize,
                  const Color* color,
                  const Color* shadowColor,
                  IImageListener* listener,
                  bool autodelete);
  
};

#endif

