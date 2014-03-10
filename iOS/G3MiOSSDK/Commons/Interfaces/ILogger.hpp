//
//  ILogger.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_ILogger
#define G3MiOSSDK_ILogger


#include <string>


enum LogLevel {
  SilenceLevel,
  InfoLevel,
  WarningLevel,
  ErrorLevel
};


class ILogger {
protected:
  const LogLevel _level;
  
  ILogger(const LogLevel level): _level(level) {}

  static ILogger* _instance;

public:
  static void setInstance(ILogger* logger) {
    if (_instance != NULL) {
      ILogger::instance()->logWarning("ILooger instance already set!");
      delete _instance;
    }
    _instance = logger;
  }
  
  static ILogger* instance() {
    return _instance;
  }
  
  
  virtual void logInfo   (const std::string x, ...) const = 0;
  virtual void logWarning(const std::string x, ...) const = 0;
  virtual void logError  (const std::string x, ...) const = 0;
  
  virtual ~ILogger() {
  }
  
};


#endif
