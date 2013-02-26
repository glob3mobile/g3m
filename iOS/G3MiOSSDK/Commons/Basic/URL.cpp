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
  IStringBuilder *isb = IStringBuilder::newStringBuilder();
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
//    std::string escapedURL = IStringUtils::instance()->replaceSubstring(path, "%", "%25");
    std::string escapedURL = IStringUtils::instance()->replaceSubstring(path, "\n", "%0A");
    escapedURL = IStringUtils::instance()->replaceSubstring(escapedURL, " ", "%20");
    escapedURL = IStringUtils::instance()->replaceSubstring(escapedURL, "\"", "%22");
    escapedURL = IStringUtils::instance()->replaceSubstring(escapedURL, "-", "%2D");
    escapedURL = IStringUtils::instance()->replaceSubstring(escapedURL, ".", "%2E");
    escapedURL = IStringUtils::instance()->replaceSubstring(escapedURL, "<", "%3C");
    escapedURL = IStringUtils::instance()->replaceSubstring(escapedURL, ">", "%3E");
    escapedURL = IStringUtils::instance()->replaceSubstring(escapedURL, "\\", "%5C");
    escapedURL = IStringUtils::instance()->replaceSubstring(escapedURL, "^", "%5E");
    escapedURL = IStringUtils::instance()->replaceSubstring(escapedURL, "_", "%5F");
    escapedURL = IStringUtils::instance()->replaceSubstring(escapedURL, "`", "%60");
    escapedURL = IStringUtils::instance()->replaceSubstring(escapedURL, "{", "%7B");
    escapedURL = IStringUtils::instance()->replaceSubstring(escapedURL, "|", "%7C");
    escapedURL = IStringUtils::instance()->replaceSubstring(escapedURL, "}", "%7D");
    escapedURL = IStringUtils::instance()->replaceSubstring(escapedURL, "~", "%7E");
    
    return escapedURL;
}
