//
//  StringUtils_iOS.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 23/08/12.
//
//

#include "StringUtils_iOS.hpp"
#include <algorithm>
#include <sstream>

#import "NSString_CppAdditions.h"

std::string StringUtils_iOS::createString(unsigned char* data,
                                          int            length) const {
  unsigned char* cStr = new unsigned char[length + 1];
  memcpy(cStr, data, length * sizeof(unsigned char));
  cStr[length] = 0;

  return (char*) cStr;
}

std::vector<std::string> StringUtils_iOS::splitLines(const std::string& string) const {
  NSString* nsString = [NSString stringWithCppString: string];

  NSArray* nsLines = [nsString componentsSeparatedByString:@"\n"];


  std::vector<std::string> lines;

  for (NSString* line in nsLines) {
    lines.push_back( [line cStringUsingEncoding:NSUTF8StringEncoding]  );
  }

  return lines;
}

bool StringUtils_iOS::beginsWith(const std::string& string,
                                 const std::string& prefix) const {
  return string.compare(0, prefix.size(), prefix) == 0;
}

int StringUtils_iOS::indexOf(const std::string& string,
                             const std::string& search) const {
  const int pos = string.find(search);
  if (pos == std::string::npos) {
    return -1;
  }
  return pos;
}

int StringUtils_iOS::indexOf(const std::string& string,
                             const std::string& search,
                             int fromIndex) const {
  const int pos = string.find(search, fromIndex);
  if (pos == std::string::npos) {
    return -1;
  }
  return pos;
}

int StringUtils_iOS::indexOf(const std::string& string,
                             const std::string& search,
                             int fromIndex,
                             int endIndex) const {
  const int pos = string.find(search, fromIndex);
  if ((pos == std::string::npos) ||
      (pos > endIndex)) {
    return -1;
  }
  return pos;
}

std::string StringUtils_iOS::substring(const std::string& string,
                                       int beginIndex,
                                       int endIndex) const {
  return string.substr(beginIndex, endIndex - beginIndex);
}

std::string StringUtils_iOS::ltrim(const std::string& string) const {
  std::string s = string;
  s.erase(s.begin(),
          std::find_if(s.begin(),
                       s.end(),
                       std::not1(std::ptr_fun<int, int>(std::isspace))
                       )
          );
  return s;
}

std::string StringUtils_iOS::rtrim(const std::string& string) const {
  std::string s = string;
  s.erase(std::find_if(s.rbegin(),
                       s.rend(),
                       std::not1(std::ptr_fun<int, int>(std::isspace))
                       ).base(),
          s.end());
  return s;
}

bool StringUtils_iOS::endsWith(const std::string& string,
                               const std::string& suffix) const {
  const int stringLength = string.length();
  const int suffixLength = suffix.length();
  if (stringLength >= suffixLength) {
    return (string.compare(stringLength - suffixLength, suffixLength, suffix) == 0);
  }
  else {
    return false;
  }
}

std::string StringUtils_iOS::toUpperCase(const std::string& string) const {
  std::string result = string;
  std::transform(result.begin(), result.end(), result.begin(), ::toupper);

  return result;
}

long long StringUtils_iOS::parseHexInt(const std::string& str) const {
  long long result;
  std::stringstream ss;
  ss << std::hex << str;
  ss >> result;

  return result;
}

int StringUtils_iOS::indexOfFirstNonBlank(const std::string& string,
                                          int fromIndex) const {
  const int stringLen = string.length();
  for (int i = fromIndex ; i < stringLen; i++) {
    if (!std::isspace( string[i] )) {
      return i;
    }
  }
  return -1;
}

//int StringUtils_iOS::indexOfFirstBlank(const std::string& string,
//                                       int fromIndex) const {
//  const int stringLen = string.length();
//  for (int i = fromIndex ; i < stringLen; i++) {
//    if (std::isspace( string[i] )) {
//      return i;
//    }
//  }
//  return -1;
//}

int StringUtils_iOS::indexOfFirstNonChar(const std::string& string,
                                         const std::string& chars,
                                         int fromIndex) const {
  const int stringLen = string.length();
  for (int i = fromIndex ; i < stringLen; i++) {
    if (chars.find(string[i]) != std::string::npos) {
      return i;
    }
  }
  return -1;
}

std::string StringUtils_iOS::toString(int value) const {
  std::stringstream ss;
  ss << value;
  return ss.str();
}

std::string StringUtils_iOS::toString(long long value) const {
  std::stringstream ss;
  ss << value;
  return ss.str();
}

std::string StringUtils_iOS::toString(double value) const {
  std::stringstream ss;
  ss << value;
  return ss.str();
}

double StringUtils_iOS::parseDouble(const std::string& str) const {
  return atof(str.c_str());
}
