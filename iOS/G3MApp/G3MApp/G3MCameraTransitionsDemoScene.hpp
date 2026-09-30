//
//  G3MCameraTransitionsDemoScene.hpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 9/30/26.
//

#ifndef __G3MApp__G3MCameraTransitionsDemoScene__
#define __G3MApp__G3MCameraTransitionsDemoScene__

#include "G3MDemoScene.hpp"

class Geodetic3D;
class Angle;


// Camera flights that exercise setAnimatedCameraPosition, computeCameraPose and setAnimatedCameraPointOfView
class G3MCameraTransitionsDemoScene : public G3MDemoScene {
private:
  void animate(const Geodetic3D& fromPosition,
               const Angle&      fromHeading,
               const Angle&      fromPitch,
               const Geodetic3D& toPosition,
               const Angle&      toHeading,
               const Angle&      toPitch,
               const double      seconds);

  void animateToTwoAnchorsPose(const Geodetic3D& position1,
                               const Geodetic3D& position2,
                               const Angle&      pitch);

  void animatePointOfView(const Geodetic3D& fromTarget,
                          const Geodetic3D& toTarget,
                          const double      fromDistance,
                          const double      toDistance,
                          const Angle&      fromAzimuth,
                          const Angle&      toAzimuth,
                          const Angle&      fromAltitude,
                          const Angle&      toAltitude,
                          const double      seconds);

protected:
  void rawActivate(const G3MContext* context);

  void rawSelectOption(const std::string& option,
                       int optionIndex);

public:
  G3MCameraTransitionsDemoScene(G3MDemoModel* model) :
  G3MDemoScene(model, "Camera Transitions", "<select transition>", -1)
  {
    _options.push_back("Nadir -> nadir (Madrid -> Sydney)");
    _options.push_back("Sky at the end (pitch -15)");
    _options.push_back("Two anchors (Madrid -> Amsterdam)");
    _options.push_back("Two anchors, oblique (Madrid -> Amsterdam, pitch -35)");
    _options.push_back("Two anchors, no solution (Madrid -> Sydney)");
    _options.push_back("PoV: Madrid, horizon -> nadir");
    _options.push_back("PoV: Madrid -> Toledo, horizon kept");
    _options.push_back("PoV: Lisbon -> Tokyo, oblique kept");
    _options.push_back("PoV: Washington -> Buenos Aires, oblique kept");
    _options.push_back("PoV: Buenos Aires -> Washington, nadir");
    _options.push_back("PoV: New York -> San Francisco, everything changes");
    _options.push_back("PoV: Madrid, zoom out 2 km -> 4000 km");
  }

};

#endif
