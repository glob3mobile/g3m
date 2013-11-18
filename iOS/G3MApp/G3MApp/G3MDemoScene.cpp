//
//  G3MDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/16/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#include "G3MDemoScene.hpp"

#include "G3MDemoModel.hpp"

void G3MDemoScene::deactivate(const G3MContext* context) {
  _model->reset();
}

void G3MDemoScene::activate(const G3MContext* context) {
  rawActivate(context);

  if (_options.size() > 0) {
    selectOption(_options[0]);
  }
}

int G3MDemoScene::getOptionIndex(const std::string& option) const {
  const int optionsSize = _options.size();
  for (int i = 0; i < optionsSize; i++) {
    if (_options[i] == option) {
      return i;
    }
  }

  return -1;
}

void G3MDemoScene::selectOption(const std::string& option) {
//  if (option != _selectedOption) {
//    const int optionIndex = getOptionIndex(option);
//    if (optionIndex >= 0) {
//      _selectedOption = option;
//
//      rawSelectOption(option, optionIndex);
//
//      _model->onChangeSceneOption(this, _selectedOption, optionIndex);
//    }
//  }

  const int optionIndex = getOptionIndex(option);
  if (optionIndex != _selectedOptionIndex) {
    if (optionIndex >= 0) {
      _selectedOptionIndex = optionIndex;

      rawSelectOption(option, optionIndex);

      _model->onChangeSceneOption(this, option, optionIndex);
    }
  }

}
