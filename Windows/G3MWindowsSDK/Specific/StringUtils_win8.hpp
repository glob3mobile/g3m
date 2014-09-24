//
//  StringUtils_win8.hpp
//  G3MWindowsSDK
//
//  Created by Oliver Koehler on 30/05/14.
//
//

#ifndef __G3MWindowsSDK__StringUtils_win8__
#define __G3MWindowsSDK__StringUtils_win8__

#include "IStringUtils.hpp"

class StringUtils_win8 : public IStringUtils{
public:  
	std::string createString(unsigned char data[], int length) const;

	std::vector<std::string> splitLines(const std::string& string) const;

	bool beginsWith(const std::string& string, const std::string& prefix) const;

	int indexOf(const std::string& string, const std::string& search) const;

	int indexOf(const std::string& string, const std::string& search, int fromIndex) const;

	int indexOf(const std::string& string, const std::string& search, int fromIndex, int endIndex) const;

	std::string substring(const std::string& string, int beginIndex, int endIndex) const;

	std::string rtrim(const std::string& string) const;

	std::string ltrim(const std::string& string) const;

	bool endsWith(const std::string& string, const std::string& suffix) const;

	std::string toUpperCase(const std::string& string) const;

	long long parseHexInt(const std::string& str) const;

	int indexOfFirstNonBlank(const std::string& string, int fromIndex) const;

	int indexOfFirstNonChar(const std::string& string, const std::string& chars, int fromIndex) const;

	std::string toString(int value) const;

	std::string toString(long long value) const;

	std::string toString(double value) const;

	double parseDouble(const std::string& str) const;

	Platform::String^ toStringHat(std::string str) const;

	std::string toStringStd(Platform::String^ strhat) const;
};


#endif