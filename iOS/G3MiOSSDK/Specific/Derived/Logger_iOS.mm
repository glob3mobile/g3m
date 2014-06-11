//
//  Logger_iOS.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "Logger_iOS.hpp"


void Logger_iOS::logInfo(const std::string x, ...) const {
  if (_level <= InfoLevel) {
//    printf("Info: ");
//    va_list args;
//    va_start(args, x);
//    vprintf(x.c_str(), args);
//    va_end(args);
//    printf("\n");

    NSString* nsX = [ @"Info: " stringByAppendingString: [NSString stringWithUTF8String: x.c_str()] ];
    va_list args;
    va_start(args, x);
    NSLogv(nsX, args);
    va_end(args);
  }
}

void Logger_iOS::logWarning(const std::string x, ...) const {
  if (_level <= WarningLevel) {
//    printf("Warning: ");
//    va_list args;
//    va_start(args, x);
//    vprintf(x.c_str(), args);
//    va_end(args);
//    printf("\n");

    NSString* nsX = [ @"Warning: " stringByAppendingString: [NSString stringWithUTF8String: x.c_str()] ];
    va_list args;
    va_start(args, x);
    NSLogv(nsX, args);
    va_end(args);
  }
}

void Logger_iOS::logError(const std::string x, ...) const {
  if (_level <= ErrorLevel) {
//    printf("ERROR: ");
//    va_list args;
//    va_start(args, x);
//    vprintf(x.c_str(), args);
//    va_end(args);
//    printf("\n");

    NSString* nsX = [ @"ERROR: " stringByAppendingString: [NSString stringWithUTF8String: x.c_str()] ];
    va_list args;
    va_start(args, x);
    NSLogv(nsX, args);
    va_end(args);
  }
}
