//
//  NullImageBackground.cpp
//  G3MiOSSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 2/19/19.
//

#include "NullImageBackground.hpp"

#include "IMathUtils.hpp"
#include "ICanvas.hpp"


const Vector2F NullImageBackground::initializeCanvas(ICanvas* canvas,
                                                     const float contentWidth,
                                                     const float contentHeight) const {
  const IMathUtils* mu = IMathUtils::instance();
  
  canvas->initialize((int) mu->ceil(contentWidth),
                     (int) mu->ceil(contentHeight));
  
  return Vector2F::zero();
}

const std::string NullImageBackground::description() const {
  return "NULL";
}

NullImageBackground* NullImageBackground::copy() const {
  return new NullImageBackground();
}
