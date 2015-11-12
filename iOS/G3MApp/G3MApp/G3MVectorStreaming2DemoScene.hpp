//
//  G3MVectorStreaming2DemoScene.hpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 7/30/15.
//  Copyright (c) 2015 Igo Software SL. All rights reserved.
//

#ifndef __G3MApp__G3MVectorStreaming2DemoScene__
#define __G3MApp__G3MVectorStreaming2DemoScene__


#include "G3MDemoScene.hpp"


class G3MVectorStreaming2DemoScene : public G3MDemoScene {
protected:
  void rawActivate(const G3MContext* context);

  void rawSelectOption(const std::string& option,
                       int optionIndex) {
    // do nothing
  }
public:
  G3MVectorStreaming2DemoScene(G3MDemoModel* model) :
  G3MDemoScene(model, "Vector Streaming 2", "", -1)
  {
  }

};

#endif
