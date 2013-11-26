//
//  G3MIsosurfaceDemoScene.h
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/19/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#ifndef __G3MApp__G3MIsosurfaceDemoScene__
#define __G3MApp__G3MIsosurfaceDemoScene__

#include "G3MDemoScene.hpp"

class G3MIsosurfaceDemoScene : public G3MDemoScene {
protected:
  void rawActivate(const G3MContext* context);

  void rawSelectOption(const std::string& option,
                       int optionIndex) {
    // do nothing, no options
  }

public:
  G3MIsosurfaceDemoScene(G3MDemoModel* model) :
  G3MDemoScene(model, "Isosurface", "", -1)
  {
  }
  
};

#endif
