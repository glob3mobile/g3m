//
//  G3MStereoDemoScene.hpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 3/4/15.
//  Copyright (c) 2015 Igo Software SL. All rights reserved.
//

#ifndef __G3MApp__G3MStereoDemoScene__
#define __G3MApp__G3MStereoDemoScene__


#include "G3MDemoScene.hpp"

class G3MStereoDemoScene : public G3MDemoScene {

protected:
  void rawActivate(const G3MContext* context);

  void rawSelectOption(const std::string& option,
                       int optionIndex) {
    // do nothing
  }

public:
  G3MStereoDemoScene(G3MDemoModel* model) :
  G3MDemoScene(model, "Stereo Vision", "", -1)
  {
  }

  void deactivate(const G3MContext* context);

};

#endif
