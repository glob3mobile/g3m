//
//  DEMProvider.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/13/16.
//
//

#include "DEMProvider.hpp"

DEMProvider::DEMProvider(const double deltaHeight) :
_deltaHeight(deltaHeight)
{
}

DEMProvider::~DEMProvider() {
#ifdef JAVA_CODE
  super.dispose();
#endif
}
