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


// Origin -> destination camera animations that exercise setAnimatedCameraPosition and computeCameraPose
class G3MCameraTransitionsDemoScene : public G3MDemoScene {
private:
  // last flight drawn by showArcs, so "View arcs from the side" can frame it
  Geodetic3D* _arcMidpoint;
  double      _arcBearingDegrees;
  double      _arcSeparation;
  double      _arcPeak;

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
                          const Angle&      fromAltitude,
                          const Angle&      toAltitude,
                          const double      seconds);

  void showArcs(const Geodetic3D& from,
                const Geodetic3D& to,
                const double      fromValue,
                const double      toValue);

  void viewArcsFromTheSide();

protected:
  void rawActivate(const G3MContext* context);

  void rawSelectOption(const std::string& option,
                       int optionIndex);

public:
  G3MCameraTransitionsDemoScene(G3MDemoModel* model) :
  G3MDemoScene(model, "Camera Transitions", "<select transition>", -1),
  _arcMidpoint(NULL),
  _arcBearingDegrees(0),
  _arcSeparation(0),
  _arcPeak(0)
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
    _options.push_back("PoV: Madrid, zoom out 2 km -> 4000 km");
    _options.push_back("View arcs from the side");
  }

  ~G3MCameraTransitionsDemoScene();

};

#endif
