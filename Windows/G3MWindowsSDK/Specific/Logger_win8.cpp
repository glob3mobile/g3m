//
//  Logger_win8.cpp
//  G3MWindowsSDK
//
//  Created by Oliver Koehler on 30/05/14.
//

#include "Logger_win8.hpp"
#include <Windows.h>

void Logger_win8::logInfo(const std::string x, ...) const {
	if (_level <= InfoLevel) {

		 //   printf("Info: ");
			//va_list args;
			//_crt_va_start(args, x);
		 //   vprintf(x.c_str(), args);
		 //   printf("\n");
			OutputDebugString(L"Info: ");
			OutputDebugStringA(x.c_str());
			OutputDebugStringA("\n");
	}

}

void Logger_win8::logWarning(const std::string x, ...) const {
	if (_level <= WarningLevel) {
		//printf("Warning: ");
		//va_list args;
		//_crt_va_start(args, x);
		//vprintf(x.c_str(), args);
		//printf("\n");
		OutputDebugString(L"Warning: ");
		OutputDebugStringA(x.c_str());
		OutputDebugStringA("\n");
	}

}

void Logger_win8::logError(const std::string x, ...) const {
	if (_level <= ErrorLevel) {
		OutputDebugString(L"Error: ");
		
		/*std::string temp = NULL;
		va_list args;

		va_start(args, x);
		temp += va_arg(args, std::string);
		while (temp != ""){
			buf += va_arg(args, std::string);
			temp = va_arg(args, std::string);
		}		
		va_end(args);*/

		OutputDebugStringA(x.c_str());
		OutputDebugStringA("\n");

		
	}
}

