//
//  Logger_iOS.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "Logger_iOS.hpp"

void Logger_iOS::log(const std::string label, const std::string x, ...) const
{
  printf("%s: ", label.c_str());
  va_list args;
  va_start (args, x);
  vprintf(x.c_str(), args);
  va_end(args);
  printf("\n");
}


void Logger_iOS::logInfo(const std::string x, ...) const
{
  if (_level >= InfoLevel) {
    log("Info", x);
  }
}

void Logger_iOS::logWarning(const std::string x, ...) const
{
  if (_level >= WarningLevel) {
    log("Warning", x);
  }    
}

void Logger_iOS::logError(const std::string x, ...) const
{
  if (_level >= ErrorLevel) {
    log("Error", x);
  }
}
