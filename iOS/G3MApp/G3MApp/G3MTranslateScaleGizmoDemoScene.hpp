//
//  G3MTranslateScaleGizmoDemoScene.hpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/18/13.
//

#ifndef __G3MApp__G3MTranslateScaleGizmoDemoScene__
#define __G3MApp__G3MTranslateScaleGizmoDemoScene__

#include "G3MDemoScene.hpp"

class Mark;
class TranslateScaleGizmo;


class G3MTranslateScaleGizmoDemoScene : public G3MDemoScene {
private:
  TranslateScaleGizmo* _gizmo;

protected:
  void rawActivate(const G3MContext* context);

  void rawSelectOption(const std::string& option,
                       int optionIndex) {
    // do nothing
  }

public:

  G3MTranslateScaleGizmoDemoScene(G3MDemoModel* model) :
  G3MDemoScene(model, "Translate Scale Gizmo", "", -1),
  _gizmo(NULL)
  {
  }

  void deactivate(const G3MContext* context);

};

#endif
