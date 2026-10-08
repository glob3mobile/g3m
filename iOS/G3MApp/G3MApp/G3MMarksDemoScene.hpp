//
//  G3MMarksDemoScene.hpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/18/13.
//

#ifndef __G3MApp__G3MMarksDemoScene__
#define __G3MApp__G3MMarksDemoScene__

#include "G3MDemoScene.hpp"

class Geodetic3D;
class Angle;


/** every Mark feature in one scene: each option of the Feature menu shows one */
class G3MMarksDemoScene : public G3MDemoScene {
private:
  void removeFeature();

  void animateCameraTo(const Geodetic3D& position,
                       const Angle& heading,
                       const Angle& pitch);

  void showBasicMark();
  void showAnimatedMarks();
  void showMovingMark();
  void showLabels();

protected:
  void rawActivate(const G3MContext* context);

  void rawSelectOption(const std::string& option,
                       int optionIndex);

public:

  G3MMarksDemoScene(G3MDemoModel* model) :
  G3MDemoScene(model, "Marks", "Feature", "<select feature>", 0)
  {
    addOption("Basic");
    addOption("Animated");
    addOption("Moving");
    addOption("Labels");
  }

  void deactivate(const G3MContext* context);

};

#endif
