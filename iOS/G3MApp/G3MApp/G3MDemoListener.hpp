//
//  G3MDemoListener.hpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/16/13.
//

#ifndef __G3MApp__G3MDemoListener__
#define __G3MApp__G3MDemoListener__

class G3MDemoScene;
class G3MColorGradingDemoScene;

#include <string>

class G3MDemoListener {
public:
  virtual void onChangedScene(const G3MDemoScene* scene) = 0;

  virtual void onChangeSceneOption(G3MDemoScene* scene,
                                   size_t groupIndex,
                                   const std::string& option,
                                   int optionIndex) = 0;

  virtual void showDialog(const std::string& title,
                          const std::string& message) const = 0;

  virtual void showColorGradingPanel(G3MColorGradingDemoScene* scene) = 0;

  virtual void hideColorGradingPanel() = 0;

  virtual ~G3MDemoListener() {

  }
};

#endif
