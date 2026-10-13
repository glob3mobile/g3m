//
//  G3MNightSkyDemoScene.hpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 10/7/26.
//

#ifndef __G3MApp__G3MNightSkyDemoScene__
#define __G3MApp__G3MNightSkyDemoScene__

#include "G3MDemoScene.hpp"


// Without atmosphere, the camera looks straight up at known constellations
class G3MNightSkyDemoScene : public G3MDemoScene {
protected:
  void rawActivate(const G3MContext* context);

  void rawSelectOption(const std::string& option,
                       int optionIndex);

public:
  G3MNightSkyDemoScene(G3MDemoModel* model);

};

#endif
