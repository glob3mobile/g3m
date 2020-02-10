

#include "StringUtils_Emscripten.hpp"

#include <sstream>


std::string StringUtils_Emscripten::createString(unsigned char* data,
                                                 size_t         length) const {
  unsigned char* cStr = new unsigned char[length + 1];
  memcpy(cStr, data, length * sizeof(unsigned char));
  cStr[length] = 0;

  return (char*) cStr;
}

std::vector<std::string> StringUtils_Emscripten::splitLines(const std::string& string) const {
  std::stringstream ss(string);
  std::istream_iterator<std::string> begin(ss);
  std::istream_iterator<std::string> end;
  std::vector<std::string> result(begin, end);

  return result;
}

bool StringUtils_Emscripten::beginsWith(const std::string& string,
                                        const std::string& prefix) const {
  return string.compare(0, prefix.size(), prefix) == 0;
}

int StringUtils_Emscripten::indexOf(const std::string& string,
                                    const std::string& search) const {
  const size_t pos = string.find(search);
  if (pos == std::string::npos) {
    return -1;
  }
  return pos;
}

int StringUtils_Emscripten::indexOf(const std::string& string,
                                    const std::string& search,
                                    size_t fromIndex) const {
  const size_t pos = string.find(search, fromIndex);
  if (pos == std::string::npos) {
    return -1;
  }
  return pos;
}

int StringUtils_Emscripten::indexOf(const std::string& string,
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

std::string StringUtils_Emscripten::substring(const std::string& string,
                                              size_t beginIndex,
                                              size_t endIndex) const {
  return string.substr(beginIndex, endIndex - beginIndex);
}

std::string StringUtils_Emscripten::ltrim(const std::string& string) const {
  std::string s = string;
  s.erase(s.begin(),
          std::find_if(s.begin(),
                       s.end(),
                       [](int c) { return !std::isspace(c); }
                       )
          );
  return s;
}

std::string StringUtils_Emscripten::rtrim(const std::string& string) const {
  std::string s = string;
  s.erase(std::find_if(s.rbegin(),
                       s.rend(),
                       [](int c) { return !std::isspace(c); }
                       ).base(),
          s.end());
  return s;
}

bool StringUtils_Emscripten::endsWith(const std::string& string,
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

std::string StringUtils_Emscripten::toUpperCase(const std::string& string) const {
  std::string result = string;
  std::transform(result.begin(), result.end(), result.begin(), ::toupper);

  return result;
}

std::string StringUtils_Emscripten::toLowerCase(const std::string& string) const {
  std::string result = string;
  std::transform(result.begin(), result.end(), result.begin(), ::tolower);

  return result;
}

long long StringUtils_Emscripten::parseHexInt(const std::string& str) const {
  long long result;
  std::stringstream ss;
  ss << std::hex << str;
  ss >> result;

  return result;
}

int StringUtils_Emscripten::indexOfFirstNonBlank(const std::string& string,
                                                 size_t fromIndex) const {
  const size_t stringLen = string.length();
  for (size_t i = fromIndex ; i < stringLen; i++) {
    if (!std::isspace( string[i] )) {
      return i;
    }
  }
  return -1;
}

int StringUtils_Emscripten::indexOfFirstNonChar(const std::string& string,
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

std::string StringUtils_Emscripten::toString(int value) const {
  std::stringstream ss;
  ss << value;
  return ss.str();
}

std::string StringUtils_Emscripten::toString(long long value) const {
  std::stringstream ss;
  ss << value;
  return ss.str();
}

std::string StringUtils_Emscripten::toString(double value) const {
  std::stringstream ss;
  ss << value;
  return ss.str();
}

std::string StringUtils_Emscripten::toString(float value) const {
  std::stringstream ss;
  ss << value;
  return ss.str();
}

double StringUtils_Emscripten::parseDouble(const std::string& str) const {
  return atof(str.c_str());
}

std::string StringUtils_Emscripten::replaceAll(const std::string& originalString,
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

std::string StringUtils_Emscripten::capitalize(const std::string& string) const {
  std::string str = string;
  std::use_facet<std::ctype<char>>(std::locale()).toupper(&str[0], &str[0] + str.size());
  return str;
}
