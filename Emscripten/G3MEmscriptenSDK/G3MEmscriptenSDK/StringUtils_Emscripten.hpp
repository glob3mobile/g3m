
#ifndef StringUtils_Emscripten_hpp
#define StringUtils_Emscripten_hpp

#include "IStringUtils.hpp"



class StringUtils_Emscripten : public IStringUtils {
public:

  std::string createString(unsigned char data[],
                           size_t        length) const;

  std::vector<std::string> splitLines(const std::string& string) const;

  bool beginsWith(const std::string& string,
                  const std::string& prefix) const;


  int indexOf(const std::string& string,
              const std::string& search) const;

  int indexOf(const std::string& string,
              const std::string& search,
              size_t fromIndex) const;

  int indexOf(const std::string& string,
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

  std::string toLowerCase(const std::string& string) const;

  long long parseHexInt(const std::string& str) const;

  int indexOfFirstNonBlank(const std::string& string,
                           size_t fromIndex) const;

  int indexOfFirstNonChar(const std::string& string,
                          const std::string& chars,
                          size_t fromIndex) const;

  std::string toString(int value) const;

  std::string toString(long long value) const;

  std::string toString(double value) const;

  std::string toString(float value) const;

  double parseDouble(const std::string& str) const;


  std::string replaceAll(const std::string& originalString,
                         const std::string& searchString,
                         const std::string& replaceString) const;

  std::string capitalize(const std::string& string) const;

};

#endif
