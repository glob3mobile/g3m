//
//  Logger_iOS.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 31/05/12.
//

#ifndef G3MiOSSDK_Logger_iOS
#define G3MiOSSDK_Logger_iOS

#include "G3MSharedSDK/ILogger.hpp"


class Logger_iOS: public ILogger {
public:
  Logger_iOS(const LogLevel level) :
  ILogger(level)
  {
  }
  
  void logInfo   (const std::string x, ...) const;
  void logWarning(const std::string x, ...) const;
  void logError  (const std::string x, ...) const;
};

#endif
