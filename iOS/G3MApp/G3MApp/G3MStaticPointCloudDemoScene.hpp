//
//  G3MStaticPointCloudDemoScene.h
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/18/13.
//

#ifndef __G3MApp__G3MStaticPointCloudDemoScene__
#define __G3MApp__G3MStaticPointCloudDemoScene__

#include "G3MDemoScene.hpp"

class Mesh;

class G3MStaticPointCloudDemoScene : public G3MDemoScene {
protected:
  void rawActivate(const G3MContext* context);

  void rawSelectOption(const std::string& option,
                       int optionIndex);


public:
  G3MStaticPointCloudDemoScene(G3MDemoModel* model) :
  G3MDemoScene(model, "Point Clouds", "", -1)
  {
  }

  void setPointCloudMesh(Mesh* mesh);

};

#endif
