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

#endif
