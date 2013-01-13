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

  virtual IImage* createLabelBitmap(const std::string& label);
  
  virtual IImage* createLabelBitmap(const std::string& label,
                                    float fontSize,
                                    const Color* color,
                                    const Color* shadowColor) = 0;

};

#endif
