//
//  ITextUtils.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/10/13.
//
//

#include "ITextUtils.hpp"

#include "Color.hpp"
#include "ILogger.hpp"


ITextUtils* ITextUtils::_instance = NULL;


void ITextUtils::createLabelImage(const std::string& label,
                                  IImageListener* listener,
                                  bool autodelete) {
  const float fontSize = 20;

  const Color color       = Color::WHITE;
  const Color shadowColor = Color::BLACK;

  createLabelImage(label,
                   fontSize,
                   &color,
                   &shadowColor,
                   listener,
                   autodelete);
}

void ITextUtils::labelImage(const IImage* image,
                            const std::string& label,
                            const LabelPosition labelPosition,
                            IImageListener* listener,
                            bool autodelete) {
  const float fontSize = 20;

  const Color color       = Color::WHITE;
  const Color shadowColor = Color::BLACK;

  const int separation = 2;

  labelImage(image,
             label,
             labelPosition,
             separation,
             fontSize,
             &color,
             &shadowColor,
             listener,
             autodelete);
}
