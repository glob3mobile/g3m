//
//  G3MLandDemoScene.hpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 10/4/16.
//

#ifndef G3MLandDemoScene_hpp
#define G3MLandDemoScene_hpp

#include "G3MDemoScene.hpp"

class G3MLandDemoScene : public G3MDemoScene {
protected:
  void rawActivate(const G3MContext* context);

  void rawSelectOption(const std::string& option,
                       int optionIndex);

public:
  G3MLandDemoScene(G3MDemoModel* model) :
  G3MDemoScene(model, "3D Land", "", -1)
  {
  }

};


#endif 
