//
//  IStringUtils.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 23/08/12.
//
//

#include "IStringUtils.hpp"

const IStringUtils* IStringUtils::_instance = NULL;


std::string IStringUtils::substring(const std::string& string,
                                    int beginIndex) const {
  return substring(string, beginIndex, string.size());
}
