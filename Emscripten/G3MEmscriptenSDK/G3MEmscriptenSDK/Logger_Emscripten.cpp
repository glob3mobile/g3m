

#include "Logger_Emscripten.hpp"

#include <emscripten/emscripten.h>


void Logger_Emscripten::logInfo(const std::string x, ...) const {
  if (_level <= InfoLevel) {
    va_list args;
    va_start(args, x);
    emscripten_log(EM_LOG_CONSOLE, x.c_str(), args);
    va_end(args);
  }
}

void Logger_Emscripten::logWarning(const std::string x, ...) const {
  if (_level <= WarningLevel) {
    va_list args;
    va_start(args, x);
    emscripten_log(EM_LOG_CONSOLE | EM_LOG_WARN, x.c_str(), args);
    va_end(args);
  }
}

void Logger_Emscripten::logError(const std::string x, ...) const {
  if (_level <= ErrorLevel) {
    va_list args;
    va_start(args, x);
    emscripten_log(EM_LOG_CONSOLE | EM_LOG_ERROR, x.c_str(), args);
    va_end(args);
  }
}
