//
//  G3MCanvas2DDemoScene.hpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 2/12/15.
//  Copyright (c) 2015 Igo Software SL. All rights reserved.
//

#ifndef __G3MApp__G3MCanvas2DDemoScene__
#define __G3MApp__G3MCanvas2DDemoScene__


#include "G3MDemoScene.hpp"


class G3MCanvas2DDemoScene : public G3MDemoScene {
protected:
  void rawActivate(const G3MContext* context);

  void rawSelectOption(const std::string& option,
                       int optionIndex) {
    // do nothing
  }
public:
  G3MCanvas2DDemoScene(G3MDemoModel* model) :
  G3MDemoScene(model, "Canvas 2D", "", -1)
  {
  }
  
};

#endif
