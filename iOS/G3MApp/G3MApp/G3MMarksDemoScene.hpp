//
//  G3MMarksDemoScene.hpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/18/13.
//

#ifndef __G3MApp__G3MMarksDemoScene__
#define __G3MApp__G3MMarksDemoScene__

#include "G3MDemoScene.hpp"

#include <G3M/MarkTransitionMode.hpp>

class Geodetic3D;
class Angle;
class JSONArray;
class URL;
class IImageFactory;
class BoxImageBackground;
class Vector2F;
class Color;


/** every Mark feature in one scene: each option of the Feature menu shows one */
class G3MMarksDemoScene : public G3MDemoScene {
private:
  // bumped by removeFeature, so a download that finishes after the option changed is dropped
  int _featureGeneration;

  bool _londonPrioritized;
  std::string _londonLabelSide; // where a label may go around its icon: an option of the London label group
  std::string _londonTransition; // an option of the London transition group
  bool _showsTerrain;

  void removeFeature();

  void animateCameraTo(const Geodetic3D& position,
                       const Angle& heading,
                       const Angle& pitch);

  void showBasicMark();
  void showAnimatedMarks();
  void showMovingMark();
  void showLabels();
  void showLondon();
  void showTerrain();
  void moveLondonCamera(const std::string& cameraOption);
  void orbitLondon();
  void applyLondonDeclutter(const std::string& declutterOption);
  void applyLondonTransition(const std::string& transitionOption);
  void applyLondonLabelSide(const std::string& labelSideOption);
  MarkTransitionMode londonSideLabelTransition() const;
  MarkTransitionMode londonVerticalLabelTransition() const;
  void loadLondonMarks();

  static IImageFactory* createLondonIcon(const URL& iconURL,
                                         const int  iconPoints);

  static BoxImageBackground* createLondonBox(const Vector2F& padding,
                                             const Color&    backgroundColor,
                                             const float     cornerRadius);

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
  _londonPrioritized(true),
  _londonLabelSide("Any side"),
  _londonTransition("Fold and alpha"),
  _showsTerrain(false)
  {
    addOption("Basic - icon, anchor, scale");
    addOption("Animated - sprites, resizing");
    addOption("Moving - across the horizon");
    addOption("Labels - alignment, icon anchor");
    addOption("London - declutter");
    addOption("Terrain - relative to ground");

    // where London crowds the most; only the London feature shows it
    const size_t cameraGroup = addOptionGroup("London camera", "<camera>", -1);
    addOption(cameraGroup, "Overview");
    addOption(cameraGroup, "Westminster");
    addOption(cameraGroup, "From the City");
    addOption(cameraGroup, "Orbit");

    const size_t declutterGroup = addOptionGroup("London declutter", "<declutter>", -1);
    addOption(declutterGroup, "Off");
    addOption(declutterGroup, "By magnitude");
    addOption(declutterGroup, "By drawing order");

    const size_t horizonGroup = addOptionGroup("Horizon", "<horizon>", 1); // Band
    addOption(horizonGroup, "Cut");
    addOption(horizonGroup, "Band");

    // Fold: a label at the side folds into its icon in width, above or below in height; icons and dots scale
    const size_t transitionGroup = addOptionGroup("London transition", "<transition>", 4); // Fold and alpha
    addOption(transitionGroup, "Alpha");
    addOption(transitionGroup, "Scale");
    addOption(transitionGroup, "Scale and alpha");
    addOption(transitionGroup, "Fold");
    addOption(transitionGroup, "Fold and alpha");

    // a label that does not fit on the right tries the left before the mark shrinks to its icon
    const size_t labelSideGroup = addOptionGroup("London label", "<label>", 3); // Any side
    addOption(labelSideGroup, "Right");
    addOption(labelSideGroup, "Right or left");
    addOption(labelSideGroup, "Top or bottom");
    addOption(labelSideGroup, "Any side");
  }

  void deactivate(const G3MContext* context);

  bool isOptionGroupVisible(size_t groupIndex) const;

  int getFeatureGeneration() const {
    return _featureGeneration;
  }

  void addLondonMarks(const JSONArray* articles);

};

#endif
