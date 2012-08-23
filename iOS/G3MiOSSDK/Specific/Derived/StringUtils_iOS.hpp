//
//  StringUtils_iOS.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 23/08/12.
//
//

#ifndef __G3MiOSSDK__StringUtils_iOS__
#define __G3MiOSSDK__StringUtils_iOS__

#include "IStringUtils.hpp"

class StringUtils_iOS : public IStringUtils {
public:
  std::string createString(unsigned char* data,
                           int            length) const;
  
};

#endif
