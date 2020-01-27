//
//  ErrorHandling.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/17/13.
//
//

#ifndef __G3M__ErrorHandling__
#define __G3M__ErrorHandling__


#if defined(C_CODE) && !defined(__EMSCRIPTEN__)
  #include <stdexcept>
  #define THROW_EXCEPTION(x) throw(std::logic_error(x));
#endif

#if defined(C_CODE) && defined(__EMSCRIPTEN__)
  #include <stdexcept>
  #include <emscripten/emscripten.h>

  #define THROW_EXCEPTION(x)                                                                               \
  {                                                                                                        \
    emscripten_log(EM_LOG_CONSOLE | EM_LOG_ERROR | EM_LOG_C_STACK | EM_LOG_JS_STACK | EM_LOG_DEMANGLE, x); \
    throw( std::logic_error(x) );                                                                          \
  }
#endif


#endif
