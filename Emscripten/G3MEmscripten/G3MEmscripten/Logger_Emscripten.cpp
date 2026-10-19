

#include "Logger_Emscripten.hpp"

#include <stdarg.h>
#include <stdio.h>
#include <string>
#include <vector>

#include <emscripten/emscripten.h>


static std::string format(const std::string& format,
                          va_list args) {
  va_list argsForLength;
  va_copy(argsForLength, args);
  const int length = vsnprintf(NULL, 0, format.c_str(), argsForLength);
  va_end(argsForLength);

  if (length < 0) {
    return "Logger_Emscripten: invalid format \"" + format + "\"";
  }

  std::vector<char> buffer(length + 1);
  vsnprintf(buffer.data(), buffer.size(), format.c_str(), args);
  return std::string(buffer.data(), length);
}


void Logger_Emscripten::logInfo(const std::string x, ...) const {
  if (_level <= InfoLevel) {
    va_list args;
    va_start(args, x);
    emscripten_log(EM_LOG_CONSOLE, "%s", format(x, args).c_str());
    va_end(args);
  }
}

void Logger_Emscripten::logWarning(const std::string x, ...) const {
  if (_level <= WarningLevel) {
    va_list args;
    va_start(args, x);
    emscripten_log(EM_LOG_CONSOLE | EM_LOG_WARN, "%s", format(x, args).c_str());
    va_end(args);
  }
}

void Logger_Emscripten::logError(const std::string x, ...) const {
  if (_level <= ErrorLevel) {
    va_list args;
    va_start(args, x);
    emscripten_log(EM_LOG_CONSOLE | EM_LOG_ERROR, "%s", format(x, args).c_str());
    va_end(args);
  }
}
