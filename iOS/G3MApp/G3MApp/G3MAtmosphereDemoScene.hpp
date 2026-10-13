//
//  G3MAtmosphereDemoScene.hpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 10/7/26.
//

#ifndef __G3MApp__G3MAtmosphereDemoScene__
#define __G3MApp__G3MAtmosphereDemoScene__

#include "G3MDemoScene.hpp"

class Layer;


// Fixed camera poses to compare the atmosphere rendering before and after shader changes
class G3MAtmosphereDemoScene : public G3MDemoScene {
private:
  Layer* _satelliteLayer;
  Layer* _openStreetMapLayer;

protected:
  void rawActivate(const G3MContext* context);

  void rawSelectOption(const std::string& option,
                       int optionIndex);

public:
  G3MAtmosphereDemoScene(G3MDemoModel* model);

};

#endif
