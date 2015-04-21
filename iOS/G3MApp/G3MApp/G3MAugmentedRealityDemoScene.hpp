//
//  G3MAugmentedRealityDemoScene.hpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 3/4/15.
//  Copyright (c) 2015 Igo Software SL. All rights reserved.
//

#ifndef __G3MApp__G3MAugmentedRealityDemoScene__
#define __G3MApp__G3MAugmentedRealityDemoScene__


#include "G3MDemoScene.hpp"

@class CLLocationManager;
@class CMMotionManager;

class G3MAugmentedRealityDemoScene : public G3MDemoScene {
private:
  CLLocationManager* _locationManager;
  CMMotionManager*   _motionManager;

protected:
  void rawActivate(const G3MContext* context);

  void rawSelectOption(const std::string& option,
                       int optionIndex) {
    // do nothing
  }

public:
  G3MAugmentedRealityDemoScene(G3MDemoModel* model) :
  G3MDemoScene(model, "Augmented Reality", "", -1),
  _locationManager(nil),
  _motionManager(nil)
  {
  }

  void deactivate(const G3MContext* context);

};

#endif
