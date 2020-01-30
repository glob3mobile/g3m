//
//  CanvasTileImageProvider.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 4/18/14.
//
//

#include "CanvasTileImageProvider.hpp"


CanvasTileImageProvider::CanvasTileImageProvider()
{
}

CanvasTileImageProvider::~CanvasTileImageProvider() {
#ifdef JAVA_CODE
  super.dispose();
#endif
}
