//
//  ErrorHandling.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/17/13.
//
//

#ifndef __G3M__ErrorHandling__
#define __G3M__ErrorHandling__

#ifdef C_CODE
  #include <stdexcept>

  #ifdef __EMSCRIPTEN__
    #include <emscripten/emscripten.h>
    #define THROW_EXCEPTION(x)                                            \
    {                                                                     \
      emscripten_log(EM_LOG_CONSOLE | EM_LOG_ERROR | EM_LOG_C_STACK | EM_LOG_JS_STACK | EM_LOG_DEMANGLE, x);  \
      throw( std::logic_error(x) );                                       \
    }
  #else
    #define THROW_EXCEPTION(x) throw(std::logic_error(x));
  #endif

#endif

#endif
