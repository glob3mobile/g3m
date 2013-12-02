//
//  ErrorHandling.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/17/13.
//
//

#ifndef __G3MiOSSDK__ErrorHandling__
#define __G3MiOSSDK__ErrorHandling__

#ifdef C_CODE
#include <string>
#define ERROR(x) throw(std::string(x));
#else
#define ERROR(x) throw new RuntimeException(x);
#endif

#endif
