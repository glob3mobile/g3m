//
//  G3MSoccerMatchDemoScene.hpp
//  G3MApp
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 11/25/18.
//  Copyright © 2018 Igo Software SL. All rights reserved.
//

#ifndef G3MSoccerMatchDemoScene_hpp
#define G3MSoccerMatchDemoScene_hpp

#include "G3MDemoScene.hpp"

class Mesh;


class G3MSoccerMatchDemoScene : public G3MDemoScene {
protected:
  void rawActivate(const G3MContext* context);

  void rawSelectOption(const std::string& option,
                       int optionIndex);


public:
  G3MSoccerMatchDemoScene(G3MDemoModel* model) :
  G3MDemoScene(model, "ESTADIO 3D", "", -1)
  {
  }

  void setMesh(Mesh* mesh);

};

#endif
