//
//  StringUtils_iOS.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 23/08/12.
//
//

#ifndef __G3MiOSSDK__StringUtils_iOS__
#define __G3MiOSSDK__StringUtils_iOS__

#include "IStringUtils.hpp"

class StringUtils_iOS : public IStringUtils {
public:
  std::string createString(unsigned char data[],
                           int            length) const;

  std::vector<std::string> splitLines(const std::string& string) const;

  bool beginsWith(const std::string& string,
                  const std::string& prefix) const;


  size_t indexOf(const std::string& string,
                 const std::string& search) const;

  size_t indexOf(const std::string& string,
                 const std::string& search,
                 size_t fromIndex) const;

  size_t indexOf(const std::string& string,
                 const std::string& search,
                 size_t fromIndex,
                 size_t endIndex) const;


  std::string substring(const std::string& string,
                        size_t beginIndex,
                        size_t endIndex) const;

  std::string rtrim(const std::string& string) const;

  std::string ltrim(const std::string& string) const;


  bool endsWith(const std::string& string,
                const std::string& suffix) const;

  std::string toUpperCase(const std::string& string) const;

  long long parseHexInt(const std::string& str) const;

  size_t indexOfFirstNonBlank(const std::string& string,
                              size_t fromIndex) const;

  //  size_t indexOfFirstBlank(const std::string& string,
  //                        size_t fromIndex) const;

  size_t indexOfFirstNonChar(const std::string& string,
                             const std::string& chars,
                             size_t fromIndex) const;

  std::string toString(int value) const;

  std::string toString(long long value) const;

  std::string toString(double value) const;

  std::string toString(float value) const;
  
  double parseDouble(const std::string& str) const;
  
};

#endif
