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

class ITextUtils {
private:
  static ITextUtils* _instance;

public:
  static void setInstance(ITextUtils* factory) {
    if (_instance != NULL) {
      ILogger::instance()->logWarning("ITextUtils instance already set!");
    }
    _instance = factory;
  }

  static ITextUtils* instance() {
    return _instance;
  }

  virtual ~ITextUtils() {

  }

  virtual IImage* createLabelBitmap(const std::string& label) = 0;

};

#endif
