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
private:
  
  static NSString* toNSString(const std::string& cppStr) {
    return [ NSString stringWithCString: cppStr.c_str()
                               encoding: NSUTF8StringEncoding ];
  }

public:
  std::string createString(unsigned char data[],
                           int            length) const;
  
  std::vector<std::string> splitLines(const std::string& string) const;
  
  bool beginsWith(const std::string& string,
                  const std::string& prefix) const;
  
  int indexOf(const std::string& string,
              const std::string& search) const;
  
  std::string substring(const std::string& string,
                        int beginIndex,
                        int endIndex) const;
  
  std::string rtrim(const std::string& string) const;
  
  std::string ltrim(const std::string& string) const;


  bool endsWith(const std::string& string,
                const std::string& suffix) const;

  std::string toUpperCase(const std::string& string) const;

  long long parseHexInt(const std::string& str) const;

};

#endif
