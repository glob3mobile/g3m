//
//  IStringUtils.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 23/08/12.
//
//

#ifndef __G3MiOSSDK__IStringUtils__
#define __G3MiOSSDK__IStringUtils__

#include <vector>
#include <string>

#include "ILogger.hpp"

class IStringUtils {
private:
#ifdef C_CODE
  static const IStringUtils* _instance;
#else
  static IStringUtils* _instance;
#endif

public:
  static void setInstance(const IStringUtils* instance) {
    if (_instance != NULL) {
      ILogger::instance()->logWarning("IStringUtils instance already set!");
      delete _instance;
    }
    _instance = instance;
  }

  static const IStringUtils* instance() {
    return _instance;
  }

  virtual ~IStringUtils() {
  }

  virtual std::string createString(unsigned char data[],
                                   int           length) const = 0;

  virtual std::vector<std::string> splitLines(const std::string& string) const = 0;

  virtual bool beginsWith(const std::string& string,
                          const std::string& prefix) const = 0;

  virtual bool endsWith(const std::string& string,
                        const std::string& suffix) const = 0;

  virtual std::string toUpperCase(const std::string& string) const = 0;


  virtual int indexOf(const std::string& string,
                      const std::string& search) const = 0;

  virtual int indexOf(const std::string& string,
                      const std::string& search,
                      int fromIndex) const = 0;

  virtual int indexOf(const std::string& string,
                      const std::string& search,
                      int fromIndex,
                      int endIndex) const = 0;

  virtual int indexOfFirstNonBlank(const std::string& string,
                                   int fromIndex) const = 0;

  //  virtual int indexOfFirstBlank(const std::string& string,
  //                                int fromIndex) const = 0;

  virtual int indexOfFirstNonChar(const std::string& string,
                                  const std::string& chars,
                                  int fromIndex) const = 0;

  /*
   Returns a new string that is a substring of this string. The substring begins at the
   specified beginIndex and extends to the character at index endIndex - 1. Thus the length
   of the substring is endIndex-beginIndex.
   */
  virtual std::string substring(const std::string& string,
                                int beginIndex,
                                int endIndex) const = 0;

  virtual std::string substring(const std::string& string,
                                int beginIndex) const;

  virtual std::string replaceAll(const std::string& originalString,
                                 const std::string& searchString,
                                 const std::string& replaceString) const = 0;

  virtual std::string left(const std::string& string,
                           int endIndex) const {
    return substring(string, 0, endIndex);
  }

  virtual std::string rtrim(const std::string& string) const = 0;

  virtual std::string ltrim(const std::string& string) const = 0;

  virtual std::string trim(const std::string& string) const {
    return rtrim(ltrim(string));
  }

  virtual long long parseHexInt(const std::string& str) const = 0;

  virtual std::string toString(int value) const = 0;

  virtual std::string toString(long long value) const = 0;

  virtual std::string toString(double value) const = 0;

  virtual std::string toString(float value) const = 0;

  virtual double parseDouble(const std::string& str) const = 0;
  
};

#endif
