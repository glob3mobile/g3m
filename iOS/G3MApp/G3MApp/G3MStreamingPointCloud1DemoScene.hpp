//
//  G3MStreamingPointCloud1DemoScene.hpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 8/19/14.
//  Copyright (c) 2014 Igo Software SL. All rights reserved.
//

#ifndef __G3MApp__G3MStreamingPointCloud1DemoScene__
#define __G3MApp__G3MStreamingPointCloud1DemoScene__

#include "G3MDemoScene.hpp"

class G3MStreamingPointCloud1DemoScene : public G3MDemoScene {
protected:
  void rawActivate(const G3MContext* context);

  void rawSelectOption(const std::string& option,
                       int optionIndex) {
    // do nothing
  }

public:
  G3MStreamingPointCloud1DemoScene(G3MDemoModel* model) :
  G3MDemoScene(model, "Streaming Points 1", "", -1)
  {
  }

};

#endif
