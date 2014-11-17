//
//  IJSONParser.cpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 02/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "IJSONParser.hpp"
#include "IStringUtils.hpp"

IJSONParser* IJSONParser::_instance = NULL;

const std::string IJSONParser::escapeHtmlText(const std::string &text) {
  const IStringUtils* su = IStringUtils::instance();
  std::string result = su->replaceSubstring(text, "/", "\\/");
  result = su->replaceSubstring(result, "\"", "\\\"");
  result = su->replaceSubstring(result, "\n", "");
  result = su->replaceSubstring(result, "\t", "");
  result = su->replaceSubstring(result, "\r", "");



  
  return result;
}
