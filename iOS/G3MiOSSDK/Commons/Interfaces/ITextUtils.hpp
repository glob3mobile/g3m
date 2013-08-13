//
//  ITextUtils.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/10/13.
//
//

#ifndef __G3MiOSSDK__ITextUtils__
#define __G3MiOSSDK__ITextUtils__

#include "ILogger.hpp"
class IImage;
class Color;
class IImageListener;

#include "LabelPosition.h"

class ITextUtils {
private:
  static ITextUtils* _instance;

public:
  static void setInstance(ITextUtils* factory) {
    if (_instance != NULL) {
      ILogger::instance()->logWarning("ITextUtils instance already set!");
      delete _instance;
    }
    _instance = factory;
  }

  static ITextUtils* instance() {
    return _instance;
  }

  virtual ~ITextUtils() {
  }


  virtual void createLabelImage(const std::string& label,
                                IImageListener* listener,
                                bool autodelete);

  virtual void createLabelImage(const std::string& label,
                                float fontSize,
                                const Color* color,
                                const Color* shadowColor,
                                IImageListener* listener,
                                bool autodelete) = 0;


  virtual void labelImage(const IImage* image,
                          const std::string& label,
                          const LabelPosition labelPosition,
                          IImageListener* listener,
                          bool autodelete);

  virtual void labelImage(const IImage* image,
                          const std::string& label,
                          const LabelPosition labelPosition,
                          int separation,
                          float fontSize,
                          const Color* color,
                          const Color* shadowColor,
                          IImageListener* listener,
                          bool autodelete) = 0;
  
};

#endif
