//
//  ILogger.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_ILogger_h
#define G3MiOSSDK_ILogger_h

enum LogLevel {
    InfoLevel,
    WarningLevel,
    ErrorLevel
};


class ILogger {
    
public:
    ILogger(LogLevel level): _level(level) {}
    
    virtual void logInfo(std::string x, ...) = 0;
    virtual void logWarning(std::string x, ...) = 0;
    virtual void logError(std::string x, ...) = 0;
    
private:
    LogLevel _level;
};


#endif
