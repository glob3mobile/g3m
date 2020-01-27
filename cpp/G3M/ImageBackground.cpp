//
//  ImageBackground.cpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 2/19/19.
//

#include "ImageBackground.hpp"



const Vector2F ImageBackground::initializeCanvas(ICanvas* canvas,
                                                 const Vector2F& contentSize) const {
  return initializeCanvas(canvas,
                          contentSize._x,
                          contentSize._y);
}
