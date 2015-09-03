//
//  G3MVectorStreamingDemoScene.hpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 7/30/15.
//  Copyright (c) 2015 Igo Software SL. All rights reserved.
//

#ifndef __G3MApp__G3MVectorStreamingDemoScene__
#define __G3MApp__G3MVectorStreamingDemoScene__


#include "G3MDemoScene.hpp"


class G3MVectorStreamingDemoScene : public G3MDemoScene {
protected:
  void rawActivate(const G3MContext* context);

  void rawSelectOption(const std::string& option,
                       int optionIndex) {
    // do nothing
  }
public:
  G3MVectorStreamingDemoScene(G3MDemoModel* model) :
  G3MDemoScene(model, "Vector Streaming", "", -1)
  {
  }

};

#endif
