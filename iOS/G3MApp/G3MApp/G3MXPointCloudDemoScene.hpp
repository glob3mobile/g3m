//
//  G3MXPointCloudDemoScene.hpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 1/15/21.
//

#ifndef __G3MApp__G3MXPointCloudDemoScene__
#define __G3MApp__G3MXPointCloudDemoScene__

#include "G3MDemoScene.hpp"

class G3MXPointCloudDemoScene : public G3MDemoScene {

protected:
  void rawActivate(const G3MContext* context);

  void rawSelectOption(const std::string& option,
                       int optionIndex) {
    // do nothing
  }

public:
  G3MXPointCloudDemoScene(G3MDemoModel* model) :
  G3MDemoScene(model, "X Point Cloud", "", -1)
  {
  }

};

#endif
