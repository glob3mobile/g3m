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
			OutputDebugString(L"Info: ");

			va_list marker;
			char szBuf[256];

			va_start(marker, x);
			vsprintf_s(szBuf, x.c_str(), marker);
			va_end(marker);

			OutputDebugStringA(szBuf);
			OutputDebugString(TEXT("\r\n"));
	}

}

void Logger_win8::logWarning(const std::string x, ...) const {
	if (_level <= WarningLevel) {
		OutputDebugString(L"Warning: ");
		va_list marker;
		char szBuf[256];

		va_start(marker, x);
		vsprintf_s(szBuf, x.c_str(), marker);
		va_end(marker);

		OutputDebugStringA(szBuf);
		OutputDebugString(TEXT("\r\n"));
	}

}

void Logger_win8::logError(const std::string x, ...) const {
	if (_level <= ErrorLevel) {
		OutputDebugString(L"Error: ");

		va_list marker;
		char szBuf[256];

		va_start(marker, x);
		vsprintf_s(szBuf, x.c_str(), marker);
		va_end(marker);

		OutputDebugStringA(szBuf);
		OutputDebugString(TEXT("\r\n"));


		//OutputDebugStringA(x.c_str());
		//OutputDebugStringA("\n");

		
	}
}

