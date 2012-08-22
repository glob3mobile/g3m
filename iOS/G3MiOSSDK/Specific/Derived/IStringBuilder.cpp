//
//  IStringBuilder.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 22/08/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "IStringBuilder.hpp"

IStringBuilder* IStringBuilder::_instance = NULL;

void IStringBuilder::setInstance(IStringBuilder* isb) {
  if (_instance != NULL) {
    printf("Warning, ISB instance set two times\n");
  }
  _instance = isb;
}

IStringBuilder* IStringBuilder::instance() {
  return _instance;
}
