//
//  NullImageBackground.hpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 2/19/19.
//

#ifndef NullImageBackground_hpp
#define NullImageBackground_hpp

#include "ImageBackground.hpp"


class NullImageBackground : public ImageBackground {
public:

  const Vector2F initializeCanvas(ICanvas* canvas,
                                  const float contentWidth,
                                  const float contentHeight) const;

  const std::string description() const;

  NullImageBackground* copy() const;

};

#endif
