//
//  G3MDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/16/13.
//

#include "G3MDemoScene.hpp"

#include "G3MDemoModel.hpp"


int G3MDemoOptionGroup::getOptionIndex(const std::string& option) const {
  const int optionsSize = _options.size();
  for (int i = 0; i < optionsSize; i++) {
    if (_options[i] == option) {
      return i;
    }
  }

  return -1;
}


G3MDemoScene::~G3MDemoScene() {
  for (size_t i = 0; i < _optionGroups.size(); i++) {
    delete _optionGroups[i];
  }
}

size_t G3MDemoScene::addOptionGroup(const std::string& name,
                                    const std::string& unselectedTitle,
                                    const int autoselectOptionIndex) {
  _optionGroups.push_back( new G3MDemoOptionGroup(name, unselectedTitle, autoselectOptionIndex) );
  return _optionGroups.size() - 1;
}

void G3MDemoScene::deactivate(const G3MContext* context) {
  _model->reset();

  for (size_t i = 0; i < _optionGroups.size(); i++) {
    _optionGroups[i]->setSelectedOptionIndex(-1);
  }
}

void G3MDemoScene::activate(const G3MContext* context) {
  rawActivate(context);
}

void G3MDemoScene::activateOptions(const G3MContext* context) {
  for (size_t groupIndex = 0; groupIndex < _optionGroups.size(); groupIndex++) {
    const G3MDemoOptionGroup* group = _optionGroups[groupIndex];
    const int autoselectOptionIndex = group->getAutoselectOptionIndex();
    if ((autoselectOptionIndex >= 0) &&
        (group->getOptionsCount() > autoselectOptionIndex)) {
      selectOption(groupIndex, group->getOption(autoselectOptionIndex));
    }
  }
}

void G3MDemoScene::selectOption(size_t groupIndex,
                                const std::string& option) {
  G3MDemoOptionGroup* group = _optionGroups[groupIndex];
  const int optionIndex = group->getOptionIndex(option);
  if ((optionIndex >= 0) &&
      (optionIndex != group->getSelectedOptionIndex())) {
    group->setSelectedOptionIndex(optionIndex);

    rawSelectGroupOption(groupIndex, option, optionIndex);

    _model->onChangeSceneOption(this, groupIndex, option, optionIndex);
  }
}
