//
//  G3MNonOverlappingMarksDemoScene.hpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 1/30/15.
//  Copyright (c) 2015 Igo Software SL. All rights reserved.
//

#ifndef __G3MApp__G3MNonOverlappingMarksDemoScene__
#define __G3MApp__G3MNonOverlappingMarksDemoScene__

#include "G3MDemoScene.hpp"


class G3MNonOverlappingMarksDemoScene : public G3MDemoScene {
protected:
  void rawActivate(const G3MContext* context);

  void rawSelectOption(const std::string& option,
                       int optionIndex) {
    // do nothing
  }

public:
  G3MNonOverlappingMarksDemoScene(G3MDemoModel* model) :
  G3MDemoScene(model, "Non Overlapping Marks", "", -1)
  {
  }

};


#endif
