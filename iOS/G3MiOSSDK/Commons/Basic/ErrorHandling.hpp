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
#include <stdexcept>
#define THROW_EXCEPTION(x) throw(std::logic_error(x));
#endif

#endif
