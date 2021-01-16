//
//  G3MOLDPointCloud2DemoScene.hpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 8/19/14.
//

#ifndef __G3MApp__G3MOLDPointCloud2DemoScene__
#define __G3MApp__G3MOLDPointCloud2DemoScene__

#include "G3MDemoScene.hpp"

class G3MOLDPointCloud2DemoScene : public G3MDemoScene {
protected:
  void rawActivate(const G3MContext* context);

  void rawSelectOption(const std::string& option,
                       int optionIndex) {
    // do nothing
  }

public:
  G3MOLDPointCloud2DemoScene(G3MDemoModel* model) :
  G3MDemoScene(model, "Amsterdam - Point Cloud streaming", "", -1)
  {
  }

};

#endif
