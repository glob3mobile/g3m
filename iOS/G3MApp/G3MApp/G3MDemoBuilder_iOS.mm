//
//  G3MDemoBuilder_iOS.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/16/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#include "G3MDemoBuilder_iOS.hpp"

#include <G3MiOSSDK/G3MBuilder_iOS.hpp>

IG3MBuilder* G3MDemoBuilder_iOS::getG3MBuilder() {
  return _builder;
}

G3MDemoBuilder_iOS::~G3MDemoBuilder_iOS() {
  delete _builder;
}

void G3MDemoBuilder_iOS::initializeWidget() {
  build();
  _builder->initializeWidget();
}
