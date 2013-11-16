//
//  G3MDemoModel.hpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/16/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#ifndef __G3MApp__G3MDemoModel__
#define __G3MApp__G3MDemoModel__

#include <vector>
#include <string>

class G3MDemoListener;


class G3MDemoScene {
private:
  const std::string _name;

public:
  G3MDemoScene(const std::string& name) :
  _name(name)
  {
  }

  const std::string getName() const {
    return _name;
  }
};


class G3MDemoModel {
private:
  G3MDemoListener* _listener;

  std::vector<G3MDemoScene*> _scenes;

public:

  G3MDemoModel(G3MDemoListener* listener);

  int getScenesCount() const {
    return _scenes.size();
  }

  const G3MDemoScene* getScene(int index) {
    return _scenes[index];
  }

  void selectScene(const std::string& sceneName);

};

#endif
