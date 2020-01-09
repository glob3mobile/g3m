
#ifndef Logger_Emscripten_hpp
#define Logger_Emscripten_hpp

#include "ILogger.hpp"


class Logger_Emscripten : public ILogger {
public:
  Logger_Emscripten(const LogLevel level) :
  ILogger(level)
  {
  }

  void logInfo   (const std::string x, ...) const;
  void logWarning(const std::string x, ...) const;
  void logError  (const std::string x, ...) const;

};

#endif
