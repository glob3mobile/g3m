//
//  GFont.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/10/13.
//
//

#include "GFont.hpp"

#include "IStringBuilder.hpp"

const std::string GFont::SERIF      = "serif";
const std::string GFont::SANS_SERIF = "sans-serif";
const std::string GFont::MONOSPACED = "monospaced";


bool GFont::isSerif() const {
  return ( _name.compare(GFont::SERIF) == 0 );
}

bool GFont::isSansSerif() const {
  return ( _name.compare(GFont::SANS_SERIF) == 0 );
}

bool GFont::isMonospaced() const {
  return ( _name.compare(GFont::MONOSPACED) == 0 );
}

const std::string GFont::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("(GFont name=\"");
  isb->addString(_name);
  isb->addString("\", size=");
  isb->addFloat(_size);
  if (_bold) {
    isb->addString(", bold");
  }
  if (_italic) {
    isb->addString(", italic");
  }
  isb->addString(")");
  const std::string s = isb->getString();
  delete isb;
  return s;
}
