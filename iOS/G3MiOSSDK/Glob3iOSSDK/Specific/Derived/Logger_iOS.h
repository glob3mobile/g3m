//
//  Logger_iOS.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_Logger_iOS_h
#define G3MiOSSDK_Logger_iOS_h

#include "ILogger.h"


class Logger_iOS: public ILogger {
    
public:
    void logInfo(const std::string x, ...);
    void logWarning(const std::string x, ...);
    void logError(const std::string x, ...);

private:
    void print(const std::string x, ...);

};

#endif
