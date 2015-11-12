//
//  G3MWMS_DEMDemoScene.hpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/20/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#ifndef __G3MApp__G3MWMS_DEMDemoScene__
#define __G3MApp__G3MWMS_DEMDemoScene__

#include "G3MDemoScene.hpp"

class G3MWMS_DEMDemoScene : public G3MDemoScene {
protected:
  void rawActivate(const G3MContext* context);

  void rawSelectOption(const std::string& option,
                       int optionIndex) {
    // do nothing, no options
  }

public:
  G3MWMS_DEMDemoScene(G3MDemoModel* model) :
  G3MDemoScene(model, "WMS DEM", "", -1)
  {
  }
  
};

#endif
