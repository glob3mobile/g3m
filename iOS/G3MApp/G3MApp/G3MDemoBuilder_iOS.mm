//
//  G3MDemoBuilder_iOS.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/16/13.
//

#include "G3MDemoBuilder_iOS.hpp"

#include <G3MiOSSDK/G3MBuilder_iOS.hpp>

#include "G3MDemoModel.hpp"


IG3MBuilder* G3MDemoBuilder_iOS::getG3MBuilder() {
  return _builder;
}

G3MDemoBuilder_iOS::~G3MDemoBuilder_iOS() {
  delete _builder;
}

void G3MDemoBuilder_iOS::initializeWidget() {
  build();
  _builder->initializeWidget();

  getModel()->initializeG3MWidget( _builder->getNativeWidget().widget );
}
