//
//  G3MStreamingPointCloud2DemoScene.hpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 8/19/14.
//  Copyright (c) 2014 Igo Software SL. All rights reserved.
//

#ifndef __G3MApp__G3MStreamingPointCloud2DemoScene__
#define __G3MApp__G3MStreamingPointCloud2DemoScene__

#include "G3MDemoScene.hpp"

class G3MStreamingPointCloud2DemoScene : public G3MDemoScene {
protected:
  void rawActivate(const G3MContext* context);

  void rawSelectOption(const std::string& option,
                       int optionIndex) {
    // do nothing
  }

public:
  G3MStreamingPointCloud2DemoScene(G3MDemoModel* model) :
  G3MDemoScene(model, "Streaming Points 2", "", -1)
  {
  }

};

#endif
