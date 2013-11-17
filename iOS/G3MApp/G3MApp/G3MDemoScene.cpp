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

void G3MDemoScene::activate() {
  rawActivate();

  if (_options.size() > 0) {
    selectOption(_options[0]);
  }
}

bool G3MDemoScene::isValidOption(const std::string& option) const {
  const int optionsSize = _options.size();
  for (int i = 0; i < optionsSize; i++) {
    if (_options[i] == option) {
      return true;
    }
  }

  return false;
}

void G3MDemoScene::selectOption(const std::string& option) {
  if (option != _selectedOption) {
    if (isValidOption(option)) {
      _selectedOption = option;

      rawSelectOption(option);

      _model->onChangeSceneOption(this, _selectedOption);
    }
  }
}
