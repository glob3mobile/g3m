//
//  URL.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "URL.hpp"

#include "IStringBuilder.hpp"
#include "IStringUtils.hpp"

const std::string URL::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("URL(");
  isb->addString(getPath());
  isb->addString(")");
  const std::string s = isb->getString();
  delete isb;
  return s; 
}

const std::string URL::FILE_PROTOCOL = "file:///";

bool URL::isFileProtocol() const {
  return (IStringUtils::instance()->beginsWith(_path, FILE_PROTOCOL));
}

const std::string URL::escape(const std::string& path) {
  const IStringUtils* su = IStringUtils::instance();
  std::string result = su->replaceSubstring(path, "\n", "%0A");
  result = su->replaceSubstring(result, " ", "%20");
  result = su->replaceSubstring(result, "\"", "%22");
  result = su->replaceSubstring(result, "-", "%2D");
  result = su->replaceSubstring(result, ".", "%2E");
  result = su->replaceSubstring(result, "<", "%3C");
  result = su->replaceSubstring(result, ">", "%3E");
  result = su->replaceSubstring(result, "\\", "%5C");
  result = su->replaceSubstring(result, "^", "%5E");
  result = su->replaceSubstring(result, "_", "%5F");
  result = su->replaceSubstring(result, "`", "%60");
  result = su->replaceSubstring(result, "{", "%7B");
  result = su->replaceSubstring(result, "|", "%7C");
  result = su->replaceSubstring(result, "}", "%7D");
  result = su->replaceSubstring(result, "~", "%7E");
  return result;
}
