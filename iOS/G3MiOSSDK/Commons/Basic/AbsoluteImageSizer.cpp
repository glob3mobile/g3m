//
//  AbsoluteImageSizer.cpp
//  G3MiOSSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 2/11/19.
//

#include "AbsoluteImageSizer.hpp"


AbsoluteImageSizer::AbsoluteImageSizer(int size) :
_size(size)
{

}

AbsoluteImageSizer::~AbsoluteImageSizer() {
#ifdef JAVA_CODE
  super.dispose();
#endif
}

int AbsoluteImageSizer::calculate() {
  return _size;
}
