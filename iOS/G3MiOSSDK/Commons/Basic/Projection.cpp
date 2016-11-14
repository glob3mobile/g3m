//
//  Projection.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/14/16.
//
//

#include "Projection.hpp"


Projection::Projection() {

}

Projection::~Projection() {
#ifdef JAVA_CODE
  super.dispose();
#endif
}
