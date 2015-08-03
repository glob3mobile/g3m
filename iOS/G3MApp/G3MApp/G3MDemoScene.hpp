//
//  G3MDemoScene.hpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/16/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#ifndef __G3MApp__G3MDemoScene__
#define __G3MApp__G3MDemoScene__

#include <string>
#include <vector>

class G3MDemoModel;
class G3MContext;


class G3MDemoScene {
private:
  G3MDemoModel* _model;
  int _selectedOptionIndex;

  int getOptionIndex(const std::string& option) const;

protected:
  const std::string        _name;
  const std::string        _optionSelectorDefaultTitle;
  std::vector<std::string> _options;
  const int _autoselectOptionIndex;

  G3MDemoScene(G3MDemoModel* model,
               const std::string& name,
               const std::string& optionSelectorDefaultTitle,
               const int autoselectOptionIndex) :
  _model(model),
  _name(name),
  _optionSelectorDefaultTitle(optionSelectorDefaultTitle),
  _autoselectOptionIndex(autoselectOptionIndex),
  _selectedOptionIndex(-1)
  {
  }

  virtual void rawActivate(const G3MContext* context) = 0;

  virtual void rawSelectOption(const std::string& option,
                               int optionIndex) = 0;

public:

  virtual ~G3MDemoScene() {
  }

  const std::string getName() const {
    return _name;
  }

  const std::string getOptionSelectorDefaultTitle() const {
    return _optionSelectorDefaultTitle;
  }

  G3MDemoModel* getModel() const {
    return _model;
  }

  const int getOptionsCount() const {
    return _options.size();
  }

  const std::string getOption(size_t index) const {
    return _options[index];
  }

  bool isSelectedOption(const std::string& option) const {
    if (_selectedOptionIndex < 0) {
      return false;
    }
    return _options[_selectedOptionIndex] == option;
  }

  void selectOption(const std::string& option);

  void activate(const G3MContext* context);

  void activateOptions(const G3MContext* context);

  virtual void deactivate(const G3MContext* context);
  
};

#endif
