//
//  ErrorHandling.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/17/13.
//
//

#ifndef __G3MiOSSDK__ErrorHandling__
#define __G3MiOSSDK__ErrorHandling__

#define ERROR(x) throw new RuntimeException(x);

#ifdef C_CODE
#undef ERROR
#include <string>
#define ERROR(x) throw(std::string(x));
#endif

#endif
