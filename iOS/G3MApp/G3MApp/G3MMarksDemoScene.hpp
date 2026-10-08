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
class JSONArray;


/** every Mark feature in one scene: each option of the Feature menu shows one */
class G3MMarksDemoScene : public G3MDemoScene {
private:
  // bumped by removeFeature, so a download that finishes after the option changed is dropped
  int _featureGeneration;

  bool _londonPrioritized;

  void removeFeature();

  void animateCameraTo(const Geodetic3D& position,
                       const Angle& heading,
                       const Angle& pitch);

  void showBasicMark();
  void showAnimatedMarks();
  void showMovingMark();
  void showLabels();
  void showLondon();
  void moveLondonCamera(const std::string& cameraOption);
  void applyLondonDeclutter(const std::string& declutterOption);

protected:
  void rawActivate(const G3MContext* context);

  void rawSelectOption(const std::string& option,
                       int optionIndex);

  void rawSelectGroupOption(size_t groupIndex,
                            const std::string& option,
                            int optionIndex);

public:

  G3MMarksDemoScene(G3MDemoModel* model) :
  G3MDemoScene(model, "Marks", "Feature", "<select feature>", 0),
  _featureGeneration(0),
  _londonPrioritized(true)
  {
    addOption("Basic - icon, anchor, scale");
    addOption("Animated - sprites, resizing");
    addOption("Moving - across the horizon");
    addOption("Labels - alignment, icon anchor");
    addOption("London - declutter");

    // where London crowds the most; only the London feature shows it
    const size_t cameraGroup = addOptionGroup("London camera", "<camera>", -1);
    addOption(cameraGroup, "Overview");
    addOption(cameraGroup, "Westminster");
    addOption(cameraGroup, "Bloomsbury and the City");

    const size_t declutterGroup = addOptionGroup("London declutter", "<declutter>", -1);
    addOption(declutterGroup, "Off");
    addOption(declutterGroup, "By magnitude");
    addOption(declutterGroup, "By drawing order");

    const size_t horizonGroup = addOptionGroup("Horizon", "<horizon>", 1); // Band
    addOption(horizonGroup, "Cut");
    addOption(horizonGroup, "Band");
  }

  void deactivate(const G3MContext* context);

  bool isOptionGroupVisible(size_t groupIndex) const;

  int getFeatureGeneration() const {
    return _featureGeneration;
  }

  void addLondonMarks(const JSONArray* articles);

};

#endif
