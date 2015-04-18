//
//  Info.cpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 02/09/14.
//
//

#include "Info.hpp"


Info::Info(const std::string& text) :
_text(text)
{
}

Info::~Info() {
}

const std::string Info::getText() const {
  return _text;
}
