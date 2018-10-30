//
//  G3MExtrusionDemoScene.hpp
//  G3MApp
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 10/24/18.
//  Copyright © 2018 Igo Software SL. All rights reserved.
//

#ifndef G3MExtrusionDemoScene_hpp
#define G3MExtrusionDemoScene_hpp

#include "G3MDemoScene.hpp"

class G3MExtrusionDemoScene : public G3MDemoScene {
protected:
  void rawActivate(const G3MContext* context);

  void rawSelectOption(const std::string& option,
                       int optionIndex);

public:
  G3MExtrusionDemoScene(G3MDemoModel* model) :
  G3MDemoScene(model, "Extrusion", "", -1)
  {
  }

};

#endif
