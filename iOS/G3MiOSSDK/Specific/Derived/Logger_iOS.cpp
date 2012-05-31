//
//  Logger_iOS.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "Logger_iOS.h"

void Logger_iOS::logInfo(const std::string x, ...)
{
  if (_level <= InfoLevel) {
    va_list args;
    va_start (args, x);
    vprintf(x.c_str(), args);
    va_end(args);
  }
}

void Logger_iOS::logWarning(const std::string x, ...)
{
  if (_level <= WarningLevel) {
    va_list args;
    va_start (args, x);
    vprintf(x.c_str(), args);
    va_end(args);
  }    
}

void Logger_iOS::logError(const std::string x, ...)
{
  if (_level <= ErrorLevel) {
    va_list args;
    va_start (args, x);
    vprintf(x.c_str(), args);
    va_end(args);
  }    
}

