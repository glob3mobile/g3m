//
//  GFont.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/10/13.
//
//

#include "GFont.hpp"

#include "IStringBuilder.hpp"
#include "IStringUtils.hpp"

const std::string GFont::SERIF      = "serif";
const std::string GFont::SANS_SERIF = "sans-serif";
const std::string GFont::MONOSPACED = "monospaced";


GFont::GFont(const std::string& name,
             const float        size,
             const bool         bold,
             const bool         italic) :
_name( IStringUtils::instance()->toLowerCase( name ) ),
_size(size),
_bold(bold),
_italic(italic)
{

}

bool GFont::isSerif() const {
  return (_name == GFont::SERIF);
}

bool GFont::isSansSerif() const {
  return (_name == GFont::SANS_SERIF);
}

bool GFont::isMonospaced() const {
  return (_name == GFont::MONOSPACED);
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
