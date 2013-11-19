//
//  G3MPointCloudDemoScene.h
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/18/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#ifndef __G3MApp__G3MPointCloudDemoScene__
#define __G3MApp__G3MPointCloudDemoScene__

#include "G3MDemoScene.hpp"

class Mesh;

class G3MPointCloudDemoScene : public G3MDemoScene {
protected:
  void rawActivate(const G3MContext* context);

  void rawSelectOption(const std::string& option,
                       int optionIndex);


public:
  G3MPointCloudDemoScene(G3MDemoModel* model) :
  G3MDemoScene(model, "Point Clouds", "", -1)
  {
  }

  void setPointsCloudMesh(Mesh* mesh);

};

#endif
