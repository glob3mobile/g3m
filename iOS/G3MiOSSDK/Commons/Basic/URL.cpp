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
  isb->addString(_path);
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
  std::string result = su->replaceAll(path, "\n", "%0A");
  result = su->replaceAll(result, " ", "%20");
  result = su->replaceAll(result, "\"", "%22");
  result = su->replaceAll(result, "-", "%2D");
  result = su->replaceAll(result, ".", "%2E");
  result = su->replaceAll(result, "<", "%3C");
  result = su->replaceAll(result, ">", "%3E");
  result = su->replaceAll(result, "\\", "%5C");
  result = su->replaceAll(result, "^", "%5E");
  result = su->replaceAll(result, "_", "%5F");
  result = su->replaceAll(result, "`", "%60");
  result = su->replaceAll(result, "{", "%7B");
  result = su->replaceAll(result, "|", "%7C");
  result = su->replaceAll(result, "}", "%7D");
  result = su->replaceAll(result, "~", "%7E");
  return result;
}
