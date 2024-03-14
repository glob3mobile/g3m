//
//  StringUtils_iOS.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 23/08/12.
//
//

#include "StringUtils_iOS.hpp"
#include <sstream>

#import "NSString_CppAdditions.h"

std::string StringUtils_iOS::createString(unsigned char* data,
                                          size_t         length) const {
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
  const size_t pos = string.find(search);
  if (pos == std::string::npos) {
    return -1;
  }
  return pos;
}

int StringUtils_iOS::indexOf(const std::string& string,
                             const std::string& search,
                             size_t fromIndex) const {
  const size_t pos = string.find(search, fromIndex);
  if (pos == std::string::npos) {
    return -1;
  }
  return pos;
}

int StringUtils_iOS::indexOf(const std::string& string,
                             const std::string& search,
                             size_t fromIndex,
                             size_t endIndex) const {
  const size_t pos = string.find(search, fromIndex);
  if ((pos == std::string::npos) ||
      (pos > endIndex)) {
    return -1;
  }
  return pos;
}

std::string StringUtils_iOS::substring(const std::string& string,
                                       size_t beginIndex,
                                       size_t endIndex) const {
  return string.substr(beginIndex, endIndex - beginIndex);
}

std::string StringUtils_iOS::ltrim(const std::string& string) const {
  std::size_t start;
  for (start = 0; start < string.length(); ++start) {
    if (!std::isspace(string[start])) {
      break;
    }
  }
  return string.substr(start);
}

std::string StringUtils_iOS::rtrim(const std::string& string) const {
  int end;
  for (end = string.length() - 1; end >= 0; --end) {
    if (!std::isspace(string[end])) {
      break;
    }
  }
  return string.substr(0, end + 1);
}

bool StringUtils_iOS::endsWith(const std::string& string,
                               const std::string& suffix) const {
  const size_t stringLength = string.length();
  const size_t suffixLength = suffix.length();
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

std::string StringUtils_iOS::toLowerCase(const std::string& string) const {
  std::string result = string;
  std::transform(result.begin(), result.end(), result.begin(), ::tolower);

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
                                          size_t fromIndex) const {
  const size_t stringLen = string.length();
  for (size_t i = fromIndex ; i < stringLen; i++) {
    if (!std::isspace( string[i] )) {
      return i;
    }
  }
  return -1;
}

int StringUtils_iOS::indexOfFirstNonChar(const std::string& string,
                                         const std::string& chars,
                                         size_t fromIndex) const {
  const size_t stringLen = string.length();
  for (size_t i = fromIndex ; i < stringLen; i++) {
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

std::string StringUtils_iOS::toString(float value) const {
  std::stringstream ss;
  ss << value;
  return ss.str();
}

double StringUtils_iOS::parseDouble(const std::string& str) const {
  return atof(str.c_str());
}


std::string StringUtils_iOS::replaceAll(const std::string& originalString,
                                        const std::string& searchString,
                                        const std::string& replaceString) const {
  std::string result = originalString;
  for ( size_t pos = 0; ; pos += replaceString.length() ) {
    // Locate the substring to replace
    pos = result.find( searchString, pos );
    if( pos == std::string::npos ) {
      break;
    }
    // Replace by erasing and inserting
    result.erase( pos, searchString.length() );
    result.insert( pos, replaceString );
  }
  return result;
}

std::string StringUtils_iOS::capitalize(const std::string& string) const {
  NSString* nsString = [NSString stringWithCppString: string];
  return std::string([[nsString capitalizedString] UTF8String]);
}
