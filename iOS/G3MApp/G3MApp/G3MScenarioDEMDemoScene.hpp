//
//  G3MScenarioDEMDemoScene.hpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/20/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#ifndef __G3MApp__G3MScenarioDEMDemoScene__
#define __G3MApp__G3MScenarioDEMDemoScene__

#include "G3MDemoScene.hpp"

class G3MScenarioDEMDemoScene : public G3MDemoScene {
protected:
  void rawActivate(const G3MContext* context);

  void rawSelectOption(const std::string& option,
                       int optionIndex) {
    // do nothing, no options
  }

public:
  G3MScenarioDEMDemoScene(G3MDemoModel* model) :
  G3MDemoScene(model, "Scenario + DEM", "", -1)
  {
  }
  
};

#endif
