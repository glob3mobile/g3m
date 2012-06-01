//
//  Logger_iOS.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_Logger_iOS_h
#define G3MiOSSDK_Logger_iOS_h

#include "ILogger.hpp"


class Logger_iOS: public ILogger {
private:
  void log(const std::string label, const std::string x, ...) const;
  
public:  
  Logger_iOS(const LogLevel level): ILogger(level) { }
  
  void logInfo(const std::string x, ...) const ;
  void logWarning(const std::string x, ...) const ;
  void logError(const std::string x, ...) const ;
};

#endif
