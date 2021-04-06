//
//  G3MOLDPointCloud1DemoScene.hpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 8/19/14.
//

#ifndef __G3MApp__G3MOLDPointCloud1DemoScene__
#define __G3MApp__G3MOLDPointCloud1DemoScene__

#include "G3MDemoScene.hpp"

class G3MOLDPointCloud1DemoScene : public G3MDemoScene {
protected:
  void rawActivate(const G3MContext* context);

  void rawSelectOption(const std::string& option,
                       int optionIndex) {
    // do nothing
  }

public:
  G3MOLDPointCloud1DemoScene(G3MDemoModel* model) :
  G3MDemoScene(model, "Loudon (VA) - Point Cloud streaming", "", -1)
  {
  }

};

#endif
