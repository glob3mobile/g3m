//
//  G3MVectorStreaming1DemoScene.hpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 7/30/15.
//

#ifndef __G3MApp__G3MVectorStreaming1DemoScene__
#define __G3MApp__G3MVectorStreaming1DemoScene__


#include "G3MDemoScene.hpp"


class G3MVectorStreaming1DemoScene : public G3MDemoScene {
protected:
  void rawActivate(const G3MContext* context);

  void rawSelectOption(const std::string& option,
                       int optionIndex) {
    // do nothing
  }
public:
  G3MVectorStreaming1DemoScene(G3MDemoModel* model) :
  G3MDemoScene(model, "Vector Streaming 1", "", -1)
  {
  }

};

#endif
