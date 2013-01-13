//
//  ITextUtils.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/10/13.
//
//

#include "ITextUtils.hpp"

#include "Color.hpp"

ITextUtils* ITextUtils::_instance = NULL;


IImage* ITextUtils::createLabelBitmap(const std::string& label) {
  const float fontSize = 20;

  const Color color       = Color::yellow();
  const Color shadowColor = Color::black();

  return createLabelBitmap(label,
                           fontSize,
                           &color,
                           &shadowColor);
}
