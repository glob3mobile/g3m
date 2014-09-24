//
//  StringUtils_win8.cpp
//  G3MWindowsSDK
//
//  Created by Oliver Koehler on 30/05/14.
//
//

#include "StringUtils_win8.hpp"
#include <algorithm>
#include <sstream>
#include <locale>



std::string StringUtils_win8::createString(unsigned char data[], int length) const{
	unsigned char* cStr = new unsigned char[length + 1];
	memcpy(cStr, data, length * sizeof(unsigned char));
	cStr[length] = 0;
	return (char*)cStr;
}

std::vector<std::string> StringUtils_win8::splitLines(const std::string& string) const{

	return std::vector<std::string>();
}

bool StringUtils_win8::beginsWith(const std::string& string, const std::string& prefix) const{
	return string.compare(0, prefix.size(), prefix) == 0;
}

int StringUtils_win8::indexOf(const std::string& string, const std::string& search) const {
	const int pos = string.find(search);
	if (pos == std::string::npos) {
		return -1;
	}
	return pos;
}

int StringUtils_win8::indexOf(const std::string& string, const std::string& search, int fromIndex) const{
	const int pos = string.find(search, fromIndex);
	if (pos == std::string::npos) {
		return -1;
	}
	return pos;
}

int StringUtils_win8::indexOf(const std::string& string, const std::string& search, int fromIndex, int endIndex) const{
	const int pos = string.find(search, fromIndex);
	if ((pos == std::string::npos) ||
		(pos > endIndex)) {
		return -1;
	}
	return pos;
}

std::string StringUtils_win8::substring(const std::string& string, int beginIndex, int endIndex) const{
	return string.substr(beginIndex, endIndex - beginIndex);
}

std::string StringUtils_win8::rtrim(const std::string& string) const{


	return NULL;
}

std::string StringUtils_win8::ltrim(const std::string& string) const{
	
	return NULL;
}

bool StringUtils_win8::endsWith(const std::string& string, const std::string& suffix) const{
	const int stringLength = string.length();
	const int suffixLength = suffix.length();
	if (stringLength >= suffixLength) {
		return (string.compare(stringLength - suffixLength, suffixLength, suffix) == 0);
	}
	else {
		return false;
	}
}

std::string StringUtils_win8::toUpperCase(const std::string& string) const{
	std::string result = string;
	std::transform(result.begin(), result.end(), result.begin(), ::toupper);

	return result;
}

long long StringUtils_win8::parseHexInt(const std::string& str) const{
	long long result;
	std::stringstream ss;
	ss << std::hex << str;
	ss >> result;

	return result;
}

int StringUtils_win8::indexOfFirstNonBlank(const std::string& string, int fromIndex) const{
	const int stringLen = string.length();
	for (int i = fromIndex; i < stringLen; i++) {
		if (!isspace(string[i])) {
			return i;
		}
	}
	return -1;
}

int StringUtils_win8::indexOfFirstNonChar(const std::string& string, const std::string& chars, int fromIndex) const{
	const int stringLen = string.length();
	for (int i = fromIndex; i < stringLen; i++) {
		if (chars.find(string[i]) != std::string::npos) {
			return i;
		}
	}
	return -1;
}

std::string StringUtils_win8::toString(int value) const {
	std::stringstream ss;
	ss << value;
	return ss.str();
}

std::string StringUtils_win8::toString(long long value) const {
	std::stringstream ss;
	ss << value;
	return ss.str();
}

std::string StringUtils_win8::toString(double value) const {
	std::stringstream ss;
	ss << value;
	return ss.str();
}

double StringUtils_win8::parseDouble(const std::string& str) const{
	return atof(str.c_str());
}

Platform::String^ StringUtils_win8::toStringHat(std::string str) const{
	std::wstring wid_str = std::wstring(str.begin(), str.end());
	const wchar_t* w_char = wid_str.c_str();
	Platform::String^ p_string = ref new Platform::String(w_char);
	return p_string;
}

std::string StringUtils_win8::toStringStd(Platform::String^ strhat) const{
	std::wstring wstr(strhat->Data());
	std::string stdStr(wstr.begin(), wstr.end());
	return stdStr;
}
