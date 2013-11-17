//
//  G3MDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/16/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#include "G3MDemoScene.hpp"

#include "G3MDemoModel.hpp"

void G3MDemoScene::deactivate() {
  _model->reset();
}
