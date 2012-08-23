//
//  IStringUtils.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 23/08/12.
//
//

#ifndef __G3MiOSSDK__IStringUtils__
#define __G3MiOSSDK__IStringUtils__

#include <string>

class IStringUtils {
private:
  static const IStringUtils* _instance;
  
public:
  static void setInstance(const IStringUtils* instance) {
    if (_instance != NULL) {
      printf("Warning, IStringUtils instance set two times\n");
    }
    _instance = instance;
  }
  
  static const IStringUtils* instance() {
    return _instance;
  }
  
  virtual ~IStringUtils() {
    
  }
  
  virtual std::string createString(unsigned char* data,
                                   int            length) const = 0;
  
  
};

#endif
