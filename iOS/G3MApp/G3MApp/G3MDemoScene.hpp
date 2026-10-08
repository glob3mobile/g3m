//
//  G3MDemoScene.hpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/16/13.
//

#ifndef __G3MApp__G3MDemoScene__
#define __G3MApp__G3MDemoScene__

#include <string>
#include <vector>

class G3MDemoModel;
class G3MContext;


class G3MDemoOptionGroup {
private:
  const std::string        _name;
  const std::string        _unselectedTitle;
  const int                _autoselectOptionIndex;
  std::vector<std::string> _options;
  int                      _selectedOptionIndex;

public:
  G3MDemoOptionGroup(const std::string& name,
                     const std::string& unselectedTitle,
                     const int autoselectOptionIndex) :
  _name(name),
  _unselectedTitle(unselectedTitle),
  _autoselectOptionIndex(autoselectOptionIndex),
  _selectedOptionIndex(-1)
  {
  }

  const std::string getName() const {
    return _name;
  }

  void addOption(const std::string& option) {
    _options.push_back(option);
  }

  size_t getOptionsCount() const {
    return _options.size();
  }

  const std::string getOption(size_t index) const {
    return _options[index];
  }

  int getOptionIndex(const std::string& option) const;

  int getAutoselectOptionIndex() const {
    return _autoselectOptionIndex;
  }

  int getSelectedOptionIndex() const {
    return _selectedOptionIndex;
  }

  void setSelectedOptionIndex(int optionIndex) {
    _selectedOptionIndex = optionIndex;
  }

  bool isSelectedOption(const std::string& option) const {
    if (_selectedOptionIndex < 0) {
      return false;
    }
    return _options[_selectedOptionIndex] == option;
  }

  const std::string getTitle() const {
    if (_selectedOptionIndex < 0) {
      return _unselectedTitle;
    }
    return _options[_selectedOptionIndex];
  }

};


class G3MDemoScene {
private:
  G3MDemoModel* _model;
  std::vector<G3MDemoOptionGroup*> _optionGroups;

protected:
  const std::string _name;

  G3MDemoScene(G3MDemoModel* model,
               const std::string& name,
               const std::string& optionSelectorDefaultTitle,
               const int autoselectOptionIndex) :
  _model(model),
  _name(name)
  {
    addOptionGroup("", optionSelectorDefaultTitle, autoselectOptionIndex);
  }

  G3MDemoScene(G3MDemoModel* model,
               const std::string& name,
               const std::string& firstGroupName,
               const std::string& firstGroupUnselectedTitle,
               const int firstGroupAutoselectOptionIndex) :
  _model(model),
  _name(name)
  {
    addOptionGroup(firstGroupName, firstGroupUnselectedTitle, firstGroupAutoselectOptionIndex);
  }

  size_t addOptionGroup(const std::string& name,
                        const std::string& unselectedTitle,
                        const int autoselectOptionIndex);

  void addOption(const std::string& option) {
    addOption(0, option);
  }

  void addOption(size_t groupIndex,
                 const std::string& option) {
    _optionGroups[groupIndex]->addOption(option);
  }

  virtual void rawActivate(const G3MContext* context) = 0;

  virtual void rawSelectOption(const std::string& option,
                               int optionIndex) = 0;

  // Scenes with more than one option group override this one.
  virtual void rawSelectGroupOption(size_t groupIndex,
                                    const std::string& option,
                                    int optionIndex) {
    rawSelectOption(option, optionIndex);
  }

public:

  virtual ~G3MDemoScene();

  const std::string getName() const {
    return _name;
  }

  G3MDemoModel* getModel() const {
    return _model;
  }

  size_t getOptionGroupsCount() const {
    return _optionGroups.size();
  }

  const G3MDemoOptionGroup* getOptionGroup(size_t groupIndex) const {
    return _optionGroups[groupIndex];
  }

  /** a group can depend on the option chosen in another; the app hides it while it does not apply */
  virtual bool isOptionGroupVisible(size_t groupIndex) const {
    return true;
  }

  void selectOption(size_t groupIndex,
                    const std::string& option);

  void activate(const G3MContext* context);

  void activateOptions(const G3MContext* context);

  virtual void deactivate(const G3MContext* context);
  
};

#endif
