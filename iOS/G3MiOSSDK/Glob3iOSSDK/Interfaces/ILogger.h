//
//  ILogger.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_ILogger_h
#define G3MiOSSDK_ILogger_h


#include <string>

enum LogLevel {
  InfoLevel,
  WarningLevel,
  ErrorLevel
};


class ILogger {

public:
    virtual void logInfo(const std::string x, ...) = 0;
    virtual void logWarning(const std::string x, ...) = 0;
    virtual void logError(const std::string x, ...) = 0;
};


#endif
