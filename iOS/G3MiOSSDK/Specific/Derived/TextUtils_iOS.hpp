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

class TextUtils_iOS : public ITextUtils {
public:
  IImage* createLabelBitmap(const std::string& label);

};

#endif

