//
//  DecoratorTileLODTester.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/26/16.
//
//

#include "DecoratorTileLODTester.hpp"

#include "ErrorHandling.hpp"


DecoratorTileLODTester::DecoratorTileLODTester(TileLODTester* tileLODTester) :
_tileLODTester(tileLODTester)
{
  if (_tileLODTester == NULL) {
    THROW_EXCEPTION("NULL NOT ALLOWED");
  }
}

DecoratorTileLODTester::~DecoratorTileLODTester() {
  delete _tileLODTester;
#ifdef JAVA_CODE
  super.dispose();
#endif
}
