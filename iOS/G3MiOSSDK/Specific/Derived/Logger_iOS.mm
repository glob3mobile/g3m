//
//  Logger_iOS.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 31/05/12.
//

#include "Logger_iOS.hpp"


void Logger_iOS::logInfo(const std::string x, ...) const {
  if (_level <= InfoLevel) {
    NSString* nsX = [ @"" stringByAppendingString: [NSString stringWithUTF8String: x.c_str()] ];
    va_list args;
    va_start(args, x);
    NSLogv(nsX, args);
    va_end(args);
  }
}

void Logger_iOS::logWarning(const std::string x, ...) const {
  if (_level <= WarningLevel) {
    NSString* nsX = [ @"Warning: " stringByAppendingString: [NSString stringWithUTF8String: x.c_str()] ];
    va_list args;
    va_start(args, x);
    NSLogv(nsX, args);
    va_end(args);
  }
}

void Logger_iOS::logError(const std::string x, ...) const {
  if (_level <= ErrorLevel) {
    NSString* nsX = [ @"ERROR: " stringByAppendingString: [NSString stringWithUTF8String: x.c_str()] ];
    va_list args;
    va_start(args, x);
    NSLogv(nsX, args);
    va_end(args);
  }
}
