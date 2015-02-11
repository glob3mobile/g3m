//
//  G3MHUDDemoScene.hpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 12/2/14.
//  Copyright (c) 2014 Igo Software SL. All rights reserved.
//

#ifndef __G3MApp__G3MHUDDemoScene__
#define __G3MApp__G3MHUDDemoScene__

#include "G3MDemoScene.hpp"

class G3MHUDDemoScene : public G3MDemoScene {
protected:
  void rawActivate(const G3MContext* context);

  void rawSelectOption(const std::string& option,
                       int optionIndex) {
    // do nothing
  }

public:
  G3MHUDDemoScene(G3MDemoModel* model) :
  G3MDemoScene(model, "HUD", "", -1)
  {
  }
  
};

#endif
