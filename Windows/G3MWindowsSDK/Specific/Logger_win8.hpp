//
//  Logger_win8.hpp
//  G3MWindowsSDK
//
//  Created by Oliver Koehler on 30/05/14.
//

#ifndef __G3MWindowsSDK_Logger_win8__
#define __G3MWindowsSDK_Logger_win8__

#include "ILogger.hpp"

class Logger_win8 :public ILogger {
public:
	Logger_win8(const LogLevel level) : ILogger(level){}

	void logInfo(const std::string x, ...) const;
	void logWarning(const std::string x, ...) const;
	void logError(const std::string x, ...) const;

	
};

#endif